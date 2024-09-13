/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.pengembalian;
import com.bopro.database.BackendDBProcess;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

public class PengembalianDana {

    private static Logger log = Logger.getLogger(PengembalianDana.class);

    public HashMap process(HashMap input) throws ParseException {
        pengembalian dana = new pengembalian();
        dana.setRrn(input.get(FieldParameter.rrn).toString());

        BackendDBProcess dp = new BackendDBProcess();
        String status = dp.PengembalianDana(dana);
        input.put(FieldParameter.resp_code, status);
        if (status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "mengembalikan dana " + input.get(FieldParameter.rrn));
            log.info("\n" + input.get(FieldParameter.userlogin).toString() + "mengembalikan dana " + input.get(FieldParameter.rrn) + "\n");
        }
        return input;
    }
}
