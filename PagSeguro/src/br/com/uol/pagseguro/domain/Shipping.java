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

import java.math.BigDecimal;

/**
 * Shipping information
 */
public class Shipping {

    /** 
     * Shipping address 
     */
    private Address address;

    /** 
     * Shipping type. See the ShippingType helper class for a list of known shipping types. 
     */
    private ShippingType type;

    /** 
     * shipping cost. 
     */
    private BigDecimal cost;

    /**
     * Initializes a new instance of the Shipping class 
     */
    public Shipping() {
    }

    /**
     * Sets the shipping address
     * @param address
     * @see Address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @return the shipping address
     * @see Address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the shipping type
     * @param type
     * @see ShippingType
     */
    public void setType(ShippingType type) {
        this.type = type;
    }

    /**
     * @return the shipping type
     * @see ShippingType
     */
    public ShippingType getType() {
        return type;
    }

    /**
     * Sets the cost
     * @param cost
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }
}