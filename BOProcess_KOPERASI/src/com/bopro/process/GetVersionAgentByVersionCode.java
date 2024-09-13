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
public class GetVersionAgentByVersionCode {

    private static Logger log = Logger.getLogger(GetVersionAgentByVersionCode.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap result = dp.getVersionAgentByVersionCode(input.get("versioncode").toString());

        input.put(FieldParameter.versioncode, result.get(FieldParameter.versioncode));
        input.put(FieldParameter.versionname, result.get(FieldParameter.versionname));

        input.put(FieldParameter.resp_code, result.get(FieldParameter.resp_code));
        return input;
    }
}
