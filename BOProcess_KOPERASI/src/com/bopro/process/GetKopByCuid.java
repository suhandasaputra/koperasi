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
public class GetKopByCuid {

    private static Logger log = Logger.getLogger(GetKopByCuid.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap result = dp.getKopByCuid(input.get("cu_id").toString());
        input.put(FieldParameter.cu_id, result.get(FieldParameter.cu_id));
        input.put(FieldParameter.nama_koperasi, result.get(FieldParameter.nama_koperasi));

        input.put(FieldParameter.status, result.get(FieldParameter.status));
        return input;
    }
}
