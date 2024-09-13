/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.Transactiontrancode;
import com.bopro.database.BackendDBProcess;
import com.bopro.parameter.FieldParameter;
//import com.bopro.model.Ads;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

public class DeactiveProduct {

    private static Logger log = Logger.getLogger(DeactiveProduct.class);

    public HashMap process(HashMap input) throws ParseException {
        Transactiontrancode trancode = new Transactiontrancode();
        trancode.setTrancodeid(input.get(FieldParameter.trancodeid).toString());
        
        BackendDBProcess dp = new BackendDBProcess();
        String status = dp.deactiveProduct(trancode);
        input.put(FieldParameter.resp_code, status);
        if (status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "menonaktifkan produk " + input.get(FieldParameter.trancodeid).toString());
        }
        return input;
    }
}
