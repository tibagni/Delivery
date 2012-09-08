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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Defines a list of known shipping types.
 * this class is not an enum to enable the introduction of new shipping types
 * without breaking this version of the library. 
 */
public final class ShippingType {

    /**
     * the shipping type value
     * Example: 1
     */
    private final int value;

    /**
     * PAC
     */
    public static final ShippingType PAC = new ShippingType(1);

    /**
     * SEDEX
     */
    public static final ShippingType SEDEX = new ShippingType(2);

    /**
     * Not specified shipping type
     */
    public static final ShippingType NOT_SPECIFIED = new ShippingType(3);

    private static final Map VALUE_MAP = new HashMap();

    static {
        VALUE_MAP.put(new Integer(PAC.getValue()), PAC);
        VALUE_MAP.put(new Integer(SEDEX.getValue()), SEDEX);
        VALUE_MAP.put(new Integer(NOT_SPECIFIED.getValue()), NOT_SPECIFIED);
    }

    public static ShippingType fromValue(Integer value) {

        Iterator i = VALUE_MAP.keySet().iterator();

        while (i.hasNext()) {
            Integer type = (Integer) i.next();
            if (type.intValue() == value.intValue()) {
                return (ShippingType) VALUE_MAP.get(type);
            }
        }

        return new ShippingType(value.intValue());
    }

    public boolean equals(ShippingType other) {
        return this.value == other.getValue();
    }

    public String toString() {
        return Integer.toString(getValue());
    }

    private ShippingType(int value) {
        this.value = value;
    }

    /**
     * @return the value of the shipping type
     */
    public int getValue() {
        return value;
    }
}