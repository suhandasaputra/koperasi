/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.Transactiontrancode;
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
public class ActiveProduct {

    private static Logger log = Logger.getLogger(ActiveProduct.class);

    public HashMap process(HashMap input) throws ParseException {
        Transactiontrancode trancode = new Transactiontrancode();
        trancode.setTrancodeid(input.get(FieldParameter.trancodeid).toString());
        
        BackendDBProcess dp = new BackendDBProcess();
        String status = dp.activeProduct(trancode);
        input.put(FieldParameter.resp_code, status);
        if (status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "mengaktifkan produk " + input.get(FieldParameter.trancodeid).toString());
        }
        return input;
    }
}
