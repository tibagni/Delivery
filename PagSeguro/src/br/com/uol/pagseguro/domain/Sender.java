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
 * Represents the party on the transaction that is sending the money
 */
public class Sender {

    /** Sender name */
    private String name;

    /** Sender email */
    private String email;

    /** Sender phone */
    private Phone phone;

    
    /**
     * Initializes a new instance of the Sender class 
     */
    public Sender() {
    }

    /**
     * Sets the Sender name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the sender name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Sender e-mail
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * @return the sender e-mail
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the sender phone
     * @param phone
     */
    public void setPhone(Phone phone) {
        this.phone = phone;
    }
    
    /**
     * @return the sender phone
     */
    public Phone getPhone() {
        return phone;
    }
}