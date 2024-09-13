/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.model;

/**
 *
 * @author suhanda
 */
public class Va {

    private String phonenumber;
    private String bank;
    private String no_va;
    private String aktivasi;
    private String status;
    private String agent_id;
    private String trx_id;
    private String expired;

    public String getTrx_id() {
        return trx_id;
    }

    public void setTrx_id(String trx_id) {
        this.trx_id = trx_id;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    
    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNo_va() {
        return no_va;
    }

    public void setNo_va(String no_va) {
        this.no_va = no_va;
    }

    public String getAktivasi() {
        return aktivasi;
    }

    public void setAktivasi(String aktivasi) {
        this.aktivasi = aktivasi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

  
}
