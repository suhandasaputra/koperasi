/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.Useryabes;
import com.bopro.database.BackendDBProcess;
import com.bopro.parameter.FieldParameter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class ListNotifUserBackendadministrator {

//    private static Logger log = Logger.getLogger(ListNotifUserBackendadministrator.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        Gson gson = new Gson();
        ArrayList<Useryabes> alluseryabes = dp.getAlluseryabesadministrator(input.get(FieldParameter.userlogin).toString());
        JsonElement element = gson.toJsonTree(alluseryabes, new TypeToken<List<Useryabes>>() {
        }.getType());
        input.put(FieldParameter.resp_code, "0000");
        input.put(FieldParameter.listAgent, element);

        return input;
    }
}
