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
public class GetQrByMerchantid {

    private static Logger log = Logger.getLogger(GetQrByMerchantid.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap result = dp.getQrByMerchantid(input.get("merchantid").toString());

        input.put(FieldParameter.merchantid, result.get(FieldParameter.merchantid));
        input.put(FieldParameter.merchantname, result.get(FieldParameter.merchantname));
//        input.put(FieldParameter.merchantbalance, result.get(FieldParameter.merchantbalance));
        input.put(FieldParameter.biayaadmin, result.get(FieldParameter.biayaadmin));
        input.put(FieldParameter.ppob_profit, result.get(FieldParameter.ppob_profit));
        input.put(FieldParameter.ref_profit, result.get(FieldParameter.ref_profit));
        input.put(FieldParameter.ppob_profit_noncorp, result.get(FieldParameter.ppob_profit_noncorp));

        input.put(FieldParameter.resp_code, result.get(FieldParameter.resp_code));
        return input;
    }
}
