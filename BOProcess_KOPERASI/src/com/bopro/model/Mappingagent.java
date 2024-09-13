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
public class Mappingagent {

    private String agent_id;
    private String agentname;
    private String trancodeid;
    private String feejual;
    private String hargajual;
    private String tctype;
    private String tctypename;
    private String trancodename;
    private String sa_commission;

    public String getSa_commission() {
        return sa_commission;
    }

    public void setSa_commission(String sa_commission) {
        this.sa_commission = sa_commission;
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
     * @return the feejual
     */
    public String getFeejual() {
        return feejual;
    }

    /**
     * @param feejual the feejual to set
     */
    public void setFeejual(String feejual) {
        this.feejual = feejual;
    }

    /**
     * @return the hargajual
     */
    public String getHargajual() {
        return hargajual;
    }

    /**
     * @param hargajual the hargajual to set
     */
    public void setHargajual(String hargajual) {
        this.hargajual = hargajual;
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
     * @return the agentname
     */
    public String getAgentname() {
        return agentname;
    }

    /**
     * @param agentname the agentname to set
     */
    public void setAgentname(String agentname) {
        this.agentname = agentname;
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
