/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.bopro.model.Version;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class UpdateVersionUser {

    private static Logger log = Logger.getLogger(UpdateVersionUser.class);

    public HashMap process(HashMap input) throws ParseException {
        Version vr = new Version();
        vr.setVersioncode(input.get(FieldParameter.versioncode).toString());
        vr.setVersionname(input.get(FieldParameter.versionname).toString());
        
        BackendDBProcess dp = new BackendDBProcess();
        String status = dp.updateVersionUser(vr);
        input.put(FieldParameter.resp_code, status);
        if (status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "mengupdate versi user " + input.get(FieldParameter.versionname).toString());
        }
        return input;
    }
}
