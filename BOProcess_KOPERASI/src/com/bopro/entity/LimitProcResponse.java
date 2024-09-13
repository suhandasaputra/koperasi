/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bopro.entity;

import java.io.Serializable;


/**
 *
 * @author herrysuganda
 */
public class LimitProcResponse implements Serializable{

    private String status = "01";
    private String statusDesc = "Gagal proses limit";
    private float curr;
    private int curr2;

    public int getCurr2() {
        return curr2;
    }

    public void setCurr2(int curr2) {
        this.curr2 = curr2;
    }

    public int getPrev2() {
        return prev2;
    }

    public void setPrev2(int prev2) {
        this.prev2 = prev2;
    }
    private float prev;
    private int prev2;
    private float  amountUpdate;

    public float getCurr() {
        return curr;
    }

    public void setCurr(float curr) {
        this.curr = curr;
    }

    public float getPrev() {
        return prev;
    }

    public void setPrev(float prev) {
        this.prev = prev;
    }

    private int  totalItemUpdate;
    private int fee;
    private int feeTerm;

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the statusDesc
     */
    public String getStatusDesc() {
        return statusDesc;
    }

    /**
     * @param statusDesc the statusDesc to set
     */
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    /**
     * @return the amountUpdate
     */
    public float getAmountUpdate() {
        return amountUpdate;
    }

    /**
     * @param amountUpdate the amountUpdate to set
     */
    public void setAmountUpdate(float amountUpdate) {
        this.amountUpdate = amountUpdate;
    }

    /**
     * @return the totalItemUpdate
     */
    public float getTotalItemUpdate() {
        return totalItemUpdate;
    }

    /**
     * @param totalItemUpdate the totalItemUpdate to set
     */
    public void setTotalItemUpdate(int totalItemUpdate) {
        this.totalItemUpdate = totalItemUpdate;
    }

    /**
     * @return the fee
     */
    public int getFee() {
        return fee+feeTerm;
    }

    /**
     * @param fee the fee to set
     */
    public void setFee(int fee) {
        this.fee = fee;
    }

    /**
     * @return the feeTerm
     */
    public int getFeeTerm() {
        return feeTerm;
    }

    /**
     * @param feeTerm the feeTerm to set
     */
    public void setFeeTerm(int feeTerm) {
        this.feeTerm = feeTerm;
    }
}
