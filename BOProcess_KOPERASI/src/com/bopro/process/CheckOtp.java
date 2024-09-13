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

public class CheckOtp {

    private static Logger log = Logger.getLogger(CheckOtp.class);

    public HashMap process(HashMap input) throws ParseException {

        BackendDBProcess dp = new BackendDBProcess();

        HashMap result = dp.getOtp(input.get(FieldParameter.agent_id).toString());
        input.put(FieldParameter.agent_id, result.get(FieldParameter.agent_id));
        input.put(FieldParameter.otp_pure, result.get(FieldParameter.otp_pure));
        input.put(FieldParameter.otp_lastupdate, result.get(FieldParameter.otp_lastupdate));
        input.put(FieldParameter.resp_code, result.get(FieldParameter.resp_code));
        return input;
    }
}
