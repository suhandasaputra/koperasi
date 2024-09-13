/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.bopro.model.Ads;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class UpdateAds {

    private static Logger log = Logger.getLogger(UpdateAds.class);

    public HashMap process(HashMap input) throws ParseException {
        Ads ads = new Ads();
        ads.setAds_id(input.get(FieldParameter.ads_id).toString());
        ads.setUrl(input.get(FieldParameter.url).toString());
        ads.setStart_date(input.get(FieldParameter.start_date).toString());
        ads.setEnd_date(input.get(FieldParameter.end_date).toString());
        ads.setSpectator(input.get(FieldParameter.spectator).toString());

        
        BackendDBProcess dp = new BackendDBProcess();
        String status = dp.updateAds(ads);
        input.put(FieldParameter.resp_code, status);
        if (status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "mengupdate Ads " + input.get(FieldParameter.ads_id).toString());
        }
        return input;
    }
}
