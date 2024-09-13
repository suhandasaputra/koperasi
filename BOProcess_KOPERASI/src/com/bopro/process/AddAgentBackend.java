/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.AgentList;
import com.bopro.database.BackendDBProcess;
import com.fesfam.function.SHA256Enc;
import com.bopro.parameter.FieldParameter;
import com.bopro.parameter.RuleNameParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class AddAgentBackend {

    private static Logger log = Logger.getLogger(AddAgentBackend.class);

    public HashMap process(HashMap input) throws ParseException {
        AgentList agentlist = new AgentList();
        agentlist.setAgent_id(input.get(RuleNameParameter.agent_id).toString());
        agentlist.setAgent_name(input.get(RuleNameParameter.agent_name).toString());
//        agentlist.setAgent_address(input.get(FieldParameter.address).toString());
        agentlist.setAgent_phone(input.get(FieldParameter.phonenumber).toString());
//        agentlist.setAgent_pass(input.get(RuleNameParameter.agent_pass).toString());
//        agentlist.setAgent_pin(input.get(RuleNameParameter.agent_pin).toString());
//        agentlist.setAgent_level(Integer.valueOf(input.get(RuleNameParameter.agent_level).toString()));
//        agentlist.setNo_ktp(input.get(FieldParameter.no_ktp).toString());

        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.addAgent(agentlist);
        input.put(FieldParameter.resp_code, Status);
//                        String username = session.getAttribute("username").toString();
//                String activitas = "menambahkan agent yabes " + request.getParameter("agentId");
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "menambahkan agent " + input.get(RuleNameParameter.agent_id).toString());
//        input.put(FieldParameter.listAgent, element);
        }
        return input;
    }
}
