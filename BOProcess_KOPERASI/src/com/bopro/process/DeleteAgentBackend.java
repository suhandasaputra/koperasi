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
public class DeleteAgentBackend {

    private static Logger log = Logger.getLogger(DeleteAgentBackend.class);

    public HashMap process(HashMap input) throws ParseException {
//        AgentList agentlist = new AgentList();
//        agentlist.setAgent_id(input.get("agent_id").toString());
//        agentlist.setAgent_name(input.get("agent_name").toString());
//        agentlist.setAgent_address(input.get("address").toString());
//        agentlist.setAgent_phone(input.get("phonenumber").toString());
//        agentlist.setAgent_pass(input.get("agent_pass").toString());
//        agentlist.setAgent_pin(input.get("agent_pin").toString());
//        agentlist.setAgent_level(Integer.valueOf(input.get("agent_level").toString()));

        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.deleteAgent(input.get(RuleNameParameter.agent_id).toString());
        input.put(FieldParameter.resp_code, Status);
//        String username = session.getAttribute("username").toString();
//        String activitas = "menghapus agent " + request.getParameter("agentId");
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "menghapus agent " + input.get(RuleNameParameter.agent_id).toString());
        }
//        input.put(FieldParameter.listAgent, element);
        return input;
    }
}
