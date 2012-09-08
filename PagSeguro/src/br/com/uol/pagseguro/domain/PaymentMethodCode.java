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
 * Defines a list of known payment method codes. 
 */
public final class PaymentMethodCode {

    /**
     * Payment method code
     * Example: 101
     */
    private final int value;

    /**
     * VISA
     */
    public static final PaymentMethodCode VISA_CREDIT_CARD = new PaymentMethodCode(101);

    /**
     * MasterCard
     */
    public static final PaymentMethodCode MASTERCARD_CREDIT_CARD = new PaymentMethodCode(102);

    /**
     * American Express
     */
    public static final PaymentMethodCode AMEX_CREDIT_CARD = new PaymentMethodCode(103);

    /**
     * Diners
     */
    public static final PaymentMethodCode DINERS_CREDIT_CARD = new PaymentMethodCode(104);

    /**
     * Hipercard
     */
    public static final PaymentMethodCode HIPERCARD_CREDIT_CARD = new PaymentMethodCode(105);

    /**
     * Aura
     */
    public static final PaymentMethodCode AURA_CREDIT_CARD = new PaymentMethodCode(106);
    
    /**
     * Elo
     */
    public static final PaymentMethodCode ELO_CREDIT_CARD = new PaymentMethodCode(107);

    /**
     * Bradesco - boleto
     */
    public static final PaymentMethodCode BRADESCO_BOLETO = new PaymentMethodCode(201);

    /**
     * Santander - boleto
     */
    public static final PaymentMethodCode SANTANDER_BOLETO = new PaymentMethodCode(202);

    /**
     * Bradesco on-line transfer
     */
    public static final PaymentMethodCode BRADESCO_ONLINE_TRANSFER = new PaymentMethodCode(301);

    /**
     * Itau on-line transfer
     */
    public static final PaymentMethodCode ITAU_ONLINE_TRANSFER = new PaymentMethodCode(302);

    /**
     * Unibanco on-line transfer
     */
    public static final PaymentMethodCode UNIBANCO_ONLINE_TRANSFER = new PaymentMethodCode(303);

    /**
     * Banco do Brasil on-line transfer 
     */
    public static final PaymentMethodCode BANCO_BRASIL_ONLINE_TRANSFER = new PaymentMethodCode(304);

    /**
     * Banco Real on-line transfer
     */
    public static final PaymentMethodCode REAL_ONLINE_TRANSFER = new PaymentMethodCode(305);

    /**
     * Banrisul on-line transfer 
     */
    public static final PaymentMethodCode BANRISUL_ONLINE_TRANSFER = new PaymentMethodCode(306);

    /**
     * PagSeguro account balance
     */
    public static final PaymentMethodCode PS_BALANCE = new PaymentMethodCode(401);

    /**
     * OiPaggo
     */
    public static final PaymentMethodCode OI_PAGGO = new PaymentMethodCode(501);

    private static final Map VALUE_MAP = new HashMap();

    static {
        VALUE_MAP.put(new Integer(VISA_CREDIT_CARD.getValue()), VISA_CREDIT_CARD);
        VALUE_MAP.put(new Integer(MASTERCARD_CREDIT_CARD.getValue()), MASTERCARD_CREDIT_CARD);
        VALUE_MAP.put(new Integer(AMEX_CREDIT_CARD.getValue()), AMEX_CREDIT_CARD);
        VALUE_MAP.put(new Integer(DINERS_CREDIT_CARD.getValue()), DINERS_CREDIT_CARD);
        VALUE_MAP.put(new Integer(HIPERCARD_CREDIT_CARD.getValue()), HIPERCARD_CREDIT_CARD);
        VALUE_MAP.put(new Integer(AURA_CREDIT_CARD.getValue()), AURA_CREDIT_CARD);
        VALUE_MAP.put(new Integer(ELO_CREDIT_CARD.getValue()), ELO_CREDIT_CARD);
        VALUE_MAP.put(new Integer(BRADESCO_BOLETO.getValue()), BRADESCO_BOLETO);
        VALUE_MAP.put(new Integer(SANTANDER_BOLETO.getValue()), SANTANDER_BOLETO);
        VALUE_MAP.put(new Integer(BRADESCO_ONLINE_TRANSFER.getValue()), BRADESCO_ONLINE_TRANSFER);
        VALUE_MAP.put(new Integer(ITAU_ONLINE_TRANSFER.getValue()), ITAU_ONLINE_TRANSFER);
        VALUE_MAP.put(new Integer(UNIBANCO_ONLINE_TRANSFER.getValue()), UNIBANCO_ONLINE_TRANSFER);
        VALUE_MAP.put(new Integer(BANCO_BRASIL_ONLINE_TRANSFER.getValue()), BANCO_BRASIL_ONLINE_TRANSFER);
        VALUE_MAP.put(new Integer(REAL_ONLINE_TRANSFER.getValue()), REAL_ONLINE_TRANSFER);
        VALUE_MAP.put(new Integer(BANRISUL_ONLINE_TRANSFER.getValue()), BANRISUL_ONLINE_TRANSFER);
        VALUE_MAP.put(new Integer(PS_BALANCE.getValue()), PS_BALANCE);
        VALUE_MAP.put(new Integer(OI_PAGGO.getValue()), OI_PAGGO);
    }

    public static PaymentMethodCode fromValue(Integer value) {

        Iterator i = VALUE_MAP.keySet().iterator();

        while (i.hasNext()) {
            Integer type = (Integer) i.next();
            if (type.intValue() == value.intValue()) {
                return (PaymentMethodCode) VALUE_MAP.get(type);
            }
        }

        return new PaymentMethodCode(value.intValue());
    }

    public boolean equals(PaymentMethodCode other) {
        return this.value == other.getValue();
    }

    public String toString() {
        return Integer.toString(getValue());
    }

    private PaymentMethodCode(int value) {
        this.value = value;
    }

    
    /**
     * @return the payment method code value
     * Example: 101
     */
    public int getValue() {
        return value;
    }

}