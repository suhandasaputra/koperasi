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
public class UpdateUserProduk {

    private static Logger log = Logger.getLogger(UpdateUserProduk.class);

    public HashMap process(HashMap input) throws ParseException {
        am_trancode am = new am_trancode();
        am.setTrancodeid(input.get(FieldParameter.trancodeid).toString());
        am.setFeejual(input.get(FieldParameter.feejual).toString());
        am.setHargajual(input.get(FieldParameter.hargajual).toString());
        am.setPpob_profit(input.get(FieldParameter.ppob_profit).toString());
        am.setRef_profit(input.get(FieldParameter.ref_profit).toString());
        am.setPoin(input.get(FieldParameter.poin).toString());
        am.setHargajual_noncorp(input.get(FieldParameter.hargajual_noncorp).toString());
        am.setPpob_profit_noncorp(input.get(FieldParameter.ppob_profit_noncorp).toString());
        am.setFeejual_noncorp(input.get(FieldParameter.feejual_noncorp).toString());
        am.setPoin_noncorp(input.get(FieldParameter.poin_noncorp).toString());

        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.updateproduk(am);
        input.put(FieldParameter.resp_code, Status);
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "mengupdate produk untuk user " + input.get(FieldParameter.trancodeid).toString());
        }
        return input;
    }
}
