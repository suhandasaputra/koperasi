/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.AgentList;
import com.bopro.model.Merchant;
import com.bopro.database.BackendDBProcess;
import com.fesfam.function.SHA256Enc;
import com.bopro.parameter.FieldParameter;
import com.bopro.parameter.RuleNameParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class AddMerchant {

    private static Logger log = Logger.getLogger(AddMerchant.class);

    public HashMap process(HashMap input) throws ParseException {
        Merchant merchant = new Merchant();
        merchant.setMerchantid(input.get(FieldParameter.merchantid).toString());
        merchant.setMerchantname(input.get(FieldParameter.merchantname).toString());
        merchant.setKodepos(input.get(FieldParameter.kodepos).toString());
        merchant.setAlamat(input.get(FieldParameter.alamat).toString());
        merchant.setEmail(input.get(FieldParameter.email).toString());
        merchant.setPassword(input.get(FieldParameter.password).toString());

        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.addMerchant(merchant);
        input.put(FieldParameter.resp_code, Status);
//                        String username = session.getAttribute("username").toString();
//                String activitas = "menambahkan agent yabes " + request.getParameter("agentId");
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "menambahkan Merchant " + input.get(FieldParameter.merchantid).toString());
//        input.put(FieldParameter.listAgent, element);
        }
        return input;
    }
}
