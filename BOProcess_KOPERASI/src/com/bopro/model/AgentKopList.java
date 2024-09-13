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
public class AgentKopList {

    private String agent_id;
    private String agent_pass;
    private String agent_name;
    private String balance;
    private String agent_address;
    private String amount;
    private String no_ktp;
    private String tgl_register;
    private String cu_id;
    private String no_ba;
    private String img_ktp;

    public String getCu_id() {
        return cu_id;
    }

    public void setCu_id(String cu_id) {
        this.cu_id = cu_id;
    }

    public String getNo_ba() {
        return no_ba;
    }

    public void setNo_ba(String no_ba) {
        this.no_ba = no_ba;
    }

    public String getImg_ktp() {
        return img_ktp;
    }

    public void setImg_ktp(String img_ktp) {
        this.img_ktp = img_ktp;
    }


    public String getTgl_register() {
        return tgl_register;
    }

    public void setTgl_register(String tgl_register) {
        this.tgl_register = tgl_register;
    }

    public String getNo_ktp() {
        return no_ktp;
    }

    public void setNo_ktp(String no_ktp) {
        this.no_ktp = no_ktp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    private String agent_pin;
    private String agent_phone;
    private int agent_level;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAgent_level() {
        return agent_level;
    }

    public void setAgent_level(int agent_level) {
        this.agent_level = agent_level;
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

    /**
     * @return the agent_pass
     */
    public String getAgent_pass() {
        return agent_pass;
    }

    /**
     * @param agent_pass the agent_pass to set
     */
    public void setAgent_pass(String agent_pass) {
        this.agent_pass = agent_pass;
    }

    /**
     * @return the agent_name
     */
    public String getAgent_name() {
        return agent_name;
    }

    /**
     * @param agent_name the agent_name to set
     */
    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    /**
     * @return the agent_address
     */
    public String getAgent_address() {
        return agent_address;
    }

    /**
     * @param agent_address the agent_address to set
     */
    public void setAgent_address(String agent_address) {
        this.agent_address = agent_address;
    }

    /**
     * @return the agent_pin
     */
    public String getAgent_pin() {
        return agent_pin;
    }

    /**
     * @param agent_pin the agent_pin to set
     */
    public void setAgent_pin(String agent_pin) {
        this.agent_pin = agent_pin;
    }

    /**
     * @return the agent_phone
     */
    public String getAgent_phone() {
        return agent_phone;
    }

    /**
     * @param agent_phone the agent_phone to set
     */
    public void setAgent_phone(String agent_phone) {
        this.agent_phone = agent_phone;
    }
}
