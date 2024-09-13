/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.bopro.model.am_trancode;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class ListProdukUser {

    private static Logger log = Logger.getLogger(ListProdukUser.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        ArrayList<am_trancode> Listproduk = dp.getAllprodukuser();
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(Listproduk);
        input.put(FieldParameter.resp_code, "0000");
        input.put(FieldParameter.listprodukuser, element);
        return input;
    }
}
