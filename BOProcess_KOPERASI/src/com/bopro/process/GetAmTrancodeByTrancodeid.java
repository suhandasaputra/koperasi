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
public class GetAmTrancodeByTrancodeid {

    private static Logger log = Logger.getLogger(GetAmTrancodeByTrancodeid.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap result = dp.getAmTrancodeByTrancodeid(input.get("trancodeid").toString());
        input.put(FieldParameter.trancodeid, result.get(FieldParameter.trancodeid));
        input.put(FieldParameter.trancodename, result.get(FieldParameter.trancodename));
        input.put(FieldParameter.feejual, result.get(FieldParameter.feejual));
        input.put(FieldParameter.hargajual, result.get(FieldParameter.hargajual));
        input.put(FieldParameter.ppob_profit, result.get(FieldParameter.ppob_profit));
        input.put(FieldParameter.ref_profit, result.get(FieldParameter.ref_profit));
        input.put(FieldParameter.poin, result.get(FieldParameter.poin));
        input.put(FieldParameter.hargajual_noncorp, result.get(FieldParameter.hargajual_noncorp));
        input.put(FieldParameter.ppob_profit_noncorp, result.get(FieldParameter.ppob_profit_noncorp));
        input.put(FieldParameter.feejual_noncorp, result.get(FieldParameter.feejual_noncorp));
        input.put(FieldParameter.poin_noncorp, result.get(FieldParameter.poin_noncorp));
        input.put(FieldParameter.tctype, result.get(FieldParameter.tctype));

        return input;
    }
}
