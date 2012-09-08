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

/**
 * Payment method
 */
public class PaymentMethod {

    /**
     * Payment method type
     */
    private PaymentMethodType type;

    /**
     * Payment method code
     */
    private PaymentMethodCode code;

    
    /**
     * Initializes a new instance of the PaymentMethod class 
     */
    public PaymentMethod() {
    }

    /**
     * Initializes a new instance of the PaymentMethod class
     * 
     * @param type
     * @param code
     */
    public PaymentMethod(PaymentMethodType type, PaymentMethodCode code) {
        this.type = type;
        this.code = code;
    }

    /**
     * @return the payment method type
     */
    public PaymentMethodType getType() {
        return type;
    }

    /**
     * Sets the payment method type 
     * @param type
     */
    public void setType(PaymentMethodType type) {
        this.type = type;
    }

    /**
     * @return the payment method code
     */
    public PaymentMethodCode getCode() {
        return code;
    }

    /**
     * Sets the payment method code
     * @param code
     */
    public void setCode(PaymentMethodCode code) {
        this.code = code;
    }

}