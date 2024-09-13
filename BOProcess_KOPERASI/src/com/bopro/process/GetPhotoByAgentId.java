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
public class GetPhotoByAgentId {

    private static Logger log = Logger.getLogger(GetPhotoByAgentId.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap result = dp.getPhotoByAgentid(input.get("agent_id").toString());

        input.put(FieldParameter.agent_id, result.get(FieldParameter.agent_id));
        input.put(FieldParameter.img_ktp, result.get(FieldParameter.img_ktp));
        input.put(FieldParameter.img_profile, result.get(FieldParameter.img_profile));
        input.put(FieldParameter.img_self, result.get(FieldParameter.img_self));
        input.put(FieldParameter.biodata_dukcapil, result.get(FieldParameter.biodata_dukcapil));

        input.put(FieldParameter.resp_code, result.get(FieldParameter.resp_code));
        return input;
    }
}
