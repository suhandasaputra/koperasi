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
public class GetMerchantByMerchantId {

    private static Logger log = Logger.getLogger(GetMerchantByMerchantId.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap result = dp.getMerchantByMerchantId(input.get("merchantid").toString());
        input.put(FieldParameter.merchantid, result.get(FieldParameter.merchantid));
        input.put(FieldParameter.merchantname, result.get(FieldParameter.merchantname));
        input.put(FieldParameter.kodepos, result.get(FieldParameter.kodepos));
        input.put(FieldParameter.alamat, result.get(FieldParameter.alamat));
        input.put(FieldParameter.email, result.get(FieldParameter.email));
        input.put(FieldParameter.resp_code, result.get(FieldParameter.resp_code));
        return input;
    }
}
