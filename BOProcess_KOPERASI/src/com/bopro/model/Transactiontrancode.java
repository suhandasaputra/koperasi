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
public class Transactiontrancode {
    private String trancodeid;
    private String trancodename;
    private String tctype;
    private String billercode;
    private String available;
    private String provider;
    private String category;
    private String detailproduct;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetailproduct() {
        return detailproduct;
    }

    public void setDetailproduct(String detailproduct) {
        this.detailproduct = detailproduct;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getBillercode() {
        return billercode;
    }

    public void setBillercode(String billercode) {
        this.billercode = billercode;
    }

    /**
     * @return the trancodeid
     */
    public String getTrancodeid() {
        return trancodeid;
    }

    /**
     * @param trancodeid the trancodeid to set
     */
    public void setTrancodeid(String trancodeid) {
        this.trancodeid = trancodeid;
    }

    /**
     * @return the trancodename
     */
    public String getTrancodename() {
        return trancodename;
    }

    /**
     * @param trancodename the trancodename to set
     */
    public void setTrancodename(String trancodename) {
        this.trancodename = trancodename;
    }

    /**
     * @return the tctype
     */
    public String getTctype() {
        return tctype;
    }

    /**
     * @param tctype the tctype to set
     */
    public void setTctype(String tctype) {
        this.tctype = tctype;
    }
}
