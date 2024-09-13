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
public class RejectVerifikasi {

    private static Logger log = Logger.getLogger(RejectVerifikasi.class);

    public HashMap process(HashMap input) throws ParseException, MessagingException, UnsupportedEncodingException {
        Verifikasi verifikasi = new Verifikasi();
        verifikasi.setAgent_id(input.get(RuleNameParameter.agent_id).toString());
        BackendDBProcess dp = new BackendDBProcess();
        String userEmail = dp.getUserEmail(input.get(RuleNameParameter.agent_id).toString());
        String Status = dp.rejectVerifikasi(input.get(RuleNameParameter.agent_id).toString());
        input.put(FieldParameter.resp_code, Status);
        if (Status.equals("0000")) {
            log.info("Try to Send Mail notif . . . .");
            String detail = dp.getUserDetail(input.get(RuleNameParameter.agent_id).toString());
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", RuleNameParameter.host);
            props.put("mail.smtp.port", "587");
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(RuleNameParameter.emailsmtp_user, RuleNameParameter.emailsmtp_password);
                }
            });
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(RuleNameParameter.emailsmtp_user, RuleNameParameter.APS_NAME));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(userEmail));
                message.setSubject("[" + RuleNameParameter.APS_NAME + "] Notifikasi");
                String msg = "<body>\n"
                        //                        + "<pre>Hi kodokmerem ,\n"
                        + "<pre>Hi " + detail + ",\n"
                        //                    + "<pre>Hi Hardcoded,\n"
                        + "\n"
                        + "       \n"
                        + "Foto anda belum memenuhi syarat verifikasi Akun " + RuleNameParameter.APS_NAME + " . Silahkan login ulang dan mengambil foto sesuai syarat dan ketentuan\n"
                        + "\n"
                        + RuleNameParameter.APS_NAME + " CUSTOMER SERVICE\n"
                        + RuleNameParameter.CC_WA
                        + RuleNameParameter.CC_WEB
                        + RuleNameParameter.CC_EMAIL
                        + "</pre>\n"
                        + RuleNameParameter.LOGO_URL
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

            dp.userYabes(input.get(FieldParameter.userlogin).toString(), "reject user " + input.get(RuleNameParameter.agent_id).toString());
        }
        return input;
    }
}
