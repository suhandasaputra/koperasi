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
public class Billerproduct {
    private String billercode;
    private String billername;
    private String tctype;
    private String tctypename;
    private String trancodeid;
    private String tcbiller;
    private String feebeli;
    private String hargabeli;
    private String conname;
    private String trancodename;
        private String poin;

    public String getPoin() {
        return poin;
    }

    public void setPoin(String poin) {
        this.poin = poin;
    }


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
    
    /**
     * @return the tcbiller
     */
    public String getTcbiller() {
        return tcbiller;
    }

    /**
     * @param tcbiller the tcbiller to set
     */
    public void setTcbiller(String tcbiller) {
        this.tcbiller = tcbiller;
    }

    /**
     * @return the feebeli
     */
    public String getFeebeli() {
        return feebeli;
    }

    /**
     * @param feebeli the feebeli to set
     */
    public void setFeebeli(String feebeli) {
        this.feebeli = feebeli;
    }

    /**
     * @return the hargabeli
     */
    public String getHargabeli() {
        return hargabeli;
    }

    /**
     * @param hargabeli the hargabeli to set
     */
    public void setHargabeli(String hargabeli) {
        this.hargabeli = hargabeli;
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
     * @return the conname
     */
    public String getConname() {
        return conname;
    }

    /**
     * @param conname the conname to set
     */
    public void setConname(String conname) {
        this.conname = conname;
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
     * @return the billername
     */
    public String getBillername() {
        return billername;
    }

    /**
     * @param billername the billername to set
     */
    public void setBillername(String billername) {
        this.billername = billername;
    }

    /**
     * @return the tctypename
     */
    public String getTctypename() {
        return tctypename;
    }

    /**
     * @param tctypename the tctypename to set
     */
    public void setTctypename(String tctypename) {
        this.tctypename = tctypename;
    }
}
