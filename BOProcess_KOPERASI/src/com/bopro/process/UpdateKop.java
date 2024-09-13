/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.bopro.model.Corporation;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class UpdateKop {

    private static Logger log = Logger.getLogger(UpdateKop.class);

    public HashMap process(HashMap input) throws ParseException {
        Corporation corp = new Corporation();
        corp.setCu_id(input.get(FieldParameter.cu_id).toString());
        corp.setNama_koperasi(input.get(FieldParameter.nama_koperasi).toString());
        
        BackendDBProcess dp = new BackendDBProcess();
        String status = dp.updateKop(corp);
        input.put(FieldParameter.resp_code, status);
        if (status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "mengupdate koperasi " + input.get(FieldParameter.nama_koperasi).toString());
        }
        return input;
    }
}
