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
 * Defines a list of known transaction types.
 * This class is not an enum to enable the introduction of new shipping types
 * without breaking this version of the library.
 */
public final class TransactionType {

    private final int value;

    /**
     * Payment
     */
    public static final TransactionType PAYMENT = new TransactionType(1);

    /**
     * Transfer
     */
    public static final TransactionType TRANSFER = new TransactionType(2);

    /**
     * Fund addition
     */
    public static final TransactionType FUND_ADDITION = new TransactionType(3);

    /**
     * Withdraw
     */
    public static final TransactionType WITHDRAW = new TransactionType(4);

    /**
     * Charge
     */
    public static final TransactionType CHARGE = new TransactionType(5);

    /**
     * Donation
     */
    public static final TransactionType DONATION = new TransactionType(6);

    /**
     * Bonus
     */
    public static final TransactionType BONUS = new TransactionType(7);

    /**
     * Bonus repass
     */
    public static final TransactionType BONUS_REPASS = new TransactionType(8);

    /**
     * Operational
     */
    public static final TransactionType OPERATIONAL = new TransactionType(9);

    /**
     * Political donation
     */
    public static final TransactionType POLITICAL_DONATION = new TransactionType(10);

    private static final Map VALUE_MAP = new HashMap();

    static {
        VALUE_MAP.put(new Integer(PAYMENT.getValue()), PAYMENT);
        VALUE_MAP.put(new Integer(TRANSFER.getValue()), TRANSFER);
        VALUE_MAP.put(new Integer(FUND_ADDITION.getValue()), FUND_ADDITION);
        VALUE_MAP.put(new Integer(WITHDRAW.getValue()), WITHDRAW);
        VALUE_MAP.put(new Integer(CHARGE.getValue()), CHARGE);
        VALUE_MAP.put(new Integer(DONATION.getValue()), DONATION);
        VALUE_MAP.put(new Integer(BONUS.getValue()), BONUS);
        VALUE_MAP.put(new Integer(BONUS_REPASS.getValue()), BONUS_REPASS);
        VALUE_MAP.put(new Integer(OPERATIONAL.getValue()), OPERATIONAL);
        VALUE_MAP.put(new Integer(POLITICAL_DONATION.getValue()), POLITICAL_DONATION);

    }

    public static TransactionType fromValue(Integer value) {

        Iterator i = VALUE_MAP.keySet().iterator();

        while (i.hasNext()) {
            Integer type = (Integer) i.next();
            if (type.intValue() == value.intValue()) {
                return (TransactionType) VALUE_MAP.get(type);
            }
        }

        return new TransactionType(value.intValue());
    }

    public boolean equals(TransactionType other) {
        return this.getValue() == other.getValue();
    }

    public String toString() {
        return Integer.toString(getValue());
    }

    private TransactionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}