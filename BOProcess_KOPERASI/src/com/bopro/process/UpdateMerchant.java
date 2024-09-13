/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.Merchant;
import com.bopro.database.BackendDBProcess;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class UpdateMerchant {

    private static Logger log = Logger.getLogger(UpdateMerchant.class);

    public HashMap process(HashMap input) throws ParseException {
        Merchant merchant = new Merchant();
        merchant.setMerchantid(input.get(FieldParameter.merchantid).toString());
        merchant.setMerchantname(input.get(FieldParameter.merchantname).toString());
        merchant.setKodepos(input.get(FieldParameter.kodepos).toString());
        merchant.setAlamat(input.get(FieldParameter.alamat).toString());
        merchant.setEmail(input.get(FieldParameter.email).toString());

        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.updateMerchant(merchant);
        input.put(FieldParameter.resp_code, Status);
//                        String username = session.getAttribute("username").toString();
//                String activitas = "menambahkan agent yabes " + request.getParameter("agentId");
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "mengupdate Merchant " + input.get(FieldParameter.merchantid).toString());
//        input.put(FieldParameter.listAgent, element);
        }
        return input;
    }
}
