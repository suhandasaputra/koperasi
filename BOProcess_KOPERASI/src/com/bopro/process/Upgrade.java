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

public class Upgrade {

    private static Logger log = Logger.getLogger(Upgrade.class);

    public HashMap process(HashMap input) throws ParseException {

        BackendDBProcess dp = new BackendDBProcess();

        HashMap result = dp.getUpgrade(input.get(FieldParameter.agent_id).toString(), input.get(FieldParameter.nama_koperasi).toString());
        input.put(FieldParameter.agent_id, result.get(FieldParameter.agent_id));
        input.put(FieldParameter.agent_name, result.get(FieldParameter.agent_name));
        input.put(FieldParameter.userlevel, result.get(FieldParameter.userlevel));
        input.put(FieldParameter.reference_id, result.get(FieldParameter.reference_id));
        input.put(FieldParameter.nama_koperasi, result.get(FieldParameter.nama_koperasi));
        input.put(FieldParameter.resp_desc, result.get(FieldParameter.resp_desc));
        input.put(FieldParameter.resp_code, result.get(FieldParameter.resp_code));
        
        
        if (result.get(FieldParameter.resp_code).equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "mengupgrade user user " + input.get(FieldParameter.agent_id).toString());
        }
        return input;
    }
}
