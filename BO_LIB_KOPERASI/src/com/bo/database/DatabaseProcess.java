/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bo.database;

import com.bo.entity.Tempmsg;
import com.bo.parameter.FieldParameter;
import com.bo.parameter.RuleNameParameter;
import com.bo.entity.Messagein;
import com.bo.entity.SocketConnectionEntity;
import com.bo.entity.SocketDetail;
import com.bo.function.JsonProcess;
import com.bo.function.SHA256Enc;
import com.bo.function.SendHttpProcess;
import com.bo.function.StringFunction;
import com.bo.parameter.StaticParameter;
import com.bo.singleton.DatasourceEntry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pri.vate.klas.function.Func1;

/**
 *
 * @author herrysuganda
 */
public class DatabaseProcess {

    private static Logger log = Logger.getLogger(DatabaseProcess.class);

    public List getSocketConnectionList() {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        List l = new ArrayList();
        SocketDetail socketDetail;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT todirect, host, statusopen, statusstart, seq, port, statusconnect, billercode, typeapp, conname, autosignon, packagename FROM socketconn where typeconn = 'socket' order by statusconnect, todirect, seq asc");
            rs = stat.executeQuery();
            while (rs.next()) {
                socketDetail = new SocketDetail();
                socketDetail.setTodirect(rs.getString("todirect"));
                socketDetail.setHost(rs.getString("host"));
                socketDetail.setStatusOpen(rs.getBoolean("statusopen"));
                socketDetail.setStatusStart(rs.getBoolean("statusstart"));
                socketDetail.setStatusConnect(rs.getBoolean("statusconnect"));
                socketDetail.setAutosignon(rs.getBoolean("autosignon"));
                socketDetail.setUrutan(rs.getInt("seq"));
                socketDetail.setJenis(rs.getString("typeapp"));
                socketDetail.setBankCode(rs.getString("billercode"));
                socketDetail.setConname(rs.getString("conname"));
                socketDetail.setPackageName(rs.getString("packagename"));
                socketDetail.setPort(rs.getInt("port"));
                l.add(socketDetail);
            }
        } catch (SQLException ex) {
            log.error("getSocketConnectionList : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return l;
    }

    public Tempmsg getMessageRespFromTempmsg(Tempmsg tempmsg) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        tempmsg.setStatusreply(false);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT respbiller, responsecode FROM tempmsg where msgid = ?  and statusreply is true");
            stat.setString(1, tempmsg.getMsgid());
            rs = stat.executeQuery();
            while (rs.next()) {
                tempmsg = new Tempmsg();
                tempmsg.setRespisobank(rs.getString("respbiller"));
                tempmsg.setResponsecode(rs.getString("responsecode"));
                tempmsg.setStatusreply(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getMessageRespFromTempmsg : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return tempmsg;
    }

    public SocketConnectionEntity getSocketConnectionDetail(SocketConnectionEntity sce) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        List l = new ArrayList();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT host, statusopen, port, headertype, lengthincl, typeapp, autosignon FROM socketconn where todirect = ? and billercode = ? and seq = ? order by seq asc");
            stat.setString(1, sce.getJenis());
            stat.setString(2, sce.getBankCode());
            stat.setInt(3, sce.getUrutan());
            rs = stat.executeQuery();
            while (rs.next()) {
                sce.setIpAddress(rs.getString("host"));
                sce.setOpenSocket(rs.getBoolean("statusopen"));
                sce.setPort(rs.getInt("port"));
                sce.setType(rs.getString("typeapp"));
                sce.setLengthIncl(rs.getBoolean("lengthincl"));
                sce.setAutosignon(rs.getBoolean("autosignon"));
                sce.setHeaderMessageType(rs.getInt("headertype"));

            }
        } catch (SQLException ex) {
            log.error("getSocketConnectionList : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return sce;
    }

    public boolean saveMessageToTempmsg(Tempmsg tempmsg, Messagein msgin) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO tempmsg(msgid, inpmsg, reqbiller, requesttime, noref, cardno, billercode, "
                    + "fromaccount, toaccount, amount, proccode, transactionid, productcode, trxidbackend, fromsocket, fromagent,custno, "
                    + "hargajual, hargabeli, feejual, feebeli, profit) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stat.setString(1, tempmsg.getMsgid());
            stat.setString(2, tempmsg.getAbdmsg());
            stat.setString(3, tempmsg.getReqisobank());
            stat.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            stat.setString(5, tempmsg.getNoref());
            stat.setString(6, tempmsg.getCardno());
            stat.setString(7, tempmsg.getBankcode());
            stat.setString(8, tempmsg.getFromaccount());
            stat.setString(9, tempmsg.getToaccount());
            stat.setString(10, tempmsg.getAmount());
            stat.setString(11, tempmsg.getProccode());
            stat.setString(12, tempmsg.getTransactionid());
            stat.setString(13, tempmsg.getProductcode());
            stat.setString(14, tempmsg.getTrxidbackend());
            stat.setString(15, tempmsg.getFromSocket());
            stat.setString(16, tempmsg.getBankcodefrom());
            stat.setString(17, tempmsg.getCustNo());
            stat.setInt(18, msgin.getHargajual());
            stat.setInt(19, msgin.getHargabeli());
            stat.setInt(20, msgin.getFeejual());
            stat.setInt(21, msgin.getFeebeli());
            stat.setInt(22, msgin.getProfit());
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("saveMessageToTempmsg : " + tempmsg.getMsgid() + " - " + ex.getMessage());
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public HashMap pinInput(String userid, String userpin) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO user_pin_tbl(userid, userpin) VALUES (?, ? )");
            stat.setInt(1, Integer.valueOf(userid));
            stat.setString(2, "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
            stat.executeUpdate();
            result.put(RuleNameParameter.resp_code, "0");

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put(RuleNameParameter.resp_code, "01");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public String getNextStan() {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        int currentStan = 1;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT stanno FROM stanmanage where standate = current_date");
            rs = stat.executeQuery();
            while (rs.next()) {
                currentStan = rs.getInt("stanno");
            }
            clearStatment(stat);
            stat = conn.prepareStatement("UPDATE stanmanage SET stanno=? WHERE standate=current_date;");
            stat.setInt(1, currentStan + 1);
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("getNextStan : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return String.valueOf(currentStan);
    }

    public boolean setStatusSocketStart(boolean status, String todirect, int urutan, String billercode) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE socketconn SET  statusstart=? WHERE todirect=? and seq=? and billercode=?");
            stat.setString(4, billercode);
            stat.setBoolean(1, status);
            stat.setString(2, todirect);
            stat.setInt(3, urutan);
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("setStatusSocketStart : " + ex.getMessage());
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean setStatusSocketConnect(boolean status, String todirect, int urutan, String billercode) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            if (todirect.equals(RuleNameParameter.yabes)) {
                stat = conn.prepareStatement("UPDATE socketconn SET  statusconnect=? WHERE todirect=? and seq=?");
            } else if (todirect.equals(RuleNameParameter.bank)) {
                stat = conn.prepareStatement("UPDATE socketconn SET  statusconnect=? WHERE todirect=? and seq=? and billercode=?");
                stat.setString(4, billercode);
            }
            stat.setBoolean(1, status);
            stat.setString(2, todirect);
            stat.setInt(3, urutan);
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("setStatusSocketConnect : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean saveMessageToMessageIn(String message, String typeMessage, String fromSocket, String trxmsgid, String packagename) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO messagein(message, waktu, typemsg, fromsocket, trxmsgid, packagename) VALUES (?, ?, ?, ?, ?, ?)");
            stat.setString(1, message);
            stat.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
            stat.setString(3, typeMessage);
            stat.setString(4, fromSocket);
            stat.setString(5, trxmsgid);
            stat.setString(6, packagename);
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("saveMessageToMessageIn : " + ex.getMessage());
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean saveMessageToMessageOut(String message, String typeMessage, String toSocket) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO messageout(message, waktu, typemsg, tosocket) VALUES (?, ?, ?, ?)");
            stat.setString(1, message);
            stat.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
            stat.setString(3, typeMessage);
            stat.setString(4, toSocket);
            stat.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("saveMessageToMessageOut : " + ex.getMessage());

        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public List getSocketConnectionList(String todirect) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        List l = new ArrayList();
        SocketDetail socketDetail;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT todirect, host, statusopen, statusstart, seq, port, statusconnect, headertype, billercode, lengthincl, typeapp, autosignon, packagename FROM socketconn where todirect = ? order by seq asc");
            stat.setString(1, todirect);
            rs = stat.executeQuery();
            while (rs.next()) {
                socketDetail = new SocketDetail();
                socketDetail.setJenis(rs.getString("todirect"));
                socketDetail.setHost(rs.getString("host"));
                socketDetail.setStatusOpen(rs.getBoolean("statusopen"));
                socketDetail.setStatusStart(rs.getBoolean("statusstart"));
                socketDetail.setStatusConnect(rs.getBoolean("statusconnect"));
                socketDetail.setUrutan(rs.getInt("seq"));
                socketDetail.setPort(rs.getInt("port"));
                socketDetail.setTypeapp(rs.getString("typeapp"));
                socketDetail.setLengthIncl(rs.getBoolean("lengthincl"));
                socketDetail.setAutosignon(rs.getBoolean("autosignon"));
                socketDetail.setHeaderType(rs.getInt("headertype"));
                socketDetail.setBankCode(rs.getString("billercode"));
                socketDetail.setPackageName(rs.getString("packagename"));
                l.add(socketDetail);
            }
        } catch (SQLException ex) {
            log.error("getSocketConnectionList : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return l;
    }

    public boolean setStatusSocketStart(boolean status) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE socketconn SET  statusstart=?");
            stat.setBoolean(1, status);
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("setStatusSocketStart : " + ex.getMessage());
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean cleanData() {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();

            log.info("clear tempmsg table");
            stat = conn.prepareStatement("INSERT INTO tempmsg_backup(msgid, inpmsg, reqbiller, respbiller, statusreply, requesttime, "
                    + "responsetime, noref, billercode, fromaccount, toaccount, "
                    + "responsecode, amount, proccode, transactionid, productcode, trxidbackend, fromagent, rcinternal, custno, "
                    + "hargajual, hargabeli, feejual, feebeli, usr_cashback, ppob_profit, ref_profit, refer, transactioncode, prev_bal, curr_bal) SELECT msgid, inpmsg, reqbiller, respbiller, statusreply, requesttime, "
                    + "responsetime, noref, billercode, fromaccount, toaccount, "
                    + "responsecode, amount, proccode, transactionid, productcode, trxidbackend, fromagent, rcinternal, custno, "
                    + "hargajual, hargabeli, feejual, feebeli, usr_cashback, ppob_profit, ref_profit, refer, transactioncode, prev_bal, curr_bal FROM tempmsg where date(requesttime) < current_date-1");
            stat.executeUpdate();
            clearStatment(stat);

//            Transaction Archievement Proccess in development
//            Check Periode
//            stat = conn.prepareStatement("select count(*) from transaction_archievement where periode ='" + Periode.today() + "'");
//            rs = stat.executeQuery();
//            while (rs.next()) {
//                if (rs.getInt("count") == 0) {
//                    log.info("New Periode Archievement");
//                    clearStatment(stat);
//                    //GetLastDayData
//                    stat = conn.prepareStatement("select count(*) as transaction, SUM(cast (amount as int)) as totalamount, SUM(cast (profit as int)) as profit "
//                            + "from tempmsg where "
//                            + "(rcinternal='0068' and proccode ='290000' and date(requesttime) < current_date-1) or"
//                            + "(rcinternal='0068' and proccode ='280000' and date(requesttime) < current_date-1) or"
//                            + "(rcinternal='0000' and proccode ='290000' and date(requesttime) < current_date-1) or"
//                            + "(rcinternal='0000' and proccode ='280000' and date(requesttime) < current_date-1) ");
//                    rs = stat.executeQuery();
//                    while (rs.next()) {
//                        rs.getString("totalamount");
//                        rs.getString("profit");
//                        stat = conn.prepareStatement("INSERT INTO transaction_archievement(periode,transaction,agentactive,total_amount_transaction,profit)values(?,?,?,?,?)");
//                        stat.setString(1, Periode.lastDayOfMonth());
//                        stat.setString(2, rs.getString("transaction"));
//                        stat.setString(3, "0");
//                        stat.setString(4, rs.getString("totalamount"));
//                        stat.setString(5, rs.getString("profit"));
//                        stat.executeUpdate();
//                    }
//                    clearResultset(rs);
//                    clearStatment(stat);
//                    //Hitung AgentActiveOfDAy
//                    stat = conn.prepareStatement("select fromagent from tempmsg "
//                            + "where requesttime  < '" + Periode.todayTimestamp() + "'"
//                            + "AND requesttime  < '" + Periode.lastDayOfMonthTimestamp() + "'"
//                            + "group by fromagent");
//                    rs = stat.executeQuery();
//                    int agentActive = 0;
//                    while (rs.next()) {
//                        agentActive += 1;
//                    }
//                    stat = conn.prepareStatement("update transaction_archievement set agentactive=cast(agentactive as int)+" + agentActive
//                            + "where periode ='" + Periode.lastDayOfMonth() + "'"
//                    );
//                    stat.executeUpdate();
//                } else {
//                    log.info("Update Periode Archievement");
//                    clearStatment(stat);
//                    //GetLastDayData
//                    stat = conn.prepareStatement("select count(*) as transaction, SUM(cast (amount as int)) as totalamount, SUM(cast (profit as int)) as profit "
//                            + "from tempmsg where "
//                            + "(rcinternal='0068' and proccode ='290000' and date(requesttime) < current_date-1) or"
//                            + "(rcinternal='0068' and proccode ='280000' and date(requesttime) < current_date-1) or"
//                            + "(rcinternal='0000' and proccode ='290000' and date(requesttime) < current_date-1) or"
//                            + "(rcinternal='0000' and proccode ='280000' and date(requesttime) < current_date-1) ");
//                    rs = stat.executeQuery();
//                    while (rs.next()) {
//                        stat = conn.prepareStatement("update transaction_archievement set "
//                                + "transaction = cast(transaction as int)+" + Integer.valueOf(rs.getString("transaction")) + ","
//                                + "totalamount = cast(totalamount as int)+" + Integer.valueOf(rs.getString("totalamount")) + ","
//                                + "profit=cast(profit as int)+" + Integer.valueOf(rs.getString("profit"))
//                                + "where periode ='" + Periode.lastDayOfMonth() + "'"
//                        );
//                        stat.executeUpdate();
//                    }
//                    clearResultset(rs);
//                    clearStatment(stat);
//                    //Hitung AgentActiveOfDAy
//                    stat = conn.prepareStatement("select fromagent from tempmsg "
//                            + "where requesttime  < '" + Periode.todayTimestamp() + "'"
//                            + "AND requesttime  < '" + Periode.lastDayOfMonthTimestamp() + "'"
//                            + "group by fromagent");
//                    rs = stat.executeQuery();
//                    int agentActive = 0;
//                    while (rs.next()) {
//                        agentActive += 1;
//                    }
//                    stat = conn.prepareStatement("update transaction_archievement set agentactive=cast(agentactive as int)+" + agentActive
//                            + "where periode ='" + Periode.lastDayOfMonth() + "'"
//                    );
//                    stat.executeUpdate();
//                }
//            }
//              Transaction Archievement Proccess end
            clearStatment(stat);

            stat = conn.prepareStatement("DELETE FROM tempmsg where date(requesttime) < current_date-1");
            stat.executeUpdate();
            log.info("clear tempmsg table successful");
            clearStatment(stat);

            log.info("clear messagein table");
            stat = conn.prepareStatement("INSERT INTO messagein_backup(message, waktu, status, typemsg, fromsocket, msginid) SELECT message, waktu, status, typemsg, fromsocket, msginid FROM messagein where date(waktu) < current_date");
            stat.executeUpdate();
            clearStatment(stat);
            stat = conn.prepareStatement("DELETE FROM messagein where date(waktu) < current_date");
            stat.executeUpdate();
            log.info("clear messagein table successful");
            clearStatment(stat);
            stat = conn.prepareStatement("DELETE FROM messagein_backup where date(waktu_backup) < current_date-30");
            stat.executeUpdate();
            log.info("clear messagein_backup table successful");
            clearStatment(stat);

            log.info("clear messageout table");
            stat = conn.prepareStatement("INSERT INTO messageout_backup(message, waktu, status, msgoutid, typemsg, tosocket) SELECT message, waktu, status, msgoutid, typemsg, tosocket FROM messageout where date(waktu) < current_date");
            stat.executeUpdate();
            clearStatment(stat);
            stat = conn.prepareStatement("DELETE FROM messageout where date(waktu) < current_date");
            stat.executeUpdate();
            log.info("clear messageout table successful");
            clearStatment(stat);
            stat = conn.prepareStatement("DELETE FROM messageout_backup where date(waktu_backup) < current_date-30");
            stat.executeUpdate();
            log.info("clear messageout_backup table successful");
            clearStatment(stat);
            log.info("EOD done");
            stat = conn.prepareStatement("UPDATE configapp SET eoddone=true");
            stat.executeUpdate();

            log.info("create next stan");
            stat = conn.prepareStatement("INSERT INTO stanmanage(standate) VALUES (current_date+1)");
            stat.executeUpdate();
            clearStatment(stat);

            log.info("clear am_update_balance table");
            stat = conn.prepareStatement("DELETE FROM am_update_balance where process_exe = 'false' and date(topup_date) < current_date-4");
            stat.executeUpdate();
            log.info("clear am_update_balance table successful");
            clearStatment(stat);

            log.info("clear emoney table");
            stat = conn.prepareStatement("INSERT INTO emoney_backup(trxcode, fromaccount, amount, createtime, exptime, toaccount, "
                    + "responsetime, statusreply) "
                    + "SELECT trxcode, fromaccount, amount, createtime, exptime, toaccount, "
                    + "responsetime, statusreply FROM emoney where date(createtime) < current_date-1");
            stat.executeUpdate();
            clearStatment(stat);
            stat = conn.prepareStatement("DELETE FROM emoney where date(createtime) < current_date-1");
            stat.executeUpdate();
            log.info("clear emoney table successful");
            clearStatment(stat);
        } catch (SQLException ex) {
            log.error("cleanData : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public void setNextStan() {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT count(*) as count from stanmanage where standate = current_date");
            rs = stat.executeQuery();
            while (rs.next()) {
                if (rs.getInt("count") == 0) {
                    log.info("create next stan");
                    clearStatment(stat);
                    stat = conn.prepareStatement("INSERT INTO stanmanage(standate) VALUES (current_date)");
                    stat.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getSelisihWaktuW4FromTempmsg : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
    }

    public HashMap backendInput(String voucherid, String vouchername, String prepaidid, int nominal) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO voucher_tbl(voucherid,vouchername,prepaidid,nominal ) VALUES (?, ?, ?, ? )");
            stat.setString(1, voucherid);
            stat.setString(2, vouchername);
            stat.setString(3, prepaidid);
            stat.setInt(4, nominal);
//          
            stat.executeUpdate();
            result.put(RuleNameParameter.resp_code, "0");

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put(RuleNameParameter.resp_code, "01");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap withdrawalCodeInput(String hpNumber, String code) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO am_authcode VALUES (?, ?)");
            stat.setString(1, hpNumber);
            stat.setString(2, code);
//          
            stat.executeUpdate();
            result.put(RuleNameParameter.resp_code, "0000");

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put(RuleNameParameter.resp_code, "1001");
            result.put(RuleNameParameter.resp_desc, "gagal membuat field am_authcode");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap backendUpdate(String voucherid, String vouchername, String prepaidid, int nominal) {
//        public HashMap backendUpdate(String voucherid) {
//       public String delete_FirstTimeUser(String userid) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();//"update bb_user set status='1' Where userid = ? "
            stat = conn.prepareStatement("update voucher_tbl set vouchername = ? , prepaidid = ?, nominal = ? where voucherid= ?");

            stat.setString(1, vouchername);
            stat.setString(2, prepaidid);
            stat.setInt(3, nominal);
            stat.setString(4, voucherid);
            stat.executeUpdate();

            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeLoginFailed);
//            rs = stat.executeQuery();

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("Delete SuperUser Failed : " + ex.getMessage());
//            result.put("respcode", "59");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
//        return new Func1().generateJson(result);
        return null;
    }

    public HashMap backendDelete(String voucherid) {
//        public HashMap backendDelete(String voucherid, String vouchername, String prepaidid, int nominal) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("DELETE from voucher_tbl where voucherid = ?");
            stat.setString(1, voucherid);
//            stat.setString(2, vouchername);
//            stat.setString(3, prepaidid);
//            stat.setInt(4, nominal);
//          
            stat.executeUpdate();
            result.put(RuleNameParameter.resp_code, "0");

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put(RuleNameParameter.resp_code, "01");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap branchInput(String branchid, String branchname, String address, String areacode, String contactperson, String telephone, String at) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO bb_branch(branchid,branchname,address,areacode,contactperson,telephone,at ) VALUES (?, ?, ?, ?, ?, ?, ? )");
            stat.setString(1, branchid);
            stat.setString(2, branchname);
            stat.setString(3, address);
            stat.setString(4, areacode);
            stat.setString(5, contactperson);
            stat.setString(6, telephone);
            stat.setString(7, at);
//          
            stat.executeUpdate();
            result.put(RuleNameParameter.resp_code, "0");

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put(RuleNameParameter.resp_code, "01");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public Messagein getSendToConn(String trancode, Messagein msgin) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT b.hargajual, "
                    + "b.feejual, "
                    + "b.ppob_profit, "
                    + "b.ref_profit, "
                    + "b.usr_cashback, "
                    //                    + "b.hargajual_noncorp, "
                    //                    + "b.ppob_profit_noncorp, "
                    + "a.billercode, "
                    + "c.hargabeli, "
                    + "c.feebeli, "
                    + "c.tcbiller, "
                    + "c.trancodeid "
                    + "FROM trancode a "
                    + "inner join "
                    + "am_trancode b "
                    + "on b.trancodeid = a.trancodeid "
                    + "inner join "
                    + "trancode_biller c "
                    + "on c.trancodeid = a.trancodeid "
                    + "and c.billercode = a.billercode "
                    + "where a.trancodeid = ?");
            stat.setString(1, trancode);
            rs = stat.executeQuery();
            while (rs.next()) {
                msgin.setSendto(rs.getString("billercode"));
                msgin.setTcbiller(rs.getString("tcbiller"));
                msgin.setHargajual(rs.getInt("hargajual"));
                msgin.setFeejual(rs.getInt("feejual"));
                msgin.setHargabeli(rs.getInt("hargabeli"));
                msgin.setFeebeli(rs.getInt("feebeli"));
                msgin.setPpob_profit(rs.getInt("ppob_profit"));
                msgin.setRef_profit(rs.getInt("ref_profit"));
                msgin.setUsr_cashback(rs.getInt("usr_cashback"));
//                msgin.setHargajual_noncorp(rs.getInt("hargajual_noncorp"));
//                msgin.setPpob_profit_noncorp(rs.getInt("ppob_profit_noncorp"));
//                log.info(rs.getString("billercode"));

            }
        } catch (SQLException ex) {
            log.error("getSendToConn : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return msgin;
    }

    public Messagein getSendToConnPostpaid(String trancode, Messagein msgin, String refer) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT b.hargajual, "
                    + "b.feejual, "
                    + "b.feejual_noncorp, "
                    + "b.ppob_profit, "
                    + "b.ref_profit, "
                    + "b.usr_cashback, "
                    //                    + "b.hargajual_noncorp, "
                    //                    + "b.ppob_profit_noncorp, "
                    + "a.billercode, "
                    + "c.hargabeli, "
                    + "c.feebeli, "
                    + "c.tcbiller, "
                    + "c.trancodeid "
                    + "FROM trancode a "
                    + "inner join "
                    + "am_trancode b "
                    + "on b.trancodeid = a.trancodeid "
                    + "inner join "
                    + "trancode_biller c "
                    + "on c.trancodeid = a.trancodeid "
                    + "and c.billercode = a.billercode "
                    + "where a.trancodeid = ?");
            stat.setString(1, trancode);
            rs = stat.executeQuery();
            while (rs.next()) {
                msgin.setRefer(refer);
                msgin.setSendto(rs.getString("billercode"));
                msgin.setTcbiller(rs.getString("tcbiller"));
                msgin.setHargajual(rs.getInt("hargajual"));
                msgin.setHargabeli(rs.getInt("hargabeli"));
                msgin.setFeebeli(rs.getInt("feebeli"));
                msgin.setPpob_profit(rs.getInt("ppob_profit"));
                msgin.setUsr_cashback(rs.getInt("usr_cashback"));
                if (refer.equals("") || refer == null) {
                    msgin.setRef_profit(rs.getInt("ref_profit"));
                    msgin.setFeejual(rs.getInt("feejual_noncorp"));
                } else {

                    msgin.setFeejual(rs.getInt("feejual"));

//                log.info(rs.getString("billercode"));
                }

//                msgin.setHargajual_noncorp(rs.getInt("hargajual_noncorp"));
//                msgin.setPpob_profit_noncorp(rs.getInt("ppob_profit_noncorp"));
//                log.info(rs.getString("billercode"));
            }
        } catch (SQLException ex) {
            log.error("getSendToConn : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return msgin;
    }

    public Messagein getSendToConnPrepaid(String trancode, Messagein msgin, String refer) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT b.hargajual, "
                    + "b.feejual, "
                    + "b.ppob_profit, "
                    + "b.ref_profit, "
                    + "b.usr_cashback, "
                    + "b.hargajual_noncorp, "
                    + "b.ppob_profit_noncorp, "
                    + "a.billercode, "
                    + "c.hargabeli, "
                    + "c.feebeli, "
                    + "c.tcbiller, "
                    + "c.trancodeid "
                    + "FROM trancode a "
                    + "inner join "
                    + "am_trancode b "
                    + "on b.trancodeid = a.trancodeid "
                    + "inner join "
                    + "trancode_biller c "
                    + "on c.trancodeid = a.trancodeid "
                    + "and c.billercode = a.billercode "
                    + "where a.trancodeid = ?");
            stat.setString(1, trancode);
            rs = stat.executeQuery();
            while (rs.next()) {
                msgin.setRefer(refer);
                msgin.setSendto(rs.getString("billercode"));
                msgin.setTcbiller(rs.getString("tcbiller"));
                msgin.setHargajual(rs.getInt("hargajual"));
                msgin.setFeejual(rs.getInt("feejual"));
                msgin.setHargabeli(rs.getInt("hargabeli"));
                msgin.setFeebeli(rs.getInt("feebeli"));
                msgin.setUsr_cashback(rs.getInt("usr_cashback"));

                if (refer.equals("") || refer == null) {
                    msgin.setHargajual_noncorp(rs.getInt("hargajual_noncorp"));
                    msgin.setPpob_profit_noncorp(rs.getInt("ppob_profit_noncorp"));
                } else {
                    msgin.setPpob_profit(rs.getInt("ppob_profit"));
                    msgin.setRef_profit(rs.getInt("ref_profit"));
//                log.info(rs.getString("billercode"));
                }
            }
        } catch (SQLException ex) {
            log.error("getSendToConn : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return msgin;
    }

    public HashMap branchUpdate(String branchid, String branchname, String address, String areacode, String contactperson, String telephone, String at) {

        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();//"update bb_user set status='1' Where userid = ? "
            stat = conn.prepareStatement("update bb_branch set branchname = ? , address = ?, areacode = ?, contactperson = ?, telephone = ?, at = ?    where voucherid= ?");

            stat.setString(1, branchname);
            stat.setString(2, address);
            stat.setString(3, areacode);
            stat.setString(4, contactperson);
            stat.setString(5, telephone);
            stat.setString(6, at);
//            stat.setString(7, areacode);
            stat.executeUpdate();

            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeLoginFailed);
//            rs = stat.executeQuery();

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("Delete SuperUser Failed : " + ex.getMessage());
//            result.put("respcode", "59");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
//        return new Func1().generateJson(result);
        return null;
    }

    public HashMap branchDelete(String voucherid) {
//        public HashMap backendDelete(String voucherid, String vouchername, String prepaidid, int nominal) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("DELETE from voucher_tbl where voucherid = ?");
            stat.setString(1, voucherid);
//            stat.setString(2, vouchername);
//            stat.setString(3, prepaidid);
//            stat.setInt(4, nominal);
//          
            stat.executeUpdate();
            result.put(RuleNameParameter.resp_code, "0");

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put(RuleNameParameter.resp_code, "01");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap createCustomer(String username, String userpassword, String userlevel,
            String usercreate, String branchid, String officeid, String cardnumber,
            String phonenumber, String registernumber, String shortname, String title,
            String firstname, String lastname, String birthdate, String birthplace,
            String productcode, String objecttype, String actiontype, String parmcode,
            String value, String institution, String clienttype, String clientnumber,
            String phonetype, String imagename, String contractnumber, String virtualnumber, String physicalnumber) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO bb_user(username,userpassword,userlevel,usercreate,"
                    + "branchid, officeid, cardnumber, phonenumber, registernumber, shortname, title, "
                    + "firstname, lastname, birthdate, birthplace, productcode, objecttype, actiontype, "
                    + "parmcode, value, institution, clienttype, clientnumber, phonetype, imagename, contractnumber ,virtualnumber, physicalnumber ) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? ,? ,? ,? ,? ,? ,? ,?, ?, ? ,? ,? ,? )");
//            stat.setString(1, userid);
            stat.setString(1, username);
            stat.setString(2, "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
//            stat.setString(2, userpassword);
            stat.setString(3, userlevel);
            stat.setString(4, usercreate);
//            stat.setString(6, status);
            stat.setString(5, branchid);
            stat.setString(6, officeid);
            stat.setString(7, cardnumber);
            stat.setString(8, phonenumber);

            stat.setString(9, registernumber);
            stat.setString(10, shortname);
            stat.setString(11, title);
//            stat.setString(6, status);
            stat.setString(12, firstname);
            stat.setString(13, lastname);
            stat.setString(14, birthdate);
            stat.setString(15, birthplace);
            stat.setString(16, productcode);
            stat.setString(17, objecttype);
            stat.setString(18, actiontype);
            stat.setString(19, parmcode);
//            stat.setString(6, status);
            stat.setString(20, value);
            stat.setString(21, institution);
            stat.setString(22, clienttype);
            stat.setString(23, clientnumber);
            stat.setString(24, phonetype);
            stat.setString(25, imagename);
            stat.setString(26, contractnumber);
            stat.setString(27, virtualnumber);
            stat.setString(28, physicalnumber);

            stat.executeUpdate();
            result.put(FieldParameter.username, username);
            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap createAgent(String username, String userpassword, String userlevel,
            String usercreate, String branchid, String officeid, String cardnumber,
            String phonenumber, String registernumber, String shortname, String title,
            String firstname, String lastname, String birthdate, String birthplace,
            String productcode, String objecttype, String actiontype, String parmcode,
            String value, String institution, String clienttype, String clientnumber,
            String phonetype, String imagename, String contractnumber, String virtualnumber) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO bb_user(username,userpassword,userlevel,usercreate,"
                    + "branchid, officeid, cardnumber, phonenumber, registernumber, shortname, title, "
                    + "firstname, lastname, birthdate, birthplace, productcode, objecttype, actiontype, "
                    + "parmcode, value, institution, clienttype, clientnumber, phonetype, imagename, contractnumber, virtualnumber  ) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? ,? ,? ,? ,? ,? ,? ,?, ?, ? ,? ,? )");
//            stat.setString(1, userid);
            stat.setString(1, username);
//            stat.setString(2, userpassword);
            stat.setString(2, "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
            stat.setString(3, userlevel);
            stat.setString(4, usercreate);
//            stat.setString(6, status);
            stat.setString(5, branchid);
            stat.setString(6, officeid);
            stat.setString(7, cardnumber);
            stat.setString(8, phonenumber);

            stat.setString(9, registernumber);
            stat.setString(10, shortname);
            stat.setString(11, title);
//            stat.setString(6, status);
            stat.setString(12, firstname);
            stat.setString(13, lastname);
            stat.setString(14, birthdate);
            stat.setString(15, birthplace);
            stat.setString(16, productcode);
            stat.setString(17, objecttype);
            stat.setString(18, actiontype);
            stat.setString(19, parmcode);
//            stat.setString(6, status);
            stat.setString(20, value);
            stat.setString(21, institution);
            stat.setString(22, clienttype);
            stat.setString(23, clientnumber);
            stat.setString(24, phonetype);
            stat.setString(25, imagename);
            stat.setString(26, contractnumber);
            stat.setString(27, virtualnumber);

            stat.executeUpdate();
            result.put(FieldParameter.username, username);
            result.put(RuleNameParameter.resp_code, "00");

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put(RuleNameParameter.resp_code, "01");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap createMBO(String username, String userpassword, String userlevel, String usercreate, String branchid, String officeid, String cardnumber, String phonenumber, String registernumber, String shortname, String title, String firstname, String lastname, String birthdate, String birthplace, String productcode, String objecttype, String actiontype, String parmcode, String value, String institution, String clienttype, String clientnumber, String phonetype, String contractnumber) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO bb_user(username,userpassword,userlevel,usercreate,branchid, officeid, cardnumber, phonenumber, registernumber, shortname, title, firstname, lastname, birthdate, birthplace, productcode, objecttype, actiontype, parmcode, value, institution, clienttype, clientnumber, phonetype, contractnumber  ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )");
//            stat.setString(1, userid);
            stat.setString(1, username);
//            stat.setString(2, userpassword);
            stat.setString(2, "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
            stat.setString(3, userlevel);
            stat.setString(4, usercreate);
//            stat.setString(6, status);
            stat.setString(5, branchid);
            stat.setString(6, officeid);
            stat.setString(7, cardnumber);
            stat.setString(8, phonenumber);

            stat.setString(9, registernumber);
            stat.setString(10, shortname);
            stat.setString(11, title);
//            stat.setString(6, status);
            stat.setString(12, firstname);
            stat.setString(13, lastname);
            stat.setString(14, birthdate);
            stat.setString(15, birthplace);
            stat.setString(16, productcode);
            stat.setString(17, objecttype);
            stat.setString(18, actiontype);
            stat.setString(19, parmcode);
//            stat.setString(6, status);
            stat.setString(20, value);
            stat.setString(21, institution);
            stat.setString(22, clienttype);
            stat.setString(23, clientnumber);
            stat.setString(24, phonetype);
            stat.setString(25, contractnumber);

            stat.executeUpdate();

            result.put(FieldParameter.username, username);
            result.put(RuleNameParameter.resp_code, "0");

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put(RuleNameParameter.resp_code, "01");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public String delete_FirstTimeUser(String userid) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("update bb_user set status='1' Where userid = ? ");
            stat.setString(1, userid);
            stat.executeUpdate();
//            rs = stat.executeQuery();

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("Delete SuperUser Failed : " + ex.getMessage());
//            result.put("respcode", "59");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
//        return new Func1().generateJson(result);
        return null;
    }

    public String LoginFirsttime(String username, String password) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * FROM bb_user where username = ? and userpassword = ?");
            stat.setString(1, username);
            stat.setString(2, password);
            rs = stat.executeQuery();
            while (rs.next()) {
//                if (rs.getString("status").equals("1")) {
                result.put("resp_code", "00");
                result.put(FieldParameter.userid, rs.getString("userid"));
                result.put(FieldParameter.username, rs.getString("username"));
                result.put(FieldParameter.userpassword, rs.getString("userpassword"));
                result.put(FieldParameter.userlevel, rs.getString("userlevel"));
                result.put(FieldParameter.usercreate, rs.getString("usercreate"));
                result.put(FieldParameter.status, rs.getString("status"));
                result.put(FieldParameter.branchid, rs.getString("branchid"));
                result.put(FieldParameter.officeid, rs.getString("officeid"));

                result.put(FieldParameter.hpNumber, rs.getString("phonenumber"));
                result.put(FieldParameter.numfaillogin, rs.getString("numfaillogin"));
            }
            return new Func1().generateJson(result);

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getStorePin : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return "";
    }

    public String addUserMBO(String userid, String username, String userpassword, String userlevel, String usercreate, String status, String brancid, String officeid, String cardnumber, String phonenumber) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO bb_branch(userid, username, userpassword, userlevel, usercreate, status, brancid, officeid, cardnumber, phonenumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stat.setString(1, userid);
            stat.setString(2, username);
            stat.setString(3, userpassword);
            stat.setString(4, userlevel);
            stat.setString(5, usercreate);
            stat.setString(6, status);
            stat.setString(7, brancid);
            stat.setString(8, officeid);
            stat.setString(9, cardnumber);
            stat.setString(10, phonenumber);
            stat.executeUpdate();
            result.put("resp_code", "00");

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put("resp_code", "01");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return new Func1().generateJson(result);
    }

    public HashMap checkFirsttime(HashMap input) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeLoginFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT userid,status FROM bb_user where userid = ?");
            stat.setString(1, input.get(FieldParameter.userid).toString());
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.userid, rs.getString("userid"));
                result.put(FieldParameter.status, rs.getString("status"));
                result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserDetail : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getCreatorDetail(HashMap input) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeLoginFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT contractnumber, userid, username, userpassword, userlevel, createdate, usercreate, status, branchid, officeid, cardnumber, phonenumber, numfaillogin FROM bb_user where userid = ?");
            stat.setString(1, input.get(FieldParameter.usercreate).toString());
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.userid, rs.getString("userid"));
                result.put(FieldParameter.username, rs.getString("username"));
                result.put(FieldParameter.userpassword, rs.getString("userpassword"));
                result.put(FieldParameter.userlevel, rs.getString("userlevel"));
                result.put(FieldParameter.usercreate, rs.getString("usercreate"));
                result.put(FieldParameter.status, rs.getString("status"));
                result.put(FieldParameter.branchid, rs.getString("branchid"));
                result.put(FieldParameter.officeid, rs.getString("officeid"));
                result.put(FieldParameter.cardNumber, rs.getString("cardNumber"));
                result.put(FieldParameter.hpNumber, rs.getString("phonenumber"));
                result.put(FieldParameter.numfaillogin, rs.getString("numfaillogin"));
                result.put(FieldParameter.contractNumber, rs.getString("contractnumber"));
                result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserDetail : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getUserDepositDetail(HashMap input) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);

        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT shortname, userid, username, userpassword, userlevel, createdate, usercreate, status, branchid, officeid, cardnumber, phonenumber, contractnumber, virtualnumber, physicalnumber, note FROM bb_user where phonenumber = ?");
            stat.setString(1, input.get(FieldParameter.hpNumber).toString());
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.name, rs.getString("shortname"));
                result.put(FieldParameter.toaccount, rs.getString("virtualnumber"));
                result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserDetail : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getRedeemItemList() {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap hmData = new HashMap();
        List resultRow = new ArrayList();
        List resultColumn = new ArrayList();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT redeem_itemid, itemname, price, stock, start_date, end_date FROM redeem_itemlist WHERE current_date between date(start_date) and date(end_date)and stock > 0 order by redeem_itemid asc");
            rs = stat.executeQuery();
            while (rs.next()) {
                resultColumn.add(rs.getString("redeem_itemid"));
                resultColumn.add(rs.getString("itemname"));
                resultColumn.add(rs.getString("price"));
                resultColumn.add(rs.getString("stock"));
                resultColumn.add(rs.getString("start_date"));
                resultColumn.add(rs.getString("end_date"));
                resultRow.add(StringUtils.join(resultColumn, "|"));
                resultColumn.clear();
            }
            List list = new ArrayList();
            list.add(StringUtils.join(resultRow, ";"));
            hmData.put("list", list);
            hmData.put(RuleNameParameter.resp_code, "0000");
        } catch (SQLException ex) {
            System.out.println("getRedeemItemList : " + ex.getMessage());
            log.error("getRedeemItemList : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return hmData;
    }

    public HashMap getRedeemItemDetail(HashMap input) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);

        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT redeem_itemid,itemname,price,stock, start_date, end_date, otherparam, trancodeid FROM redeem_itemlist where redeem_itemid = ?");
            stat.setString(1, input.get(RuleNameParameter.redeem_itemid).toString());
            rs = stat.executeQuery();
            while (rs.next()) {
                if (Integer.valueOf(rs.getString("stock")) > 0) {
                    result.put(RuleNameParameter.redeem_itemid, rs.getString("redeem_itemid"));
                    result.put(RuleNameParameter.itemname, rs.getString("itemname"));
                    result.put(RuleNameParameter.amount, rs.getString("price"));
                    result.put(RuleNameParameter.stock, rs.getString("stock"));
                    result.put(RuleNameParameter.startdate, rs.getString("start_date"));
                    result.put(RuleNameParameter.enddate, rs.getString("end_date"));
                    result.put(RuleNameParameter.otherparam, rs.getString("otherparam"));
                    result.put(RuleNameParameter.product_code, rs.getString("trancodeid"));
                    result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
                } else {
                    result.put(RuleNameParameter.resp_code, "0055");
                    result.put(RuleNameParameter.resp_desc, "STOCK HABIS");

                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserDetail : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getScanItemDetail(HashMap input) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);

        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT scan_itemlistid,itemname,price,stock, start_date, end_date, otherparam, trancodeid FROM scan_itemlist where scan_itemlistid = ?");
            stat.setString(1, input.get(RuleNameParameter.scan_itemlistid).toString());
            rs = stat.executeQuery();
            while (rs.next()) {
                if (Integer.valueOf(rs.getString("stock")) > 0) {
                    result.put(RuleNameParameter.scan_itemlistid, rs.getString("scan_itemlistid"));
                    result.put(RuleNameParameter.itemname, rs.getString("itemname"));
                    result.put(RuleNameParameter.amount, rs.getString("price"));
                    result.put(RuleNameParameter.stock, rs.getString("stock"));
                    result.put(RuleNameParameter.startdate, rs.getString("start_date"));
                    result.put(RuleNameParameter.enddate, rs.getString("end_date"));
                    result.put(RuleNameParameter.otherparam, rs.getString("otherparam"));
                    result.put(RuleNameParameter.product_code, rs.getString("trancodeid"));
                    result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
                } else {
                    result.put(RuleNameParameter.resp_code, "0055");
                    result.put(RuleNameParameter.resp_desc, "STOCK HABIS");

                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserDetail : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public String getProductCode(String msgid) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String result = "";
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT productcode FROM tempmsg where msgid = ?");
            stat.setString(1, msgid);
            rs = stat.executeQuery();
            while (rs.next()) {
                result = rs.getString("productcode");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserDetail : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getVersion(HashMap input) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap resultVersion = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * from version");
            rs = stat.executeQuery();
            while (rs.next()) {
                resultVersion.put(FieldParameter.versionname, rs.getString("versionname"));
                resultVersion.put(FieldParameter.versioncode, rs.getString("versioncode"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserDetail : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return resultVersion;
    }

    public HashMap getUserDorman(HashMap input) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        HashMap result = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeLoginFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * from checkid(?)");
            stat.setString(1, input.get(FieldParameter.hpNumber).toString());
            rs2 = stat.executeQuery();
            while (rs2.next()) {
                if (rs2.getString("vo_status").equals("00")) {
                    stat = conn.prepareStatement("SELECT agent_id, password, agent_name, agent_pin, address, phonenumber, userlevel, no_ktp, numfaillogin, numfailpin, status, verified FROM am_user where " + rs2.getString("vo_statusdesc") + " = ?");
                    stat.setString(1, input.get(FieldParameter.hpNumber).toString());
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        result.put(FieldParameter.userlogin, rs.getString("agent_id"));
                        result.put(FieldParameter.userpassword, rs.getString("password"));
                        result.put(FieldParameter.username, rs.getString("agent_name"));
                        result.put(FieldParameter.hpNumber, rs.getString("phonenumber"));
                        conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
                        stat = conn.prepareStatement("SELECT * FROM am_balance where agent_id=? ");
                        stat.setString(1, rs.getString("agent_id"));
                        rs = stat.executeQuery();
                        while (rs.next()) {
                            result.put(FieldParameter.balance, rs.getString("curr_bal"));
                        }
                        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserDetail : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getUserDetail(HashMap input) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        HashMap result = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeLoginFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * from checkid(?)");
            stat.setString(1, input.get(FieldParameter.userlogin).toString());
            rs2 = stat.executeQuery();
            while (rs2.next()) {
                if (rs2.getString("vo_status").equals("00")) {
                    stat = conn.prepareStatement("SELECT agent_id, password, agent_name, agent_pin, address, phonenumber, userlevel, no_ktp, numfaillogin, numfailpin, status, verified FROM am_user where " + rs2.getString("vo_statusdesc") + " = ?");
                    stat.setString(1, input.get(FieldParameter.userlogin).toString());
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        result.put(FieldParameter.userlogin, rs.getString("agent_id"));
                        result.put(FieldParameter.userpassword, rs.getString("password"));
                        result.put(FieldParameter.username, rs.getString("agent_name"));
                        result.put(FieldParameter.hpNumber, rs.getString("phonenumber"));
                        result.put(FieldParameter.pin, rs.getString("agent_pin"));
                        result.put(FieldParameter.address, rs.getString("address"));
                        result.put(FieldParameter.dc, String.valueOf(rs.getInt("userlevel")));
                        result.put(FieldParameter.ktp, rs.getString("no_ktp"));
                        result.put(FieldParameter.status, rs.getString("status"));
                        result.put(FieldParameter.verified, rs.getString("verified"));
                        result.put(FieldParameter.numfaillogin, rs.getString("numfaillogin"));
                        result.put(FieldParameter.numfailpin, rs.getString("numfailpin"));
                        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserDetail : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getUserDetail2(String agent_id) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        HashMap result = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeLoginFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * from checkid(?)");
            stat.setString(1, agent_id);
            rs2 = stat.executeQuery();
            while (rs2.next()) {
                if (rs2.getString("vo_status").equals("00")) {
                    stat = conn.prepareStatement("SELECT agent_id, password, agent_name, agent_pin, address, phonenumber, userlevel, no_ktp, numfaillogin, numfailpin, status, verified FROM am_user where " + rs2.getString("vo_statusdesc") + " = ?");
                    stat.setString(1, agent_id);
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        result.put(FieldParameter.userlogin, rs.getString("agent_id"));
                        result.put(FieldParameter.userpassword, rs.getString("password"));
                        result.put(FieldParameter.username, rs.getString("agent_name"));
                        result.put(FieldParameter.hpNumber, rs.getString("phonenumber"));
                        result.put(FieldParameter.pin, rs.getString("agent_pin"));
                        result.put(FieldParameter.address, rs.getString("address"));
                        result.put(FieldParameter.dc, String.valueOf(rs.getInt("userlevel")));
                        result.put(FieldParameter.ktp, rs.getString("no_ktp"));
                        result.put(FieldParameter.status, rs.getString("status"));
                        result.put(FieldParameter.verified, rs.getString("verified"));
                        result.put(FieldParameter.numfaillogin, rs.getString("numfaillogin"));
                        result.put(FieldParameter.numfailpin, rs.getString("numfailpin"));
                        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserDetail : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public Boolean checkKYCdetail(HashMap input) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        HashMap result = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeLoginFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * from checkid(?)");
            stat.setString(1, input.get(FieldParameter.userlogin).toString());
            rs2 = stat.executeQuery();
            while (rs2.next()) {
                if (rs2.getString("vo_status").equals("00")) {
                    stat = conn.prepareStatement("SELECT img_ktp, img_self, img_profile FROM am_user where " + rs2.getString("vo_statusdesc") + " = ?");
                    stat.setString(1, input.get(FieldParameter.userlogin).toString());
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        if ((rs.getString("img_ktp")) == null) {
                            log.info("kyc : img ktp not found");
                            return false;
                        } else if ((rs.getString("img_self")) == null) {
                            log.info("kyc : img self not found");
                            return false;
                        } else if ((rs.getString("img_profile")) == null) {
                            log.info("kyc : img profile not found");
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserDetail : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return true;
    }

    public String getUserPhone(String userlogin) {
        String Phone = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeLoginFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT agent_id, phonenumber, status FROM am_user where agent_id = ?");
            stat.setString(1, userlogin);
            rs = stat.executeQuery();
            while (rs.next()) {
                return rs.getString("phonenumber");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserPhone : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return Phone;
    }

    public HashMap getKodeDetail(HashMap input) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeLoginFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * FROM am_authcode where phonenumber = ?");
            stat.setString(1, input.get(FieldParameter.hpNumber).toString());
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.hpNumber, rs.getString("phonenumber"));
                result.put(FieldParameter.withdrawalCode, rs.getString("authcode"));
                result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserDetail : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }
//
//    public HashMap getVersion(HashMap input) {
//        Connection conn = null;
//        PreparedStatement stat = null;
//        ResultSet rs = null;
//        HashMap result = new HashMap();
//        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeLoginFailed);
//        try {
//            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
//            stat = conn.prepareStatement("SELECT versionid, version, description  FROM version_tbl WHERE versionid = ?");
//            stat.setInt(1, Integer.valueOf(input.get(FieldParameter.versionid).toString()));
////            stat.setString(2, input.get(FieldParameter.desc).toString());
//
//            rs = stat.executeQuery();
//            while (rs.next()) {
//                result.put(FieldParameter.versionid, rs.getString("versionid"));
//                result.put(FieldParameter.version, rs.getString("version"));
//                result.put(FieldParameter.description, rs.getString("description"));
////                 resultColumn.add(rs.getString("bankid"));
////                resultColumn.add(rs.getString("bankname"));
////                result.put(FieldParameter.userpassword, rs.getString("userpassword"));
////                result.put(FieldParameter.userlevel, rs.getString("userlevel"));
////                result.put(FieldParameter.usercreate, rs.getString("usercreate"));
////                result.put(FieldParameter.status, rs.getString("status"));
////                result.put(FieldParameter.branchid, rs.getString("branchid"));
////                result.put(FieldParameter.officeid, rs.getString("officeid"));
////                result.put(FieldParameter.cardNumber, rs.getString("cardNumber"));
////                result.put(FieldParameter.hpNumber, rs.getString("phonenumber"));
////                result.put(FieldParameter.numfaillogin, rs.getString("numfaillogin"));
////                result.put(FieldParameter.contractNumber, rs.getString("contractnumber"));
//                result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            log.error("getVersion : " + ex.getMessage());
//        } finally {
//            clearAllConnStatRS(conn, stat, rs);
//        }
//        return result;
//    }

    public HashMap getListAccount(String userid, boolean onus) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        List resultRow = new ArrayList();
        List resultColumn = new ArrayList();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT a.acctno, a.acctname, a.bankid, b.bankname FROM user_account_tbl a right join bank_tbl b on a.bankid = b.bankid where userid = ? and onus = ?");
            stat.setInt(1, Integer.parseInt(userid));
            stat.setBoolean(2, onus);
            rs = stat.executeQuery();
            while (rs.next()) {
                resultColumn.add(rs.getString("bankid"));
                resultColumn.add(rs.getString("bankname"));
                resultColumn.add(rs.getString("acctno"));
                resultColumn.add(rs.getString("acctname"));
                resultRow.add(StringUtils.join(resultColumn, "|"));
                resultColumn.clear();
            }
            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            result.put("list", StringUtils.join(resultRow, ";"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getListAccount : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getListBankCode() {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        List resultRow = new ArrayList();
        List resultColumn = new ArrayList();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT bankid, bankname FROM bank_tbl");
            rs = stat.executeQuery();
            while (rs.next()) {
                resultColumn.add(rs.getString("bankid"));
                resultColumn.add(rs.getString("bankname"));
                resultRow.add(StringUtils.join(resultColumn, "|"));
                resultColumn.clear();
            }
            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            result.put("list", StringUtils.join(resultRow, ";"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getListBankCode : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getListMerchantPayment() {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        List resultRow = new ArrayList();
        List resultColumn = new ArrayList();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT trancode, paymentname FROM payment_tbl");
            rs = stat.executeQuery();
            while (rs.next()) {
                resultColumn.add(rs.getString("trancode"));
                resultColumn.add(rs.getString("paymentname"));
                resultRow.add(StringUtils.join(resultColumn, "|"));
                resultColumn.clear();
            }
            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            result.put("list", StringUtils.join(resultRow, ";"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getListMerchantBiller : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getSaldo(String userlogin) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
//        List resultRow = new ArrayList();
//        List resultColumn = new ArrayList();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);
        String curr_bal = "";
        String poin_bal = "";
        String max_bal_day = "";
        String max_bal_month = "";
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * FROM am_balance where agent_id=? ");
            stat.setString(1, userlogin);
            rs = stat.executeQuery();
            while (rs.next()) {
                curr_bal = rs.getString("curr_bal");
                poin_bal = rs.getString("poin_bal");
                max_bal_day = rs.getString("max_bal_day");
                max_bal_month = rs.getString("max_bal_month");
            }
            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            result.put(FieldParameter.balance, curr_bal);
            result.put(FieldParameter.balance_poin, poin_bal);
            result.put(FieldParameter.balance_day, max_bal_day);
            result.put(FieldParameter.balance_month, max_bal_month);
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getListMerchantPrepaid : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getToAccount(String toAccount) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        HashMap result = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeLoginFailed);
        try {

            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * from checkid(?)");
            stat.setString(1, toAccount);
            rs2 = stat.executeQuery();
            while (rs2.next()) {
                if (rs2.getString("vo_status").equals("00")) {

//                    log.info("lets try1 : " + rs2.getString("vo_statusdesc"));
//                    log.info("lets try2 : " + toAccount);
                    stat = conn.prepareStatement("SELECT agent_id, agent_name, verified, phonenumber, status FROM am_user where " + rs2.getString("vo_statusdesc") + " = ?");
                    stat.setString(1, toAccount);
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        log.info("lets try3 : " + rs.getString("verified"));
                        if (rs.getString("verified").equals("1")) {
                            result.put(FieldParameter.userlogin, rs.getString("agent_id"));
                            result.put(FieldParameter.name, rs.getString("agent_name"));
                            result.put(FieldParameter.hpNumber, rs.getString("phonenumber"));
                            result.put(FieldParameter.verified, rs.getString("verified"));
                            result.put(FieldParameter.status, rs.getString("status"));
                            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
                        } else {
                            result.put(FieldParameter.name, rs.getString("agent_name"));
                            result.put(FieldParameter.status, rs.getString("status"));
                            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);
                            result.put(RuleNameParameter.resp_desc, "Account tujuan belum terverifikasi");
                        }

                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getUserDetail : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getListTransaction(String userlogin, String dateStart, String dateEnd) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        List resultRow = new ArrayList();
        List resultColumn = new ArrayList();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);
        try {
            String date = null;
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT to_char(requesttime, 'YYYYMMDD') as requesttime, custno, noref, responsecode, fromaccount FROM tempmsg where fromaccount=? and (to_date(?,'YYYYMMDD')<= date(requesttime) and date(requesttime) <= to_date(?,'YYYYMMDD')) and proccode='700002' or proccode='290000' order by requesttime desc");

            stat.setString(1, userlogin);
            stat.setString(2, dateStart);
            stat.setString(3, dateEnd);
            rs = stat.executeQuery();
            while (rs.next()) {
                date = rs.getString("requesttime");
                resultColumn.add(rs.getString("noref"));
                resultColumn.add(rs.getString("custno"));
                resultColumn.add(rs.getString("responsecode"));
                resultColumn.add(rs.getString("fromaccount"));
                resultRow.add(StringUtils.join(resultColumn, "|"));
                resultColumn.clear();
            }
            System.out.println(date);
            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            result.put("list", StringUtils.join(resultRow, ";"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getListMerchantPrepaid : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap CorpActivation(HashMap input) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO am_corp(phonenumber, cu_id, no_anggota) VALUES (?, ?, ?)");
            stat.setString(1, input.get("userlogin").toString());
            stat.setString(3, input.get("username").toString());
            stat.setString(2, input.get("cuid").toString());
            stat.executeUpdate();
            result.put("resp_code", "0000");
            result.put(RuleNameParameter.resp_desc, "Success");

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put("resp_code", "0001");
            result.put(RuleNameParameter.resp_desc, "gagal");

        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }

        return result;

//        return new Func1().generateJson(result);
    }

    public HashMap getListMyCorp(String phonenumber) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        List resultRow = new ArrayList();
        List resultColumn = new ArrayList();
        HashMap resultHm = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * from ("
                    + "SELECT a.phonenumber, a.cu_id, a.no_anggota, b.nama_koperasi "
                    + "from am_corp a , profile_corp b "
                    + "where a.cu_id=b.cu_id and a.status='1' and b.status='1' "
                    + "and a.phonenumber=? "
                    + ") AS listmycorp ");
            stat.setString(1, phonenumber);
//            stat.setString(2, userlogin);
            rs = stat.executeQuery();
            while (rs.next()) {
                resultColumn.add(rs.getString("cu_id"));
                resultColumn.add(rs.getString("no_anggota"));
                resultColumn.add(rs.getString("nama_koperasi"));
//                resultColumn.add(rs.getString("requesttime"));
//                resultColumn.add(rs.getString("noref"));
//                resultColumn.add(rs.getString("custno"));
//                resultColumn.add(rs.getString("transactioncode"));
                resultRow.add(StringUtils.join(resultColumn, "|"));
                resultColumn.clear();
            }
            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            result.put("list", StringUtils.join(resultRow, ";"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getListMyCorp : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getNListTransaction(String userlogin) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        List resultRow = new ArrayList();
        List resultColumn = new ArrayList();
        HashMap resultHm = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * from (SELECT a.requesttime, a.noref, a.custno, a.responsecode, a.transactioncode, b.trancodename, CAST (a.amount as int) + feejual as amount "
                    + "FROM tempmsg a inner join trancode b on a.fromagent= ? and a.proccode in ('280000', '290000', '180000', '100000', '260000', '400000', '400001', '400002', '400003') "
                    + "and a.productcode = b.trancodeid "
                    + "union all "
                    + "SELECT a.requesttime, a.noref, a.custno, a.responsecode, a.transactioncode, b.trancodename, CAST (a.amount as int) + feejual as amount "
                    + "FROM tempmsg_backup a inner join trancode b on a.fromagent= ? and a.proccode in ('280000', '290000', '180000', '100000', '260000', '400000', '400001', '400002', '400003') "
                    + "and a.productcode = b.trancodeid) "
                    + "AS tempmsg order by requesttime desc");
            stat.setString(1, userlogin);
            stat.setString(2, userlogin);
            rs = stat.executeQuery();
            while (rs.next()) {
                resultColumn.add(rs.getString("trancodename"));
                resultColumn.add(rs.getString("amount"));
                resultColumn.add(rs.getString("responsecode"));
                resultColumn.add(rs.getString("requesttime"));
                resultColumn.add(rs.getString("noref"));
                resultColumn.add(rs.getString("custno"));
                resultColumn.add(rs.getString("transactioncode"));
                resultRow.add(StringUtils.join(resultColumn, "|"));
                resultColumn.clear();
            }
            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            result.put("list", StringUtils.join(resultRow, ";"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getListTRX : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getDetailTransaction(String userlogin, String noref) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * from (SELECT a.requesttime, a.responsetime, a.respbiller, a.noref, a.custno, a.responsecode, a.transactioncode, b.trancodename, CAST (a.amount as int) + feejual as amount "
                    + "FROM tempmsg a inner join trancode b on a.fromagent= ? and a.proccode in ('280000', '290000', '180000', '100000') "
                    + "and a.productcode = b.trancodeid "
                    + "union all "
                    + "SELECT a.requesttime, a.responsetime, a.respbiller, a.noref, a.custno, a.responsecode, a.transactioncode, b.trancodename, CAST (a.amount as int) + feejual as amount "
                    + "FROM tempmsg_backup a inner join trancode b on a.fromagent= ? and a.proccode in ('280000', '290000', '180000', '100000') "
                    + "and a.productcode = b.trancodeid) "
                    + "AS tempmsg where tempmsg.noref=?");
            stat.setString(1, userlogin);
            stat.setString(2, userlogin);
            stat.setString(3, noref);
            rs = stat.executeQuery();
            while (rs.next()) {
                HashMap detail = JsonProcess.decodeJson(rs.getString("respbiller"));
                detail.remove("agent_pass");
                detail.remove("agent_id");
                detail.put(FieldParameter.amount, rs.getString("amount"));
                result.put(FieldParameter.detail, detail);
            }

            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);

//            result.put("list", StringUtils.join(resultRow, ";"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getListMerchantPrepaid : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getNListPoin(String userlogin, int nTrx) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        ResultSet rsPoin = null;
        HashMap result = new HashMap();
        List resultRow = new ArrayList();
        List resultColumn = new ArrayList();
        HashMap resultHm = new HashMap();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT  noref, description, poin from am_update_poin where agent_id=? order by trx_date desc fetch first 10 rows only");
            stat.setString(1, userlogin);
            rsPoin = stat.executeQuery();
            while (rsPoin.next()) {
//                System.out.println("Test : " + rsPoin.getString("noref") + " : " + userlogin);
                stat = conn.prepareStatement("SELECT * from (SELECT a.requesttime, a.noref, a.custno, a.responsecode, a.transactioncode, b.trancodename, CAST (a.amount as int) + feejual as amount "
                        + "FROM tempmsg a inner join trancode b on a.fromagent= ? and a.noref = ? "
                        + "and a.productcode = b.trancodeid "
                        + "union all "
                        + "SELECT a.requesttime, a.noref, a.custno, a.responsecode, a.transactioncode, b.trancodename, CAST (a.amount as int) + feejual as amount "
                        + "FROM tempmsg_backup a inner join trancode b on a.fromagent= ? and a.noref = ? "
                        + "and a.productcode = b.trancodeid) "
                        + "AS tempmsg");
                stat.setString(1, userlogin);
                stat.setString(2, rsPoin.getString("noref"));
                stat.setString(3, userlogin);
                stat.setString(4, rsPoin.getString("noref"));
                rs = stat.executeQuery();
                while (rs.next()) {

                    resultColumn.add(rs.getString("trancodename"));
                    resultColumn.add(rsPoin.getString("poin"));
                    resultColumn.add(rsPoin.getString("description"));
                    resultColumn.add(rs.getString("requesttime"));
                    resultColumn.add(rsPoin.getString("noref"));
                    if (rsPoin.getString("description").equals("REDEEM POIN")) {
                        resultColumn.add(rs.getString("custno"));
                        resultColumn.add(rs.getString("transactioncode"));
                    } else {
                        resultColumn.add("-");
                        resultColumn.add("-");
                    }
                    resultRow.add(StringUtils.join(resultColumn, "|"));
                    resultColumn.clear();
                }

            }

            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            result.put("list", StringUtils.join(resultRow, ";"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getListMerchantPrepaid : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getListMerchantPrepaid() {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        List resultRow = new ArrayList();
        List resultColumn = new ArrayList();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT trancode, prepaidname FROM prepaid_tbl");
            rs = stat.executeQuery();
            while (rs.next()) {
                resultColumn.add(rs.getString("trancode"));
                resultColumn.add(rs.getString("prepaidname"));
                resultRow.add(StringUtils.join(resultColumn, "|"));
                resultColumn.clear();
            }
            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            result.put("list", StringUtils.join(resultRow, ";"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getListMerchantPrepaid : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getListVoucher(String prepaidid) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        List resultRow = new ArrayList();
        List resultColumn = new ArrayList();
        result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeProcessFailed);
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT voucherid, vouchername, nominal, denomination FROM voucher_tbl where prepaidid=?");
            stat.setString(1, prepaidid);
            rs = stat.executeQuery();
            while (rs.next()) {
                resultColumn.add(rs.getString("voucherid"));
                resultColumn.add(rs.getString("vouchername"));
                resultColumn.add(rs.getString("nominal"));
                resultColumn.add(rs.getString("denomination"));
                resultRow.add(StringUtils.join(resultColumn, "|"));
                resultColumn.clear();
            }
            result.put(RuleNameParameter.resp_code, RuleNameParameter.respcodeSuccess);
            result.put("list", StringUtils.join(resultRow, ";"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getListVoucher : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public String getStorePin(String userid) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT userpin FROM user_pin_tbl where userid = ?");
            stat.setInt(1, Integer.parseInt(userid));
            rs = stat.executeQuery();
            while (rs.next()) {
                return rs.getString("userpin");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getStorePin : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return "";
    }

    public HashMap createSuperuser(String userid, String userpassword, String userlevel, String username, String usercreate) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO am_user(userid, userpassword, userlevel, username, usercreate) VALUES (?, ?, ?, ?, ?)");
            stat.setString(1, userid);
            stat.setString(2, userpassword);
            stat.setString(3, userlevel);
            stat.setString(4, username);
            stat.setString(5, usercreate);
            stat.executeUpdate();
            result.put("resp_code", "0000");
            result.put(RuleNameParameter.resp_desc, "Success");

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put("resp_code", "0001");
            result.put(RuleNameParameter.resp_desc, "gagal");

        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }

        return result;

//        return new Func1().generateJson(result);
    }

    public String addTransaction(String norek, int nominal, String keterangan, String status, String noref) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO trxhist(norek, nominal, keterangan, status, noref) VALUES (?, ?, ?, ?, ?)");
            stat.setString(1, norek);
            stat.setInt(2, nominal);
            stat.setString(3, keterangan);
            stat.setString(4, status);
            stat.setString(5, noref);
            stat.executeUpdate();
            result.put("resp_code", "0000");

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put("resp_code", "0001");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return new Func1().generateJson(result);
    }

    public String addWithdrawalCode(String norek_from, String norek_to, int nominal, String keterangan, String status, String noref, String withdrawal_code, String idwithdrawal) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO withdrawal_tbl(norek_from,norek_to, nominal, keterangan, status, noref, withdrawal_code, idwithdrawal) VALUES (?, ?, ?, ?, ?,?,?,?)");
            stat.setString(1, norek_from);
            stat.setString(2, norek_to);
            stat.setInt(3, nominal);
            stat.setString(4, keterangan);
            stat.setString(5, status);
            stat.setString(6, noref);
            stat.setString(7, withdrawal_code);
            stat.setString(8, idwithdrawal);
            stat.executeUpdate();
            result.put("resp_code", "0000");

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("addTransaction : " + ex.getMessage());
            result.put("resp_code", "0001");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return new Func1().generateJson(result);
    }

    public String inquiryWithdrawalCode(String idwithdrawal, String withdrawal_code) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * From withdrawal_tbl Where withdrawal_code = ? and idwithdrawal = ? ");
            stat.setString(1, withdrawal_code);
            stat.setString(2, idwithdrawal);
            rs = stat.executeQuery();

            while (rs.next()) {
                if (rs.getString("status").equals("0")) {
                    result.put("resp_code", "0000");
                    result.put(FieldParameter.status, rs.getString("status"));
                    result.put(FieldParameter.date, rs.getString("tanggal"));
                    result.put(FieldParameter.amount, rs.getString("nominal"));
                    result.put(FieldParameter.fromaccount, rs.getString("norek_from"));
                } else {
                    result.put(FieldParameter.withdrew, rs.getString("withdrew"));
                    result.put("resp_code", "0057");

                }

                return new Func1().generateJson(result);

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getWithdrawal : " + ex.getMessage());
            result.put("resp_code", "0004");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return new Func1().generateJson(result);

    }

    public String deleteFirstuser(String userid) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("update bb_user set status='0' Where userid = ?");
            stat.setString(1, userid);
            stat.executeUpdate();
            System.out.println("sukses delete");
//            rs = stat.executeQuery();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("delete fail");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return new Func1().generateJson(result);

    }

    public String getWithdrawal(String idwithdrawal, String withdrawal_code, String withdrew) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("update withdrawal_tbl set status='1', withdrew='" + withdrew + "' Where withdrawal_code = ? and idwithdrawal = ? ");
            stat.setString(1, withdrawal_code);
            stat.setString(2, idwithdrawal);
            stat.executeUpdate();
//            rs = stat.executeQuery();

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("withdrew : " + ex.getMessage());
            result.put("resp_code", "0058");
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return new Func1().generateJson(result);

    }

    public String getMerchantPaymentName(String trancode) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("select paymentname FROM payment_tbl where trancode=?");
            stat.setString(1, trancode);
            rs = stat.executeQuery();
            while (rs.next()) {
                return rs.getString("paymentname");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getMerchantPaymentName : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return "";
    }

    public String getMerchantPrepaidName(String trancode) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("select prepaidname FROM prepaid_tbl where trancode=?");
            stat.setString(1, trancode);
            rs = stat.executeQuery();
            while (rs.next()) {
                return rs.getString("prepaidname");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("getMerchantPrepaidName : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return "";
    }

    public HashMap getWebConnectionResource(String todirect) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap webconn = new HashMap();
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT host, billercode FROM socketconn where todirect = ?");
            stat.setString(1, todirect);
            rs = stat.executeQuery();
            while (rs.next()) {
                webconn.put(rs.getString("billercode"), rs.getString("host"));
            }
        } catch (SQLException ex) {
            log.error("getWebConnectionResource : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return webconn;
    }

    public static void clearAllConnStatRS(Connection conn, PreparedStatement stat, ResultSet rs) {
        clearResultset(rs);
        clearStatment(stat);
        clearDBConnection(conn);
    }

    public static void clearStatment(PreparedStatement stat) {
//        log.info("stat 2 : " + stat);
        if (stat != null) {
            try {
//                log.info("stat A");
                stat.clearBatch();
//                log.info("stat B");
                stat.clearParameters();
//                log.info("stat C");
                stat.close();
//                log.info("stat D");
                stat = null;
//                log.info("stat E");
            } catch (SQLException ex) {
//                log.error("clearStatment : " +ex.getMessage());
//                ex.printStackTrace();
            }
        }
    }

    public static void clearDBConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException ex) {
            }
        }
    }

    public static void clearResultset(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException ex) {
            }
        }
    }

    public boolean saveMessageToTempmsgInquiry(Messagein msgin, Tempmsg tempmsg) {
        Connection conn = null;
        PreparedStatement stat = null;
        String amount;
        amount = "";
        try {
            
//            stat = conn.prepareStatement("INSERT INTO tempmsg(msgid, inpmsg, reqbiller, requesttime, noref, billercode, "
//                    + "fromaccount, toaccount, amount, proccode, transactionid, productcode, custno, fromagent, "
//                    + "hargajual, hargabeli, feejual, feebeli, ppob_profit, prev_bal, curr_bal, ref_profit, prev_biller_bal, curr_biller_bal, usr_cashback, refer) "
//                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO tempmsg(msgid, inpmsg, reqbiller, requesttime, noref, billercode, "
                    + "fromaccount, toaccount, proccode, transactionid, productcode, custno, fromagent, "
                    + "hargajual, hargabeli, feejual, feebeli, ppob_profit, ref_profit, usr_cashback, refer) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stat.setString(1, tempmsg.getMsgid());
            stat.setString(2, msgin.getInput().toString());
            stat.setString(3, tempmsg.getReqbiller());
            stat.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            stat.setString(5, tempmsg.getNoref());
            stat.setString(6, tempmsg.getBankcode());
            stat.setString(7, tempmsg.getFromaccount());
            stat.setString(8, tempmsg.getToaccount());
            stat.setString(9, tempmsg.getProccode());
            stat.setString(10, tempmsg.getTransactionid());
            stat.setString(11, tempmsg.getProductcode());
            stat.setString(12, tempmsg.getCustNo());
            stat.setString(13, tempmsg.getBankcodefrom());
            stat.setInt(14, msgin.getHargajual());
            stat.setInt(15, msgin.getHargabeli());
            stat.setInt(16, msgin.getFeejual());
            stat.setInt(17, msgin.getFeebeli());
            stat.setInt(18, msgin.getPpob_profit());
            stat.setInt(19, msgin.getRef_profit());
            stat.setInt(20, msgin.getUsr_cashback());
            stat.setString(21, msgin.getRefer());
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("saveMessageToTempmsg : " + msgin.getMsgid() + " - " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean saveMessageToTempmsgEmoneySeller(Messagein msgin, Tempmsg tempmsg) {
        Connection conn = null;
        PreparedStatement stat = null;
        String amount;
        amount = "";
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO tempmsg(msgid, inpmsg, reqbiller, statusreply, requesttime, responsetime, noref, billercode, "
                    + "responsecode, amount, proccode, transactionid, productcode, custno, fromagent, rcinternal, "
                    + "hargajual, hargabeli, feejual, feebeli, profit, prev_bal, curr_bal, fromaccount, toaccount) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stat.setString(1, msgin.getMsgid());
            stat.setString(2, msgin.getInput().toString());
            stat.setString(3, tempmsg.getReqbiller());
            stat.setBoolean(4, true);
            stat.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
            stat.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
            stat.setString(7, tempmsg.getNoref());
            stat.setString(8, msgin.getSendto());
            stat.setString(9, "0000");
            stat.setString(10, msgin.getInput().get(FieldParameter.amount).toString());
            stat.setString(11, tempmsg.getProccode());
            stat.setString(12, tempmsg.getTransactionid());
            stat.setString(13, tempmsg.getProductcode());
            stat.setString(14, tempmsg.getCustNo());
            stat.setString(15, tempmsg.getBankcodefrom());
            stat.setString(16, "0000");
            stat.setInt(17, msgin.getHargajual());
            stat.setInt(18, msgin.getHargabeli());
            stat.setInt(19, msgin.getFeejual());
            stat.setInt(20, msgin.getFeebeli());
            stat.setInt(21, msgin.getProfit());
            stat.setFloat(22, tempmsg.getPrev_bal());
            stat.setFloat(23, tempmsg.getCurr_bal());
            stat.setString(24, tempmsg.getFromaccount());
            stat.setString(25, tempmsg.getToaccount());
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("saveMessageToTempmsgSeller : " + msgin.getMsgid() + " - " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean saveMessageToTempmsgTransfer(Messagein msgin, Tempmsg tempmsg) {
        Connection conn = null;
        PreparedStatement stat = null;
        String amount;
        amount = "";
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO tempmsg(msgid, inpmsg, reqbiller, statusreply, requesttime, responsetime, noref, billercode, "
                    + "responsecode, amount, proccode, transactionid, productcode, custno, fromagent, rcinternal, "
                    + "hargajual, hargabeli, feejual, feebeli, profit, prev_bal, curr_bal, toaccount) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stat.setString(1, msgin.getMsgid());
            stat.setString(2, msgin.getInput().toString());
            stat.setString(3, tempmsg.getReqbiller());
            stat.setBoolean(4, true);
            stat.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
            stat.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
            stat.setString(7, tempmsg.getNoref());
            stat.setString(8, msgin.getSendto());
            stat.setString(9, "0000");
            stat.setString(10, msgin.getInput().get(FieldParameter.amount).toString());
            stat.setString(11, tempmsg.getProccode());
            stat.setString(12, tempmsg.getTransactionid());
            stat.setString(13, tempmsg.getProductcode());
            stat.setString(14, tempmsg.getFromaccount());
            stat.setString(15, tempmsg.getBankcodefrom());
            stat.setString(16, "0000");
            stat.setInt(17, msgin.getHargajual());
            stat.setInt(18, msgin.getHargabeli());
            stat.setInt(19, msgin.getFeejual());
            stat.setInt(20, msgin.getFeebeli());
            stat.setInt(21, msgin.getProfit());
            stat.setFloat(22, tempmsg.getPrev_bal());
            stat.setFloat(23, tempmsg.getCurr_bal());
            stat.setString(24, tempmsg.getToaccount());

            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("saveMessageToTempmsgSeller : " + msgin.getMsgid() + " - " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean saveMessageToTempmsgDeposit(Messagein msgin, Tempmsg tempmsg) {
        Connection conn = null;
        PreparedStatement stat = null;
        String amount;
        amount = "";
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO tempmsg(msgid, inpmsg, reqbiller, statusreply, requesttime, responsetime, noref, billercode, "
                    + "responsecode, amount, proccode, transactionid, productcode, custno, fromagent, rcinternal, "
                    + "hargajual, hargabeli, feejual, feebeli, profit, prev_bal, curr_bal, toaccount, fromaccount) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stat.setString(1, msgin.getMsgid());
            stat.setString(2, msgin.getInput().toString());
            stat.setString(3, tempmsg.getReqbiller());
            stat.setBoolean(4, true);
            stat.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
            stat.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
            stat.setString(7, tempmsg.getNoref());
            stat.setString(8, tempmsg.getBankcodefrom());
//            stat.setString(8, msgin.getSendto());
            stat.setString(9, "0000");
            stat.setString(10, msgin.getInput().get(FieldParameter.amount).toString());
            stat.setString(11, tempmsg.getProccode());
            stat.setString(12, tempmsg.getTransactionid());
            stat.setString(13, tempmsg.getProductcode());
            stat.setString(14, tempmsg.getCustNo());
//            stat.setString(15, tempmsg.getBankcodefrom());
            stat.setString(15, tempmsg.getToaccount());
            stat.setString(16, "0000");
            stat.setInt(17, msgin.getHargajual());
            stat.setInt(18, msgin.getHargabeli());
            stat.setInt(19, msgin.getFeejual());
            stat.setInt(20, msgin.getFeebeli());
            stat.setInt(21, msgin.getProfit());
            stat.setFloat(22, tempmsg.getPrev_bal());
            stat.setFloat(23, tempmsg.getCurr_bal());
            stat.setString(24, tempmsg.getToaccount());
            stat.setString(25, tempmsg.getFromaccount());

            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("saveMessageToTempmsgDeposit : " + msgin.getMsgid() + " - " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean saveMessageToTempmsgEmoney(Messagein msgin, Tempmsg tempmsg) {
        Connection conn = null;
        PreparedStatement stat = null;
        String amount;
        amount = "";
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO tempmsg(msgid, inpmsg, statusreply, requesttime, responsetime, noref, billercode, "
                    + "responsecode, amount, proccode, transactionid, productcode, custno, fromagent, rcinternal, "
                    + "hargajual, hargabeli, feejual, feebeli, profit, prev_bal, curr_bal) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stat.setString(1, tempmsg.getMsgid());
            stat.setString(2, msgin.getInput().toString());
            stat.setBoolean(3, false);
            stat.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            stat.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
            stat.setString(6, tempmsg.getNoref());
            stat.setString(7, msgin.getSendto());
            stat.setString(8, "0001");
            stat.setString(9, tempmsg.getAmount());
            stat.setString(10, tempmsg.getProccode());
            stat.setString(11, tempmsg.getTransactionid());
            stat.setString(12, tempmsg.getProductcode());
            stat.setString(13, tempmsg.getCustNo());
            stat.setString(14, tempmsg.getCustNo());
            stat.setString(15, "0001");
            stat.setInt(16, msgin.getHargajual());
            stat.setInt(17, msgin.getHargabeli());
            stat.setInt(18, msgin.getFeejual());
            stat.setInt(19, msgin.getFeebeli());
            stat.setInt(20, msgin.getProfit());
            stat.setFloat(21, tempmsg.getPrev_bal());
            stat.setFloat(22, tempmsg.getCurr_bal());
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("saveMessageToTempmsg : " + msgin.getMsgid() + " - " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean saveMessageToEmoney(Messagein msgin, Tempmsg tempmsg) {
        Connection conn = null;
        PreparedStatement stat = null;
        String amount;
//        log.info("parameter 1 : " + msgin.getInput().get(FieldParameter.userlogin).toString() + msgin.getInput().get(FieldParameter.rrn).toString());
//        log.info("parameter 2 : " + msgin.getInput().get(FieldParameter.userlogin).toString());
//        log.info("parameter 3 : " + msgin.getInput().get(FieldParameter.toAccount).toString());
//        log.info("parameter 4 : " + msgin.getInput().get(FieldParameter.amount).toString());
//        log.info("parameter 5 : " + new Timestamp(new java.util.Date().getTime()));
//        log.info("parameter 6 : " + new Timestamp(new java.util.Date().getTime() + 60 * 60 * 1000));
//        log.info("parameter 7 : " + msgin.getMsgid());
        amount = "";
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO emoney(trxcode, fromaccount, toaccount, amount, createtime, exptime, temp_from)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            stat.setString(1, tempmsg.getWithdrawalCode());
//            stat.setString(1, msgin.getInput().get(FieldParameter.userlogin).toString() + msgin.getInput().get(FieldParameter.rrn).toString());
            stat.setString(2, msgin.getInput().get(FieldParameter.userlogin).toString());
            stat.setString(3, msgin.getInput().get(FieldParameter.toAccount).toString());
            stat.setString(4, msgin.getInput().get(FieldParameter.amount).toString());
            stat.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
            stat.setTimestamp(6, new Timestamp(new java.util.Date().getTime() + 60 * 60 * 1000 * 2));//2 jam
            stat.setString(7, msgin.getMsgid());
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("saveMessageToEmoney : " + msgin.getMsgid() + " - " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean updateStatusReplyToEmoney(Messagein msgin, Tempmsg tempmsg) {
        Connection conn = null;
        PreparedStatement stat = null;
        String amount;
        amount = "";
        try {
            log.info("updateStatusReplyToEmoney : " + msgin.getInput().get(FieldParameter.withdrawalCode).toString());
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE emoney SET toaccount=?, responsetime=?, statusreply=?, fromagent=? WHERE trxcode=?");
//            stat = conn.prepareStatement("UPDATE emoney SET toaccount=?, responsetime=?, statusreply=?, fromagent=? WHERE trxcode=?");
            stat.setString(1, msgin.getInput().get(FieldParameter.userlogin).toString());
            stat.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
            stat.setBoolean(3, true);
            stat.setString(4, msgin.getInput().get(FieldParameter.bank_name).toString());
            stat.setString(5, msgin.getInput().get(FieldParameter.withdrawalCode).toString());

            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("updateStatusReplyToEmoney : " + msgin.getMsgid() + " - " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean saveMessageToTempmsgPayment(Messagein msgin, Tempmsg tempmsg) {
        Connection conn = null;
        PreparedStatement stat = null;
        String amount;
        amount = "";
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO tempmsg(msgid, inpmsg, reqbiller, requesttime, noref, billercode, "
                    + "fromaccount, toaccount, amount, proccode, transactionid, productcode, custno, fromagent, "
                    + "hargajual, hargabeli, feejual, feebeli, ppob_profit, prev_bal, curr_bal, ref_profit, prev_biller_bal, curr_biller_bal, usr_cashback, refer) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stat.setString(1, tempmsg.getMsgid());
            stat.setString(2, msgin.getInput().toString());
            stat.setString(3, tempmsg.getReqbiller());
            stat.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            stat.setString(5, tempmsg.getNoref());
            stat.setString(6, tempmsg.getBankcode());
            stat.setString(7, tempmsg.getFromaccount());
            stat.setString(8, tempmsg.getToaccount());
            stat.setString(9, tempmsg.getAmount());
            stat.setString(10, tempmsg.getProccode());
            stat.setString(11, tempmsg.getTransactionid());
            stat.setString(12, tempmsg.getProductcode());
            stat.setString(13, tempmsg.getCustNo());
            stat.setString(14, tempmsg.getBankcodefrom());
            stat.setInt(15, msgin.getHargajual());
            stat.setInt(16, msgin.getHargabeli());
            stat.setInt(17, msgin.getFeejual());
            stat.setInt(18, msgin.getFeebeli());
            stat.setInt(19, msgin.getPpob_profit());
            stat.setFloat(20, tempmsg.getPrev_bal());
            stat.setFloat(21, tempmsg.getCurr_bal());
            stat.setFloat(22, msgin.getRef_profit());
            stat.setFloat(23, tempmsg.getPrev_biller_bal());
            stat.setFloat(24, tempmsg.getCurr_biller_bal());
            stat.setFloat(25, msgin.getUsr_cashback());
            stat.setString(26, msgin.getRefer());
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("saveMessageToTempmsg : " + msgin.getMsgid() + " - " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean saveMessageToTempmsgPrepaidReload(Messagein msgin, Tempmsg tempmsg) {
        Connection conn = null;
        PreparedStatement stat = null;
        String amount;
        amount = "";
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO tempmsg(msgid, inpmsg, reqbiller, requesttime, noref, billercode, "
                    + "fromaccount, toaccount, amount, proccode, transactionid, productcode, custno, fromagent, "
                    + "hargajual, hargabeli, feejual, feebeli, ppob_profit, prev_bal, curr_bal, ref_profit, prev_biller_bal, curr_biller_bal, usr_cashback, refer) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stat.setString(1, tempmsg.getMsgid());
            stat.setString(2, msgin.getInput().toString());
            stat.setString(3, tempmsg.getReqbiller());
            stat.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            stat.setString(5, tempmsg.getNoref());
            stat.setString(6, tempmsg.getBankcode());
            stat.setString(7, tempmsg.getFromaccount());
            stat.setString(8, tempmsg.getToaccount());
            stat.setString(9, tempmsg.getAmount());
            stat.setString(10, tempmsg.getProccode());
            stat.setString(11, tempmsg.getTransactionid());
            stat.setString(12, tempmsg.getProductcode());
            stat.setString(13, tempmsg.getCustNo());
            stat.setString(14, tempmsg.getBankcodefrom());
            stat.setInt(15, msgin.getHargajual());
            stat.setInt(16, msgin.getHargabeli());
            stat.setInt(17, msgin.getFeejual());
            stat.setInt(18, msgin.getFeebeli());
            stat.setInt(19, msgin.getPpob_profit());//profit
            stat.setFloat(20, tempmsg.getPrev_bal());
            stat.setFloat(21, tempmsg.getCurr_bal());
            stat.setFloat(22, msgin.getRef_profit());
            stat.setFloat(23, tempmsg.getPrev_biller_bal());
            stat.setFloat(24, tempmsg.getCurr_biller_bal());
            stat.setFloat(25, msgin.getUsr_cashback());
            stat.setString(26, msgin.getRefer());
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("saveMessageToTempmsg : " + msgin.getMsgid() + " - " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean saveMessageToTempmsgRedeemPoin(Messagein msgin, Tempmsg tempmsg) {
        System.out.println("save redeempoin");
        Connection conn = null;
        PreparedStatement stat = null;
        String amount;
        amount = "";
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO tempmsg(msgid, inpmsg, reqbiller, requesttime, noref, billercode, "
                    + "fromaccount, toaccount, amount, proccode, transactionid, productcode, custno, fromagent, "
                    + "hargajual, hargabeli, feejual, feebeli, profit, prev_bal, curr_bal, sa_commission) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stat.setString(1, tempmsg.getMsgid());
            stat.setString(2, msgin.getInput().toString());
            stat.setString(3, tempmsg.getReqbiller());
            stat.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            stat.setString(5, tempmsg.getNoref());
            stat.setString(6, tempmsg.getBankcode());
            stat.setString(7, tempmsg.getFromaccount());
            stat.setString(8, tempmsg.getToaccount());
            stat.setString(9, tempmsg.getAmount());
            stat.setString(10, tempmsg.getProccode());
            stat.setString(11, tempmsg.getTransactionid());
            stat.setString(12, tempmsg.getProductcode());
            stat.setString(13, tempmsg.getCustNo());
            stat.setString(14, tempmsg.getBankcodefrom());
            stat.setInt(15, 0);
            stat.setInt(16, 0);
            stat.setInt(17, 0);
            stat.setInt(18, 0);
            stat.setInt(19, 0);
            stat.setFloat(20, tempmsg.getPrev_bal());
            stat.setFloat(21, tempmsg.getCurr_bal());
            stat.setFloat(22, 0);
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("saveMessageToTempmsg : " + msgin.getMsgid() + " - " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean updateBalanceFromTempmsg(Messagein msgin) {
        Connection conn = null;
        PreparedStatement stat = null;
        String amount = "";
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE am_balance SET curr_bal = curr_bal + cast("
                    + "(SELECT case when a.proccode = '180000' then cast(cast(a.amount as int) + a.feejual as text) end as nominal "
                    + "FROM tempmsg a where msgid = ? and statusreply = 'false') as int) WHERE agent_id = ?");
            stat.setString(1, msgin.getMsgid());
            stat.setString(2, (String) msgin.getInput().get(FieldParameter.userlogin));
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();

        } catch (SQLException ex) {
            log.error("saveMessageToTempmsg : " + msgin.getMsgid() + " - " + ex.getMessage());
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean updateBalanceFromTempmsg2(Messagein msgin) {
        Connection conn = null;
        PreparedStatement stat = null;
        String amount = "";
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE am_balance SET curr_bal = curr_bal - cast("
                    + "(SELECT case when a.proccode = '180000' then cast(cast(a.amount as int) as text) end as nominal "
                    + "FROM tempmsg a where msgid = ? and statusreply = 'false') as int) WHERE agent_id = (select toaccount from tempmsg where msgid = ?)");
            stat.setString(1, msgin.getMsgid());
            stat.setString(2, msgin.getMsgid());
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();

        } catch (SQLException ex) {
            log.error("saveMessageToTempmsg : " + msgin.getMsgid() + " - " + ex.getMessage());
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean getDetailFromEmoney(Messagein msgin) {
        Connection conn = null;
        PreparedStatement stat = null;
        String amount = "";
        ResultSet rs = null;
        msgin.getInput().put(RuleNameParameter.resp_code, "0021");
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT amount, fromaccount, temp_from FROM emoney where statusreply = false and ? < exptime and trxcode = ?");
            stat.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
            stat.setString(2, msgin.getInput().get(FieldParameter.withdrawalCode).toString());
            rs = stat.executeQuery();
            while (rs.next()) {
                msgin.getInput().put(FieldParameter.amount, rs.getString("amount"));
                msgin.getInput().put(FieldParameter.toAccount, rs.getString("fromaccount"));
                msgin.getInput().put(FieldParameter.msgId, rs.getString("temp_from"));
                msgin.getInput().put(RuleNameParameter.resp_code, "0000");
            }
        } catch (SQLException ex) {
            log.error("getAmountFromEmoney : " + msgin.getMsgid() + " - " + ex.getMessage());
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public boolean saveMessageToTempmsgInquiry(Messagein msgin) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("INSERT INTO tempmsg(msgid, inpmsg, requesttime, noref, "
                    + "fromaccount, toaccount, amount, proccode, transactionid, productcode, "
                    + "trxidbackend, custno, fromagent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stat.setString(1, msgin.getMsgid());
            stat.setString(2, msgin.getInput().toString());
            stat.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
            stat.setString(4, msgin.getInput().get(RuleNameParameter.rrn).toString());
            stat.setString(5, msgin.getInput().get(FieldParameter.userlogin).toString());
            stat.setString(6, "");
            stat.setString(7, "");
            stat.setString(8, msgin.getInput().get(FieldParameter.procCode).toString());
            stat.setString(9, "");
            stat.setString(10, msgin.getInput().get(RuleNameParameter.product_code).toString());
            stat.setString(11, msgin.getInput().get(RuleNameParameter.rrn).toString());
            stat.setString(12, msgin.getInput().get(RuleNameParameter.customer_id).toString());
            stat.setString(13, msgin.getInput().get(FieldParameter.userlogin).toString());
            stat.executeUpdate();
        } catch (SQLException ex) {
            log.error("saveMessageToTempmsg : " + msgin.getMsgid() + " - " + ex.getMessage());
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public Tempmsg updateStatusReplyTempmsgBillInquiry(String msgid, String respbiller, String responsecode, String rcinternal) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        Tempmsg tempmsg = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE tempmsg SET respbiller=?, responsecode=?, statusreply=?, responsetime=?, rcinternal=? WHERE msgid=?");
            stat.setString(1, respbiller);
            stat.setString(2, responsecode);
            stat.setBoolean(3, true);
            stat.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            stat.setString(5, rcinternal);
            stat.setString(6, msgid);
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();
        } catch (SQLException ex) {
            log.error("updateStatusReplyTempmsg : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return tempmsg;
    }

    public Tempmsg updateStatusReplyTempmsgPrepaidReload(String msgid, String respbiller, String responsecode, String rcinternal) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        Tempmsg tempmsg = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE tempmsg SET respbiller=?, responsecode=?, statusreply=?, responsetime=?, rcinternal=? WHERE msgid=?");
            stat.setString(1, respbiller);
            stat.setString(2, responsecode);
            stat.setBoolean(3, true);
            stat.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            stat.setString(5, rcinternal);
            stat.setString(6, msgid);
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();
            switch (rcinternal) {
                case "0000":
                    break;
                case "0068":
                    break;
                default:
                    stat.close();

                    stat = null;
                    stat = conn.prepareStatement("UPDATE tempmsg SET curr_bal=curr_bal+cast("
                            + " (SELECT case when a.proccode = '280000' then cast(cast(a.amount as int) + a.feejual as text) "
                            + " when a.proccode = '290000' then cast(a.hargajual as text) end as nominal "
                            + " FROM tempmsg a where msgid = ?) as int) WHERE msgid = ? ");
                    stat.setString(1, msgid);
                    stat.setString(2, msgid);
                    stat.executeUpdate();
                    stat.clearBatch();
                    stat.clearParameters();
                    break;
            }

        } catch (SQLException ex) {
            log.error("updateStatusReplyTempmsg : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return tempmsg;
    }

    public Tempmsg updateStatusReplyTempmsgBillPayment(String msgid, String respbiller, String responsecode, String rcinternal) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        Tempmsg tempmsg = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE tempmsg SET respbiller=?, responsecode=?, statusreply=?, responsetime=?, rcinternal=? WHERE msgid=?");
            stat.setString(1, respbiller);
            stat.setString(2, responsecode);
            stat.setBoolean(3, true);
            stat.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            stat.setString(5, rcinternal);
            stat.setString(6, msgid);
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();
            switch (rcinternal) {
                case "0000":
                    break;
                case "0068":
                    break;
                default:
                    stat.close();

                    stat = null;
                    stat = conn.prepareStatement("UPDATE tempmsg SET curr_bal=curr_bal+cast("
                            + " (SELECT case when a.proccode = '280000' then cast(cast(a.amount as int) + a.feejual as text) "
                            + " when a.proccode = '290000' then cast(a.hargajual as text) end as nominal "
                            + " FROM tempmsg a where msgid = ?) as int) WHERE msgid = ? ");
                    stat.setString(1, msgid);
                    stat.setString(2, msgid);
                    stat.executeUpdate();
                    stat.clearBatch();
                    stat.clearParameters();
                    break;
            }

        } catch (SQLException ex) {
            log.error("updateStatusReplyTempmsg : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return tempmsg;
    }

    public Tempmsg updateStatusReplyTempmsg4(String msgid, String responsecode, String rcinternal, Tempmsg tempmsg) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE tempmsg SET responsecode=?, statusreply=?, responsetime=?, rcinternal=?, prev_bal=?, curr_bal=? WHERE msgid=?");
            stat.setString(1, responsecode);
            stat.setBoolean(2, true);
            stat.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
            stat.setString(4, rcinternal);
            stat.setFloat(5, tempmsg.getPrev_bal());
            stat.setFloat(6, tempmsg.getCurr_bal());
            stat.setString(7, msgid);
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();
        } catch (SQLException ex) {
            log.error("updateStatusReplyTempmsg4 : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return tempmsg;
    }

    public Tempmsg updateStatusReplyTempmsg(String msgid, String respbiller, String responsecode, String rcinternal) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        Tempmsg tempmsg = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE tempmsg SET respbiller=?, responsecode=?, statusreply=?, responsetime=?, rcinternal=? WHERE msgid=?");
            stat.setString(1, respbiller);
            stat.setString(2, responsecode);
            stat.setBoolean(3, true);
            stat.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            stat.setString(5, rcinternal);
            stat.setString(6, msgid);
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();
        } catch (SQLException ex) {
            log.error("updateStatusReplyTempmsg : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return tempmsg;
    }

    public Tempmsg updateStatusReplyTempmsg2(String msgid, String respbiller, String responsecode, String rcinternal, String toaccount) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        Tempmsg tempmsg = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE tempmsg SET respbiller=?, responsecode=?, statusreply=?, responsetime=?, toaccount=?, rcinternal=? WHERE msgid=?");
            stat.setString(1, respbiller);
            stat.setString(2, responsecode);
            stat.setBoolean(3, true);
            stat.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            stat.setString(5, toaccount);
            stat.setString(6, rcinternal);
            stat.setString(7, msgid);
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();
        } catch (SQLException ex) {
            log.error("updateStatusReplyTempmsg : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return tempmsg;
    }

    public Tempmsg updateStatusReplyTempmsg3(String msgid, String transactioncode) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        Tempmsg tempmsg = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE tempmsg SET transactioncode = ? WHERE msgid=?");
            stat.setString(1, transactioncode);
            stat.setString(2, msgid);
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();
        } catch (SQLException ex) {
            log.error("updateStatusReplyTempmsg3 : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return tempmsg;
    }

    public Tempmsg updateStatusReplyTempmsg4(Messagein msgin, Tempmsg tempmsg) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE tempmsg SET statusreply = ?, responsetime = ?, toaccount = ?, responsecode = ?, rcinternal = ?, custno = ?  WHERE msgid=?");
            stat.setBoolean(1, true);
            stat.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
            stat.setString(3, msgin.getInput().get(FieldParameter.userlogin).toString());
            stat.setString(4, "0000");
            stat.setString(5, "0000");
            stat.setString(6, msgin.getInput().get(FieldParameter.userlogin).toString());
            stat.setString(7, msgin.getInput().get(FieldParameter.withdrawalCode).toString());
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();
        } catch (SQLException ex) {
            log.error("updateStatusReplyTempmsg4 : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return tempmsg;
    }

    public Messagein checkCode(Messagein msgin) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        log.info("masuk checkCode");
        msgin.getInput().put(RuleNameParameter.resp_code, "0021");
        try {
//            log.info("param 1 : " + new Timestamp(new java.util.Date().getTime()));
//            log.info("param 2 : " + msgin.getInput().get(FieldParameter.withdrawalCode).toString());
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
//            stat = conn.prepareStatement("SELECT a.fromaccount, a.toaccount, a.amount, b.agent_name FROM emoney a inner join am_user b on statusreply = false and ? < a.exptime and trxcode = ? and a.fromaccount = b.agent_id");

            stat = conn.prepareStatement("SELECT a.fromaccount, a.toaccount, a.amount, b.agent_name FROM emoney a inner join am_user b on statusreply = false and ? < a.exptime and trxcode = ? ");
            stat.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
            stat.setString(2, msgin.getInput().get(FieldParameter.withdrawalCode).toString());

            rs = stat.executeQuery();
            while (rs.next()) {
                msgin.getInput().put(FieldParameter.fromaccount, rs.getString("fromaccount"));
                msgin.getInput().put(FieldParameter.toaccount, rs.getString("toaccount"));
                msgin.getInput().put(FieldParameter.amount, rs.getString("amount"));
                msgin.getInput().put(FieldParameter.name, rs.getString("agent_name"));
                msgin.getInput().put(RuleNameParameter.resp_code, "0000");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("checkCode : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return msgin;
    }

    public boolean checkAmount(String userlogin, String amount) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        int amountdb = 0;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT curr_bal FROM am_balance where agent_id = ?");
            stat.setString(1, userlogin);
            rs = stat.executeQuery();
            while (rs.next()) {
                amountdb = rs.getInt("curr_bal");
            }
            if (amountdb >= Integer.valueOf(amount)) {
                return true;
            }
        } catch (SQLException ex) {
            log.error("checkAmount : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return false;
    }

    public boolean checkVerified(String userlogin) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        int amountdb = 0;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * from checkid(?)");
            stat.setString(1, userlogin);
            rs2 = stat.executeQuery();
            while (rs2.next()) {
                stat = conn.prepareStatement("SELECT verified FROM am_user where " + rs2.getString("vo_statusdesc") + " = ?");
                stat.setString(1, userlogin);
                rs = stat.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("verified") == 1) {
                        return true;
                    }
                }
            }

        } catch (SQLException ex) {
            log.error("checkVerifiedAccount : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return false;
    }

    public boolean checkMaxBalance(String userlogin, String amount) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        int balance = 0;
        int max_balance = 0;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT * from checkid(?)");
            stat.setString(1, userlogin);
            rs2 = stat.executeQuery();
            while (rs2.next()) {
//                log.info("vodesc : " + rs2.getString("vo_statusdesc"));
                stat = conn.prepareStatement("SELECT agent_id from am_user where " + rs2.getString("vo_statusdesc") + "= ?");
                stat.setString(1, userlogin);
                rs2 = stat.executeQuery();
                while (rs2.next()) {
                    stat = conn.prepareStatement("SELECT curr_bal, max_curr_bal FROM am_balance where agent_id = ?");
                    stat.setString(1, rs2.getString("agent_id"));
                    rs = stat.executeQuery();
                    while (rs.next()) {
                        balance = rs.getInt("curr_bal") + Integer.valueOf(amount);
                        max_balance = rs.getInt("max_curr_bal");
                    }

//                    log.info("checkMaxBalance : " + balance + " | " + max_balance);
                    if (balance < max_balance) {
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("checkMaxBalance : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return false;
    }

    public boolean yabesLogin(String userlogin, String userpass) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String passdb = "";
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("SELECT userpass FROM ppob_user where userlogin = ?");
            stat.setString(1, userlogin);
            rs = stat.executeQuery();
            while (rs.next()) {
                passdb = rs.getString("userpass");
            }
            if (userpass.equals(passdb)) {
                return true;
            }
        } catch (SQLException ex) {
            log.error("yabesLogin : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return false;
    }

    public String getSaldoPPOB(String userlogin) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("select curr_bal FROM am_balance where agent_id=?");
            stat.setString(1, userlogin);
            rs = stat.executeQuery();
            while (rs.next()) {
                return rs.getString("curr_bal");
            }
        } catch (SQLException ex) {
            log.error("getMerchantPrepaidName : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return "";
    }

    public String getInquiryBuyer(Messagein msgin) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("select a.fromaccount, a.amount, b.agent_name FROM tempmsg a inner join am_user b on a.fromaccount = b.agent_id where a.msgid = ?");
            stat.setString(1, msgin.getInput().get(FieldParameter.withdrawalCode).toString());
            rs = stat.executeQuery();
            while (rs.next()) {
                msgin.getInput().put(FieldParameter.fromaccount, rs.getString("fromaccount"));
                msgin.getInput().put(FieldParameter.amount, rs.getString("amount"));
                msgin.getInput().put(FieldParameter.name, rs.getString("agent_name"));
            }
        } catch (SQLException ex) {
            log.error("getMerchantPrepaidName : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return "";
    }

//    public String getHargaBeliPrepaid(String trancodeid) {
//        String hargabeli = null;
//        Connection conn = null;
//        PreparedStatement stat = null;
//        ResultSet rs = null;
//        try {
//            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
//            stat = conn.prepareStatement("select hargabeli from trancode_biller where trancodeid =?");
//            stat.setString(1, trancodeid);
//            rs = stat.executeQuery();
//            while (rs.next()) {
//                return hargabeli;
//            }
//        } catch (SQLException ex) {
//            log.error("getHargaBeliPrepaid : " + ex.getMessage());
//        } finally {
//            clearAllConnStatRS(conn, stat, rs);
//        }
//        return hargabeli;
//    }
    public void updateNumfaillogin(String userlogin) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE am_user SET numfaillogin=numfaillogin+1 WHERE agent_id=?");
            stat.setString(1, userlogin);
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("updatenumfaillogin : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
    }

    public void updateNumfailpin(String userlogin) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE am_user SET numfailpin=numfailpin+1 WHERE agent_id=?");
            stat.setString(1, userlogin);
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("updatenumfailpin : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
    }

    public boolean requestOTPDorman(String hpNumber) {
//        System.out.println("resetOTP : " + hpNumber);
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String otp;
        try {
            otp = StringFunction.getCurrentTimemmsss();
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE am_user SET password=? WHERE phonenumber=?");
            stat.setString(1, SHA256Enc.encryptProc(otp));
            stat.setString(2, hpNumber);
            stat.executeUpdate();
            //send sms to handphone//
            SendHttpProcess http = new SendHttpProcess();
            String urlpath = "https://reguler.zenziva.net/apps/smsapi.php";
            String text = "(" + otp + StaticParameter.pesan_dorman;
            String urlparam = StaticParameter.userkey + "=" + StaticParameter.userkey_value
                    + "&" + StaticParameter.passkey + "=" + StaticParameter.passkey_value
                    + "&" + StaticParameter.nohp + "=" + hpNumber
                    + "&" + StaticParameter.pesan + "=" + text;

//            System.out.println("api sms : " + urlpath + "?" + urlparam);
            String xmlstring = http.sendHttpRequest(urlpath, urlparam);
            if (xmlstring.equals("timeout")) {
//                System.out.println("send OTP Failed.. ! ");
                return false;
            } else if (xmlstring.equals("error")) {
//                System.out.println("send OTP Failed.. ! ");
                return false;
            } else {
                return true;

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("request OTP : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return true;

    }

    public boolean smsWithdrawalCode(String hpNumber, String Withdrawal) {
//        System.out.println("resetOTP : " + hpNumber);
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String otp;
        try {
            otp = StringFunction.getCurrentTimemmsss();
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE am_user SET password=? WHERE phonenumber=?");
            stat.setString(1, SHA256Enc.encryptProc(otp));
            stat.setString(2, hpNumber);
            stat.executeUpdate();
            //send sms to handphone//
            SendHttpProcess http = new SendHttpProcess();
            String urlpath = "https://reguler.zenziva.net/apps/smsapi.php";
            String text = "(" + Withdrawal + StaticParameter.pesan_withdrawal;
            String urlparam = StaticParameter.userkey + "=" + StaticParameter.userkey_value
                    + "&" + StaticParameter.passkey + "=" + StaticParameter.passkey_value
                    + "&" + StaticParameter.nohp + "=" + hpNumber
                    + "&" + StaticParameter.pesan + "=" + text;

//            System.out.println("api sms : " + urlpath + "?" + urlparam);
            String xmlstring = http.sendHttpRequest(urlpath, urlparam);
            if (xmlstring.equals("timeout")) {
//                System.out.println("send OTP Failed.. ! ");
                return false;
            } else if (xmlstring.equals("error")) {
//                System.out.println("send OTP Failed.. ! ");
                return false;
            } else {
                return true;

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("request OTP : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return true;

    }

    public boolean requestOTP(String hpNumber) {
//        System.out.println("resetOTP : " + hpNumber);
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String otp;
        try {
            otp = StringFunction.getCurrentTimemmsss();
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE am_user SET password=? WHERE phonenumber=?");
            stat.setString(1, SHA256Enc.encryptProc(otp));
            stat.setString(2, hpNumber);
            stat.executeUpdate();
            //send sms to handphone//
            SendHttpProcess http = new SendHttpProcess();
            String urlpath = "https://reguler.zenziva.net/apps/smsapi.php";
            String text = "(" + otp + StaticParameter.pesan_intro;
            String urlparam = StaticParameter.userkey + "=" + StaticParameter.userkey_value
                    + "&" + StaticParameter.passkey + "=" + StaticParameter.passkey_value
                    + "&" + StaticParameter.nohp + "=" + hpNumber
                    + "&" + StaticParameter.pesan + "=" + text;

//            System.out.println("api sms : " + urlpath + "?" + urlparam);
            String xmlstring = http.sendHttpRequest(urlpath, urlparam);
            if (xmlstring.equals("timeout")) {
//                System.out.println("send OTP Failed.. ! ");
                return false;
            } else if (xmlstring.equals("error")) {
//                System.out.println("send OTP Failed.. ! ");
                return false;
            } else {
                return true;

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("request OTP : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return true;

    }

    public boolean changeprofileOTP(String hpNumber) {
//        System.out.println("resetOTP : " + hpNumber);
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String otp;
        try {
            otp = StringFunction.getCurrentTimemmsss();
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE am_user SET password=? WHERE phonenumber=?");
            stat.setString(1, SHA256Enc.encryptProc(otp));
            stat.setString(2, hpNumber);
            stat.executeUpdate();
            //send sms to handphone//
            SendHttpProcess http = new SendHttpProcess();
            String urlpath = "https://reguler.zenziva.net/apps/smsapi.php";
            String text = "(" + otp + StaticParameter.pesan_changeprofile;
            String urlparam = StaticParameter.userkey + "=" + StaticParameter.userkey_value
                    + "&" + StaticParameter.passkey + "=" + StaticParameter.passkey_value
                    + "&" + StaticParameter.nohp + "=" + hpNumber
                    + "&" + StaticParameter.pesan + "=" + text;

//            System.out.println("api sms : " + urlpath + "?" + urlparam);
            String xmlstring = http.sendHttpRequest(urlpath, urlparam);
            if (xmlstring.equals("timeout")) {
//                System.out.println("send OTP Failed.. ! ");
                return false;
            } else if (xmlstring.equals("error")) {
//                System.out.println("send OTP Failed.. ! ");
                return false;
            } else {
                return true;

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("request OTP : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return true;

    }

    public boolean registrationOTP(String hpNumber) {
        log.info("request regis OTP : " + hpNumber);
//        System.out.println("resetOTP : " + hpNumber);
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String otp;
        try {
            otp = StringFunction.getCurrentTimemmsss();
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE am_user SET password=? WHERE phonenumber=?");
            stat.setString(1, SHA256Enc.encryptProc(otp));
            stat.setString(2, hpNumber);
            stat.executeUpdate();
            //send sms to handphone//
            SendHttpProcess http = new SendHttpProcess();
            String urlpath = "https://reguler.zenziva.net/apps/smsapi.php";
            String text = "(" + otp + StaticParameter.pesan_intro;
            String urlparam = StaticParameter.userkey + "=" + StaticParameter.userkey_value
                    + "&" + StaticParameter.passkey + "=" + StaticParameter.passkey_value
                    + "&" + StaticParameter.nohp + "=" + hpNumber
                    + "&" + StaticParameter.pesan + "=" + text;

//            System.out.println("api sms : " + urlpath + "?" + urlparam);
            String xmlstring = http.sendHttpRequest(urlpath, urlparam);
            if (xmlstring.equals("timeout")) {
//                System.out.println("send OTP Failed.. ! ");
                return false;
            } else if (xmlstring.equals("error")) {
//                System.out.println("send OTP Failed.. ! ");
                return false;
            } else {
                return true;

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("request OTP : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return true;

    }

    public void resetNumfaillogin(String userlogin) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
//            stat = conn.prepareStatement("UPDATE am_user SET numfaillogin=0, status=1 WHERE agent_id=?");
            stat = conn.prepareStatement("UPDATE am_user SET numfaillogin=0 WHERE agent_id=?");
            stat.setString(1, userlogin);
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("updatenumfaillogin : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
    }

    public void resetNumfailpin(String userlogin) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE am_user SET numfailpin=0 WHERE agent_id=?");
            stat.setString(1, userlogin);
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("updatenumfaillogin : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
    }

    public Tempmsg updateBalancePPOB(String userlogin, int amount) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        Tempmsg tempmsg = null;
        try {
            conn = DatasourceEntry.getInstance().getMbdatasource().getConnection();
            stat = conn.prepareStatement("UPDATE am_balance SET curr_bal=? WHERE agent_id=?");
            stat.setFloat(1, amount);
            stat.setString(2, userlogin);
            stat.executeUpdate();
            stat.clearBatch();
            stat.clearParameters();
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("updateStatusReplyTempmsg : " + ex.getMessage());
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return tempmsg;
    }
}
