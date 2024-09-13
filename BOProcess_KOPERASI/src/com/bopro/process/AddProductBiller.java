/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.Billerproduct;
import com.bopro.database.BackendDBProcess;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author suhanda
 */
public class AddProductBiller {

    private static Logger log = Logger.getLogger(AddProductBiller.class);

    public HashMap process(HashMap input) throws ParseException {
        Billerproduct pbiller = new Billerproduct();
        pbiller.setBillercode(input.get(FieldParameter.billercode).toString());
        pbiller.setTctypename(input.get(FieldParameter.transactiontype).toString());
        pbiller.setTrancodeid(input.get(FieldParameter.trancodeid).toString());
        pbiller.setTcbiller(input.get(FieldParameter.tcbiller).toString());
        pbiller.setHargabeli(input.get(FieldParameter.hargabeli).toString());
        pbiller.setFeebeli(input.get(FieldParameter.feebeli).toString());
//        pbiller.setPoin(input.get(FieldParameter.poin).toString());


        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.addBillerProduct(pbiller);
        input.put(FieldParameter.resp_code, Status);
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "menambahkan product biller " + input.get(FieldParameter.trancodeid).toString());
        }
        return input;
    }
}
