/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.timeout;
import com.bopro.database.BackendDBProcess;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

public class Timeout {

    private static Logger log = Logger.getLogger(Timeout.class);

    public HashMap process(HashMap input) throws ParseException {
        timeout dana = new timeout();
        dana.setRrn(input.get(FieldParameter.rrn).toString());

        BackendDBProcess dp = new BackendDBProcess();
        String status = dp.Timeout_sukses_dibiller(dana);
        input.put(FieldParameter.resp_code, status);
        if (status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "merubah trx timeout " + input.get(FieldParameter.rrn)+ " menjadi sukses");
            log.info("\n" + input.get(FieldParameter.userlogin).toString() + "merubah trx timeout " + input.get(FieldParameter.rrn) + " menjadi sukses"+ "\n");
        }
        return input;
    }
}
