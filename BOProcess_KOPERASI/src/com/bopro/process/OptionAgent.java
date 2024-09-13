/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.Reportyabes;
import com.bopro.database.BackendDBProcess;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.bopro.model.AgentList;
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
public class OptionAgent {
    

    private static Logger log = Logger.getLogger(OptionAgent.class);

    public HashMap process(HashMap input) throws ParseException {
        String cu_id = input.get(FieldParameter.cu_id).toString();
        BackendDBProcess dp = new BackendDBProcess();
        ArrayList<AgentList> cagent = dp.getOptionAgent(cu_id);
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(cagent, new TypeToken<List<Reportyabes>>() {
        }.getType());
        input.put(FieldParameter.resp_code, "0000");
        input.put(FieldParameter.listConnection, element);
        return input;
    }
}
