/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.bopro.model.am_trancode;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class AddUserProdukPayment {

    private static Logger log = Logger.getLogger(AddUserProdukPayment.class);

    public HashMap process(HashMap input) throws ParseException {
        am_trancode list = new am_trancode();
        list.setTrancodeid(input.get(FieldParameter.trancodeid).toString());
        list.setHargajual(input.get(FieldParameter.hargajual).toString());
        list.setPpob_profit(input.get(FieldParameter.ppob_profit).toString());
        list.setRef_profit(input.get(FieldParameter.ref_profit).toString());
        list.setPoin(input.get(FieldParameter.poin).toString());
        list.setHargajual_noncorp(input.get(FieldParameter.hargajual_noncorp).toString());
        list.setPpob_profit_noncorp(input.get(FieldParameter.ppob_profit_noncorp).toString());
        list.setPoin_noncorp(input.get(FieldParameter.poin_noncorp).toString());

        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.addprodukuserpayment(list);
        input.put(FieldParameter.resp_code, Status);
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "menambahkan produk untuk user " + input.get(FieldParameter.trancodeid).toString());
        }
        return input;
    }
}
