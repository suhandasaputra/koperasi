/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.Billerproduct;
import com.bopro.database.BackendDBProcess;
import com.bopro.parameter.FieldParameter;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class UpdateProductBiller {

    private static Logger log = Logger.getLogger(UpdateProductBiller.class);

    public HashMap process(HashMap input) throws ParseException {
        Billerproduct billerproduct = new Billerproduct();
        
        
//         req.put(FieldParameter.procCode, ProcessingCode.updateProductBiller);
//            req.put(FieldParameter.userlogin, session.getAttribute("userlogin"));
//            req.put(FieldParameter.billername, request.getParameter(FieldParameter.billername));
//            req.put(FieldParameter.tcbiller, request.getParameter(FieldParameter.tcbiller));
//            req.put(FieldParameter.trancodename, request.getParameter(FieldParameter.trancodename));
//            req.put(FieldParameter.tctype, request.getParameter(FieldParameter.tctype));
//            req.put(FieldParameter.hargabeli, request.getParameter(FieldParameter.hargabeli));
//            req.put(FieldParameter.feebeli, request.getParameter(FieldParameter.feebeli));
        
        
        
        
        billerproduct.setBillername(input.get(FieldParameter.billername).toString());
        billerproduct.setTcbiller(input.get(FieldParameter.tcbiller).toString());
        billerproduct.setTrancodename(input.get(FieldParameter.trancodename).toString());
        billerproduct.setTctype(input.get(FieldParameter.tctype).toString());
        billerproduct.setHargabeli(input.get(FieldParameter.hargabeli).toString());
        billerproduct.setFeebeli(input.get(FieldParameter.feebeli).toString());
//                billerproduct.setPoin(input.get(FieldParameter.poin).toString());


        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.updateProductBiller(billerproduct);
        input.put(FieldParameter.resp_code, Status);
//                        String username = session.getAttribute("username").toString();
//                String activitas = "menambahkan agent yabes " + request.getParameter("agentId");
        if (Status.equals("0000")) {
            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "mengupdate product biller " + input.get(FieldParameter.tcbiller).toString());
//        input.put(FieldParameter.listAgent, element);
        }
        return input;
    }
}
