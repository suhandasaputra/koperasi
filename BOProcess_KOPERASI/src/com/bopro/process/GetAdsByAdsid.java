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
public class GetAdsByAdsid {

    private static Logger log = Logger.getLogger(GetAdsByAdsid.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap result = dp.getAdsByAdsid(input.get("ads_id").toString());

        input.put(FieldParameter.ads_id, result.get(FieldParameter.ads_id));
        input.put(FieldParameter.url, result.get(FieldParameter.url));
        input.put(FieldParameter.start_date, result.get(FieldParameter.start_date));
        input.put(FieldParameter.end_date, result.get(FieldParameter.end_date));
        input.put(FieldParameter.spectator, result.get(FieldParameter.spectator));
        input.put(FieldParameter.nama_koperasi, result.get(FieldParameter.nama_koperasi));

        input.put(FieldParameter.resp_code, result.get(FieldParameter.resp_code));
        return input;
    }
}
