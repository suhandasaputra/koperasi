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
public class GetTrancodeByTrancodeid {

    private static Logger log = Logger.getLogger(GetTrancodeByTrancodeid.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap result = dp.getprodukbytrancodeid(input.get("trancodeid").toString());
        input.put(FieldParameter.trancodeid, result.get(FieldParameter.trancodeid));
        input.put(FieldParameter.trancodename, result.get(FieldParameter.trancodename));
        input.put(FieldParameter.billercode, result.get(FieldParameter.billercode));
        input.put(FieldParameter.tctype, result.get(FieldParameter.tctype));
        
        input.put(FieldParameter.provider, result.get(FieldParameter.provider));
        input.put(FieldParameter.category, result.get(FieldParameter.category));
        input.put(FieldParameter.detailproduct, result.get(FieldParameter.detailproduct));

        input.put(FieldParameter.resp_code, result.get(FieldParameter.resp_code));
        return input;
    }
}
