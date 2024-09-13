/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.model.Verifikasi;
import com.bopro.database.BackendDBProcess;
import com.bopro.parameter.FieldParameter;
import com.bopro.parameter.RuleNameParameter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class UpdateVerifikasi {

    private static Logger log = Logger.getLogger(UpdateVerifikasi.class);

    public HashMap process(HashMap input) throws ParseException, MessagingException, UnsupportedEncodingException {
        Verifikasi verifikasi = new Verifikasi();
        verifikasi.setAgent_id(input.get(RuleNameParameter.agent_id).toString());

        BackendDBProcess dp = new BackendDBProcess();
        String Status = dp.updateVerifikasi(input.get(RuleNameParameter.agent_id).toString());
        input.put(FieldParameter.resp_code, Status);
        if (Status.equals("0000")) {

            log.info("Try to Send Mail notif . . . .");
            String useremail = input.get(RuleNameParameter.agent_id).toString();

            String detail = dp.getUserDetail(useremail);
            final String username = "verification@cuso-mobile.id";
            final String password = "211011elda";
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "cuso-mobile.id");
            props.put("mail.smtp.port", "587");
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username, "CUSOMOBILE"));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(useremail));
                message.setSubject("[cuso-mobile.id] Notifikasi");
                String msg = "<body>\n"
//                        + "<pre>Hi kodokmerem ,\n"
                        + "<pre>Hi " + detail + ",\n"

                        //                    + "<pre>Hi Hardcoded,\n"
                        + "\n"
                        + "       \n"
                        + "Akun Cuso Mobile anda telah terverifikasi. Silahkan login ulang untuk mengaktifkan akun anda :\n"
                        + "\n"
                        + "CUSO mobile CUSTOMER SERVICE\n"
                        + "      Whatsapp (WA)	:	08119180060\n"
                        + "      Web		:	http://www.cuso-mobile.id\n"
                        + "      Email		:	cs@cuso-mobile.id\n"
                        + "</pre>\n"
                        + "<img src=\"http://cuso-mobile.id/cusologo.png\"  height=\"47\">\n"
                        + "\n"
                        + "</body>\n"
                        + "";
                message.setContent(msg, "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                System.out.println("E : " + e);
                System.out.println("Fail");
                log.info("Send notif to email : Fail!");
//            return false;
//            return "0001";
//                        return "0000";

//            throw new RuntimeException(e);
            }

            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "memverifikasi agent " + input.get(RuleNameParameter.agent_id).toString());
        }
        return input;
    }
}
