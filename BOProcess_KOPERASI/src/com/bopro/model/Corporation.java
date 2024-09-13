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
public class Corporation {

    private String cu_id;
    private String phonenumber;
    private String no_anggota;
    private String aktivasi;
    private String status;
    private String nama_koperasi;
    private String password_corp;

    public String getPassword_corp() {
        return password_corp;
    }

    public void setPassword_corp(String password_corp) {
        this.password_corp = password_corp;
    }


    public String getNama_koperasi() {
        return nama_koperasi;
    }

    public void setNama_koperasi(String nama_koperasi) {
        this.nama_koperasi = nama_koperasi;
    }

    public String getCu_id() {
        return cu_id;
    }

    public void setCu_id(String cu_id) {
        this.cu_id = cu_id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getNo_anggota() {
        return no_anggota;
    }

    public void setNo_anggota(String no_anggota) {
        this.no_anggota = no_anggota;
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
