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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a page of transactions returned by the transaction search service
 */
public class TransactionSearchResult {

    /** 
     * Date/time when this search was executed 
     */
    private Date date;

    /** 
     * Transactions in the current page
     */
    private int resultsInThisPage;

    /** 
     * Total number of pages 
     */
    private int totalPages;

    /** 
     * Current page. 
     */
    private int page;

    /** 
     * Transactions in this page 
     */
    private List transactionSummaries;

    /**
     * @return the date/time when this search was executed
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the date/time when this search was executed 
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the number of Transactions in the current page
     */
    public int getResultsInThisPage() {
        return resultsInThisPage;
    }

    /**
     * Sets the number of Transactions in the current page
     * 
     * @param resultsInThisPage
     */
    public void setResultsInThisPage(int resultsInThisPage) {
        this.resultsInThisPage = resultsInThisPage;
    }

    /**
     * @return the total number of pages 
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Sets the total number of pages 
     * 
     * @param totalPages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * @return the current page number
     */
    public int getPage() {
        return page;
    }

    /**
     * Sets the current page number
     * 
     * @param page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * @return the Transactions in this page
     * @see TransactionSummary
     */
    public List getTransactionSummaries() {
        return transactionSummaries;
    }

    /**
     * Sets the transactions in this page
     * 
     * @param transactionSummaries
     */
    public void setTransactionSummaries(List transactionSummaries) {
        this.transactionSummaries = transactionSummaries;
    }

    /** 
     * @param transactionSummary
     */
    public void addTransactionSummary(TransactionSummary transactionSummary) {
        if (getTransactionSummaries() == null) {
            setTransactionSummaries(new LinkedList());
        }
        getTransactionSummaries().add(transactionSummary);
    }
    
    public String toString(){
        StringBuffer sb = new StringBuffer(256);
        sb.append("TransactionSearchResult(Date=");
        sb.append(date);
        sb.append(",CurrentPage=");
        sb.append(page);
        sb.append(",TotalPages=");
        sb.append(totalPages);
        sb.append(",Transactions in this page=");
        sb.append(transactionSummaries != null ? transactionSummaries.size() : 0);
    	
    	return sb.toString();
    }

}
