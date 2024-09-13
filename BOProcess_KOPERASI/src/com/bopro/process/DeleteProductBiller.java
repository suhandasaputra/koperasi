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
public class DeleteProductBiller {

    private static Logger log = Logger.getLogger(DeleteProductBiller.class);

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
        String Status = dp.deleteBillerproduct(input.get(FieldParameter.tcbiller).toString(), input.get(FieldParameter.billercode).toString());
        input.put(FieldParameter.resp_code, Status);
//        String username = session.getAttribute("username").toString();
//        String activitas = "menghapus agent " + request.getParameter("agentId");
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "menghapus productbiller " + input.get(FieldParameter.tcbiller).toString());
        }
//        input.put(FieldParameter.listAgent, element);
        return input;
    }
}
