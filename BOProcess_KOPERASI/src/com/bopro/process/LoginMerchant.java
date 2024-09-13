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
public class LoginMerchant {

    private static Logger log = Logger.getLogger(LoginMerchant.class);

    public HashMap process(HashMap input) {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap validate = dp.validateMerchant(input.get(FieldParameter.userlogin).toString(), input.get(FieldParameter.password).toString());
        if (validate.get("status").equals("0000")) {
            input.put(FieldParameter.resp_code, "0000");
//            input.put(FieldParameter.userlevel, validate.get(FieldParameter.userlevel));
        } else {
            input.put(FieldParameter.resp_code, "0001");
        }

        return input;
    }
}
