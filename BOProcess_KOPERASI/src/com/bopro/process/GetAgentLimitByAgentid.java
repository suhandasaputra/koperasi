/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class GetAgentLimitByAgentid {

    private static Logger log = Logger.getLogger(GetAgentLimitByAgentid.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap result = dp.getLimitByagentId(input.get("agent_id").toString());
        input.put(FieldParameter.resp_code, "0000");
        input.put(FieldParameter.agentLimit, result);
        return input;
    }
}
