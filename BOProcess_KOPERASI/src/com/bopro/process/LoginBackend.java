/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.bopro.parameter.FieldParameter;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class LoginBackend {

    private static Logger log = Logger.getLogger(LoginBackend.class);

    public HashMap process(HashMap input) {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap status = dp.validate(input.get(FieldParameter.userlogin).toString(), input.get(FieldParameter.password).toString());
        if (status.get(FieldParameter.status).equals("0000")) {
            input.put(FieldParameter.resp_code, "0000");
            input.put(FieldParameter.userlevel, status.get(FieldParameter.userlevel));
        } else {
            input.put(FieldParameter.resp_code, "0001");
        }
        return input;
    }
}
