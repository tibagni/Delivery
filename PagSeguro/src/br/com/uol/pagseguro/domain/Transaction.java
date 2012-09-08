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
import java.util.Date;
import java.util.List;

/**
 * Represents a PagSeguro transaction
 */
public class Transaction {

    /** 
     * Last event date
     * Date the last notification about this transaction was sent 
     */
    private Date lastEventDate;

    /** 
     * Transaction date 
     */
    private Date date;

    /** 
     * Transaction code 
     */
    private String code;

    /**
     *  Reference code
     *  You can use the reference code to store an identifier so you can 
     *  associate the PagSeguro transaction to a transaction in your system.
     */
    private String reference;

    /** 
     * Transaction type
     * @see TransactionType 
     */
    private TransactionType type;

    /** 
     * Transaction status
     * @see TransactionStatus
     */
    private TransactionStatus status;

    /**
     * Payment method
     * @see PaymentMethod
     */
    private PaymentMethod paymentMethod;

    /**
     * Gross amount of the transaction
     */
    private BigDecimal grossAmount;

    /**
     * Discount amount
     */
    private BigDecimal discountAmount;

    /**
     * Fee amount
     */
    private BigDecimal feeAmount;

    /**
     * Net amount
     */
    private BigDecimal netAmount;

    /**
     * Extra amount
     */
    private BigDecimal extraAmount;

    /**
     * Installment count
     */
    private Integer installmentCount;

    /**
     * item/product list in this transaction
     * @see Item
     */
    private List items;

    /**
     * Payer information, who is sending money
     * @see Sender
     */
    private Sender sender;

    /**
     * Shipping information
     * @see Shipping
     */
    private Shipping shipping;

    /**
     * Initializes a new instance of the Transaction class
     */
    public Transaction() {
    }

    /**
     * Date the last notification about this transaction was sent
     * @return the last event date
     */
    public Date getLastEventDate() {
        return lastEventDate;
    }

    /**
     * Sets the last event date
     * 
     * @param lastEventDate
     */
    public void setLastEventDate(Date lastEventDate) {
        this.lastEventDate = lastEventDate;
    }

    /**
     * @return the transaction date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the transaction date
     * 
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the transaction code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the transaction code
     * 
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * You can use the reference code to store an identifier so you can 
     *  associate the PagSeguro transaction to a transaction in your system.
     *  
     * @return the reference code
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the reference code
     * 
     * @param reference
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * @return the transaction Type
     * @see TransactionType
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * Sets the transaction type
     * @see TransactionType
     * 
     * @param type
     */
    public void setType(TransactionType type) {
        this.type = type;
    }

    /**
     * @return the transaction status
     * @see TransactionStatus
     */
    public TransactionStatus getStatus() {
        return status;
    }

    /**
     * Sets the transaction status
     * @see TransactionStatus
     * 
     * @param status
     */
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    /**
     * @return the payment method used in this transaction
     * @see PaymentMethod
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Sets the payment method for this transaction
     * 
     * @param paymentMethod
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * @return the transaction gross amount
     */
    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    /**
     * Sets the transaction gross amount
     * 
     * @param grossAmount
     */
    public void setGrossAmount(BigDecimal totalValue) {
        this.grossAmount = totalValue;
    }

    /**
     * @return the discount amount
     */
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Sets the discount amount
     * 
     * @param discountAmount
     */
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**
     * @return the fee amount
     */
    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    /**
     * Sets the fee amount
     * 
     * @param feeAmount
     */
    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    /**
     * @return the net amount
     */
    public BigDecimal getNetAmount() {
        return netAmount;
    }

    /**
     * Sets the net amount
     * 
     * @param netAmount
     */
    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    /**
     * @return the extra amount
     */
    public BigDecimal getExtraAmount() {
        return extraAmount;
    }

    /**
     * Sets the extra amount
     * 
     * @param extraAmount
     */
    public void setExtraAmount(BigDecimal extraAmount) {
        this.extraAmount = extraAmount;
    }

    /**
     * @return the installment count
     */
    public Integer getInstallmentCount() {
        return installmentCount;
    }

    /**
     * Sets the installment count in this transaction
     * 
     * @param installmentCount
     */
    public void setInstallmentCount(Integer installmentCount) {
        this.installmentCount = installmentCount;
    }

    /**
     * @return the items/products list in this transaction
     * @see Item
     */
    public List getItems() {
        return items;
    }

    /**
     * Sets the list of items/products in this transaction
     * @see Item
     * 
     * @param items
     */
    public void setItems(List items) {
        this.items = items;
    }

    /**
     * @return the items/products count in this transaction
     */
    public Integer getItemCount() {
        return items == null ? null : new Integer(items.size());
    }

    /**
     * @return the sender information
     * @see Sender
     */
    public Sender getSender() {
        return sender;
    }

    /**
     * Sets the sender information, who is sending money in this transaction
     * @see Sender 
     * 
     * @param sender
     */
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    /**
     * @return the shipping information
     * @see Shipping
     */
    public Shipping getShipping() {
        return shipping;
    }

    /**
     * sets the shipping information for this transaction
     * @see Shipping
     * 
     * @param shipping
     */
    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer(256);
        sb.append("Transaction(Code=");
        sb.append(code);
        sb.append(",Date=");
        sb.append(date);
        sb.append(",Reference=");
        sb.append(reference);
        sb.append(",Status=");
        sb.append(status.getValue());
        sb.append(",Email=");
        sb.append(sender != null ? sender.getEmail() : null);
        sb.append(",ItemsCount=");
        sb.append(items != null ? items.size() : 0);
        sb.append(")");
    	return sb.toString();
    }
}