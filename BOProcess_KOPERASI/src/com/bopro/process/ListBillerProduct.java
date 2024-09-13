/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.Billerproduct;
import com.bopro.model.Reportyabes;
import com.bopro.database.BackendDBProcess;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class ListBillerProduct {

    private static Logger log = Logger.getLogger(ListBillerProduct.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        ArrayList<Billerproduct> billerproduct = dp.getAllBillerproduct();
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(billerproduct, new TypeToken<List<Reportyabes>>() {
        }.getType());
        input.put(FieldParameter.resp_code, "0000");
        input.put(FieldParameter.listProductBiller, element);
        return input;
    }
}
