package com.bopro.process;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.bopro.database.BackendDBProcess;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.bopro.model.Version;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class ListVersionAgent {

    private static Logger log = Logger.getLogger(ListVersionAgent.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        ArrayList<Version> ListVersionAgent = dp.getAgentVersion();
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(ListVersionAgent);
        input.put(FieldParameter.resp_code, "0000");
        input.put(FieldParameter.ListVersionAgent, element);
        return input;
    }
}
