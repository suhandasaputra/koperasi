/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class DeleteConnection {

    private static Logger log = Logger.getLogger(DeleteConnection.class);

    public HashMap process(HashMap input) throws ParseException {
        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.deleteConnectionBiller(input.get(FieldParameter.billercode).toString());
        input.put(FieldParameter.resp_code, Status);
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "menghapus connection " + input.get(FieldParameter.billercode).toString());
        }
        return input;
    }
}
