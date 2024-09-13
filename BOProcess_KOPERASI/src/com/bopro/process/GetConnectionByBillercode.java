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
public class GetConnectionByBillercode {

    private static Logger log = Logger.getLogger(GetConnectionByBillercode.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap result = dp.getConnectionByBillercode(input.get("billercode").toString());
        input.put(FieldParameter.billercode, result.get(FieldParameter.billercode));
        input.put(FieldParameter.billername, result.get(FieldParameter.billername));
        input.put(FieldParameter.packagename, result.get(FieldParameter.packagename));
        input.put(FieldParameter.urlhost, result.get(FieldParameter.urlhost));
//        List Detail = new ArrayList();
//        Detail.add(input);
        input.put(FieldParameter.resp_code, result.get(FieldParameter.resp_code));

//        input.put(FieldParameter.listAgent, Detail);
        return input;
    }
}
