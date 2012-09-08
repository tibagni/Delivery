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
 * HTTP errors that PagSeguro web services can return.
 */
public final class HttpError {

    /**
     * HTTP error 400 - Verify that all parameters were filled correctly. Check the errors messages, correct your
     * request and try again.
     */
    public static final HttpError BAD_REQUEST = new HttpError(400,
            "Verify that all parameters were filled correctly. Check the errors messages, "
                    + "correct your request and try again.");

    /**
     * HTTP error 401 - Invalid credentials, Make sure you entered your credentials(e-mail / token) correctly.
     */
    public static final HttpError UNAUTHORIZED = new HttpError(401,
            "Invalid credentials. Make sure you entered your credentials(e-mail / token) correctly.");

    /**
     * HTTP error 403 - You don't have permission to call this service.
     */
    public static final HttpError FORBIDDEN = new HttpError(403, "You don't have permission to call this service.");

    /**
     * HTTP error 404 - Check if you are calling the correct URL.
     */
    public static final HttpError NOT_FOUND = new HttpError(404, "Check if you are calling the correct URL.");

    /**
     * HTTP error 500 - Internal server error, please try again.
     */
    public static final HttpError INTERNAL_SERVER_ERROR = new HttpError(500, "Internal server error, please try again.");

    private static final Map VALUE_MAP = new HashMap();

    // put all elements in a Hash to obtain them key/value
    static {
        VALUE_MAP.put(new Integer(NOT_FOUND.getValue()), NOT_FOUND);
        VALUE_MAP.put(new Integer(FORBIDDEN.getValue()), FORBIDDEN);
        VALUE_MAP.put(new Integer(INTERNAL_SERVER_ERROR.getValue()), INTERNAL_SERVER_ERROR);
        VALUE_MAP.put(new Integer(BAD_REQUEST.getValue()), BAD_REQUEST);
        VALUE_MAP.put(new Integer(UNAUTHORIZED.getValue()), UNAUTHORIZED);
    }

    private int value;
    private String message;

    public int getValue() {
        return value;
    }

    public static HttpError fromValue(int value) {
        Iterator i = VALUE_MAP.keySet().iterator();
        while (i.hasNext()) {
            HttpError httpError = (HttpError) VALUE_MAP.get((Integer) i.next());
            if (httpError.getValue() == value) {
                return httpError;
            }
        }
        return new HttpError(value, null);
    }

    public boolean equals(HttpError other) {
        return this.value == other.getValue();
    }

    public String toString() {
        return Integer.toString(getValue());
    }

    /**
     * @return the error description
     */
    public String getMessage() {
        return message;
    }

    private HttpError(int value, String message) {
        this.value = value;
        this.message = message;
    }

}
