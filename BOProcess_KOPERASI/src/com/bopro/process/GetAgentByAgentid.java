/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.bopro.parameter.FieldParameter;
import com.bopro.parameter.RuleNameParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class GetAgentByAgentid {

    private static Logger log = Logger.getLogger(GetAgentByAgentid.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap result = dp.getAgentByAgentid(input.get("agent_id").toString());
        input.put(RuleNameParameter.agent_id, result.get(RuleNameParameter.agent_id));
        input.put(RuleNameParameter.agent_name, result.get(RuleNameParameter.agent_name));
//        input.put(FieldParameter.address, result.get(FieldParameter.address));
        input.put(FieldParameter.email, result.get(FieldParameter.email));
//        List Detail = new ArrayList();
//        Detail.add(input);
        input.put(FieldParameter.resp_code, result.get(FieldParameter.resp_code));
//        input.put(FieldParameter.listAgent, Detail);
        return input;
    }
}
