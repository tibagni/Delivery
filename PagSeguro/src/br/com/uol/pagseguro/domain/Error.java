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

import br.com.uol.pagseguro.exception.PagSeguroServiceException;

/**
 * Represents a PagSeguro web service error
 * @see PagSeguroServiceException 
 */
public class Error {

    /**
     * Error code
     */
    private String code;

    /**
     * Error description
     */
    private String message;

    public Error() {
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the error description
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error description
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
