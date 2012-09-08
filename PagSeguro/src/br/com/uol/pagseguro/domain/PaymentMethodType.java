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
 * Defines a list of known payment method types.
 */
public final class PaymentMethodType {

    /**
     * Payment method type value
     * Example: 1
     */
    private final int value;

    /**
     * Credit card
     */
    public static final PaymentMethodType CREDIT_CARD = new PaymentMethodType(1);

    /**
     * Boleto - is a form of invoicing in Brazil
     */
    public static final PaymentMethodType BOLETO = new PaymentMethodType(2);

    /**
     * Online transfer
     */
    public static final PaymentMethodType ONLINE_TRANSFER = new PaymentMethodType(3);

    /**
     * PagSeguro account balance
     */
    public static final PaymentMethodType BALANCE = new PaymentMethodType(4);

    /**
     * OiPaggo
     */
    public static final PaymentMethodType OI_PAGGO = new PaymentMethodType(5);

    private static final Map VALUE_MAP = new HashMap();

    static {
        VALUE_MAP.put(new Integer(CREDIT_CARD.getValue()), CREDIT_CARD);
        VALUE_MAP.put(new Integer(BOLETO.getValue()), BOLETO);
        VALUE_MAP.put(new Integer(ONLINE_TRANSFER.getValue()), ONLINE_TRANSFER);
        VALUE_MAP.put(new Integer(BALANCE.getValue()), BALANCE);
        VALUE_MAP.put(new Integer(OI_PAGGO.getValue()), OI_PAGGO);
    }

    private PaymentMethodType(int value) {
        this.value = value;
    }

    public static PaymentMethodType fromValue(Integer value) {
        Iterator i = VALUE_MAP.keySet().iterator();

        while (i.hasNext()) {
            Integer type = (Integer) i.next();
            if (type.intValue() == value.intValue()) {
                return (PaymentMethodType) VALUE_MAP.get(type);
            }
        }

        return new PaymentMethodType(value.intValue());
    }

    public String toString() {
        return Integer.toString(getValue());
    }

    /**
     * @return payment method type value
     * Example: 1
     */
    public int getValue() {
        return value;
    }

    public boolean equals(PaymentMethodType other) {
        return other.getValue() == this.getValue();
    }
}