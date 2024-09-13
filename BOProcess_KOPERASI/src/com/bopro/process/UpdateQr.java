/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.bopro.model.Warung;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class UpdateQr {

    private static Logger log = Logger.getLogger(UpdateQr.class);

    public HashMap process(HashMap input) throws ParseException {
        Warung qr = new Warung();
        qr.setMerchantid(input.get(FieldParameter.merchantid).toString());
        qr.setMerchantname(input.get(FieldParameter.merchantname).toString());
//        qr.setMerchantbalance(input.get(FieldParameter.merchantbalance).toString());
        qr.setBiayaadmin(input.get(FieldParameter.biayaadmin).toString());
        qr.setPpob_profit(input.get(FieldParameter.ppob_profit).toString());
        qr.setRef_profit(input.get(FieldParameter.ref_profit).toString());
        qr.setPpob_profit_noncorp(input.get(FieldParameter.ppob_profit_noncorp).toString());

        BackendDBProcess dp = new BackendDBProcess();
        String status = dp.updateQr(qr);
        input.put(FieldParameter.resp_code, status);
        if (status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "mengupdate Qr " + input.get(FieldParameter.merchantid).toString());
        }
        return input;
    }
}
