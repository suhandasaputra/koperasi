/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.bopro.model.Transactiontrancode;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class AddProduk {

    private static Logger log = Logger.getLogger(AddProduk.class);

    public HashMap process(HashMap input) throws ParseException {
        Transactiontrancode list = new Transactiontrancode();
        list.setTrancodeid(input.get(FieldParameter.trancodeid).toString());
        list.setTrancodename(input.get(FieldParameter.trancodename).toString());
        list.setBillercode(input.get(FieldParameter.billercode).toString());
        list.setTctype(input.get(FieldParameter.tctype).toString());
        
        list.setProvider(input.get(FieldParameter.provider).toString());
        list.setCategory(input.get(FieldParameter.category).toString());
        list.setDetailproduct(input.get(FieldParameter.detailproduct).toString());


        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.addproduk(list);
        input.put(FieldParameter.resp_code, Status);
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "menambahkan produk " + input.get(FieldParameter.trancodeid).toString()+" "+ input.get(FieldParameter.trancodename).toString());
        }
        return input;
    }
}
