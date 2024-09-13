/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.Connectionbiller;
import com.bopro.database.BackendDBProcess;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class AddConnectionBiller {

    private static Logger log = Logger.getLogger(AddConnectionBiller.class);

    public HashMap process(HashMap input) throws ParseException {
        Connectionbiller cbiller = new Connectionbiller();
        cbiller.setBillername(input.get(FieldParameter.billername).toString());
        cbiller.setBillercode(input.get(FieldParameter.billercode).toString());
        cbiller.setPackagename(input.get(FieldParameter.packagename).toString());
        cbiller.setUrlhost(input.get(FieldParameter.urlhost).toString()); 
        
        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.addConnectionBiller(cbiller);
        input.put(FieldParameter.resp_code, Status);
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "menambahkan connection " + input.get(FieldParameter.billername).toString());
        }
        return input;
    }
}
