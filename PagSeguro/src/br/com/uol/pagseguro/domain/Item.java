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
 * Represents a product/item in a transaction
 */
public class Item {

    /**
     * Product identifier, such as SKU
     */
    private String id;

    /**
     * Product description
     */
    private String description;

    /**
     * Quantity
     */
    private Integer quantity;

    /**
     * Product unit price
     */
    private BigDecimal amount;

    /**
     * Single unit weight, in grams
     */
    private Long weight;

    /**
     * Single unit shipping cost
     */
    private BigDecimal shippingCost;

    /**
     * Initializes a new instance of the Item class
     */
    public Item() {

    }

    /**
     * Initializes a new instance of the Item class
     * 
     * @param id
     * @param description
     * @param quantity
     * @param amount
     * @param weight
     * @param shippingCost
     */
    public Item(String id, String description, Integer quantity, BigDecimal amount, Long weight, BigDecimal shippingCost) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
        this.amount = amount;
        this.weight = weight;
        this.shippingCost = shippingCost;
    }

    /**
     * @return the product identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the product identifier
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the product description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the product description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the unit amount for this item
     * @param amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the weight
     */
    public Long getWeight() {
        return weight;
    }

    /**
     * Sets the single unit weight
     * @param weight
     */
    public void setWeight(Long weight) {
        this.weight = weight;
    }

    /**
     * @return the unit shipping cost for this item
     */
    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    /**
     * Sets the unit shipping cost for this item
     * @param shippingCost
     */
    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String toString() {
        return "[id: " + id + ", description: " + description + ", quantity: " + quantity + ", amount: " + amount
                + ", weight: " + weight + ", shippingCost: " + shippingCost + "]";
    }
    
}