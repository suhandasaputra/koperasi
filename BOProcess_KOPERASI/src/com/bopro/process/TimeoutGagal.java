/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.bopro.model.timeoutgagal;
import com.bopro.parameter.FieldParameter;
//import com.bopro.model.Ads;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

public class TimeoutGagal {

    private static Logger log = Logger.getLogger(TimeoutGagal.class);

    public HashMap process(HashMap input) throws ParseException {
        timeoutgagal dana = new timeoutgagal();
        dana.setRrn(input.get(FieldParameter.rrn).toString());

        BackendDBProcess dp = new BackendDBProcess();
        String status = dp.Timeout_gagal_dibiller(dana);
        input.put(FieldParameter.resp_code, status);
        if (status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "merubah trx timeout " + input.get(FieldParameter.rrn)+ " menjadi sukses");
            log.info("\n" + input.get(FieldParameter.userlogin).toString() + "merubah trx timeout " + input.get(FieldParameter.rrn) + " menjadi sukses"+ "\n");
        }
        return input;
    }
}
