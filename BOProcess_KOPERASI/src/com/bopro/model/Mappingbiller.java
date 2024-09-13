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
public class Mappingbiller {
    private String trancodeid;
    private String trancodename;
    private String tctype;
    private String billercode;    
    private String billername;    

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
}
