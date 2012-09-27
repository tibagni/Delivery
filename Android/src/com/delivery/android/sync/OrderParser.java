package com.delivery.android.sync;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.delivery.android.provider.Address;
import com.delivery.android.provider.Order;

public class OrderParser {

	public static Order parseOrderfromXml(InputStream xmlStream) throws IOException, XmlPullParserException {
		try {
			XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(xmlStream, "ISO-8859-1");
            parser.nextTag();

            return readOrder(parser);
		} finally {
			xmlStream.close();
		}
	}

	private static Order readOrder(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, null, "order");
	    Order order = new Order();
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("id")) {
	            order.setRemoteId(readLongTag(parser, "id"));
	        } else if (name.equals("client")) {
	            order.setClientName(readStringTag(parser, "client"));
	        } else if (name.equals("datetime")) {
	            order.setDateTime(readStringTag(parser, "datetime"));
	        }  else if (name.equals("address")) {
	            order.setRemoteAddress(readAddress(parser));
	        } else if (name.equals("charge")) {
	            order.setCharge(readIntTag(parser, "charge") == 1);
	        } else if (name.equals("change")) {
	            order.setChange(readDoubleTag(parser, "change"));
	        } else if (name.equals("paymentVal")) {
	            order.setPaymentValue(readDoubleTag(parser, "paymentVal"));
	        } else if (name.equals("description")) {
	            order.setDescription(readStringTag(parser, "description"));
	        } else {
	            skip(parser);
	        }
	    }
	    return order;
	}

	private static Address readAddress(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, null, "address");
	    Address address = new Address();
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("street")) {
	            address.setStreet(readStringTag(parser, "street"));
	        } else if (name.equals("number")) {
	            address.setNumber(readIntTag(parser, "number"));
	        } else if (name.equals("complement")) {
	            address.setComplement(readStringTag(parser, "complement"));
	        } else if (name.equals("district")) {
	            address.setDistrict(readStringTag(parser, "district"));
	        } else if (name.equals("city")) {
	            address.setCity(readStringTag(parser, "city"));
	        } else if (name.equals("uf")) {
	            address.setUf(readStringTag(parser, "uf"));
	        } else if (name.equals("zip")) {
	            address.setZip(readStringTag(parser, "zip"));
	        } else {
	            skip(parser);
	        }
	    }
	    return address;
	}

	private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}

	private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }

	private static long readLongTag(XmlPullParser parser, String tag) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, tag);
	    String text = readText(parser);
	    parser.require(XmlPullParser.END_TAG, null, tag);
	    return Long.parseLong(text);
	}

	private static int readIntTag(XmlPullParser parser, String tag) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, tag);
	    String text = readText(parser);
	    parser.require(XmlPullParser.END_TAG, null, tag);
	    return Integer.parseInt(text);
	}

	private static String readStringTag(XmlPullParser parser, String tag) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, tag);
	    String text = readText(parser);
	    parser.require(XmlPullParser.END_TAG, null, tag);
	    return text;
	}

	private static double readDoubleTag(XmlPullParser parser, String tag) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, tag);
	    String text = readText(parser);
	    parser.require(XmlPullParser.END_TAG, null, tag);
	    return new BigDecimal(text).doubleValue();
	}
}
