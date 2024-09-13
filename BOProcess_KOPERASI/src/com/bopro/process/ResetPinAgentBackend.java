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
public class ResetPinAgentBackend {

    private static Logger log = Logger.getLogger(ResetPinAgentBackend.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.resetPin(input.get(RuleNameParameter.agent_id).toString());
        input.put(FieldParameter.resp_code, Status);
//        String username = session.getAttribute("username").toString();
//        String activitas = "menghapus agent " + request.getParameter("agentId");
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "Reset agent Pin" + input.get(RuleNameParameter.agent_id).toString());
        }
//        input.put(FieldParameter.listAgent, element);
        return input;
    }
}
