/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.bopro.model.Transactiontrancode;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class ListProduk {

    private static Logger log = Logger.getLogger(ListProduk.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        ArrayList<Transactiontrancode> Listproduk = dp.getAllproduk();
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(Listproduk);
        input.put(FieldParameter.resp_code, "0000");
        input.put(FieldParameter.listproduk, element);
        return input;
    }
}
