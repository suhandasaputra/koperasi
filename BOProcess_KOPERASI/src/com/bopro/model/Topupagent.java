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
public class Topupagent {
    private String agent_id;
    private String agent_name;
    private String topup_date;    
    private String process_exe;    
    private String amount;
    private String update_date;
    private String before_balance;
    private String current_balance;
    private String bank_name;
    private String acct_no;
    private String topup_id;
    private String transfer_date;

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
     * @return the agent_id
     */
    public String getAgent_id() {
        return agent_id;
    }

    /**
     * @param agent_id the agent_id to set
     */
    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
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

    /**
     * @return the process_exe
     */
    public String getProcess_exe() {
        return process_exe;
    }

    /**
     * @param process_exe the process_exe to set
     */
    public void setProcess_exe(String process_exe) {
        this.process_exe = process_exe;
    }

    /**
     * @return the update_date
     */
    public String getUpdate_date() {
        return update_date;
    }

    /**
     * @param update_date the update_date to set
     */
    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    /**
     * @return the topup_id
     */
    public String getTopup_id() {
        return topup_id;
    }

    /**
     * @param topup_id the topup_id to set
     */
    public void setTopup_id(String topup_id) {
        this.topup_id = topup_id;
    }
    
}
