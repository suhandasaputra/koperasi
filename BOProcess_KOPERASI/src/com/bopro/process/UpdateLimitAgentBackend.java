/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.Agentlimit;
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
public class UpdateLimitAgentBackend {

    private static Logger log = Logger.getLogger(UpdateLimitAgentBackend.class);

    public HashMap process(HashMap input) throws ParseException {
        Agentlimit agentlimit = new Agentlimit();
        agentlimit.setAgent_id(input.get(RuleNameParameter.agent_id).toString());
        agentlimit.setMax_bal_day(input.get(RuleNameParameter.max_bal_day).toString());
        agentlimit.setMax_bal_month(input.get(RuleNameParameter.max_bal_month).toString());
        agentlimit.setMax_item_day(input.get(RuleNameParameter.max_item_day).toString());
        agentlimit.setMax_item_month(input.get(RuleNameParameter.max_item_month).toString());
        agentlimit.setMax_curr_bal(input.get(RuleNameParameter.max_curr_bal).toString());
        
        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.updateLimit(agentlimit);
        input.put(FieldParameter.resp_code, Status);
        if (Status.equals("0000")) {
//            dp.userYabes("qqqqq", "update limit agent " + input.get(RuleNameParameter.agent_id).toString());
                    dp.userYabes(input.get(FieldParameter.userlogin).toString(), "update limit agent " + input.get(RuleNameParameter.agent_id).toString());

        }
        return input;
    }
}
