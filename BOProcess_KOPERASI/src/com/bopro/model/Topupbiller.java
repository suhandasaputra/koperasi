/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.model;

/**
 *
 * @author syukur
 */
public class Topupbiller {
    private String billercode;
    private String amount;
    private String before_balance;
    private String current_balance;
    private String bank_name;
    private String acct_no;
    private String transfer_date;
    private String topup_date;

    /**
     * @return the billercode
     */
    public String getBillercode() {
        return billercode;
    }

    /**
     * @param billercode the billercode to set
     */
    public void setBillercode(String billercode) {
        this.billercode = billercode;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return the before_balance
     */
    public String getBefore_balance() {
        return before_balance;
    }

    /**
     * @param before_balance the before_balance to set
     */
    public void setBefore_balance(String before_balance) {
        this.before_balance = before_balance;
    }

    /**
     * @return the current_balance
     */
    public String getCurrent_balance() {
        return current_balance;
    }

    /**
     * @param current_balance the current_balance to set
     */
    public void setCurrent_balance(String current_balance) {
        this.current_balance = current_balance;
    }

    /**
     * @return the bank_name
     */
    public String getBank_name() {
        return bank_name;
    }

    /**
     * @param bank_name the bank_name to set
     */
    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    /**
     * @return the acct_no
     */
    public String getAcct_no() {
        return acct_no;
    }

    /**
     * @param acct_no the acct_no to set
     */
    public void setAcct_no(String acct_no) {
        this.acct_no = acct_no;
    }

    /**
     * @return the transfer_date
     */
    public String getTransfer_date() {
        return transfer_date;
    }

    /**
     * @param transfer_date the transfer_date to set
     */
    public void setTransfer_date(String transfer_date) {
        this.transfer_date = transfer_date;
    }

    /**
     * @return the topup_date
     */
    public String getTopup_date() {
        return topup_date;
    }

    /**
     * @param topup_date the topup_date to set
     */
    public void setTopup_date(String topup_date) {
        this.topup_date = topup_date;
    }
    
}
