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
public class GetCorpByCuidAndPhonenumber {

    private static Logger log = Logger.getLogger(GetCorpByCuidAndPhonenumber.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap result = dp.getCorpByCuidAndPhonenumber(input.get("cu_id").toString(), input.get("phonenumber").toString(), input.get("no_anggota").toString());

        input.put(FieldParameter.cu_id, result.get(FieldParameter.cu_id));
        input.put(FieldParameter.phonenumber, result.get(FieldParameter.phonenumber));
        input.put(FieldParameter.no_anggota, result.get(FieldParameter.no_anggota));

        input.put(FieldParameter.resp_code, result.get(FieldParameter.resp_code));
        return input;
    }
}
