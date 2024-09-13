/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.AgentList;
import com.bopro.database.BackendDBProcess;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class ListAgentqBackend {

    private static Logger log = Logger.getLogger(ListAgentqBackend.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        ArrayList<AgentList> ListAgent = dp.getAllAgent();
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(ListAgent);
        input.put(FieldParameter.resp_code, "0000");
        input.put(FieldParameter.listAgent, element);
        return input;
    }
}
