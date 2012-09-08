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

/**
 * Represents a summary of a PagSeguro transaction, typically returned by search services.
 */
public class TransactionSummary {

    /** 
     * Transaction status
     * @see TransactionStatus
     */
    private TransactionStatus status;

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
     * Last event date
     * Date the last notification about this transaction was sent
     */
    private Date lastEvent;

    /** Transaction date */
    private Date date;

    /** Transaction Code. */
    private String code;

    /** Transaction Reference. */
    private String reference;

    /** Transaction Gross Amount. */
    private BigDecimal grossAmount;

    /** Transaction Type */
    private TransactionType type;

    /** Payment Method. */
    private PaymentMethod paymentMethod;

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(BigDecimal extraAmount) {
        this.extraAmount = extraAmount;
    }

    public Date getLastEvent() {
        return lastEvent;
    }

    public void setLastEvent(Date lastEvent) {
        this.lastEvent = lastEvent;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethodType getPaymentMethodType() {
        return paymentMethod.getType();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String toString(){
    	return "Status: " + status;	
    }
    
}
