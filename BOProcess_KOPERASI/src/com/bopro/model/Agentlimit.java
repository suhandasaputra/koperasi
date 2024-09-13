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
public class Agentlimit {
    private String agent_id;
    private String max_bal_day;
    private String max_bal_month;    
    private String max_item_day;
    private String max_item_month;
    private String max_curr_bal;

    public String getMax_curr_bal() {
        return max_curr_bal;
    }

    public void setMax_curr_bal(String max_curr_bal) {
        this.max_curr_bal = max_curr_bal;
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
     * @return the max_bal_day
     */
    public String getMax_bal_day() {
        return max_bal_day;
    }

    /**
     * @param max_bal_day the max_bal_day to set
     */
    public void setMax_bal_day(String max_bal_day) {
        this.max_bal_day = max_bal_day;
    }

    /**
     * @return the max_bal_month
     */
    public String getMax_bal_month() {
        return max_bal_month;
    }

    /**
     * @param max_bal_month the max_bal_month to set
     */
    public void setMax_bal_month(String max_bal_month) {
        this.max_bal_month = max_bal_month;
    }

    /**
     * @return the max_item_day
     */
    public String getMax_item_day() {
        return max_item_day;
    }

    /**
     * @param max_item_day the max_item_day to set
     */
    public void setMax_item_day(String max_item_day) {
        this.max_item_day = max_item_day;
    }

    /**
     * @return the max_item_month
     */
    public String getMax_item_month() {
        return max_item_month;
    }

    /**
     * @param max_item_month the max_item_month to set
     */
    public void setMax_item_month(String max_item_month) {
        this.max_item_month = max_item_month;
    }
}
