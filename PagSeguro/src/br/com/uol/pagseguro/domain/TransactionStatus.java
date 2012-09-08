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
 * Defines a list of known transaction statuses.
 * This class is not an enum to enable the introduction of new shipping types
 * without breaking this version of the library.
 */
public final class TransactionStatus {

    /**
     * the value of the transaction status
     * Example: 3
     */
    private final int value;

    /**
     * Initiated
     */
    public static final TransactionStatus INITIATED = new TransactionStatus(0);

    /**
     * Waiting payment
     */
    public static final TransactionStatus WAITING_PAYMENT = new TransactionStatus(1);

    /**
     * In analysis
     */
    public static final TransactionStatus IN_ANALYSIS = new TransactionStatus(2);

    /**
     * Paid
     */
    public static final TransactionStatus PAID = new TransactionStatus(3);

    /**
     * Available
     */
    public static final TransactionStatus AVAILABLE = new TransactionStatus(4);

    /**
     * In dispute
     */
    public static final TransactionStatus IN_DISPUTE = new TransactionStatus(5);

    /**
     * Refunded
     */
    public static final TransactionStatus REFUNDED = new TransactionStatus(6);

    /**
     * Cancelled
     */
    public static final TransactionStatus CANCELLED = new TransactionStatus(7);

    private static final Map VALUE_MAP = new HashMap();

    static {
        VALUE_MAP.put(new Integer(INITIATED.getValue()), INITIATED);
        VALUE_MAP.put(new Integer(WAITING_PAYMENT.getValue()), WAITING_PAYMENT);
        VALUE_MAP.put(new Integer(IN_ANALYSIS.getValue()), IN_ANALYSIS);
        VALUE_MAP.put(new Integer(PAID.getValue()), PAID);
        VALUE_MAP.put(new Integer(AVAILABLE.getValue()), AVAILABLE);
        VALUE_MAP.put(new Integer(IN_DISPUTE.getValue()), IN_DISPUTE);
        VALUE_MAP.put(new Integer(REFUNDED.getValue()), REFUNDED);
        VALUE_MAP.put(new Integer(CANCELLED.getValue()), CANCELLED);
    }

    public static TransactionStatus fromValue(Integer value) {

        Iterator i = VALUE_MAP.keySet().iterator();

        while (i.hasNext()) {
            Integer status = (Integer) i.next();
            if (status.intValue() == value.intValue()) {
                return (TransactionStatus) VALUE_MAP.get(status);
            }
        }

        return new TransactionStatus(value.intValue());
    }

    public boolean equals(TransactionStatus other) {
        return this.getValue() == other.getValue();
    }

    public String toString() {
        return Integer.toString(getValue());
    }

    private TransactionStatus(int value) {
        this.value = value;
    }

    /**
     * @return the transaction status value.
     */
    public int getValue() {
        return value;
    }
}