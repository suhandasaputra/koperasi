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
public class GetProductbillerBybillercodeAndtcbiller {

    private static Logger log = Logger.getLogger(GetProductbillerBybillercodeAndtcbiller.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap result = dp.getProductbillerBybillercodeAndtcbiller(input.get("tcbiller").toString(), input.get("billercode").toString());

        input.put(FieldParameter.billername, result.get(FieldParameter.billername));
        input.put(FieldParameter.tcbiller, result.get(FieldParameter.tcbiller));
        input.put(FieldParameter.trancodename, result.get(FieldParameter.trancodename));
        input.put(FieldParameter.hargabeli, result.get(FieldParameter.hargabeli));
        input.put(FieldParameter.feebeli, result.get(FieldParameter.feebeli));
        input.put(FieldParameter.tctype, result.get(FieldParameter.tctype));
//                        input.put(FieldParameter.poin, result.get(FieldParameter.poin));

        input.put(FieldParameter.resp_code, result.get(FieldParameter.resp_code));

        return input;
    }
}
