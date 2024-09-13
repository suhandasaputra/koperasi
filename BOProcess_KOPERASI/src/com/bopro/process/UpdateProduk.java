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
public class UpdateProduk {

    private static Logger log = Logger.getLogger(UpdateProduk.class);

    public HashMap process(HashMap input) throws ParseException {
        Transactiontrancode agentlist = new Transactiontrancode();
        agentlist.setTrancodeid(input.get(FieldParameter.trancodeid).toString());
        agentlist.setTrancodename(input.get(FieldParameter.trancodename).toString());
        agentlist.setBillercode(input.get(FieldParameter.billercode).toString());
        agentlist.setTctype(input.get(FieldParameter.tctype).toString());
                agentlist.setProvider(input.get(FieldParameter.provider).toString());
        agentlist.setCategory(input.get(FieldParameter.category).toString());
        agentlist.setDetailproduct(input.get(FieldParameter.detailproduct).toString());


        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.updateproduk(agentlist);
        input.put(FieldParameter.resp_code, Status);
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "mengupdate produk " + input.get(FieldParameter.trancodeid).toString()+" "+input.get(FieldParameter.trancodename).toString());
        }
        return input;
    }
}
