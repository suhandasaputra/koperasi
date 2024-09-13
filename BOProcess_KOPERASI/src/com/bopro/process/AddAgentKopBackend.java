/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.bopro.model.AgentKopList;
import com.bopro.parameter.FieldParameter;
import com.bopro.parameter.RuleNameParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class AddAgentKopBackend {

    private static Logger log = Logger.getLogger(AddAgentKopBackend.class);

    public HashMap process(HashMap input) throws ParseException {
        AgentKopList agentkoplist = new AgentKopList();
        agentkoplist.setCu_id(input.get(RuleNameParameter.cu_id).toString());
        agentkoplist.setNo_ba(input.get(RuleNameParameter.no_ba).toString());
        agentkoplist.setImg_ktp(input.get(RuleNameParameter.img_ktp).toString());
        
        agentkoplist.setAgent_id(input.get(RuleNameParameter.agent_id).toString());
        agentkoplist.setAgent_name(input.get(RuleNameParameter.agent_name).toString());
        agentkoplist.setAgent_phone(input.get(FieldParameter.phonenumber).toString());

        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.addAgentKop(agentkoplist);
        input.put(FieldParameter.resp_code, Status);
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "menambahkan agent Koperasi " + input.get(RuleNameParameter.agent_id).toString());
        }
        return input;
    }
}
