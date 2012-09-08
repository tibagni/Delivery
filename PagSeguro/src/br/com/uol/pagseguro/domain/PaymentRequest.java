/**
 * Copyright [2011] [PagSeguro Internet Ltda.]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package br.com.uol.pagseguro.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.service.PaymentService;

/**
 * Represents a payment request
 */
public class PaymentRequest {

    /**
     * Party that will be sending the money
     */
    private Sender sender;

    /**
     * Payment currency. 
 	 * @see Currency
     */
    private String currency;

    /**
     * Products/items in this payment request
     */
    private List items;

    /**
     * Uri to where the PagSeguro payment page should redirect the user after the payment information is processed.
     * Typically this is a confirmation page on your web site.
     */
    private URL redirectURL;

    /**
     * Extra amount to be added to the transaction total
     * 
     * This value can be used to add an extra charge to the transaction
     * or provide a discount in the case ExtraAmount is a negative value.
     */
    private BigDecimal extraAmount;

    /**
     * Reference code
     * 
     * Optional. You can use the reference code to store an identifier so you can 
     * associate the PagSeguro transaction to a transaction in your system.
     */
    private String reference;

    /**
     * Shipping information associated with this payment request
     */
    private Shipping shipping;

    /**
     * How long this payment request will remain valid, in seconds.
     * 
     * Optional. After this payment request is submitted, the payment code returned
     * will remain valid for the period specified here. 
     */
    private BigInteger maxAge;

    /**
     * How many times the payment redirect uri returned by the payment web service can be accessed.
     * 
     * Optional. After this payment request is submitted, the payment redirect uri returned by
     * the payment web service will remain valid for the number of uses specified here. 
     */
    private BigInteger maxUses;

    
    /**
     * Initializes a new instance of the PaymentRequest class
     */
    public PaymentRequest() {
        items = new ArrayList();
    }

    /**
     * @return the sender
     * 
     * Party that will be sending the Uri to where the PagSeguro payment page should redirect the user after the payment information is processed.
     * money
     */
    public Sender getSender() {
        return sender;
    }

    /**
     * Sets the Sender, party that will be sending the money
     * 
     * @param sender
     * @return
     */
    public PaymentRequest setSender(Sender sender) {
        this.sender = sender;
        return this;
    }

    /**
     * Sets the Sender, party that will be sending the money
     * 
     * @param name
     * @param email
     * @return
     */
    public PaymentRequest setSender(String name, String email) {
        if (sender == null) {
            sender = new Sender();
        }
        sender.setName(name);
        sender.setEmail(email);
        return this;
    }

    /**
     * Sets the Sender, party that will be sending the money
     * 
     * @param name
     * @param email
     * @param areaCode
     * @param number
     * @return
     */
    public PaymentRequest setSender(String name, String email, String areaCode, String number) {
        if (sender == null) {
            sender = new Sender();
        }
        sender.setName(name);
        sender.setEmail(email);
        sender.setPhone(new Phone(areaCode, number));
        return this;
    }

    /**
     * Sets the Sender phone number, phone of the party that will be sending the money
     * 
     * @param areaCode
     * @param number
     * @return
     */
    public PaymentRequest setSenderPhone(String areaCode, String number) {
        if (sender == null) {
            sender = new Sender();
        }
        sender.setPhone(new Phone(areaCode, number));
        return this;
    }

    /**
     * @return the currency
     * Example: BRL
     * @see Currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency
     * @see Currency
     *  
     * @param currency
     * @return
     */
    public PaymentRequest setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    /**
     * @return the items/products list in this payment request
     * @see Item
     */
    public List getItems() {
        return items;
    }

    /**
     * Sets the items/products list in this payment request
     * @see Item
     * 
     * @param items
     * @return
     */
    public PaymentRequest setItems(List items) {
        this.items = items;
        return this;
    }

    /**
     * Adds a new product/item in this payment request
     * @see Item
     * 
     * @param id
     * @param description
     * @param quantity
     * @param amount
     * @param weight
     * @param shippingCost
     */
    public void addItem(String id, String description, Integer quantity, BigDecimal amount, Long weight, BigDecimal shippingCost) {
        if (items == null) {
            items = new ArrayList();
        }
        items.add(new Item(id, description, quantity, amount, weight, shippingCost));
    }

    /**
     * Adds a new product/item in this payment request
     * @see Item
     * 
     * @param item
     */
    public void addItem(Item item) {
        if (items == null) {
            items = new ArrayList();
        }
        items.add(item);
    }

    /**
     * Uri to where the PagSeguro payment page should redirect the user after the payment information is processed.
     * Typically this is a confirmation page on your web site.
     * 
     * @return the redirectURL
     */
    public URL getRedirectURL() {
        return redirectURL;
    }

    /**
     * Sets the redirect URL
     * 
     * Uri to where the PagSeguro payment page should redirect the user after the payment information is processed.
     * Typically this is a confirmation page on your web site.
     * 
     * @param redirectURL
     * @return
     */
    public PaymentRequest setRedirectURL(URL redirectURL) {
        this.redirectURL = redirectURL;
        return this;
    }

    /**
     * This value can be used to add an extra charge to the transaction
     * or provide a discount in the case ExtraAmount is a negative value.
     * 
     * @return the extra amount
     */
    public BigDecimal getExtraAmount() {
        return extraAmount;
    }

    /**
     * Sets the extra amount
     * This value can be used to add an extra charge to the transaction
     * or provide a discount in the case <b>extraAmount</b> is a negative value.
     * 
     * @param extraAmount
     * @return
     */
    public PaymentRequest setExtraAmount(BigDecimal extraAmount) {
        this.extraAmount = extraAmount;
        return this;
    }

    /**
     * @return the reference of this payment request
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the reference of this payment request
     * 
     * @param reference
     * @return
     */
    public PaymentRequest setReference(String reference) {
        this.reference = reference;
        return this;
    }

    /**
     * @return the shipping information for this payment request
     * @see Shipping
     */
    public Shipping getShipping() {
        return shipping;
    }

    /**
     * Sets the shipping information for this payment request
     * @see Shipping
     * 
     * @param shipping

     */
    public PaymentRequest setShipping(Shipping shipping) {
        this.shipping = shipping;
        return this;
    }

    /**
     * Sets the shipping type for this payment request
     * @see ShippingType
     * 
     * @param type
     * @return
     */
    public PaymentRequest setShippingType(ShippingType type) {
        if (shipping == null) {
            shipping = new Shipping();
        }
        shipping.setType(type);
        return this;
    }

    /**
     * Sets the shipping information for this payment
     * 
     * @param type
     * @param country
     * @param state
     * @param city
     * @param district
     * @param postalCode
     * @param street
     * @param number
     * @param complement
     * @return
     */
    public PaymentRequest setShipping(ShippingType type, String country, String state, String city, String district,
            String postalCode, String street, String number, String complement) {
        if (shipping == null) {
            shipping = new Shipping();
        }
        shipping.setType(type);
        shipping.setAddress(new Address(country, state, city, district, postalCode, street, number, complement));
        return this;
    }

    /**
     * Sets the shipping address for this payment request
     * 
     * @param country
     * @param state
     * @param city
     * @param district
     * @param postalCode
     * @param street
     * @param number
     * @param complement
     * @return
     */
    public PaymentRequest setShippingAddress(String country, String state, String city, String district,
            String postalCode, String street, String number, String complement) {
        if (shipping == null) {
            shipping = new Shipping();
        }
        shipping.setAddress(new Address(country, state, city, district, postalCode, street, number, complement));
        return this;
    }

    /**
     * @return the max age of this payment request
     * 
     * After this payment request is submitted, the payment code returned
     * will remain valid for the period specified. 
     */
    public BigInteger getMaxAge() {
        return maxAge;
    }

    /**
     * Sets the max age of this payment request
     * After this payment request is submitted, the payment code returned
     * will remain valid for the period specified here. 
     * 
     * @param maxAge
     * @return
     */
    public PaymentRequest setMaxAge(BigInteger maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    /**
     * After this payment request is submitted, the payment redirect uri returned by
     * the payment web service will remain valid for the number of uses specified here.
     * 
     * @return the max uses configured for this payment request
     */
    public BigInteger getMaxUses() {
        return maxUses;
    }

    /**
     * Sets the max uses of this payment request
     * 
     * After this payment request is submitted, the payment redirect uri returned by
     * the payment web service will remain valid for the number of uses specified here.
     * 
     * @param maxUses
     * @return
     */
    public PaymentRequest setMaxUses(BigInteger maxUses) {
        this.maxUses = maxUses;
        return this;
    }

    /**
     * Calls the PagSeguro web service and register this request for payment
     * 
     * @param credentials
     * @return The URL to where the user needs to be redirected to in order to complete the payment process
     * @throws PagSeguroServiceException
     */
    public URL register(Credentials credentials) throws PagSeguroServiceException {
        return PaymentService.doPayment(credentials, this);
    }
    
    public String toString(){
        StringBuffer sb = new StringBuffer(128);
    	sb.append("PaymentRequest(Reference=");
    	sb.append(reference);
    	sb.append(",SenderEmail=");
    	sb.append(sender != null ? sender.getEmail() : null);
    	sb.append(")");
    	return sb.toString();
    }
    
}