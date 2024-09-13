/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.model;

import java.awt.image.BufferedImage;

/**
 *
 * @author syukur
 */
public class Verifikasi {
    
    private String agent_id;
    private String agent_name;
    private String phonenumber;
    private String no_ktp;
    private String max_curr_bal;
    private String verified;
    private String img_ktp;
    private String img_self;
    private String img_profile;
    private String biodata_dukcapil;

    public String getBiodata_dukcapil() {
        return biodata_dukcapil;
    }

    public void setBiodata_dukcapil(String biodata_dukcapil) {
        this.biodata_dukcapil = biodata_dukcapil;
    }

    public String getImg_profile() {
        return img_profile;
    }

    public void setImg_profile(String img_profile) {
        this.img_profile = img_profile;
    }

    public String getImg_self() {
        return img_self;
    }

    public void setImg_self(String img_self) {
        this.img_self = img_self;
    }

    public String getImg_ktp() {
        return img_ktp;
    }

    public void setImg_ktp(String img_ktp) {
        this.img_ktp = img_ktp;
    }

//    public BufferedImage getImg_ktp_buffer() {
//        return img_ktp_buffer;
//    }
//
//    public void setImg_ktp_buffer(BufferedImage img_ktp_buffer) {
//        this.img_ktp_buffer = img_ktp_buffer;
//    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    
    public String getMax_curr_bal() {
        return max_curr_bal;
    }

    public void setMax_curr_bal(String max_curr_bal) {
        this.max_curr_bal = max_curr_bal;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getNo_ktp() {
        return no_ktp;
    }

    public void setNo_ktp(String no_ktp) {
        this.no_ktp = no_ktp;
    }
    private String curr_bal;

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

   

    public String getCurr_bal() {
        return curr_bal;
    }

    public void setCurr_bal(String curr_bal) {
        this.curr_bal = curr_bal;
    }
    
    
}
