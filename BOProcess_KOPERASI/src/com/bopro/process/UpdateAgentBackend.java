/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.AgentList;
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
public class UpdateAgentBackend {

    private static Logger log = Logger.getLogger(UpdateAgentBackend.class);

    public HashMap process(HashMap input) throws ParseException {
        AgentList agentlist = new AgentList();
        System.out.println("ini input : " + input);
        agentlist.setAgent_id(input.get(RuleNameParameter.agent_id).toString());
        agentlist.setAgent_name(input.get(RuleNameParameter.agent_name).toString());
        agentlist.setAgent_phone(input.get(FieldParameter.email).toString());
        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.updateAgent(agentlist);
        input.put(FieldParameter.resp_code, Status);
//                        String username = session.getAttribute("username").toString();
//                String activitas = "menambahkan agent yabes " + request.getParameter("agentId");
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "mengupdate agent " + input.get(RuleNameParameter.agent_id).toString());
//        input.put(FieldParameter.listAgent, element);
        }
        return input;
    }
}
