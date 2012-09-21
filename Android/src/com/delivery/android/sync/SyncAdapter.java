/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.delivery.android.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParserException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.delivery.android.account.AccountInfo;
import com.delivery.android.client.NetworkUtils;
import com.delivery.android.exception.ParseOrderListException;
import com.delivery.android.preferences.DeliveryPreferences;
import com.delivery.android.provider.Address;
import com.delivery.android.provider.Order;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";
    private static final boolean NOTIFY_AUTH_FAILURE = true;

    private final AccountManager mAccountManager;

    private final Context mContext;
    DeliveryPreferences mPrefs;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mAccountManager = AccountManager.get(context);
        mPrefs = DeliveryPreferences.getInstance(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
        ContentProviderClient provider, SyncResult syncResult) {
    	int syncStatus = SyncManager.STATUS_SUCCESS;
    	SyncManager syncManager = SyncManager.getInstance(mContext);
    	syncManager.notifySyncStarted();

        final String syncURL = mPrefs.getOrderServerAddress() + "/mobile/ListOrders";
        final HttpResponse resp;
    	try {
    		// First we need to get the account token
    		Bundle result = mAccountManager.getAuthToken(account, AccountInfo.AUTH_TOKEN_TYPE, null,
    				NOTIFY_AUTH_FAILURE, null, null).getResult();
    		String token = result.getString(AccountManager.KEY_AUTHTOKEN);

    		final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("token", token));
            final HttpEntity entity;
            try {
                entity = new UrlEncodedFormEntity(params);
            } catch (final UnsupportedEncodingException e) {
                // this should never happen.
                throw new IllegalStateException(e);
            }
            Log.i(TAG, "Starting sync to: " + syncURL);
            final HttpPost post = new HttpPost(syncURL);
            post.addHeader(entity.getContentType());
            post.setEntity(entity);
            try {
            	// First we query all orders. It should return one Id per line
            	ArrayList<Long> ordersToFetch = new ArrayList<Long>();
            	ArrayList<Long> ordersToRemove = new ArrayList<Long>();
                resp = NetworkUtils.getHttpClient().execute(post);
                int statusCode = resp.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    InputStream istream = (resp.getEntity() != null) ? resp.getEntity().getContent() : null;
                    if (istream != null) {
                        BufferedReader ireader = new BufferedReader(new InputStreamReader(istream));
                        String line = null;
                    	try {
	                        while ((line = ireader.readLine()) != null) {
	                    		ordersToFetch.add(Long.parseLong(line));
	                        }
                    	} catch(NumberFormatException e) {
                    		throw new ParseOrderListException("Failed to parse id: " + line);
                    	} finally {
                    		ireader.close();
                    	}

                        // Now we need to download the orders that are not yet stored
                        // on the local database
                        prepareFetchAndExcludeLists(ordersToFetch, ordersToRemove);

                        // Now we can fetch each order and insert into the database
                        ContentResolver resolver = mContext.getContentResolver();
                        for (long l : ordersToFetch) {
                        	Order remoteOrder = fetchOrder(l, token);
                        	Address remoteAddress = remoteOrder.getRemoteAddress();
                        	Uri insertedUri = resolver.insert(Address.CONTENT_URI, remoteAddress.toContentValues());
                        	long addressKey = Long.parseLong(insertedUri.getLastPathSegment());
                        	remoteOrder.setAddressKey(addressKey);
                        	resolver.insert(Order.CONTENT_URI, remoteOrder.toContentValues());
                        }

                        // Finally, we need to remove the orders that are not still on the server
                        // from the device
                        removeLocalOrders(ordersToRemove);
                    }
                } else if (statusCode == HttpStatus.SC_FORBIDDEN) {
                	throw new AuthenticationException();
                } else {
                	throw new IOException("Error " + statusCode);
                }
            } catch (final IOException e) {
                Log.e(TAG, "IOException when trying to sync", e);
                throw e;
            } finally {
                Log.v(TAG, "sync completing");
            }

        } catch (final AuthenticatorException e) {
        	syncStatus = SyncManager.STATUS_AUTH_ERROR;
            Log.e(TAG, "AuthenticatorException", e);
            syncResult.stats.numParseExceptions++;
        } catch (final OperationCanceledException e) {
        	syncStatus = SyncManager.STATUS_CANCELED;
            Log.e(TAG, "OperationCanceledExcetpion", e);
        } catch (final IOException e) {
        	syncStatus = SyncManager.STATUS_IO_ERROR;
            Log.e(TAG, "IOException", e);
            syncResult.stats.numIoExceptions++;
        } catch (final AuthenticationException e) {
        	syncStatus = SyncManager.STATUS_AUTH_ERROR;
            Log.e(TAG, "AuthenticationException", e);
            syncResult.stats.numAuthExceptions++;
        } catch (final ParseOrderListException e) {
        	syncStatus = SyncManager.STATUS_PARSER_ERROR;
            Log.e(TAG, "ParseOrderListException", e);
            syncResult.stats.numParseExceptions++;
        } catch (XmlPullParserException e) {
        	syncStatus = SyncManager.STATUS_PARSER_ERROR;
            Log.e(TAG, "XmlPullParserException", e);
            syncResult.stats.numParseExceptions++;
        }

    	syncManager.notifySyncFinished(syncStatus);
    }

    private void removeLocalOrders(ArrayList<Long> ordersToRemove) {
    	// TODO improve remove several rows (batch delete)
		ContentResolver resolver = mContext.getContentResolver();
		for (long remoteId : ordersToRemove) {
			resolver.delete(Order.CONTENT_URI, Order.COLUMN_REMOTE_ID + "=?",
					new String[] {String.valueOf(remoteId)});
		}
	}

	private void prepareFetchAndExcludeLists(List<Long> ordersToFetch, List<Long> ordersToRemove) {
		ContentResolver resolver = mContext.getContentResolver();
		Cursor cursor = resolver.query(Order.CONTENT_URI, Order.REMOTE_ID_PROJECTION, null, null, null);
		ArrayList<Long> localOrders = new ArrayList<Long>();
    	while (cursor.moveToNext()) {
    		long remoteIdLocalDB = cursor.getLong(0);
    		if (!ordersToFetch.contains(remoteIdLocalDB)) {
    			// If the order was not listed but we have it on our database
    			// we should delete it.
    			ordersToRemove.add(remoteIdLocalDB);
    		} else {
        		localOrders.add(remoteIdLocalDB);
    		}
    	}
    	cursor.close();

    	// We don't need to fetch all the orders we already have stored again.
    	ordersToFetch.removeAll(localOrders);
    }

	private Order fetchOrder(long orderId, String token) throws ClientProtocolException, IOException,
	ParseOrderListException, XmlPullParserException {
        final String getOrderURL = mPrefs.getOrderServerAddress() + "/mobile/GetOrder";
        final HttpResponse resp;
		final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("orderId", String.valueOf(orderId)));
        final HttpEntity entity;
        try {
            entity = new UrlEncodedFormEntity(params);
        } catch (final UnsupportedEncodingException e) {
            // this should never happen.
            throw new IllegalStateException(e);
        }
        Log.i(TAG, "Starting sync to: " + getOrderURL);
        final HttpPost post = new HttpPost(getOrderURL);
        post.addHeader(entity.getContentType());
        post.setEntity(entity);
        resp = NetworkUtils.getHttpClient().execute(post);
        int statusCode = resp.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_OK) {
            InputStream istream = (resp.getEntity() != null) ? resp.getEntity().getContent() : null;
            if (istream != null) {
            	return OrderParser.parseOrderfromXml(istream);
            } else {
        		throw new ParseOrderListException("Failed to parse order: " + orderId);
            }
        } else {
        	throw new IOException();
        }
	}
}

