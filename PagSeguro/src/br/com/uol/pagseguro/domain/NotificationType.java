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
    Defines a list of known notification types.
   	This class is not an enum to enable the introduction of new shipping types
    without breaking this version of the library.
 */
public final class NotificationType {

    private final String value;

    /**
     * Transaction
     */
    public static final NotificationType TRANSACTION = new NotificationType("transaction");

    private static final Map VALUE_MAP = new HashMap();

    static {
        VALUE_MAP.put(TRANSACTION.getValue(), TRANSACTION);
    }

    /**
     * @param value
     * @return the NotificationType corresponding to the informed value 
     */
    public static NotificationType fromValue(String value) {

        Iterator i = VALUE_MAP.keySet().iterator();

        while (i.hasNext()) {
            String type = (String) i.next();
            if (type.equals(value)) {
                return (NotificationType) VALUE_MAP.get(type);
            }
        }

        return new NotificationType(value);
    }

    public boolean equals(NotificationType other) {
        return this.value.equals(other.value);
    }

    public String toString() {
        return getValue();
    }

    private NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}