/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.database;

import com.fesfam.function.FunctionSupportMB;
import com.fesfam.function.StringFunction;
import com.bopro.model.Verifikasi;
import com.bopro.model.Agentlimit;
import com.bopro.model.AgentList;
import com.bopro.model.Billerproduct;
import com.bopro.model.Connectionbiller;
import com.bopro.model.Crmagent;
import com.bopro.model.Crmbiller;
import com.bopro.model.LogList;
import com.bopro.model.Mappingagent;
import com.bopro.model.Mappingbiller;
import com.bopro.model.Merchant;
import com.bopro.model.Reportyabes;
import com.bopro.model.Settlement;
import com.bopro.model.Topupagent;
import com.bopro.model.Topupbiller;
import com.bopro.model.Transactiontrancode;
import com.bopro.model.Userlogin;
import com.bopro.model.Useryabes;
import com.bopro.model.agentbalance;
import com.bopro.singleton.DatasourceEntryBackend;
import com.bopro.parameter.FieldParameter;
import com.bopro.parameter.RuleNameParameter;
import com.bopro.model.Ads;
import com.bopro.model.AgentKopList;
import com.bopro.model.Corporation;
import com.bopro.model.HystoriCorp;
import com.bopro.model.Va;
import com.bopro.model.Version;
import com.bopro.model.Warung;
import com.bopro.model.am_trancode;
import com.bopro.model.pengembalian;
import com.bopro.model.timeout;
import com.bopro.model.timeoutgagal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import org.apache.log4j.Logger;
import pri.vate.klas.function.Func5;

public class BackendDBProcess {

    private static final Logger log = Logger.getLogger(BackendDBProcess.class);

    private void clearStatment(PreparedStatement stat) {
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

    private void clearDBConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException ex) {
//                log.error("clearDBConnection : "+ex.getMessage());
            }
        }
    }

    private void clearResultset(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException ex) {
//                log.error("clearResultset : "+ex.getMessage());
            }
        }
    }

    private void clearAllConnStatRS(Connection conn, PreparedStatement stat, ResultSet rs) {
        clearResultset(rs);
        clearStatment(stat);
        clearDBConnection(conn);
    }

    //proses login    
    public HashMap validate(String userlogin, String userpass) {
        HashMap result = new HashMap();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
                            System.out.println("ini password yg masuk : "+userpass);

            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * FROM ppob_user WHERE userlogin=? AND userpass=?");
            stat.setString(1, userlogin);
            stat.setString(2, userpass);
            rs = stat.executeQuery();
            if (rs.next()) {
                result.put(FieldParameter.userlogin, rs.getString("userlogin"));
                result.put(FieldParameter.password, rs.getString("userpass"));
                result.put(FieldParameter.userlevel, rs.getString("username"));
                result.put(FieldParameter.status, "0000");
            } else {
                result.put(FieldParameter.status, "0002");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap validateMerchant(String merchantid, String password) {
        HashMap result = new HashMap();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * FROM merchant WHERE merchantid=? AND password=?");
            stat.setString(1, merchantid);
            stat.setString(2, password);
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.merchantid, rs.getString("merchantid"));
                result.put(FieldParameter.password, rs.getString("password"));
                result.put("status", "0000");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public ArrayList<agentbalance> getAgentBalance() throws SQLException {
        ArrayList<agentbalance> ppobList = new ArrayList<agentbalance>();
        Connection conn = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;

        ResultSet rs1 = null;
        ResultSet rs2 = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat1 = conn.prepareStatement("select * from am_user");
            rs1 = stat1.executeQuery();
            while (rs1.next()) {
                stat2 = conn.prepareStatement("select * from am_balance where agent_id='" + rs1.getString("agent_id") + "'");
                rs2 = stat2.executeQuery();
                while (rs2.next()) {
                    agentbalance ab = new agentbalance();
                    ab.setAgent_id(rs1.getString("agent_id"));
                    ab.setAgent_name(rs1.getString("agent_name"));
                    ab.setAgent_phone(rs1.getString("phonenumber"));
                    ab.setNo_ktp(rs1.getString("no_ktp"));
                    ab.setCurr_bal(rs2.getString("curr_bal"));
                    ppobList.add(ab);

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {

            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return ppobList;
    }

    public String getUserlevel(String userlogin, String userpass) {
        Connection conn = null;
        String userlevel = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * FROM ppob_user WHERE userlogin=? and userpass=?");
            stat.setString(1, userlogin);
            stat.setString(2, userpass);
            rs = stat.executeQuery();

            if (rs.next()) {
                userlevel = rs.getString("username");
            }
        } catch (SQLException e) {
            return userlevel = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return userlevel;
    }

    public ArrayList<Userlogin> getAlluser() {
        ArrayList<Userlogin> userloginList = new ArrayList<Userlogin>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from ppob_user");
            rs = stat.executeQuery();
            while (rs.next()) {
                Userlogin userlogin = new Userlogin();
                userlogin.setUserlogin(rs.getString("userlogin"));
                userlogin.setUsername(rs.getString("username"));
                userloginList.add(userlogin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return userloginList;
    }

    public String addUser(Userlogin Userlogin) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("INSERT INTO ppob_user (userlogin,userpass,username) VALUES (?,?,?)");
            stat.setString(1, Userlogin.getUserlogin());
            stat.setString(2, Userlogin.getUserpass());
            stat.setString(3, Userlogin.getUsername());
            stat.executeUpdate();
            stat.clearParameters();

        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status = "Sukses menambahkan user ppob";
    }

    public boolean userYabes(String username, String activitas) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("INSERT INTO ppob_user_activity(username, activityname) VALUES (?, ?)");
            stat.setString(1, username);
            stat.setString(2, activitas);
            stat.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            clearStatment(stat);
            clearDBConnection(conn);
        }
        return true;
    }

    public String deleteUserlogin(String userLogin) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("DELETE FROM ppob_user WHERE userlogin=?");
            stat.setString(1, userLogin);
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status = "Sukses menghapus user ppob";
    }

    public Userlogin getUserByUserlogin(String userLogin) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        Userlogin userlogin = new Userlogin();
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * FROM ppob_user WHERE userlogin=?");
            stat.setString(1, userLogin);
            rs = stat.executeQuery();

            if (rs.next()) {
                userlogin.setUserlogin(rs.getString("userlogin"));
                userlogin.setUsername(rs.getString("username"));
                userlogin.setUserpass(rs.getString("userpass"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return userlogin;
    }

    public String updateUserlogin(Userlogin Userlogin) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE ppob_user SET userlogin=?, userpass=?, username=? WHERE userlogin=?");
            stat.setString(1, Userlogin.getUserlogin());
            stat.setString(2, Userlogin.getUserpass());
            stat.setString(3, Userlogin.getUsername());
            stat.setString(4, Userlogin.getUserlogin());
            stat.executeUpdate();
            stat.clearParameters();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status = "Sukses mengupdate user ppob";
    }

    public ArrayList<Useryabes> getAlluseryabes(String userlogin) {
        ArrayList<Useryabes> useryabesList = new ArrayList<Useryabes>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from ppob_user_activity where username like ? order by activitytime desc limit 10");
            stat.setString(1, userlogin);
            rs = stat.executeQuery();
            while (rs.next()) {
                Useryabes useryabes = new Useryabes();
                useryabes.setUsername(rs.getString("username"));
                useryabes.setActivitas(rs.getString("activityname"));
                useryabes.setActivitastime(rs.getString("activitytime"));
                useryabesList.add(useryabes);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return useryabesList;
    }

    public ArrayList<Useryabes> getAlluseryabesadministrator(String userlogin) {
        ArrayList<Useryabes> useryabesList = new ArrayList<Useryabes>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from ppob_user_activity order by activitytime desc");
            rs = stat.executeQuery();
            while (rs.next()) {
                Useryabes useryabes = new Useryabes();
                useryabes.setUsername(rs.getString("username"));
                useryabes.setActivitas(rs.getString("activityname"));
                useryabes.setActivitastime(rs.getString("activitytime"));
                useryabesList.add(useryabes);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return useryabesList;
    }

    public String addAgent(AgentList Agentyabes) {
        Connection conn = null;
        String status;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareStatement("select * from am_user where agent_id=? or phonenumber=?");
            stat.setString(1, Agentyabes.getAgent_id());
            stat.setString(2, Agentyabes.getAgent_phone());
            rs = stat.executeQuery();
            status1 = rs.next();
            if (status1 == true) {
                return status = "0002";

            } else {
                stat = conn.prepareStatement("INSERT INTO am_user(agent_id, password, agent_name, phonenumber, address, app_id, no_ktp) VALUES (?, ?, ?, ?, ?, ?, ?)");
                stat.setString(1, Agentyabes.getAgent_id());
                String pw = "P@ssw0rd";
                stat.setString(2, pw);
                stat.setString(3, Agentyabes.getAgent_name());
                stat.setString(4, Agentyabes.getAgent_phone());
                stat.setString(5, "-");
                stat.setString(6, "cm");
                stat.setString(7, "-");
                stat.executeUpdate();
                stat.close();
                stat = conn.prepareStatement("INSERT INTO am_balance(agent_id) VALUES (?)");
                stat.setString(1, Agentyabes.getAgent_id());
                stat.executeUpdate();
                stat.close();
                stat = conn.prepareStatement("INSERT INTO am_itemlimit(agent_id) VALUES (?)");
                stat.setString(1, Agentyabes.getAgent_id());
                stat.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
            return status = "0002";
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "0000";
    }

    public String addAgentKop(AgentKopList agentkop) {
        Connection conn = null;
        String status;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareStatement("select * from am_user where agent_id=? or phonenumber=?");
            stat.setString(1, agentkop.getAgent_id());
            stat.setString(2, agentkop.getAgent_phone());
            rs = stat.executeQuery();
            status1 = rs.next();
            if (status1 == true) {
                return status = "0002";
            } else {
                stat = conn.prepareStatement("INSERT INTO am_user(agent_id, password, agent_name, agent_pin, phonenumber, address, reference_id, app_id, no_ktp, verified, img_ktp, img_self, img_profile) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                stat.setString(1, agentkop.getAgent_id());
                String pw = "P@ssw0rd";
                stat.setString(2, pw);
                stat.setString(3, agentkop.getAgent_name());
                stat.setString(4, "-");
                stat.setString(5, agentkop.getAgent_phone());
                stat.setString(6, "-");
                stat.setString(7, agentkop.getCu_id());
                stat.setString(8, "LKD");
                stat.setString(9, "-");
                stat.setString(10, "1");
                stat.setString(11, agentkop.getImg_ktp());
                stat.setString(12, agentkop.getImg_ktp());
                stat.setString(13, agentkop.getImg_ktp());
                stat.executeUpdate();
                stat.close();
                stat = conn.prepareStatement("INSERT INTO am_balance(agent_id, max_bal_day, max_bal_month, max_curr_bal) VALUES (?, ?, ?, ?)");
                stat.setString(1, agentkop.getAgent_id());
                stat.setInt(2, 75000000);
                stat.setInt(3, 75000000);
                stat.setInt(4, 75000000);
                stat.executeUpdate();
                stat.close();
                stat = conn.prepareStatement("INSERT INTO am_itemlimit(agent_id, max_item_day, max_item_month) VALUES (?, ?, ?)");
                stat.setString(1, agentkop.getAgent_id());
                stat.setInt(2, 100000);
                stat.setInt(3, 3000000);
                stat.executeUpdate();
                stat.close();
                stat = conn.prepareStatement("INSERT INTO am_corp(phonenumber, cu_id, no_anggota) VALUES (?, ?, ?)");
                stat.setString(1, agentkop.getAgent_phone());
                stat.setString(2, agentkop.getCu_id());
                stat.setString(3, agentkop.getNo_ba());
                stat.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
            return status = "0002";
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "0000";
    }

    public String addMerchant(Merchant merchant) {
        Connection conn = null;
        String status;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareStatement("select * from merchant where merchantid=?");
            stat.setString(1, merchant.getMerchantid());
            rs = stat.executeQuery();
            status1 = rs.next();
            if (status1 == true) {
                return status = "0002";
            } else {
                stat = conn.prepareStatement("INSERT INTO merchant(merchantid, merchantname, kodepos, alamat, email, password) VALUES (?, ?, ?, ?, ?, ?)");
                stat.setString(1, merchant.getMerchantid());
                stat.setString(2, merchant.getMerchantname());
                stat.setString(3, merchant.getKodepos());
                stat.setString(4, merchant.getAlamat());
                stat.setString(5, merchant.getEmail());
                stat.setString(6, merchant.getPassword());
                stat.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
            return status = "0002";
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "0000";
    }

    public ArrayList<AgentList> getAllAgent() throws ParseException {
        ArrayList<AgentList> agentyabesList = new ArrayList<AgentList>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;

        SimpleDateFormat requesttime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requesttime2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * FROM am_user where status IN ('1', '2')");
            rs = stat.executeQuery();
            while (rs.next()) {

                AgentList agentyabes = new AgentList();
                if (rs.getString("agent_id") == null) {
                    agentyabes.setAgent_id("-");
                } else {
//                    agentyabes.setAgent_id(rs.getString("agent_id").replace(" ", "%20"));
                    agentyabes.setAgent_id(rs.getString("agent_id"));
                }
                if (rs.getString("tgl_register") == null) {
                    agentyabes.setTgl_register("-");
                } else {
                    Date tglregis = requesttime1.parse(rs.getString("tgl_register"));
                    agentyabes.setTgl_register(requesttime2.format(tglregis));
                }
                if (rs.getString("email") == null) {
                    agentyabes.setAgent_phone("-");
                } else {
                    agentyabes.setAgent_phone(rs.getString("email"));
                }
                if (rs.getString("agent_name") == null) {
                    agentyabes.setAgent_name("-");
                } else {
                    agentyabes.setAgent_name(rs.getString("agent_name"));
                }

                if (rs.getString("userlevel") == null) {
                    agentyabes.setUserlevel2("-");
                } else if (rs.getString("userlevel").equals("0") && !"0".equals(rs.getString("reference_id"))) {
                    agentyabes.setUserlevel2("Member");
                } else if (rs.getString("userlevel").equals("0") && "0".equals(rs.getString("reference_id"))) {
                    agentyabes.setUserlevel2("Umum");
                    agentyabes.setNama_koperasi("-");
                } else if (rs.getString("userlevel").equals("1")) {
                    agentyabes.setUserlevel2("Leader");
                }

                stat1 = conn.prepareStatement("select cu_id, nama_koperasi from profile_corp where cu_id = ?");
                stat1.setString(1, rs.getString("reference_id"));
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    if (rs.getString("userlevel") == null) {
                        agentyabes.setNama_koperasi("-");
                    } else if (rs.getString("userlevel").equals("0") && !"0".equals(rs.getString("reference_id"))) {
                        agentyabes.setNama_koperasi(rs1.getString("nama_koperasi"));
                    } else if (rs.getString("userlevel").equals("1")) {
                        agentyabes.setNama_koperasi(rs1.getString("nama_koperasi"));
                    } else {
                        agentyabes.setNama_koperasi("-");
                    }
                }
                if (rs.getString("status") == null) {
                    agentyabes.setStatus("-");
                } else if (rs.getString("status").equals("2")) {
                    agentyabes.setStatus("unconfirm OTP");
                } else if (rs.getString("status").equals("1")) {
                    agentyabes.setStatus("active");
                }

//                if (rs.getString("userlevel") == null) {
//                    agentyabes.setNama_koperasi("-");
//                } else if (rs.getString("userlevel").equals("0")) {
//                    agentyabes.setNama_koperasi("-");
//                } else if (rs.getString("userlevel").equals("1")) {
//                    stat1 = conn.prepareStatement("select cu_id, nama_koperasi from profile_corp where cu_id = ?");
//                    stat1.setString(1, rs.getString("agent_id"));
//                    rs1 = stat1.executeQuery();
//                    while (rs1.next()) {
//                        agentyabes.setNama_koperasi(rs1.getString("nama_koperasi"));
//                    }
//                }
                agentyabesList.add(agentyabes);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return agentyabesList;
    }

    public ArrayList<Merchant> getAllMerchant() {
        ArrayList<Merchant> merchantList = new ArrayList<Merchant>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * from merchant");
            rs = stat.executeQuery();
            while (rs.next()) {
                Merchant merchant = new Merchant();
                if (rs.getString("merchantid") == null) {
                    merchant.setMerchantid("-");
                } else {
//                    merchant.setMerchantid(rs.getString("merchantid").replace(" ", "%20"));
                    merchant.setMerchantid(rs.getString("merchantid"));
                    
                }
                if (rs.getString("merchantname") == null) {
                    merchant.setMerchantname("-");
                } else {
                    merchant.setMerchantname(rs.getString("merchantname"));
                }
                if (rs.getString("kodepos") == null) {
                    merchant.setKodepos("-");
                } else {
                    merchant.setKodepos(rs.getString("kodepos"));
                }
                if (rs.getString("alamat") == null) {
                    merchant.setAlamat("-");
                } else {
                    merchant.setAlamat(rs.getString("alamat"));
                }
                if (rs.getString("email") == null) {
                    merchant.setEmail("-");
                } else {
                    merchant.setEmail(rs.getString("email"));
                }
                merchantList.add(merchant);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return merchantList;
    }

    public HashMap getLimitByagentId(String agent_id) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * FROM am_balance WHERE agent_id=?");
            stat.setString(1, agent_id);
            rs = stat.executeQuery();
            if (rs.next()) {
                result.put(RuleNameParameter.agent_id, rs.getString("agent_id"));
                result.put(RuleNameParameter.max_bal_day, rs.getString("max_bal_day"));
                result.put(RuleNameParameter.max_bal_month, rs.getString("max_bal_month"));
                result.put(RuleNameParameter.max_curr_bal, rs.getString("max_curr_bal"));
            }
            stat = conn.prepareStatement("SELECT * FROM am_itemlimit WHERE agent_id=?");
            stat.setString(1, agent_id);
            rs = stat.executeQuery();
            if (rs.next()) {
                result.put(RuleNameParameter.max_item_day, (String.valueOf(rs.getInt("max_item_day"))));
                result.put(RuleNameParameter.max_item_month, String.valueOf(rs.getString("max_item_month")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public String updateLimit(Agentlimit Agentlimit) {
        Connection conn = null;
        String status;
        PreparedStatement stat = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE am_balance SET max_bal_day=?, max_bal_month=?, max_curr_bal=? WHERE agent_id=?");
            stat.setFloat(1, Float.parseFloat(Agentlimit.getMax_bal_day()));
            stat.setFloat(2, Float.parseFloat(Agentlimit.getMax_bal_month()));
            stat.setFloat(3, Float.parseFloat(Agentlimit.getMax_curr_bal()));

            stat.setString(4, Agentlimit.getAgent_id());
            stat.executeUpdate();
            stat = conn.prepareStatement("UPDATE am_itemlimit SET  max_item_day=?, max_item_month=? WHERE agent_id=?");
            stat.setInt(1, Integer.parseInt(Agentlimit.getMax_item_day()));
            stat.setInt(2, Integer.parseInt(Agentlimit.getMax_item_month()));
            stat.setString(3, Agentlimit.getAgent_id());
            stat.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return status = ex.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status = "0000";
    }

    public ArrayList<Agentlimit> getAllLimit() {
        ArrayList<Agentlimit> agentlimitList = new ArrayList<Agentlimit>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * FROM am_user where status in ('1', '2')");
            rs1 = stat.executeQuery();
            while (rs1.next()) {
                stat = conn.prepareStatement("SELECT * FROM am_balance WHERE agent_id='" + rs1.getString("agent_id") + "'");
                rs2 = stat.executeQuery();
                while (rs2.next()) {
                    stat = conn.prepareStatement("SELECT * FROM am_itemlimit WHERE agent_id='" + rs1.getString("agent_id") + "'");
                    rs3 = stat.executeQuery();
                    while (rs3.next()) {
                        Agentlimit agentlimit = new Agentlimit();
                        agentlimit.setAgent_id(rs1.getString("agent_id"));
                        agentlimit.setMax_bal_day(rs2.getString("max_bal_day"));
                        agentlimit.setMax_bal_month(rs2.getString("max_bal_month"));
                        agentlimit.setMax_item_day(rs3.getString("max_item_day"));
                        agentlimit.setMax_item_month(rs3.getString("max_item_month"));
                        agentlimit.setMax_curr_bal(rs2.getString("max_curr_bal"));
                        agentlimitList.add(agentlimit);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            clearResultset(rs3);
            clearResultset(rs1);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return agentlimitList;
    }

    public String updateConnectionBiller(Connectionbiller Connectionbiller) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("update socketconn set host=?, billercode=?, conname=?, packagename=? where billercode=?");
            stat.setString(1, Connectionbiller.getUrlhost());
            stat.setString(2, Connectionbiller.getBillercode());
            stat.setString(3, Connectionbiller.getBillername());
            stat.setString(4, Connectionbiller.getPackagename());
            stat.setString(5, Connectionbiller.getBillercode());
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "0000";
    }

    public ArrayList<Connectionbiller> getAllConnectionbillers() {
        ArrayList<Connectionbiller> connectionList = new ArrayList<Connectionbiller>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from socketconn");
            rs = stat.executeQuery();
            while (rs.next()) {
                Connectionbiller connectionbiller = new Connectionbiller();
                connectionbiller.setUrlhost(rs.getString("host"));
//                connectionbiller.setBillercode(rs.getString("billercode").replace(" ", "%20"));
                connectionbiller.setBillercode(rs.getString("billercode"));
                
                connectionbiller.setBillername(rs.getString("conname"));
                connectionbiller.setPackagename(rs.getString("packagename"));
                connectionList.add(connectionbiller);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return connectionList;
    }

    public ArrayList<Connectionbiller> getOptionBiller() {
        ArrayList<Connectionbiller> connectionList = new ArrayList<Connectionbiller>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select conname, billercode from socketconn");
            rs = stat.executeQuery();
            while (rs.next()) {
                Connectionbiller connectionbiller = new Connectionbiller();
                connectionbiller.setBillername(rs.getString("conname"));
                connectionbiller.setBillercode(rs.getString("billercode"));
                connectionList.add(connectionbiller);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return connectionList;
    }

    public ArrayList<agentbalance> getOptionAgentNew() {
        ArrayList<agentbalance> connectionList = new ArrayList<agentbalance>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select agent_id, agent_name from am_user where userlevel = 1 order by agent_name asc");
            rs = stat.executeQuery();
            while (rs.next()) {
                agentbalance connectionbiller = new agentbalance();
                connectionbiller.setAgent_id(rs.getString("agent_id"));
                connectionbiller.setAgent_name(rs.getString("agent_name"));
                connectionList.add(connectionbiller);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return connectionList;
    }

    public ArrayList<AgentList> getOptionAgent(String cu_id) {
        ArrayList<AgentList> agentlist = new ArrayList<AgentList>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select agent_id, agent_name from am_user where status in ('1', '2') and app_id = ? and reference_id = ?");
            stat.setString(1, "LKD");
            stat.setString(2, cu_id);
            rs = stat.executeQuery();
            while (rs.next()) {
                AgentList agentj = new AgentList();
                agentj.setAgent_id(rs.getString("agent_id"));
                agentj.setAgent_name(rs.getString("agent_name"));
                agentlist.add(agentj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return agentlist;
    }

    public ArrayList<Corporation> getOptioncuid() {
        ArrayList<Corporation> corpList = new ArrayList<Corporation>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select cu_id, nama_koperasi from profile_corp order by cu_id desc");
            rs = stat.executeQuery();
            while (rs.next()) {
                Corporation corp = new Corporation();
                corp.setCu_id(rs.getString("cu_id"));
                corp.setNama_koperasi(rs.getString("nama_koperasi"));
                corpList.add(corp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return corpList;
    }

    public String deleteConnectionBiller(String billercode) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("delete from socketconn where billercode=?");
            stat.setString(1, billercode);
            stat.executeUpdate();
            stat.clearParameters();
            stat.close();
            stat = null;
            stat = conn.prepareStatement("delete from balance_us where billercode=?");
            stat.setString(1, billercode);
            stat.executeUpdate();
            stat.clearParameters();
            stat.close();
            stat = null;
            stat = conn.prepareStatement("delete from biller_update_balance where billercode=?");
            stat.setString(1, billercode);
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status = "0000";
    }

    public HashMap getConnectionByBillercode(String billercode) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from socketconn where billercode=?");
            stat.setString(1, billercode);
            rs = stat.executeQuery();
            if (rs.next()) {
                result.put(FieldParameter.billercode, rs.getString("billercode"));
                result.put(FieldParameter.billername, rs.getString("conname"));
                result.put(FieldParameter.packagename, rs.getString("packagename"));
                result.put(FieldParameter.urlhost, rs.getString("host"));
                result.put(FieldParameter.resp_code, "0000");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public String addConnectionBiller(Connectionbiller Connectionbiller) {
        String status;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareStatement("SELECT * FROM socketconn WHERE billercode=?");
            stat.setString(1, Connectionbiller.getBillercode());
            rs = stat.executeQuery();
            status1 = rs.next();
            stat.close();
            if (status1 == true) {
                return status = "0001";
            } else {
                stat = conn.prepareStatement("INSERT INTO socketconn(todirect, host, seq, billercode, conname, packagename, typeconn) VALUES ('web', ?, (select seq+1 from socketconn where todirect = 'web' order by seq desc fetch first 1 rows only), ?, ?, ?, 'web')");
                stat.setString(1, Connectionbiller.getUrlhost());
                stat.setString(2, Connectionbiller.getBillercode());
                stat.setString(3, Connectionbiller.getBillername());
                stat.setString(4, Connectionbiller.getPackagename());
                stat.executeUpdate();
                stat.clearParameters();
                stat.close();

                stat = conn.prepareStatement("INSERT INTO balance_us(billercode) VALUES (?)");
                stat.setString(1, Connectionbiller.getBillercode());
                stat.executeUpdate();
            }
        } catch (SQLException e) {
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "0000";
    }

    public String addBillerProduct(Billerproduct Billerproduct) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareStatement("select * from trancode_biller where tcbiller=?");
            stat.setString(1, Billerproduct.getTcbiller());
            rs = stat.executeQuery();
            status1 = rs.next();
            stat.close();
            if (status1 == true) {
                return status = "0001";
            } else {
                stat = conn.prepareStatement("INSERT INTO trancode_biller(billercode, tcbiller, feebeli, hargabeli, trancodeid) VALUES (?, ?, ?, ?, ?)");
                stat.setString(1, Billerproduct.getBillercode());
                stat.setString(2, Billerproduct.getTcbiller());
                stat.setInt(3, Integer.valueOf(Billerproduct.getFeebeli()));
                stat.setInt(4, Integer.valueOf(Billerproduct.getHargabeli()));
                stat.setString(5, Billerproduct.getTrancodeid());
//                stat.setInt(6, Integer.valueOf(Billerproduct.getPoin()));
                stat.executeUpdate();
            }
        } catch (SQLException e) {
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "0000";
    }

    public String updateBillerproduct(Billerproduct Billerproduct) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("update trancode_biller set billercode=?, tcbiller=?, feebeli=?, hargabeli=?, trancodeid=?, poin=? where billercode=? and tcbiller=?");
            stat.setString(1, Billerproduct.getBillercode());
            stat.setString(2, Billerproduct.getTcbiller());
            stat.setInt(3, Integer.valueOf(Billerproduct.getFeebeli()));
            stat.setInt(4, Integer.valueOf(Billerproduct.getHargabeli()));
            stat.setString(5, Billerproduct.getTrancodeid());
            stat.setString(6, Billerproduct.getPoin());

            stat.setString(7, Billerproduct.getBillercode());
            stat.setString(8, Billerproduct.getTcbiller());
            stat.executeUpdate();
        } catch (SQLException e) {
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "Sukses mengupdate biller product";
    }

    public ArrayList<Billerproduct> getAllBillerproduct() {
        ArrayList<Billerproduct> billerproductList = new ArrayList<Billerproduct>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from trancode_biller");
            rs1 = stat.executeQuery();
            while (rs1.next()) {
                stat = conn.prepareStatement("select * from trancode where trancodeid='" + rs1.getString("trancodeid") + "'");
                rs2 = stat.executeQuery();
                while (rs2.next()) {
                    stat = conn.prepareStatement("select * from socketconn where billercode='" + rs1.getString("billercode") + "'");
                    rs3 = stat.executeQuery();
                    while (rs3.next()) {

                        Billerproduct billerproduct = new Billerproduct();

                        if (rs1.getString("billercode") == null) {
                            billerproduct.setBillercode("-");
                        } else {
                            billerproduct.setBillercode(rs1.getString("billercode"));
                        }
                        if (rs1.getString("tcbiller") == null) {
                            billerproduct.setTcbiller("-");
                        } else {
                            billerproduct.setTcbiller(rs1.getString("tcbiller"));
                        }
                        if (rs1.getString("feebeli") == null) {
                            billerproduct.setFeebeli("-");
                        } else {
                            billerproduct.setFeebeli(rs1.getString("feebeli") + ".00");
                        }
                        if (rs1.getString("hargabeli") == null) {
                            billerproduct.setHargabeli("-");
                        } else {
                            billerproduct.setHargabeli(rs1.getString("hargabeli") + ".00");
                        }
                        if (rs2.getString("trancodename") == null) {
                            billerproduct.setTrancodename("-");
                        } else {
                            billerproduct.setTrancodename(rs2.getString("trancodename"));
                        }
                        int payment = 280000;
                        int prepaid = 290000;
                        int topup = 400000;
                        int withdrawal = 400004;
                        int sendmoney = 180000;
                        int receivemoney = 100000;
                        if (rs2.getInt("tctype") == payment) {
                            billerproduct.setTctype("Bill Payment");
                        } else if (rs2.getInt("tctype") == prepaid) {
                            billerproduct.setTctype("Prepaid Reload");
                        } else {
                            billerproduct.setTctype("Other Transaction");
                        }

                        if (rs3.getString("conname") == null) {
                            billerproduct.setBillername("-");
                        } else {
                            billerproduct.setBillername(rs3.getString("conname"));
                        }
                        billerproductList.add(billerproduct);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clearResultset(rs3);
            clearResultset(rs1);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return billerproductList;
    }

    public ArrayList<Billerproduct> getAlltransaction(String transactionType) {
        ArrayList<Billerproduct> transactionList = new ArrayList<Billerproduct>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
        PreparedStatement stat2 = null;
        ResultSet rs2 = null;

        if (transactionType.equals("Bill Payment")) {
            try {
                conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
                stat = conn.prepareStatement("select * from trancode where tctype='280000'");
                rs = stat.executeQuery();
                while (rs.next()) {
                    Billerproduct biller = new Billerproduct();
                    biller.setTrancodeid(rs.getString("trancodeid"));
                    biller.setTrancodename(rs.getString("trancodename"));
                    transactionList.add(biller);
                }
//                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (transactionType.equals("Prepaid Reload")) {
            try {
                conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
                stat1 = conn.prepareStatement("select * from trancode where tctype='290000'");
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    Billerproduct biller = new Billerproduct();
                    biller.setTrancodeid(rs1.getString("trancodeid"));
                    biller.setTrancodename(rs1.getString("trancodename"));
                    transactionList.add(biller);
                }
//                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (transactionType.equals("Ppob")) {
            try {
                stat2 = conn.prepareStatement("select * from trancode where tctype='180000'");
                rs2 = stat2.executeQuery();
                while (rs2.next()) {
                    Billerproduct biller = new Billerproduct();
                    biller.setTrancodeid(rs.getString("trancodeid"));
                    biller.setTrancodename(rs.getString("trancodename"));
                    transactionList.add(biller);
                }
//                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (transactionType.equals("Select Transaction Type")) {
            Billerproduct biller = new Billerproduct();
            biller.setTrancodeid("Select Transaction Name");
            biller.setTrancodename("Select Transaction Name");
            transactionList.add(biller);
        }
        clearStatment(stat1);
        clearResultset(rs1);
        clearStatment(stat2);
        clearResultset(rs2);
        clearDBConnection(conn);
        return transactionList;
    }

    public String deleteBillerproduct(String tcbiller, String billercode) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("delete from trancode_biller where tcbiller=? and billercode=?");
            stat.setString(1, tcbiller);
            stat.setString(2, billercode);
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "0000";
    }

    public Billerproduct getBillerByTcbiller(String tcbiller, String billercode) {
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        Billerproduct billerproduct = new Billerproduct();
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from trancode_biller where tcbiller=? and billercode=?");
            stat.setString(1, tcbiller);
            stat.setString(2, billercode);
            rs = stat.executeQuery();
            while (rs.next()) {
                stat1 = conn.prepareStatement("select * from socketconn where billercode=?");
                stat1.setString(1, billercode);
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    stat2 = conn.prepareStatement("select * from trancode where trancodeid=?");
                    stat2.setString(1, rs.getString("trancodeid"));
                    rs2 = stat2.executeQuery();
                    while (rs2.next()) {
                        billerproduct.setTrancodename(rs2.getString("trancodename"));
                        billerproduct.setTctype(rs2.getString("tctype"));
                        if (rs2.getString("tctype").equals("280000")) {
                            billerproduct.setTctypename("Bill Payment");
                        } else if (rs2.getString("tctype").equals("290000")) {
                            billerproduct.setTctypename("Prepaid Reload");
                        }
                    }
                    billerproduct.setBillername(rs1.getString("conname"));
                }
                billerproduct.setBillercode(rs.getString("billercode"));
                billerproduct.setTcbiller(rs.getString("tcbiller"));
                billerproduct.setFeebeli(rs.getString("feebeli") + ".00");
                billerproduct.setHargabeli(rs.getString("hargabeli") + ".00");
                billerproduct.setTrancodeid(rs.getString("trancodeid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return billerproduct;
    }

    public ArrayList<Billerproduct> getAllbiller() {
        ArrayList<Billerproduct> billerList = new ArrayList<Billerproduct>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from socketconn");
            rs = stat.executeQuery();
            while (rs.next()) {
                Billerproduct biller = new Billerproduct();
                biller.setBillercode(rs.getString("billercode"));
                biller.setConname(rs.getString("conname"));
                billerList.add(biller);
            }
//            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return billerList;
    }

    public String addMappingAgent(Mappingagent Mappingagent) {
        String status;
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        ResultSet rs = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareStatement("select * from am_trancode where agent_id=? and trancodeid=?");
            stat.setString(1, Mappingagent.getAgent_id());
            stat.setString(2, Mappingagent.getTrancodeid());
            rs = stat.executeQuery();
            status1 = rs.next();
            if (status1 == true) {
                return status = "Maaf Agent id / trancode id sudah ada";
            } else {
                stat1 = conn.prepareStatement("INSERT INTO am_trancode(agent_id, trancodeid, feejual, hargajual,sa_commission) VALUES (?, ?, ?, ?, ?)");
                stat1.setString(1, Mappingagent.getAgent_id());
                stat1.setString(2, Mappingagent.getTrancodeid());
                stat1.setInt(3, Integer.valueOf(Mappingagent.getFeejual()));
                stat1.setInt(4, Integer.valueOf(Mappingagent.getHargajual()));
                stat1.setInt(5, Integer.valueOf(Mappingagent.getSa_commission()));
                stat1.executeUpdate();
            }
        } catch (SQLException e) {
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
            clearStatment(stat1);
        }
        return status = "Sukses menambahkan mapping agent";
    }

    public ArrayList<Mappingagent> getAllagent() {
        ArrayList<Mappingagent> agentList = new ArrayList<Mappingagent>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from am_user");
            rs = stat.executeQuery();
            while (rs.next()) {
                Mappingagent agent = new Mappingagent();
                agent.setAgent_id(rs.getString("agent_id"));
                if (rs.getString("agent_name") != null) {
                    agent.setAgentname(rs.getString("agent_name"));
                } else {
                    agent.setAgentname("-");
                }
                agentList.add(agent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return agentList;
    }

    public String deleteMappingagent(String trancodeid, String agent_id) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("delete from am_trancode where trancodeid=? and agent_id=?");
            stat.setString(1, trancodeid);
            stat.setString(2, agent_id);
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "Sukses menghapus mapping agent";
    }

    public Mappingagent getMappingagentByTrancodeid(String trancodeid, String agent_id) {
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        Mappingagent mappingagent = new Mappingagent();
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from am_trancode where trancodeid=? and agent_id=?");
            stat.setString(1, trancodeid);
            stat.setString(2, agent_id);
            rs = stat.executeQuery();
            while (rs.next()) {
                stat1 = conn.prepareStatement("select * from am_user where agent_id=?");
                stat1.setString(1, agent_id);
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    stat2 = conn.prepareStatement("select * from trancode where trancodeid=?");
                    stat2.setString(1, trancodeid);
                    rs2 = stat2.executeQuery();
                    while (rs2.next()) {
                        mappingagent.setTrancodename(rs2.getString("trancodename"));
                        mappingagent.setTctype(rs2.getString("tctype"));
                        if (rs2.getString("tctype").equals("280000")) {
                            mappingagent.setTctypename("Bill Payment");
                        } else if (rs2.getString("tctype").equals("290000")) {
                            mappingagent.setTctypename("Prepaid Reload");
                        } else if (rs2.getString("tctype").equals("180000")) {
                            mappingagent.setTctypename("Ppob");
                        }
                    }
                    mappingagent.setAgentname(rs1.getString("agent_name"));
                }
                mappingagent.setAgent_id(rs.getString("agent_id"));
                mappingagent.setTrancodeid(rs.getString("trancodeid"));
                mappingagent.setFeejual(rs.getString("feejual") + ".00");
                mappingagent.setHargajual(rs.getString("hargajual") + ".00");
                mappingagent.setSa_commission(rs.getString("sa_commission") + ".00");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return mappingagent;
    }

    public String updateMappingagent(Mappingagent Mappingagent) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("update am_trancode set agent_id=?, trancodeid=?, feejual=?, hargajual=?, sa_commission=? where agent_id=? and trancodeid=?");
            stat.setString(1, Mappingagent.getAgent_id());
            stat.setString(2, Mappingagent.getTrancodeid());
            stat.setInt(3, Integer.valueOf(Mappingagent.getFeejual()));
            stat.setInt(4, Integer.valueOf(Mappingagent.getHargajual()));
            stat.setInt(5, Integer.valueOf(Mappingagent.getSa_commission()));
            stat.setString(6, Mappingagent.getAgent_id());
            stat.setString(7, Mappingagent.getTrancodeid());
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "Sukses mengupdate mapping agent";
    }

    public ArrayList<Mappingagent> getAllMappingagent() {
        ArrayList<Mappingagent> mappingagentList = new ArrayList<Mappingagent>();
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from am_trancode");
            rs = stat.executeQuery();
            while (rs.next()) {
                stat1 = conn.prepareStatement("select * from trancode where trancodeid='" + rs.getString("trancodeid") + "'");
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    stat2 = conn.prepareStatement("select * from am_user where agent_id='" + rs.getString("agent_id") + "'");
                    rs2 = stat2.executeQuery();
                    while (rs2.next()) {
                        Mappingagent mappingagent = new Mappingagent();
                        mappingagent.setAgent_id(rs.getString("agent_id"));
//                        System.out.println("agent_id : " + rs.getString("agent_id"));
                        mappingagent.setFeejual(rs.getString("feejual") + ".00");
//                        System.out.println("feejual : " + rs.getString("feejual"));
                        mappingagent.setHargajual(rs.getString("hargajual") + ".00");
//                        System.out.println("hargajual : " + rs.getString("hargajual"));
                        mappingagent.setTrancodeid(rs.getString("trancodeid"));
                        mappingagent.setSa_commission(rs.getString("sa_commission") + ".00");
                        mappingagent.setTrancodename(rs1.getString("trancodename"));
//                        System.out.println("trancodename : " + rs1.getString("trancodename"));
                        int payment = 280000;
                        int prepaid = 290000;
                        int sendmoney = 180000;
                        int receivemoney = 100000;
                        if (rs1.getInt("tctype") == payment) {
                            mappingagent.setTctype("Bill Payment");
                        } else if (rs1.getInt("tctype") == prepaid) {
                            mappingagent.setTctype("Prepaid Reload");
                        } else if (rs1.getInt("tctype") == sendmoney) {
                            mappingagent.setTctype("Send Money");
                        } else if (rs1.getInt("tctype") == receivemoney) {
                            mappingagent.setTctype("Receive Money");
                        }
                        mappingagent.setAgentname(rs2.getString("agent_name"));
                        mappingagentList.add(mappingagent);
                    }
                }
            }
//            rs1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return mappingagentList;
    }

    public ArrayList<Mappingbiller> getMappingbiller() {
        ArrayList<Mappingbiller> mappingbillerList = new ArrayList<Mappingbiller>();
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from trancode");
            rs = stat.executeQuery();
            while (rs.next()) {
                Mappingbiller mappingbiller = new Mappingbiller();
                mappingbiller.setTrancodeid(rs.getString("trancodeid"));
                mappingbiller.setTrancodename(rs.getString("trancodename"));
                if (rs.getString("billercode") == null) {
                    mappingbiller.setBillercode("-");
                } else {
                    mappingbiller.setBillercode(rs.getString("billercode"));
                }
                int payment = 280000;
                int prepaid = 290000;
                int sendmoney = 180000;
                int receivemoney = 100000;
                if (rs.getInt("tctype") == payment) {
                    mappingbiller.setTctype("Bill Payment");
                } else if (rs.getInt("tctype") == prepaid) {
                    mappingbiller.setTctype("Prepaid Reload");
                } else if (rs.getInt("tctype") == sendmoney) {
                    mappingbiller.setTctype("Send Money");
                } else if (rs.getInt("tctype") == receivemoney) {
                    mappingbiller.setTctype("Receive Money");
                }
                stat1 = conn.prepareStatement("select * from socketconn where billercode='" + rs.getString("billercode") + "'");
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    if (rs.getString("billercode") == null) {
                        mappingbiller.setBillername("-");
                    } else {
                        mappingbiller.setBillername(rs1.getString("conname"));
                    }
                }
                mappingbillerList.add(mappingbiller);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat);
            clearResultset(rs);
            clearDBConnection(conn);
        }
        return mappingbillerList;
    }

    public Mappingbiller getMappingbillerByTrancodeid(String trancodeid) {
        Mappingbiller mappingbiller = new Mappingbiller();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from trancode where trancodeid=?");
            stat.setString(1, trancodeid);
            rs = stat.executeQuery();
            while (rs.next()) {
                mappingbiller.setTrancodeid(rs.getString("trancodeid"));
                mappingbiller.setTrancodename(rs.getString("trancodename"));
                if (rs.getString("tctype").equals("280000")) {
                    mappingbiller.setTctype("Bill Payment");
                } else if (rs.getString("tctype").equals("290000")) {
                    mappingbiller.setTctype("Prepaid Reload");
                } else if (rs.getString("tctype").equals("180000")) {
                    mappingbiller.setTctype("Ppob");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return mappingbiller;
    }

    public ArrayList<Mappingbiller> getAllbillerMapping() {
        ArrayList<Mappingbiller> billerList = new ArrayList<Mappingbiller>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from socketconn");
            rs = stat.executeQuery();
            while (rs.next()) {
                Mappingbiller biller = new Mappingbiller();
                biller.setBillercode(rs.getString("billercode"));
                biller.setBillername(rs.getString("conname"));
                billerList.add(biller);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return billerList;
    }

    public String updateMappingBiller(Mappingbiller mappingbiller) {
        String status;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("update trancode set  billercode=? where trancodeid=?");
            stat.setString(1, mappingbiller.getBillercode());
            stat.setString(2, mappingbiller.getTrancodeid());
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "Sukses mengupdate mapping biller";
    }

    public ArrayList<Crmbiller> getAllCrmbiller() {
        ArrayList<Crmbiller> crmbillerList = new ArrayList<Crmbiller>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from biller_crm");
            rs = stat.executeQuery();
            while (rs.next()) {
                Crmbiller crmbiller = new Crmbiller();
                if (rs.getString("codecrm") == null) {
                    crmbiller.setCodecrm("-");
                } else {
                    crmbiller.setCodecrm(rs.getString("codecrm"));
                }
                if (rs.getString("billercode") == null) {
                    crmbiller.setBillercode("-");

                } else {
                    crmbiller.setBillercode(rs.getString("billercode"));
                }
                if (rs.getString("posisi") == null) {
                    crmbiller.setPosisi("-");
                } else {
                    crmbiller.setPosisi(rs.getString("posisi"));
                }
                if (rs.getString("nama") == null) {
                    crmbiller.setNama("-");
                } else {
                    crmbiller.setNama(rs.getString("nama"));
                }
                if (rs.getString("numberphone") == null) {
                    crmbiller.setNumberphone("-");

                } else {
                    crmbiller.setNumberphone(rs.getString("numberphone"));
                }
                if (rs.getString("email") == null) {
                    crmbiller.setEmail("-");
                } else {
                    crmbiller.setEmail(rs.getString("email"));
                }
                crmbillerList.add(crmbiller);
            }
//            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return crmbillerList;
    }

    public String addCrmBiller(Crmbiller Crmbiller) {
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        ResultSet rs = null;
        String uniqcode = Crmbiller.getBillercode().toString() + Crmbiller.getPosisi().toString();
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareCall("SELECT * FROM biller_crm where billercode=? and posisi=?");
            stat.setString(1, Crmbiller.getBillercode());
            stat.setString(2, Crmbiller.getPosisi());
            rs = stat.executeQuery();
            status1 = rs.next();
            if (status1 == true) {
                return status = "Maaf nama biller dan posisi sudah ada";
            } else {
                stat1 = conn.prepareStatement("INSERT INTO biller_crm (codecrm,billercode,posisi,nama,numberphone,email) VALUES (?,?,?,?,?,?)");
                stat1.setString(1, uniqcode);
                stat1.setString(2, Crmbiller.getBillercode());
                stat1.setString(3, Crmbiller.getPosisi());
                stat1.setString(4, Crmbiller.getNama());
                stat1.setString(5, Crmbiller.getNumberphone());
                stat1.setString(6, Crmbiller.getEmail());
                stat1.executeUpdate();
            }
        } catch (SQLException e) {
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
            clearStatment(stat1);
        }
        return status = "Sukses menambahkan crm biller";
    }

    public String deleteCrmbiller(String codecrm) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("DELETE FROM biller_crm WHERE codecrm=?");
            stat.setString(1, codecrm);
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "Sukses menghapus crm biller";
    }

    public Crmbiller getCrmbillerByCodecrm(String codecrm) {
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

        Crmbiller crmbiller = new Crmbiller();
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from biller_crm WHERE codecrm=?");
            stat.setString(1, codecrm);
            rs = stat.executeQuery();

            while (rs.next()) {
                stat1 = conn.prepareStatement("select * from socketconn where billercode=?");
                stat1.setString(1, rs.getString("billercode"));
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    crmbiller.setBillername(rs1.getString("conname"));
                    crmbiller.setBillercode(rs.getString("billercode"));
                    crmbiller.setPosisi(rs.getString("posisi"));
                    crmbiller.setNama(rs.getString("nama"));
                    crmbiller.setNumberphone(rs.getString("numberphone"));
                    crmbiller.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat);
            clearResultset(rs);
            clearDBConnection(conn);
        }
        return crmbiller;
    }

    public String updateCrmbiller(Crmbiller Crmbiller) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status;
        String uniqcode = Crmbiller.getBillercode().toString() + Crmbiller.getPosisi().toString();
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE biller_crm SET codecrm=?, billercode=?, posisi=?, nama=?, numberphone=?, email=? WHERE codecrm=?");
            stat.setString(1, uniqcode);
            stat.setString(2, Crmbiller.getBillercode());
            stat.setString(3, Crmbiller.getPosisi());
            stat.setString(4, Crmbiller.getNama());
            stat.setString(5, Crmbiller.getNumberphone());
            stat.setString(6, Crmbiller.getEmail());
            stat.setString(7, uniqcode);
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "Sukses mengupdate crm biller";
    }

    public String addCrmagent(Crmagent Crmagent) {
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        ResultSet rs = null;
        String uniqcode = Crmagent.getAgent_id().toString() + Crmagent.getPosisi().toString();
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareCall("SELECT * FROM agent_crm where agent_id=? and posisi=?");
            stat.setString(1, Crmagent.getAgent_id());
            stat.setString(2, Crmagent.getPosisi());
            rs = stat.executeQuery();
            status1 = rs.next();
            if (status1 == true) {
                return status = "Maaf nama agent dan posisi sudah ada";
            } else {
                stat1 = conn.prepareStatement("INSERT INTO agent_crm (codecrm,agent_id,posisi,nama,numberphone,email) VALUES (?,?,?,?,?,?)");
                stat1.setString(1, uniqcode);
                stat1.setString(2, Crmagent.getAgent_id());
                stat1.setString(3, Crmagent.getPosisi());
                stat1.setString(4, Crmagent.getNama());
                stat1.setString(5, Crmagent.getNumberphone());
                stat1.setString(6, Crmagent.getEmail());
                stat1.executeUpdate();
            }
        } catch (SQLException e) {
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
            clearStatment(stat1);
        }
        return status = "Sukses menambahkan crm agent";
    }

    public ArrayList<Crmagent> getAllCrmagent() {
        ArrayList<Crmagent> crmagentList = new ArrayList<Crmagent>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from am_crm");
            rs = stat.executeQuery();
            while (rs.next()) {
                Crmagent crmagent = new Crmagent();
                if (rs.getString("codecrm") == null) {
                    crmagent.setCodecrm("-");
                } else {
                    crmagent.setCodecrm(rs.getString("codecrm"));
                }
                if (rs.getString("agent_id") == null) {
                    crmagent.setAgent_id("-");

                } else {
                    crmagent.setAgent_id(rs.getString("agent_id"));
                }
                if (rs.getString("posisi") == null) {
                    crmagent.setPosisi("-");
                } else {
                    crmagent.setPosisi(rs.getString("posisi"));
                }
                if (rs.getString("nama") == null) {
                    crmagent.setNama("-");
                } else {
                    crmagent.setNama(rs.getString("nama"));
                }
                if (rs.getString("numberphone") == null) {
                    crmagent.setNumberphone("-");

                } else {
                    crmagent.setNumberphone(rs.getString("numberphone"));
                }
                if (rs.getString("email") == null) {
                    crmagent.setEmail("-");
                } else {
                    crmagent.setEmail(rs.getString("email"));
                }
                crmagentList.add(crmagent);
            }
//            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);

        }
        return crmagentList;
    }

    public String deleteCrmagent(String codecrm) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("DELETE FROM agent_crm WHERE codecrm=?");
            stat.setString(1, codecrm);
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "Sukses menghapus crm agent";
    }

    public Crmagent getCrmagentByCodecrm(String codecrm) {
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Crmagent crmagent = new Crmagent();
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from agent_crm WHERE codecrm=?");
            stat.setString(1, codecrm);
            rs = stat.executeQuery();

            while (rs.next()) {
                stat1 = conn.prepareStatement("select * from am_user where agent_id=?");
                stat1.setString(1, rs.getString("agent_id"));
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    crmagent.setAgentname(rs1.getString("agent_name"));
                    crmagent.setAgent_id(rs.getString("agent_id"));
                    crmagent.setPosisi(rs.getString("posisi"));
                    crmagent.setNama(rs.getString("nama"));
                    crmagent.setNumberphone(rs.getString("numberphone"));
                    crmagent.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat);
            clearResultset(rs);
            clearDBConnection(conn);
        }
        return crmagent;
    }

    public String updateCrmbiller(Crmagent Crmagent) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status;
        String uniqcode = Crmagent.getAgent_id().toString() + Crmagent.getPosisi().toString();
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE agent_crm SET codecrm=?, agent_id=?, posisi=?, nama=?, numberphone=?, email=? WHERE codecrm=?");
            stat.setString(1, uniqcode);
            stat.setString(2, Crmagent.getAgent_id());
            stat.setString(3, Crmagent.getPosisi());
            stat.setString(4, Crmagent.getNama());
            stat.setString(5, Crmagent.getNumberphone());
            stat.setString(6, Crmagent.getEmail());
            stat.setString(7, uniqcode);
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "Sukses mengupdate crm agent";
    }

    public String addTopupBiller(Topupbiller Topupbiller, String username) {
        Connection conn = null;
        String status;
        PreparedStatement stat = null;
        ResultSet rs = null;
        Integer saldoBefore = null;
        Integer newAmount = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * FROM balance_us WHERE billercode=?");
            stat.setString(1, Topupbiller.getBillercode());
            rs = stat.executeQuery();
            while (rs.next()) {
                saldoBefore = Integer.parseInt(rs.getString("curr_bal"));
            }
            rs.close();
            stat.close();
            stat = conn.prepareStatement("UPDATE balance_us SET curr_bal=?  WHERE billercode=?");
            stat.setInt(1, Integer.parseInt(Topupbiller.getAmount()) + saldoBefore);
            stat.setString(2, Topupbiller.getBillercode());
            stat.executeUpdate();
            stat.close();
            stat = conn.prepareStatement("INSERT INTO biller_update_balance(billercode,amount,before_balance,current_balance,bank_name,acct_no,transfer_date) VALUES (?,?,?,?,?,?,?)");
            stat.setString(1, Topupbiller.getBillercode());
            stat.setInt(2, Integer.parseInt(Topupbiller.getAmount()));
            stat.setInt(3, saldoBefore);
            stat.setInt(4, Integer.parseInt(Topupbiller.getAmount()) + saldoBefore);
            stat.setString(5, Topupbiller.getBank_name());
            stat.setString(6, Topupbiller.getAcct_no());
            stat.setString(7, Topupbiller.getTransfer_date());
            stat.executeUpdate();

            log.info("");
            log.info("============== ADD TOPUP BILLER : " + Topupbiller.getBillercode() + ", amount :" + Topupbiller.getAmount() + ", Previous Balance :" + saldoBefore + ", Current Balance  :" + (Integer.parseInt(Topupbiller.getAmount()) + saldoBefore) + ", bank :" + Topupbiller.getBank_name() + ", acct number :" + Topupbiller.getAcct_no() + ", Transfer Date :" + Topupbiller.getTransfer_date() + " Create By :" + username + " =================");
            log.info("");

        } catch (SQLException ex) {
            ex.printStackTrace();
            return status = ex.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "Success Topup Biller";
    }

    public ArrayList<Topupbiller> getAllTopupbiller() {
        ArrayList<Topupbiller> topupbillerList = new ArrayList<Topupbiller>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from biller_update_balance");
            rs = stat.executeQuery();
            while (rs.next()) {
                Topupbiller topupbiller = new Topupbiller();
                topupbiller.setBillercode(rs.getString("billercode"));
                topupbiller.setAmount(rs.getString("amount"));
                topupbiller.setBefore_balance(rs.getString("before_balance"));
                topupbiller.setCurrent_balance(rs.getString("current_balance"));
                topupbiller.setBank_name(rs.getString("bank_name"));
                topupbiller.setAcct_no(rs.getString("acct_no"));
                topupbiller.setTransfer_date(rs.getString("transfer_date"));
                topupbiller.setTopup_date(rs.getString("topup_date"));
                topupbillerList.add(topupbiller);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return topupbillerList;
    }

    public String addTopupagent(Topupagent Topupagent, String username) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("INSERT INTO am_update_balance(agent_id, amount, bank_name, acct_no, transfer_date) VALUES (?, ?, ?, ?, ?)");
            stat.setString(1, Topupagent.getAgent_id());
            stat.setInt(2, Integer.parseInt(Topupagent.getAmount()));
            stat.setString(3, Topupagent.getBank_name());
            stat.setString(4, Topupagent.getAcct_no());
            stat.setString(5, Topupagent.getTransfer_date());
            stat.executeUpdate();
            log.info("");
            log.info("============== ADD TOPUP AGENT : " + Topupagent.getAgent_id() + ", amount :" + Topupagent.getAmount() + ", bank :" + Topupagent.getBank_name() + ", acct number :" + Topupagent.getAcct_no() + ", Transfer Date :" + Topupagent.getTransfer_date() + " Create By :" + username + " =================");
            log.info("");
        } catch (SQLException e) {
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);

        }
        return status = "Success , Waiting Approve";
    }

    public boolean updateTopupagent(String agentid, String topupid, String username) {
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;
        PreparedStatement stat3 = null;
        PreparedStatement stat4 = null;
        PreparedStatement stat5 = null;

        ResultSet rs3 = null;
        ResultSet rs2 = null;
        ResultSet rs1 = null;
        ResultSet rs = null;
        Integer saldoBefore = null;
        Integer newAmount = null;
        Integer currbal = null;
        try {

            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT process_exe FROM am_update_balance where topup_id=?");
            stat.setFloat(1, Float.parseFloat(topupid));
            rs = stat.executeQuery();
            while (rs.next()) {
                if (rs.getString("process_exe").equals("f")) {
                    stat1 = conn.prepareStatement("SELECT curr_bal FROM am_balance where agent_id=?");
                    stat1.setString(1, agentid);
                    rs1 = stat1.executeQuery();
                    while (rs1.next()) {
                        saldoBefore = rs1.getInt("curr_bal");
                    }
//                    rs.close();
//                    stat.close();

                    stat2 = conn.prepareStatement("SELECT amount FROM am_update_balance where topup_id=?");
                    stat2.setFloat(1, Float.parseFloat(topupid));
                    rs2 = stat2.executeQuery();
                    while (rs2.next()) {
                        newAmount = rs2.getInt("amount");
//                        currbal = rs2.getInt("current_balance");
                    }
//                    stat.close();
                    stat3 = conn.prepareStatement("UPDATE am_balance SET curr_bal=curr_bal+? WHERE agent_id=?");
                    stat3.setFloat(1, newAmount);
                    stat3.setString(2, agentid);
                    stat3.executeUpdate();
                    stat4 = conn.prepareStatement("UPDATE am_update_balance SET process_exe=true, update_date=current_timestamp, before_balance=?, current_balance=amount+? WHERE topup_id=?");
                    stat4.setFloat(1, saldoBefore);
                    stat4.setFloat(2, saldoBefore);
                    stat4.setInt(3, Integer.parseInt(topupid));
                    stat4.executeUpdate();

                    stat5 = conn.prepareStatement("SELECT curr_bal FROM am_balance where agent_id=?");
                    stat5.setString(1, agentid);
                    rs3 = stat5.executeQuery();
                    while (rs3.next()) {
                        currbal = rs3.getInt("curr_bal");
                    }
                    log.info("");
                    log.info("============== APPROVE TOPUP AGENT : " + agentid + ", last balance :" + saldoBefore + ", amount :" + newAmount + ", current balance :" + currbal + ", Approve By :" + username + " =================");
                    log.info("");
//                    System.out.println("topup agent :"+ agentid + ", last balance :"+saldoBefore+", amount :"+newAmount+", current balance :"+currbal+", Approve By :"+ username);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            clearStatment(stat4);
//            clearResultset(rs4);
            clearStatment(stat5);
//            clearResultset(rs5);
            clearStatment(stat3);
            clearResultset(rs3);
            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return true;
    }

    public ArrayList<Topupagent> getAllTopupagent() throws ParseException {
        ArrayList<Topupagent> topupagentList = new ArrayList<Topupagent>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        SimpleDateFormat transfer_date1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat transfer_date2 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat topup_date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat topup_date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT b.agent_name, a.agent_id, a.amount, a.bank_name, a.acct_no, a.transfer_date, a.process_exe, a.topup_date, a.topup_id FROM am_update_balance a inner join am_user b on a.agent_id = b.agent_id order by a.topup_date DESC");
            rs = stat.executeQuery();

//            ResultSet resultSet1 = stat1.executeQuery("SELECT agent_name,agent_id,amount,bank_name,acct_no,transfer_date,process_exe,topup_date,topup_id  FROM am_update_balance order by topup_date");
            while (rs.next()) {
                Topupagent topupagent = new Topupagent();
                Date topup_date = topup_date1.parse(rs.getString("topup_date"));
                topupagent.setTopup_date(topup_date1.format(topup_date));
                topupagent.setAgent_name(rs.getString("agent_name"));
                topupagent.setAgent_id(rs.getString("agent_id"));
                topupagent.setAmount(rs.getString("amount"));
                topupagent.setBank_name(rs.getString("bank_name"));
                topupagent.setAcct_no(rs.getString("acct_no"));
                Date transfer_date = transfer_date1.parse(rs.getString("transfer_date"));
                topupagent.setTransfer_date(transfer_date2.format(transfer_date));
                topupagent.setTopup_id(rs.getString("topup_id"));
                topupagent.setProcess_exe(rs.getString("process_exe"));
                topupagentList.add(topupagent);
            }
//            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return topupagentList;
    }

    public ArrayList<Reportyabes> getAlltransactionyabes() throws ParseException {
        ArrayList<Reportyabes> listTransaction = new ArrayList<Reportyabes>();
        SimpleDateFormat requesttime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requesttime2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Connection conn = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;
        PreparedStatement stat3 = null;
        PreparedStatement stat4 = null;
        PreparedStatement stat5 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat1 = conn.prepareStatement("select * from "
                    + "(select transactioncode, fromaccount, toaccount, msgid, proccode, statusreply, requesttime, refer, responsetime, noref, billercode, amount, productcode, fromagent, rcinternal, custno, hargajual, hargabeli, ref_profit, ppob_profit, feejual, feebeli, prev_bal, curr_bal, no_ba, usr_cashback, payment_method "
                    + "from tempmsg union all "
                    + "select transactioncode, fromaccount, toaccount, msgid, proccode, statusreply, requesttime, refer, responsetime, noref, billercode, amount, productcode, fromagent, rcinternal, custno, hargajual, hargabeli, ref_profit, ppob_profit, feejual, feebeli, prev_bal, curr_bal, no_ba, usr_cashback, payment_method "
                    + "from tempmsg_backup order by msgid) "
                    + "AS tempmsg where proccode NOT IN ('200000', '380000', '300002', '300003', '300004', '260000')");
            rs1 = stat1.executeQuery();
            while (rs1.next()) {

                Reportyabes reportyabes = new Reportyabes();
                reportyabes.setAgentid(rs1.getString("fromagent"));
                reportyabes.setEmail("-");

                stat5 = conn.prepareStatement("select agent_name, app_id from am_user where agent_id = ?");
                stat5.setString(1, rs1.getString("fromagent"));
                rs5 = stat5.executeQuery();
                while (rs5.next()) {
                    if (rs1.getString("fromagent") == null) {
                        reportyabes.setAgentid("-");
                    } else {
                        reportyabes.setAgentid(rs1.getString("fromagent"));
                        reportyabes.setEmail(rs5.getString("agent_name"));
                    }
                    if (rs5.getString("app_id") == null) {
                        reportyabes.setApp_id("-");
                    } else {
                        reportyabes.setApp_id(rs5.getString("app_id"));
                    }
                }
                stat4 = conn.prepareStatement("select * from trancode where trancodeid='" + rs1.getString("productcode") + "'");
                rs4 = stat4.executeQuery();
                if (rs1.getString("billercode") == null) {
                    reportyabes.setBillername("-");
                } else {
                    reportyabes.setBillername(rs1.getString("billercode"));
                }
                while (rs4.next()) {
                    if (rs4.getString("trancodename") == null) {
                        reportyabes.setNameproduct("-");
                    } else {
                        reportyabes.setNameproduct(rs4.getString("trancodename"));
                    }
                }
                if (rs1.getString("proccode").equals("400001")) {
                    if (rs1.getString("fromaccount") == null) {
                        reportyabes.setFromaccount("-");
                    } else {
                        reportyabes.setFromaccount(rs1.getString("fromaccount"));
                    }
                    reportyabes.setToaccount(rs1.getString("toaccount"));
                } else {
                    reportyabes.setFromaccount("-");
                    reportyabes.setToaccount("-");
                }
                if (rs1.getString("noref") == null) {
                    reportyabes.setNoref("-");
                } else {
                    reportyabes.setNoref(rs1.getString("noref"));
                }
                if (rs1.getString("custno") == null) {
                    reportyabes.setCustno("-");
                } else {
                    reportyabes.setCustno(rs1.getString("custno"));
                }
                if (rs1.getString("requesttime") == null) {
                    reportyabes.setRequesttime("-");
                } else {
                    Date requesttime = requesttime1.parse(rs1.getString("requesttime"));
                    reportyabes.setRequesttime(requesttime2.format(requesttime));
                }
                if (rs1.getString("rcinternal") == null) {
                    reportyabes.setRcinternal("-");
                } else {
                    reportyabes.setRcinternal(rs1.getString("rcinternal"));
                }
                if (rs1.getString("rcinternal") == null) {
                    reportyabes.setStatus("-");
                } else if (rs1.getString("rcinternal").equals("0000")) {
                    reportyabes.setStatus("success");
                } else if (rs1.getString("rcinternal").equals("0020")) {
                    reportyabes.setStatus("error");
                } else if (rs1.getString("rcinternal").equals("0068")) {
                    reportyabes.setStatus("timeout");
                } else {
                    reportyabes.setStatus("error transaction");
                }
                if (rs1.getString("statusreply") == null) {
                    reportyabes.setStatusreply("-");
                } else {
                    reportyabes.setStatusreply(rs1.getString("statusreply"));
                }
                if (rs1.getString("amount") == null) {
                    reportyabes.setAmount("-");
                } else {
                    int amount = rs1.getInt("amount");
                    int admin = rs1.getInt("feejual");
                    int aaa = amount + admin;
                    reportyabes.setAmount(Integer.toString(aaa));
                }
                if (rs1.getString("hargabeli") == null) {
                    reportyabes.setHargabeli("-");
                } else {
                    reportyabes.setHargabeli(rs1.getString("hargabeli"));
                }
                if (rs1.getString("hargajual") == null) {
                    reportyabes.setHargajual("-");
                } else {
                    reportyabes.setHargajual(rs1.getString("hargajual"));
                }
                if (rs1.getString("feebeli") == null) {
                    reportyabes.setFeebeli("-");
                } else {
                    reportyabes.setFeebeli(rs1.getString("feebeli"));
                }
                if (rs1.getString("feejual") == null) {
                    reportyabes.setFeejual("-");
                } else {
                    reportyabes.setFeejual(rs1.getString("feejual"));
                }
                if (rs1.getString("ppob_profit") == null) {
                    reportyabes.setPpob_profit("-");
                } else {
                    reportyabes.setPpob_profit(rs1.getString("ppob_profit"));
                }
                if (rs1.getString("ref_profit") == null) {
                    reportyabes.setRef_profit("-");
                } else {
                    reportyabes.setRef_profit(rs1.getString("ref_profit"));
                }
//                if (rs1.getString("refer").equals("")) {
//                    reportyabes.setRefer("-");
//                } else if (rs1.getString("refer").equals("0")) {
//                    reportyabes.setRefer("-");
//                } else {
//                    stat5 = conn.prepareStatement("select nama_koperasi from profile_corp where cu_id='" + rs1.getString("refer") + "'");
//                    rs5 = stat5.executeQuery();
//                    while (rs5.next()) {
//                        reportyabes.setRefer(rs5.getString("nama_koperasi"));
//                    }
//                }
                reportyabes.setRefer("-");
                if (rs1.getString("prev_bal") == null) {
                    reportyabes.setPrev_bal("-");
                } else {
                    reportyabes.setPrev_bal(rs1.getString("prev_bal"));
                }
                if (rs1.getString("curr_bal") == null) {
                    reportyabes.setCurr_bal("-");
                } else {
                    reportyabes.setCurr_bal(rs1.getString("curr_bal"));
                }
                if (rs1.getString("no_ba") == null) {
                    reportyabes.setNo_ba("-");
                } else {
                    reportyabes.setNo_ba(rs1.getString("no_ba"));
                }
                if (rs1.getString("usr_cashback") == null) {
                    reportyabes.setUsr_cashback("-");
                } else {
                    reportyabes.setUsr_cashback(rs1.getString("usr_cashback"));
                }
                switch (rs1.getString("proccode")) {
                    case "290000":
                        if (rs1.getString("transactioncode").equals("0")) {
                            reportyabes.setTransactioncode("suspect");
                        } else {
                            reportyabes.setTransactioncode(rs1.getString("transactioncode"));
                        }   break;
                    case "400011":
                        reportyabes.setTransactioncode(rs1.getString("transactioncode"));
                        break;
                    default:
                        reportyabes.setTransactioncode("-");
                        break;
                }
                if (null == rs1.getString("payment_method")) {
                    reportyabes.setPayment_method("-");
                } else switch (rs1.getString("payment_method")) {
                    case "1":
                        reportyabes.setPayment_method("balance");
                        break;
                    case "2":
                        reportyabes.setPayment_method("poin");
                        break;
                    default:
                        break;
                }
                listTransaction.add(reportyabes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearStatment(stat4);
            clearResultset(rs4);
            clearStatment(stat5);
            clearResultset(rs5);
            clearStatment(stat3);
            clearResultset(rs3);
            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return listTransaction;
    }

    public ArrayList<Reportyabes> getTodayTransaction() throws ParseException {
        ArrayList<Reportyabes> listTransaction = new ArrayList<Reportyabes>();
        SimpleDateFormat requesttime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requesttime2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Connection conn = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;
        PreparedStatement stat3 = null;
        PreparedStatement stat4 = null;
        PreparedStatement stat5 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat1 = conn.prepareStatement(" select * from "
                    + " (select transactioncode, fromaccount, toaccount, msgid, proccode, statusreply, requesttime, refer, responsetime, noref, billercode, amount, productcode, fromagent, rcinternal, custno, hargajual, hargabeli, ref_profit, ppob_profit, feejual, feebeli, prev_bal, curr_bal, no_ba, usr_cashback, payment_method "
                    + " from tempmsg union all "
                    + " select transactioncode, fromaccount, toaccount, msgid, proccode, statusreply, requesttime, refer, responsetime, noref, billercode, amount, productcode, fromagent, rcinternal, custno, hargajual, hargabeli, ref_profit, ppob_profit, feejual, feebeli, prev_bal, curr_bal, no_ba, usr_cashback, payment_method "
                    + " from tempmsg_backup order by msgid) "
                    + " AS tempmsg where proccode NOT IN ('200000', '380000', '300002', '300003', '300004', '260000')"
                    + " and requesttime  >= DATE(now())");
            rs1 = stat1.executeQuery();
            while (rs1.next()) {
                Reportyabes reportyabes = new Reportyabes();
                reportyabes.setAgentid(rs1.getString("fromagent"));
                reportyabes.setEmail("-");
                stat5 = conn.prepareStatement("select agent_name, app_id from am_user where agent_id = ?");
                stat5.setString(1, rs1.getString("fromagent"));
                rs5 = stat5.executeQuery();
                while (rs5.next()) {
                    if (rs1.getString("fromagent") == null) {
                        reportyabes.setAgentid("-");
                    } else {
                        reportyabes.setAgentid(rs1.getString("fromagent"));
                        reportyabes.setEmail(rs5.getString("agent_name"));
                    }
                    if (rs5.getString("app_id") == null) {
                        reportyabes.setApp_id("-");
                    } else {
                        reportyabes.setApp_id(rs5.getString("app_id"));
                    }
                }
                stat4 = conn.prepareStatement("select * from trancode where trancodeid='" + rs1.getString("productcode") + "'");
                rs4 = stat4.executeQuery();
                if (rs1.getString("billercode") == null) {
                    reportyabes.setBillername("-");
                } else {
                    reportyabes.setBillername(rs1.getString("billercode"));
                }
                while (rs4.next()) {
                    if (rs4.getString("trancodename") == null) {
                        reportyabes.setNameproduct("-");
                    } else {
                        reportyabes.setNameproduct(rs4.getString("trancodename"));
                    }
                }
                if (rs1.getString("proccode").equals("400001")) {
                    if (rs1.getString("fromaccount") == null) {
                        reportyabes.setFromaccount("-");
                    } else {
                        reportyabes.setFromaccount(rs1.getString("fromaccount"));
                    }
                    reportyabes.setToaccount(rs1.getString("toaccount"));
                } else {
                    reportyabes.setFromaccount("-");
                    reportyabes.setToaccount("-");
                }
                if (rs1.getString("noref") == null) {
                    reportyabes.setNoref("-");
                } else {
                    reportyabes.setNoref(rs1.getString("noref"));
                }
                if (rs1.getString("custno") == null) {
                    reportyabes.setCustno("-");
                } else {
                    reportyabes.setCustno(rs1.getString("custno"));
                }
                if (rs1.getString("requesttime") == null) {
                    reportyabes.setRequesttime("-");
                } else {
                    Date requesttime = requesttime1.parse(rs1.getString("requesttime"));
                    reportyabes.setRequesttime(requesttime2.format(requesttime));
                }
                if (rs1.getString("rcinternal") == null) {
                    reportyabes.setRcinternal("-");
                } else {
                    reportyabes.setRcinternal(rs1.getString("rcinternal"));
                }
                if (null == rs1.getString("rcinternal")) {
                    reportyabes.setStatus("-");
                } else switch (rs1.getString("rcinternal")) {
                    case "0000":
                        reportyabes.setStatus("success");
                        break;
                    case "0020":
                        reportyabes.setStatus("error");
                        break;
                    case "0068":
                        reportyabes.setStatus("timeout");
                        break;
                    default:
                        reportyabes.setStatus("error transaction");
                        break;
                }
                if (rs1.getString("statusreply") == null) {
                    reportyabes.setStatusreply("-");
                } else {
                    reportyabes.setStatusreply(rs1.getString("statusreply"));
                }
                if (rs1.getString("amount") == null) {
                    reportyabes.setAmount("-");
                } else {
                    int amount = rs1.getInt("amount");
                    int admin = rs1.getInt("feejual");
                    int aaa = amount + admin;
                    reportyabes.setAmount(Integer.toString(aaa));
                }
                if (rs1.getString("hargabeli") == null) {
                    reportyabes.setHargabeli("-");
                } else {
                    reportyabes.setHargabeli(rs1.getString("hargabeli"));
                }
                if (rs1.getString("hargajual") == null) {
                    reportyabes.setHargajual("-");
                } else {
                    reportyabes.setHargajual(rs1.getString("hargajual"));
                }
                if (rs1.getString("feebeli") == null) {
                    reportyabes.setFeebeli("-");
                } else {
                    reportyabes.setFeebeli(rs1.getString("feebeli"));
                }
                if (rs1.getString("feejual") == null) {
                    reportyabes.setFeejual("-");
                } else {
                    reportyabes.setFeejual(rs1.getString("feejual"));
                }
                if (rs1.getString("ppob_profit") == null) {
                    reportyabes.setPpob_profit("-");
                } else {
                    reportyabes.setPpob_profit(rs1.getString("ppob_profit"));
                }
                if (rs1.getString("ref_profit") == null) {
                    reportyabes.setRef_profit("-");
                } else {
                    reportyabes.setRef_profit(rs1.getString("ref_profit"));
                }
                switch (rs1.getString("refer")) {
                    case "":
                        reportyabes.setRefer("-");
                        break;
                    case "0":
                        reportyabes.setRefer("-");
                        break;
                    default:
                        stat5 = conn.prepareStatement("select nama_koperasi from profile_corp where cu_id='" + rs1.getString("refer") + "'");
                        rs5 = stat5.executeQuery();
                        while (rs5.next()) {
                            reportyabes.setRefer(rs5.getString("nama_koperasi"));
                        }   break;
                }
                if (rs1.getString("prev_bal") == null) {
                    reportyabes.setPrev_bal("-");
                } else {
                    reportyabes.setPrev_bal(rs1.getString("prev_bal"));
                }
                if (rs1.getString("curr_bal") == null) {
                    reportyabes.setCurr_bal("-");
                } else {
                    reportyabes.setCurr_bal(rs1.getString("curr_bal"));
                }
                if (rs1.getString("no_ba") == null) {
                    reportyabes.setNo_ba("-");
                } else {
                    reportyabes.setNo_ba(rs1.getString("no_ba"));
                }
                if (rs1.getString("usr_cashback") == null) {
                    reportyabes.setUsr_cashback("-");
                } else {
                    reportyabes.setUsr_cashback(rs1.getString("usr_cashback"));
                }
                switch (rs1.getString("proccode")) {
                    case "290000":
                        if (rs1.getString("transactioncode").equals("0")) {
                            reportyabes.setTransactioncode("suspect");
                        } else {
                            reportyabes.setTransactioncode(rs1.getString("transactioncode"));
                        }   break;
                    case "400011":
                        reportyabes.setTransactioncode(rs1.getString("transactioncode"));
                        break;
                    default:
                        reportyabes.setTransactioncode("-");
                        break;
                }
                if (null == rs1.getString("payment_method")) {
                    reportyabes.setPayment_method("-");
                } else switch (rs1.getString("payment_method")) {
                    case "1":
                        reportyabes.setPayment_method("balance");
                        break;
                    case "2":
                        reportyabes.setPayment_method("poin");
                        break;
                    default:
                        break;
                }
                listTransaction.add(reportyabes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearStatment(stat4);
            clearResultset(rs4);
            clearStatment(stat5);
            clearResultset(rs5);
            clearStatment(stat3);
            clearResultset(rs3);
            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return listTransaction;
    }

    public ArrayList<Reportyabes> get7dayTransaction() throws ParseException {
        ArrayList<Reportyabes> listTransaction = new ArrayList<Reportyabes>();
        SimpleDateFormat requesttime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requesttime2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Connection conn = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;
        PreparedStatement stat3 = null;
        PreparedStatement stat4 = null;
        PreparedStatement stat5 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat1 = conn.prepareStatement(" select * from "
                    + " (select transactioncode, fromaccount, toaccount, msgid, proccode, statusreply, requesttime, refer, responsetime, noref, billercode, amount, productcode, fromagent, rcinternal, custno, hargajual, hargabeli, ref_profit, ppob_profit, feejual, feebeli, prev_bal, curr_bal, no_ba, usr_cashback, payment_method "
                    + " from tempmsg union all "
                    + " select transactioncode, fromaccount, toaccount, msgid, proccode, statusreply, requesttime, refer, responsetime, noref, billercode, amount, productcode, fromagent, rcinternal, custno, hargajual, hargabeli, ref_profit, ppob_profit, feejual, feebeli, prev_bal, curr_bal, no_ba, usr_cashback, payment_method "
                    + " from tempmsg_backup order by msgid) "
                    + " AS tempmsg where proccode NOT IN ('200000', '380000', '300002', '300003', '300004', '260000')"
                    + " and requesttime >= now()- INTERVAL '7 DAY'");
            rs1 = stat1.executeQuery();
            while (rs1.next()) {

                Reportyabes reportyabes = new Reportyabes();
                reportyabes.setAgentid(rs1.getString("fromagent"));
                reportyabes.setEmail("-");

                stat5 = conn.prepareStatement("select agent_name, app_id from am_user where agent_id = ?");
                stat5.setString(1, rs1.getString("fromagent"));
                rs5 = stat5.executeQuery();
                while (rs5.next()) {
                    if (rs1.getString("fromagent") == null) {
                        reportyabes.setAgentid("-");
                    } else {
                        reportyabes.setAgentid(rs1.getString("fromagent"));
                        reportyabes.setEmail(rs5.getString("agent_name"));
                    }
                    if (rs5.getString("app_id") == null) {
                        reportyabes.setApp_id("-");
                    } else {
                        reportyabes.setApp_id(rs5.getString("app_id"));
                    }
                }
                stat4 = conn.prepareStatement("select * from trancode where trancodeid='" + rs1.getString("productcode") + "'");
                rs4 = stat4.executeQuery();
                if (rs1.getString("billercode") == null) {
                    reportyabes.setBillername("-");
                } else {
                    reportyabes.setBillername(rs1.getString("billercode"));
                }
                while (rs4.next()) {
                    if (rs4.getString("trancodename") == null) {
                        reportyabes.setNameproduct("-");
                    } else {
                        reportyabes.setNameproduct(rs4.getString("trancodename"));
                    }
                }
                if (rs1.getString("proccode").equals("400001")) {
                    if (rs1.getString("fromaccount") == null) {
                        reportyabes.setFromaccount("-");
                    } else {
                        reportyabes.setFromaccount(rs1.getString("fromaccount"));
                    }
                    reportyabes.setToaccount(rs1.getString("toaccount"));
                } else {
                    reportyabes.setFromaccount("-");
                    reportyabes.setToaccount("-");
                }
                if (rs1.getString("noref") == null) {
                    reportyabes.setNoref("-");
                } else {
                    reportyabes.setNoref(rs1.getString("noref"));
                }
                if (rs1.getString("custno") == null) {
                    reportyabes.setCustno("-");
                } else {
                    reportyabes.setCustno(rs1.getString("custno"));
                }
                if (rs1.getString("requesttime") == null) {
                    reportyabes.setRequesttime("-");
                } else {
                    Date requesttime = requesttime1.parse(rs1.getString("requesttime"));
                    reportyabes.setRequesttime(requesttime2.format(requesttime));
                }
                if (rs1.getString("rcinternal") == null) {
                    reportyabes.setRcinternal("-");
                } else {
                    reportyabes.setRcinternal(rs1.getString("rcinternal"));
                }
                if (null == rs1.getString("rcinternal")) {
                    reportyabes.setStatus("-");
                } else switch (rs1.getString("rcinternal")) {
                    case "0000":
                        reportyabes.setStatus("success");
                        break;
                    case "0020":
                        reportyabes.setStatus("error");
                        break;
                    case "0068":
                        reportyabes.setStatus("timeout");
                        break;
                    default:
                        reportyabes.setStatus("error transaction");
                        break;
                }
                if (rs1.getString("statusreply") == null) {
                    reportyabes.setStatusreply("-");
                } else {
                    reportyabes.setStatusreply(rs1.getString("statusreply"));
                }
                if (rs1.getString("amount") == null) {
                    reportyabes.setAmount("-");
                } else {
                    int amount = rs1.getInt("amount");
                    int admin = rs1.getInt("feejual");
                    int aaa = amount + admin;
                    reportyabes.setAmount(Integer.toString(aaa));
                }
                if (rs1.getString("hargabeli") == null) {
                    reportyabes.setHargabeli("-");
                } else {
                    reportyabes.setHargabeli(rs1.getString("hargabeli"));
                }
                if (rs1.getString("hargajual") == null) {
                    reportyabes.setHargajual("-");
                } else {
                    reportyabes.setHargajual(rs1.getString("hargajual"));
                }
                if (rs1.getString("feebeli") == null) {
                    reportyabes.setFeebeli("-");
                } else {
                    reportyabes.setFeebeli(rs1.getString("feebeli"));
                }
                if (rs1.getString("feejual") == null) {
                    reportyabes.setFeejual("-");
                } else {
                    reportyabes.setFeejual(rs1.getString("feejual"));
                }
                if (rs1.getString("ppob_profit") == null) {
                    reportyabes.setPpob_profit("-");
                } else {
                    reportyabes.setPpob_profit(rs1.getString("ppob_profit"));
                }
                if (rs1.getString("ref_profit") == null) {
                    reportyabes.setRef_profit("-");
                } else {
                    reportyabes.setRef_profit(rs1.getString("ref_profit"));
                }
//                if (rs1.getString("refer").equals("")) {
//                    reportyabes.setRefer("-");
//                } else if (rs1.getString("refer").equals("0")) {
//                    reportyabes.setRefer("-");
//                } else {
//                    stat5 = conn.prepareStatement("select nama_koperasi from profile_corp where cu_id='" + rs1.getString("refer") + "'");
//                    rs5 = stat5.executeQuery();
//                    while (rs5.next()) {
//                        reportyabes.setRefer(rs5.getString("nama_koperasi"));
//                    }
//                }
                reportyabes.setRefer("-");
                if (rs1.getString("prev_bal") == null) {
                    reportyabes.setPrev_bal("-");
                } else {
                    reportyabes.setPrev_bal(rs1.getString("prev_bal"));
                }
                if (rs1.getString("curr_bal") == null) {
                    reportyabes.setCurr_bal("-");
                } else {
                    reportyabes.setCurr_bal(rs1.getString("curr_bal"));
                }
                if (rs1.getString("no_ba") == null) {
                    reportyabes.setNo_ba("-");
                } else {
                    reportyabes.setNo_ba(rs1.getString("no_ba"));
                }
                if (rs1.getString("usr_cashback") == null) {
                    reportyabes.setUsr_cashback("-");
                } else {
                    reportyabes.setUsr_cashback(rs1.getString("usr_cashback"));
                }
                switch (rs1.getString("proccode")) {
                    case "290000":
                        if (rs1.getString("transactioncode").equals("0")) {
                            reportyabes.setTransactioncode("suspect");
                        } else {
                            reportyabes.setTransactioncode(rs1.getString("transactioncode"));
                        }   break;
                    case "400011":
                        reportyabes.setTransactioncode(rs1.getString("transactioncode"));
                        break;
                    default:
                        reportyabes.setTransactioncode("-");
                        break;
                }
                if (null == rs1.getString("payment_method")) {
                    reportyabes.setPayment_method("-");
                } else switch (rs1.getString("payment_method")) {
                    case "1":
                        reportyabes.setPayment_method("balance");
                        break;
                    case "2":
                        reportyabes.setPayment_method("poin");
                        break;
                    default:
                        break;
                }
                listTransaction.add(reportyabes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearStatment(stat4);
            clearResultset(rs4);
            clearStatment(stat5);
            clearResultset(rs5);
            clearStatment(stat3);
            clearResultset(rs3);
            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return listTransaction;
    }

    public ArrayList<Reportyabes> getThisMonthTransaction() throws ParseException {
        ArrayList<Reportyabes> listTransaction = new ArrayList<Reportyabes>();
        SimpleDateFormat requesttime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requesttime2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Connection conn = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;
        PreparedStatement stat3 = null;
        PreparedStatement stat4 = null;
        PreparedStatement stat5 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat1 = conn.prepareStatement(" select * from "
                    + " (select transactioncode, fromaccount, toaccount, msgid, proccode, statusreply, requesttime, refer, responsetime, noref, billercode, amount, productcode, fromagent, rcinternal, custno, hargajual, hargabeli, ref_profit, ppob_profit, feejual, feebeli, prev_bal, curr_bal, no_ba, usr_cashback, payment_method "
                    + " from tempmsg union all "
                    + " select transactioncode, fromaccount, toaccount, msgid, proccode, statusreply, requesttime, refer, responsetime, noref, billercode, amount, productcode, fromagent, rcinternal, custno, hargajual, hargabeli, ref_profit, ppob_profit, feejual, feebeli, prev_bal, curr_bal, no_ba, usr_cashback, payment_method "
                    + " from tempmsg_backup order by msgid) "
                    + " AS tempmsg where proccode NOT IN ('200000', '380000', '300002', '300003', '300004', '260000')"
                    + " and EXTRACT(MONTH FROM requesttime) >=EXTRACT(MONTH FROM date_trunc('month', CURRENT_DATE))");
            rs1 = stat1.executeQuery();
            while (rs1.next()) {

                Reportyabes reportyabes = new Reportyabes();
                reportyabes.setAgentid(rs1.getString("fromagent"));
                reportyabes.setEmail("-");

                stat5 = conn.prepareStatement("select agent_name, app_id from am_user where agent_id = ?");
                stat5.setString(1, rs1.getString("fromagent"));
                rs5 = stat5.executeQuery();
                while (rs5.next()) {
                    if (rs1.getString("fromagent") == null) {
                        reportyabes.setAgentid("-");
                    } else {
                        reportyabes.setAgentid(rs1.getString("fromagent"));
                        reportyabes.setEmail(rs5.getString("agent_name"));
                    }
                    if (rs5.getString("app_id") == null) {
                        reportyabes.setApp_id("-");
                    } else {
                        reportyabes.setApp_id(rs5.getString("app_id"));
                    }
                }
                stat4 = conn.prepareStatement("select * from trancode where trancodeid='" + rs1.getString("productcode") + "'");
                rs4 = stat4.executeQuery();
                if (rs1.getString("billercode") == null) {
                    reportyabes.setBillername("-");
                } else {
                    reportyabes.setBillername(rs1.getString("billercode"));
                }
                while (rs4.next()) {
                    if (rs4.getString("trancodename") == null) {
                        reportyabes.setNameproduct("-");
                    } else {
                        reportyabes.setNameproduct(rs4.getString("trancodename"));
                    }
                }
                if (rs1.getString("proccode").equals("400001")) {
                    if (rs1.getString("fromaccount") == null) {
                        reportyabes.setFromaccount("-");
                    } else {
                        reportyabes.setFromaccount(rs1.getString("fromaccount"));
                    }
                    reportyabes.setToaccount(rs1.getString("toaccount"));
                } else {
                    reportyabes.setFromaccount("-");
                    reportyabes.setToaccount("-");
                }
                if (rs1.getString("noref") == null) {
                    reportyabes.setNoref("-");
                } else {
                    reportyabes.setNoref(rs1.getString("noref"));
                }
                if (rs1.getString("custno") == null) {
                    reportyabes.setCustno("-");
                } else {
                    reportyabes.setCustno(rs1.getString("custno"));
                }
                if (rs1.getString("requesttime") == null) {
                    reportyabes.setRequesttime("-");
                } else {
                    Date requesttime = requesttime1.parse(rs1.getString("requesttime"));
                    reportyabes.setRequesttime(requesttime2.format(requesttime));
                }
                if (rs1.getString("rcinternal") == null) {
                    reportyabes.setRcinternal("-");
                } else {
                    reportyabes.setRcinternal(rs1.getString("rcinternal"));
                }
                if (null == rs1.getString("rcinternal")) {
                    reportyabes.setStatus("-");
                } else switch (rs1.getString("rcinternal")) {
                    case "0000":
                        reportyabes.setStatus("success");
                        break;
                    case "0020":
                        reportyabes.setStatus("error");
                        break;
                    case "0068":
                        reportyabes.setStatus("timeout");
                        break;
                    default:
                        reportyabes.setStatus("error transaction");
                        break;
                }
                if (rs1.getString("statusreply") == null) {
                    reportyabes.setStatusreply("-");
                } else {
                    reportyabes.setStatusreply(rs1.getString("statusreply"));
                }
                if (rs1.getString("amount") == null) {
                    reportyabes.setAmount("-");
                } else {
                    int amount = rs1.getInt("amount");
                    int admin = rs1.getInt("feejual");
                    int aaa = amount + admin;
                    reportyabes.setAmount(Integer.toString(aaa));
                }
                if (rs1.getString("hargabeli") == null) {
                    reportyabes.setHargabeli("-");
                } else {
                    reportyabes.setHargabeli(rs1.getString("hargabeli"));
                }
                if (rs1.getString("hargajual") == null) {
                    reportyabes.setHargajual("-");
                } else {
                    reportyabes.setHargajual(rs1.getString("hargajual"));
                }
                if (rs1.getString("feebeli") == null) {
                    reportyabes.setFeebeli("-");
                } else {
                    reportyabes.setFeebeli(rs1.getString("feebeli"));
                }
                if (rs1.getString("feejual") == null) {
                    reportyabes.setFeejual("-");
                } else {
                    reportyabes.setFeejual(rs1.getString("feejual"));
                }
                if (rs1.getString("ppob_profit") == null) {
                    reportyabes.setPpob_profit("-");
                } else {
                    reportyabes.setPpob_profit(rs1.getString("ppob_profit"));
                }
                if (rs1.getString("ref_profit") == null) {
                    reportyabes.setRef_profit("-");
                } else {
                    reportyabes.setRef_profit(rs1.getString("ref_profit"));
                }
//                if (rs1.getString("refer").equals("")) {
//                    reportyabes.setRefer("-");
//                } else if (rs1.getString("refer").equals("0")) {
//                    reportyabes.setRefer("-");
//                } else {
//                    stat5 = conn.prepareStatement("select nama_koperasi from profile_corp where cu_id='" + rs1.getString("refer") + "'");
//                    rs5 = stat5.executeQuery();
//                    while (rs5.next()) {
//                        reportyabes.setRefer(rs5.getString("nama_koperasi"));
//                    }
//                }
                reportyabes.setRefer("-");
                if (rs1.getString("prev_bal") == null) {
                    reportyabes.setPrev_bal("-");
                } else {
                    reportyabes.setPrev_bal(rs1.getString("prev_bal"));
                }
                if (rs1.getString("curr_bal") == null) {
                    reportyabes.setCurr_bal("-");
                } else {
                    reportyabes.setCurr_bal(rs1.getString("curr_bal"));
                }
                if (rs1.getString("no_ba") == null) {
                    reportyabes.setNo_ba("-");
                } else {
                    reportyabes.setNo_ba(rs1.getString("no_ba"));
                }
                if (rs1.getString("usr_cashback") == null) {
                    reportyabes.setUsr_cashback("-");
                } else {
                    reportyabes.setUsr_cashback(rs1.getString("usr_cashback"));
                }
                switch (rs1.getString("proccode")) {
                    case "290000":
                        if (rs1.getString("transactioncode").equals("0")) {
                            reportyabes.setTransactioncode("suspect");
                        } else {
                            reportyabes.setTransactioncode(rs1.getString("transactioncode"));
                        }   break;
                    case "400011":
                        reportyabes.setTransactioncode(rs1.getString("transactioncode"));
                        break;
                    default:
                        reportyabes.setTransactioncode("-");
                        break;
                }
                if (null == rs1.getString("payment_method")) {
                    reportyabes.setPayment_method("-");
                } else switch (rs1.getString("payment_method")) {
                    case "1":
                        reportyabes.setPayment_method("balance");
                        break;
                    case "2":
                        reportyabes.setPayment_method("poin");
                        break;
                    default:
                        break;
                }
                listTransaction.add(reportyabes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearStatment(stat4);
            clearResultset(rs4);
            clearStatment(stat5);
            clearResultset(rs5);
            clearStatment(stat3);
            clearResultset(rs3);
            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return listTransaction;
    }

    public ArrayList<Merchant> getAlltransactionMerchant(String merchantid) throws ParseException {
        ArrayList<Merchant> listTrxMerchant = new ArrayList<Merchant>();

        SimpleDateFormat requesttime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requesttime2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
        PreparedStatement stat2 = null;
        ResultSet rs2 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from (select billercode, toaccount, proccode, noref, requesttime, productcode, productcode as productname, fromagent, proccode as jenis_transaksi, amount as tagihan, hargajual as hargabeli, rcinternal as status, status_bayar from tempmsg union all select billercode, toaccount, proccode, noref, requesttime, productcode, productcode as productname, fromagent, proccode as jenis_transaksi, amount as tagihan, hargajual as hargabeli, rcinternal as status, status_bayar from tempmsg_backup order by requesttime) AS tempmsg where proccode in ('280000', '290000', '260000', '400001', '400002', '400003', '400004', '100000', '180000', '400000') and billercode = '" + merchantid + "'");
            rs = stat.executeQuery();
            while (rs.next()) {
                Merchant merchant = new Merchant();
                merchant.setNoref(rs.getString("noref"));

                Date requesttime = requesttime1.parse(rs.getString("requesttime"));
                merchant.setRequesttime(requesttime2.format(requesttime));

                merchant.setProductcode(rs.getString("productcode"));
                stat1 = conn.prepareStatement("select * from trancode where trancodeid='" + rs.getString("productcode") + "'");
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    merchant.setProductname(rs1.getString("trancodename"));
                }
                merchant.setAgent_id(rs.getString("fromagent"));
//                merchant.setFromagent(rs.getString("fromagent"));
                merchant.setBillercode(rs.getString("billercode"));

                merchant.setJenis_transaksi(rs.getString("jenis_transaksi"));
                merchant.setTagihan(rs.getString("tagihan"));
                merchant.setHargabeli(rs.getString("hargabeli"));
                merchant.setStatus(rs.getString("status"));
                merchant.setStatus_bayar(rs.getString("status_bayar"));
                listTrxMerchant.add(merchant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearStatment(stat);
            clearResultset(rs);
            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return listTrxMerchant;
    }

    public ArrayList<Settlement> getAllsettlement() throws ParseException {
        ArrayList<Settlement> listSettlement = new ArrayList<Settlement>();

        SimpleDateFormat requesttime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requesttime2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
        PreparedStatement stat2 = null;
        ResultSet rs2 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from (select responsecode, toaccount, proccode, noref, requesttime, productcode, productcode as productname, fromagent, proccode as jenis_transaksi, amount, hargajual as hargabeli, rcinternal as status, status_bayar from tempmsg union all select responsecode, toaccount, proccode, noref, requesttime, productcode, productcode as productname, fromagent, proccode as jenis_transaksi, amount, hargajual as hargabeli, rcinternal as status, status_bayar from tempmsg_backup order by requesttime) AS tempmsg where proccode = '400004' and responsecode = '0000' and productcode = '400004' and status_bayar = 'f'");
            rs = stat.executeQuery();
            while (rs.next()) {
                Settlement settle = new Settlement();
                settle.setNoref(rs.getString("noref"));

                Date requesttime = requesttime1.parse(rs.getString("requesttime"));
                settle.setRequesttime(requesttime2.format(requesttime));

                settle.setUserid(rs.getString("fromagent"));
                settle.setProductcode(rs.getString("productcode"));
                if (rs.getString("productcode").equals("400000")) {
                    settle.setStatus("piutang");
                } else if (rs.getString("productcode").equals("400004")) {
                    settle.setStatus("hutang");
                } else if (rs.getString("productcode").equals("606001")) {
                    settle.setStatus("hutang");
                } else if (rs.getString("productcode").equals("606002")) {
                    settle.setStatus("hutang");
                } else if (rs.getString("productcode").equals("606003")) {
                    settle.setStatus("hutang");
                } else if (rs.getString("productcode").equals("606004")) {
                    settle.setStatus("hutang");
                }
                stat1 = conn.prepareStatement("select trancodename from trancode where trancodeid='" + rs.getString("productcode") + "'");
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    settle.setProductname(rs1.getString("trancodename"));
                }
                settle.setMerchantid(rs.getString("fromagent"));

                if (rs.getString("jenis_transaksi").equals("290000")) {
                    settle.setJenistrx("Pembayaran");
                } else if (rs.getString("jenis_transaksi").equals("400000")) {
                    settle.setJenistrx("Topup");
                } else if (rs.getString("jenis_transaksi").equals("400004")) {
                    settle.setJenistrx("Merchant Withdrawal");
                } else {
                    settle.setJenistrx("Belum ada");
                }
                settle.setAmount(rs.getString("amount"));
                if (rs.getString("status_bayar").equals("f")) {
                    settle.setStatustrx("Belum terbayar");
                } else if (rs.getString("status_bayar").equals("t")) {
                    settle.setStatustrx("Terbayar");
                }
                listSettlement.add(settle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearStatment(stat);
            clearResultset(rs);
            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);

        }
        return listSettlement;
    }

    public ArrayList<Settlement> getAllHutangTerbayar() throws ParseException {
        ArrayList<Settlement> listSettlement = new ArrayList<Settlement>();

        SimpleDateFormat requesttime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requesttime2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
        PreparedStatement stat2 = null;
        ResultSet rs2 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from (select tgl_bayar, responsecode, toaccount, proccode, noref, requesttime, productcode, productcode as productname, fromagent, proccode as jenis_transaksi, amount, hargajual as hargabeli, rcinternal as status, status_bayar from tempmsg union all select tgl_bayar, responsecode, toaccount, proccode, noref, requesttime, productcode, productcode as productname, fromagent, proccode as jenis_transaksi, amount, hargajual as hargabeli, rcinternal as status, status_bayar from tempmsg_backup order by requesttime) AS tempmsg where proccode = '400004' and responsecode = '0000' and productcode = '400004' and status_bayar = 't'");
            rs = stat.executeQuery();
            while (rs.next()) {
                Settlement settle = new Settlement();
                settle.setNoref(rs.getString("noref"));
                Date requesttime = requesttime1.parse(rs.getString("requesttime"));
                settle.setRequesttime(requesttime2.format(requesttime));

                Date tgl_bayar = requesttime1.parse(rs.getString("tgl_bayar"));
                settle.setTgl_bayar(requesttime2.format(tgl_bayar));
//                settle.setTgl_bayar(rs.getString("tgl_bayar"));

                settle.setUserid(rs.getString("fromagent"));
                settle.setProductcode(rs.getString("productcode"));
                if (rs.getString("productcode").equals("400000")) {
                    settle.setStatus("piutang");
                } else if (rs.getString("productcode").equals("400004")) {
                    settle.setStatus("hutang");
                } else if (rs.getString("productcode").equals("606001")) {
                    settle.setStatus("hutang");
                } else if (rs.getString("productcode").equals("606002")) {
                    settle.setStatus("hutang");
                } else if (rs.getString("productcode").equals("606003")) {
                    settle.setStatus("hutang");
                } else if (rs.getString("productcode").equals("606004")) {
                    settle.setStatus("hutang");
                }
                stat1 = conn.prepareStatement("select * from trancode where trancodeid='" + rs.getString("productcode") + "'");
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    settle.setProductname(rs1.getString("trancodename"));
                }
                settle.setMerchantid(rs.getString("fromagent"));

                if (rs.getString("jenis_transaksi").equals("290000")) {
                    settle.setJenistrx("Pembayaran");
                } else if (rs.getString("jenis_transaksi").equals("400000")) {
                    settle.setJenistrx("Topup");
                } else if (rs.getString("jenis_transaksi").equals("400004")) {
                    settle.setJenistrx("Merchant Withdrawal");
                } else {
                    settle.setJenistrx("Belum ada");
                }
                settle.setAmount(rs.getString("amount"));
                if (rs.getString("status_bayar").equals("f")) {
                    settle.setStatustrx("Belum terbayar");
                } else if (rs.getString("status_bayar").equals("t")) {
                    settle.setStatustrx("Terbayar");
                }
                listSettlement.add(settle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearStatment(stat);
            clearResultset(rs);
            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);

        }
        return listSettlement;
    }

    public ArrayList<Settlement> getAllPiutangTerbayar() throws ParseException {
        ArrayList<Settlement> listSettlement = new ArrayList<Settlement>();

        SimpleDateFormat requesttime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requesttime2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
        PreparedStatement stat2 = null;
        ResultSet rs2 = null;
        try {

            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat2 = conn.prepareStatement("select merchantid from merchant");
            rs2 = stat2.executeQuery();
            while (rs2.next()) {
                String merchant = rs2.getString("merchantid");
                conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
                stat = conn.prepareStatement("select * from (select tgl_bayar, responsecode, toaccount, proccode, noref, requesttime, productcode, productcode as productname, fromagent, proccode as jenis_transaksi, amount, hargajual as hargabeli, rcinternal as status, status_bayar, billercode from tempmsg union all select tgl_bayar, responsecode, toaccount, proccode, noref, requesttime, productcode, productcode as productname, fromagent, proccode as jenis_transaksi, amount, hargajual as hargabeli, rcinternal as status, status_bayar ,billercode from tempmsg_backup order by requesttime) AS tempmsg where proccode = '400000' and responsecode = '0000' and productcode = '400000' and status_bayar = 't' and billercode = ?");
                stat.setString(1, merchant);
                rs = stat.executeQuery();
                while (rs.next()) {
                    Settlement settle = new Settlement();
                    settle.setNoref(rs.getString("noref"));
                    Date requesttime = requesttime1.parse(rs.getString("requesttime"));
                    settle.setRequesttime(requesttime2.format(requesttime));

                    Date tgl_bayar = requesttime1.parse(rs.getString("tgl_bayar"));
                    settle.setTgl_bayar(requesttime2.format(tgl_bayar));
//                settle.setTgl_bayar(rs.getString("tgl_bayar"));
                    settle.setUserid(rs.getString("fromagent"));
                    settle.setProductcode(rs.getString("productcode"));
                    if (rs.getString("productcode").equals("400000")) {
                        settle.setStatus("piutang");
                    } else if (rs.getString("productcode").equals("400004")) {
                        settle.setStatus("hutang");
                    } else if (rs.getString("productcode").equals("606001")) {
                        settle.setStatus("hutang");
                    } else if (rs.getString("productcode").equals("606002")) {
                        settle.setStatus("hutang");
                    } else if (rs.getString("productcode").equals("606003")) {
                        settle.setStatus("hutang");
                    } else if (rs.getString("productcode").equals("606004")) {
                        settle.setStatus("hutang");
                    }
                    stat1 = conn.prepareStatement("select * from trancode where trancodeid='" + rs.getString("productcode") + "'");
                    rs1 = stat1.executeQuery();
                    while (rs1.next()) {
                        settle.setProductname(rs1.getString("trancodename"));
                    }
                    settle.setMerchantid(rs.getString("fromagent"));

                    if (rs.getString("jenis_transaksi").equals("290000")) {
                        settle.setJenistrx("Pembayaran");
                    } else if (rs.getString("jenis_transaksi").equals("400000")) {
                        settle.setJenistrx("Topup");
                    } else if (rs.getString("jenis_transaksi").equals("400004")) {
                        settle.setJenistrx("Merchant Withdrawal");
                    } else {
                        settle.setJenistrx("Belum ada");
                    }
                    settle.setAmount(rs.getString("amount"));
                    if (rs.getString("status_bayar").equals("f")) {
                        settle.setStatustrx("Belum terbayar");
                    } else if (rs.getString("status_bayar").equals("t")) {
                        settle.setStatustrx("Terbayar");
                    }
                    listSettlement.add(settle);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearStatment(stat);
            clearResultset(rs);
            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return listSettlement;
    }

    public ArrayList<Settlement> getAllPiutang() throws ParseException {
        ArrayList<Settlement> listSettlement = new ArrayList<Settlement>();

        SimpleDateFormat requesttime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requesttime2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
        PreparedStatement stat2 = null;
        ResultSet rs2 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat2 = conn.prepareStatement("select merchantid from merchant");
            rs2 = stat2.executeQuery();
            while (rs2.next()) {
                String merchant = rs2.getString("merchantid");
                conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
                stat = conn.prepareStatement("select * from (select responsecode, toaccount, proccode, noref, requesttime, productcode, productcode as productname, fromagent, proccode as jenis_transaksi, amount, hargajual as hargabeli, rcinternal as status, status_bayar, billercode from tempmsg union all select responsecode, toaccount, proccode, noref, requesttime, productcode, productcode as productname, fromagent, proccode as jenis_transaksi, amount, hargajual as hargabeli, rcinternal as status, status_bayar, billercode from tempmsg_backup order by requesttime) AS tempmsg where proccode = '400000' and responsecode = '0000' and status_bayar = 'f' and billercode = ?");
                stat.setString(1, rs2.getString("merchantid"));
                rs = stat.executeQuery();
                while (rs.next()) {
                    Settlement settle = new Settlement();
                    settle.setNoref(rs.getString("noref"));
                    Date requesttime = requesttime1.parse(rs.getString("requesttime"));
                    settle.setRequesttime(requesttime2.format(requesttime));

                    settle.setUserid(rs.getString("fromagent"));
                    settle.setProductcode(rs.getString("productcode"));
                    if (rs.getString("productcode").equals("400000")) {
                        settle.setStatus("piutang");
                    } else if (rs.getString("productcode").equals("400004")) {
                        settle.setStatus("hutang");
                    } else if (rs.getString("productcode").equals("606001")) {
                        settle.setStatus("hutang");
                    } else if (rs.getString("productcode").equals("606002")) {
                        settle.setStatus("hutang");
                    } else if (rs.getString("productcode").equals("606003")) {
                        settle.setStatus("hutang");
                    } else if (rs.getString("productcode").equals("606004")) {
                        settle.setStatus("hutang");
                    }
                    stat1 = conn.prepareStatement("select * from trancode where trancodeid='" + rs.getString("productcode") + "'");
                    rs1 = stat1.executeQuery();
                    while (rs1.next()) {
                        settle.setProductname(rs1.getString("trancodename"));
                    }
                    settle.setMerchantid(rs.getString("fromagent"));

                    if (rs.getString("jenis_transaksi").equals("290000")) {
                        settle.setJenistrx("Pembayaran");
                    } else if (rs.getString("jenis_transaksi").equals("400000")) {
                        settle.setJenistrx("Topup");
                    } else if (rs.getString("jenis_transaksi").equals("400004")) {
                        settle.setJenistrx("Merchant Withdrawal");
                    } else {
                        settle.setJenistrx("Belum ada");
                    }
                    settle.setAmount(rs.getString("amount"));
                    if (rs.getString("status_bayar").equals("f")) {
                        settle.setStatustrx("Belum terbayar");
                    } else if (rs.getString("status_bayar").equals("t")) {
                        settle.setStatustrx("Terbayar");
                    }
                    listSettlement.add(settle);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearStatment(stat);
            clearResultset(rs);
            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return listSettlement;
    }

    public String deleteAgent(String agent_id) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("DELETE FROM am_user WHERE agent_id=?");
            stat.setString(1, agent_id);
            stat.executeUpdate();
            stat.clearParameters();
            stat = null;
            stat = conn.prepareStatement("DELETE FROM am_balance WHERE agent_id=?");
            stat.setString(1, agent_id);
            stat.executeUpdate();
            stat.clearParameters();
            stat = null;
            stat = conn.prepareStatement("DELETE FROM am_itemlimit WHERE agent_id=?");
            stat.setString(1, agent_id);
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "0000";
    }

    public HashMap getAgentByAgentid(String agent_id) {
        Connection conn = null;
        PreparedStatement stat = null;
        HashMap result = new HashMap();
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from am_user WHERE agent_id=?");
            stat.setString(1, agent_id);
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(RuleNameParameter.agent_id, rs.getString("agent_id"));
                result.put(RuleNameParameter.agent_name, rs.getString("agent_name"));
//                result.put(FieldParameter.address, rs.getString("address"));
                result.put(FieldParameter.email, rs.getString("email"));
                result.put(FieldParameter.resp_code, "0000");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getMerchantByMerchantId(String merchantid) {
        Connection conn = null;
        PreparedStatement stat = null;
        HashMap result = new HashMap();
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from merchant WHERE merchantid=?");
            stat.setString(1, merchantid);
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.merchantid, rs.getString("merchantid"));
                result.put(FieldParameter.merchantname, rs.getString("merchantname"));
                result.put(FieldParameter.kodepos, rs.getString("kodepos"));
                result.put(FieldParameter.alamat, rs.getString("alamat"));
                result.put(FieldParameter.email, rs.getString("email"));
                result.put(FieldParameter.resp_code, "0000");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public String updateAgent(AgentList Agentyabes) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE am_user SET  agent_name=?, email=?, lastupdate=?  WHERE agent_id=?");
            stat.setString(1, Agentyabes.getAgent_name());
//            stat.setString(2, Agentyabes.getAgent_address());
            stat.setString(2, Agentyabes.getAgent_phone());
            stat.setTimestamp(3, Timestamp.from(Instant.now()));
            stat.setString(4, Agentyabes.getAgent_id());
            int sts = stat.executeUpdate();
            if (sts == 1) {
                status = "0000";
            }
            stat.clearParameters();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public String updateMerchant(Merchant merchant) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE merchant SET  merchantname=?, kodepos=?, alamat=?, email=?  WHERE merchantid=?");
            stat.setString(1, merchant.getMerchantname());
            stat.setString(2, merchant.getKodepos());
            stat.setString(3, merchant.getAlamat());
            stat.setString(4, merchant.getEmail());
            stat.setString(5, merchant.getMerchantid());
            int sts = stat.executeUpdate();
            if (sts == 1) {
                status = "0000";
            }
            stat.clearParameters();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public String resetPassword(String agent_id) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE am_user SET  password=?, numfaillogin=? WHERE agent_id=?");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd");
            LocalDateTime now = LocalDateTime.now();

            System.out.println("reset reset");
            System.out.println("reset reset");
            System.out.println("reset reset");
            System.out.println(dtf.format(now));
            System.out.println(dtf.format(now));
            System.out.println(dtf.format(now));
            stat.setString(1, "ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f");
            stat.setInt(2, 0);
            stat.setString(3, agent_id);
            int sts = stat.executeUpdate();
            if (sts == 1) {
                status = "0000";
            }
            stat.clearParameters();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public String resetPin(String agent_id) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE am_user SET agent_pin=?, numfailpin=? WHERE agent_id=?");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd");
            LocalDateTime now = LocalDateTime.now();
            stat.setString(1, FunctionSupportMB.encryptOneWayDataSave(dtf.format(now)));
            stat.setInt(2, 0);
            stat.setString(3, agent_id);
            int sts = stat.executeUpdate();
            if (sts == 1) {
                status = "0000";
            }
            stat.clearParameters();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public ArrayList<Verifikasi> getAllVerifikasi() {
        ArrayList<Verifikasi> listverified = new ArrayList<Verifikasi>();
        Connection conn = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;

        ResultSet rs1 = null;
        ResultSet rs2 = null;

        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat1 = conn.prepareStatement("select * from am_user where status in ('1', '2')");
            rs1 = stat1.executeQuery();
            while (rs1.next()) {
                stat2 = conn.prepareStatement("select * from am_balance where agent_id='" + rs1.getString("agent_id") + "'");
                rs2 = stat2.executeQuery();
                while (rs2.next()) {
                    Verifikasi ab = new Verifikasi();
                    if (rs1.getString("agent_id") == null) {
                        ab.setAgent_id("-");
                    } else {
                        ab.setAgent_id(rs1.getString("agent_id"));
                    }

                    if (rs1.getString("agent_name") == null) {
                        ab.setAgent_name("-");
                    } else {
                        ab.setAgent_name(rs1.getString("agent_name"));
                    }

                    if (rs1.getString("email") == null) {
                        ab.setPhonenumber("-");
                    } else {
                        ab.setPhonenumber(rs1.getString("email"));
                    }

                    if (rs1.getString("no_ktp") == null) {
                        ab.setNo_ktp("-");
                    } else {
                        ab.setNo_ktp(rs1.getString("no_ktp"));
                    }

                    if (rs2.getString("curr_bal") == null) {
                        ab.setCurr_bal("-");
                    } else {
                        ab.setCurr_bal(rs2.getString("curr_bal"));
                    }

                    if (rs2.getString("max_curr_bal") == null) {
                        ab.setMax_curr_bal("-");
                    } else {
                        ab.setMax_curr_bal(rs2.getString("max_curr_bal"));
                    }

                    if (rs1.getString("verified") == null) {
                        ab.setVerified("-");
                    } else {
                        ab.setVerified(rs1.getString("verified"));
                    }

                    if (rs1.getString("img_ktp") == null) {
                        ab.setImg_ktp("-");
                    } else {
                        ab.setImg_ktp("foto_ktp");
                    }
                    if (rs1.getString("img_self") == null) {
                        ab.setImg_self("-");
                    } else {
                        ab.setImg_self("foto_selfi");
                    }
                    if (rs1.getString("img_profile") == null) {
                        ab.setImg_profile("-");
                    } else {
                        ab.setImg_profile("foto_profile");
                    }
                    listverified.add(ab);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return listverified;
    }

    public String updateVerifikasi(String agent_id) {

        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("update am_user set verified=1 where agent_id=?");
            stat.setString(1, agent_id);
//            stat.executeUpdate();
//            stat.close();
            int sts = stat.executeUpdate();
            if (sts == 1) {
                status = "0000";
            }
            stat.clearParameters();

            stat = conn.prepareStatement("update am_balance set max_curr_bal = 10000001 where agent_id=?");
            stat.setString(1, agent_id);
            stat.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status;
    }

    public String rejectVerifikasi(String agent_id) {
        System.out.println("masuk ke database proses reject");
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("update am_user set verified=0, img_ktp=null, img_profile=null, img_self=null where agent_id=?");
            stat.setString(1, agent_id);
            int sts = stat.executeUpdate();
            if (sts == 1) {
                status = "0000";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status;
    }

    public String bayarHutang(String noref) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("update tempmsg set status_bayar = 't', tgl_bayar = now() where noref = '" + noref + "'");
            int sts = stat.executeUpdate();
            if (sts == 1) {
                status = "0000";
            }
            stat.clearParameters();
            clearStatment(stat);

            stat = conn.prepareStatement("update tempmsg_backup set status_bayar = 't', tgl_bayar = now() where noref = '" + noref + "'");
            int sts1 = stat.executeUpdate();
            if (sts1 == 1) {
                status = "0000";
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public String piutangDibayar(String noref) {

        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("update tempmsg set status_bayar = 't', tgl_bayar = now() where noref = '" + noref + "'");
            int sts = stat.executeUpdate();
            if (sts == 1) {
                status = "0000";
            }
            stat.clearParameters();
            clearStatment(stat);

            stat = conn.prepareStatement("update tempmsg_backup set status_bayar = 't', tgl_bayar = now() where noref = '" + noref + "'");
            int sts1 = stat.executeUpdate();
            if (sts1 == 1) {
                status = "0000";
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public ArrayList<Billerproduct> getAllOptionProductBiller(String transactiontype) {
        ArrayList<Billerproduct> transactionList = new ArrayList<Billerproduct>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
        PreparedStatement stat2 = null;
        ResultSet rs2 = null;

        switch (transactiontype) {
            case "Other":
                try {
                conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
                stat = conn.prepareStatement("select * from trancode "
                        + "where "
                        + "tctype NOT LIKE '280000' AND "
                        + "tctype NOT LIKE '290000' order by trancodename asc");
                rs = stat.executeQuery();
                while (rs.next()) {
                    Billerproduct biller = new Billerproduct();
                    biller.setTrancodeid(rs.getString("trancodeid"));
                    biller.setTrancodename(rs.getString("trancodename"));
                    transactionList.add(biller);
                }
//                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
            case "Bill Payment":
                try {
                conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
                stat = conn.prepareStatement("select * from trancode where tctype='280000' order by trancodename asc");
                rs = stat.executeQuery();
                while (rs.next()) {
                    Billerproduct biller = new Billerproduct();
                    biller.setTrancodeid(rs.getString("trancodeid"));
                    biller.setTrancodename(rs.getString("trancodename"));
                    transactionList.add(biller);
                }
//                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
            case "Prepaid Reload":
                try {
                conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
                stat1 = conn.prepareStatement("select * from trancode where tctype='290000' order by trancodename asc");
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    Billerproduct biller = new Billerproduct();
                    biller.setTrancodeid(rs1.getString("trancodeid"));
                    biller.setTrancodename(rs1.getString("trancodename"));
                    transactionList.add(biller);
                }
//                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
            case "Ppob":
                try {
                stat2 = conn.prepareStatement("select * from trancode where tctype='180000' order by trancodename asc");
                rs2 = stat2.executeQuery();
                while (rs2.next()) {
                    Billerproduct biller = new Billerproduct();
                    biller.setTrancodeid(rs.getString("trancodeid"));
                    biller.setTrancodename(rs.getString("trancodename"));
                    transactionList.add(biller);
                }
//                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
            case "Select Transaction Type":
                Billerproduct biller = new Billerproduct();
                biller.setTrancodeid("Select Transaction Name");
                biller.setTrancodename("Select Transaction Name");
                transactionList.add(biller);
                break;

        }
        clearStatment(stat);
        clearResultset(rs);
        clearStatment(stat1);
        clearResultset(rs1);
        clearStatment(stat2);
        clearResultset(rs2);
        clearDBConnection(conn);
        return transactionList;
    }

    public HashMap getProductbillerBybillercodeAndtcbiller(String tcbiller, String billercode) {
        Connection conn = null;
        HashMap result = new HashMap();
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
        PreparedStatement stat2 = null;
        ResultSet rs2 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from trancode_biller WHERE tcbiller=? and billercode=?");
            stat.setString(1, tcbiller);
            stat.setString(2, billercode);
            rs = stat.executeQuery();
            while (rs.next()) {
                stat1 = conn.prepareStatement("select tctype, trancodename from trancode WHERE trancodeid=?");
                stat1.setString(1, rs.getString("trancodeid"));
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    stat2 = conn.prepareStatement("select conname from socketconn WHERE billercode=?");
                    stat2.setString(1, billercode);
                    rs2 = stat2.executeQuery();
                    while (rs2.next()) {
                        result.put(FieldParameter.billername, rs2.getString("conname"));
                        result.put(FieldParameter.tcbiller, rs.getString("tcbiller"));
                        result.put(FieldParameter.trancodename, rs1.getString("trancodename"));
                        result.put(FieldParameter.hargabeli, rs.getString("hargabeli"));
                        result.put(FieldParameter.feebeli, rs.getString("feebeli"));
                        result.put(FieldParameter.tctype, rs1.getString("tctype"));
//                        result.put(FieldParameter.poin, rs.getString("poin"));

                        result.put(FieldParameter.resp_code, "0000");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearStatment(stat);
            clearResultset(rs);
            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);

        }
        return result;
    }

    public String updateProductBiller(Billerproduct billerproduct) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE trancode_biller SET hargabeli=?, feebeli=? WHERE tcbiller=?");
            stat.setInt(1, Integer.valueOf(billerproduct.getHargabeli()));
            stat.setInt(2, Integer.valueOf(billerproduct.getFeebeli()));
//            stat.setInt(3, Integer.valueOf(billerproduct.getPoin()));
            stat.setString(3, billerproduct.getTcbiller());
            int sts = stat.executeUpdate();
            if (sts == 1) {
                status = "0000";
            }
            stat.clearParameters();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public ArrayList<LogList> getAllLog() {
        ArrayList<LogList> userlogList = new ArrayList<LogList>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * from ppob_user_activity");
            rs = stat.executeQuery();
            while (rs.next()) {
                LogList logactivity = new LogList();
                if (rs.getString("username") == null) {
                    logactivity.setUsername("-");
                } else {
                    logactivity.setUsername(rs.getString("username"));
                }
                if (rs.getString("activityname") == null) {
                    logactivity.setActivityname("-");
                } else {
                    logactivity.setActivityname(rs.getString("activityname"));
                }
                if (rs.getString("activitytime") == null) {
                    logactivity.setActivitytime("-");
                } else {
                    logactivity.setActivitytime(rs.getString("activitytime"));
                }
                userlogList.add(logactivity);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return userlogList;
    }

    public ArrayList<LogList> getAllLogselfuser(String userlogin) {
        ArrayList<LogList> userlogList = new ArrayList<LogList>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * from ppob_user_activity where username = ?");
            stat.setString(1, userlogin);
            rs = stat.executeQuery();
            while (rs.next()) {
                LogList logactivity = new LogList();
                if (rs.getString("username") == null) {
                    logactivity.setUsername("-");
                } else {
                    logactivity.setUsername(rs.getString("username"));
                }
                if (rs.getString("activityname") == null) {
                    logactivity.setActivityname("-");
                } else {
                    logactivity.setActivityname(rs.getString("activityname"));
                }
                if (rs.getString("activitytime") == null) {
                    logactivity.setActivitytime("-");
                } else {
                    logactivity.setActivitytime(rs.getString("activitytime"));
                }
                userlogList.add(logactivity);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return userlogList;
    }

    public ArrayList<Corporation> getAllCorp() {
        ArrayList<Corporation> listCorp = new ArrayList<Corporation>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * from am_corp where status = '1'");
            rs = stat.executeQuery();
            while (rs.next()) {
                Corporation corp = new Corporation();
                stat1 = conn.prepareStatement("SELECT nama_koperasi from profile_corp where cu_id=?");
                stat1.setString(1, rs.getString("cu_id").toString());
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    if (rs.getString("cu_id") == null) {
                        corp.setCu_id("-");
                    } else {
                        corp.setCu_id(rs.getString("cu_id"));
                    }
                    if (rs1.getString("nama_koperasi") == null) {
                        corp.setNama_koperasi("-");
                    } else {
                        corp.setNama_koperasi(rs1.getString("nama_koperasi"));
                    }
                    if (rs.getString("phonenumber") == null) {
                        corp.setPhonenumber("-");
                    } else {
                        corp.setPhonenumber(rs.getString("phonenumber"));
                    }
                    if (rs.getString("no_anggota") == null) {
                        corp.setNo_anggota("-");
                    } else {
                        corp.setNo_anggota(rs.getString("no_anggota"));
                    }
                    if (rs.getString("aktivasi") == null) {
                        corp.setAktivasi("-");
                    } else {
                        corp.setAktivasi(rs.getString("aktivasi"));
                    }
                    listCorp.add(corp);
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearStatment(stat);
            clearResultset(rs);
            clearStatment(stat1);
            clearResultset(rs1);
            clearDBConnection(conn);
        }
        return listCorp;
    }

    public String addCorp(Corporation corp) {
        Connection conn = null;
        String status;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareStatement("select * from am_corp where cu_id=? and phonenumber=? and no_anggota=?");
            stat.setString(1, corp.getCu_id());
            stat.setString(2, corp.getPhonenumber());
            stat.setString(3, corp.getNo_anggota());
            rs = stat.executeQuery();
            status1 = rs.next();
            if (status1 == true) {
                return status = "0002";
            } else {
                stat = conn.prepareStatement("INSERT INTO am_corp(cu_id, phonenumber, no_anggota) VALUES (?, ?, ?)");
                stat.setString(1, corp.getCu_id());
                stat.setString(2, corp.getPhonenumber());
                stat.setString(3, corp.getNo_anggota());
                stat.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
            return status = "0002";
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "0000";
    }

    public String updateCorp(Corporation corp) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE am_corp SET no_anggota=? WHERE cu_id=? and phonenumber=? and status='1'");
            stat.setString(1, corp.getNo_anggota());
            stat.setString(2, corp.getCu_id());
            stat.setString(3, corp.getPhonenumber());
            stat.executeUpdate();

            status = "0000";
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public String deleteCorp(String cu_id, String phonenumber, String no_anggota) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE am_corp SET status='0' WHERE cu_id=? and phonenumber=? and no_anggota=?");
            stat.setString(1, cu_id);
            stat.setString(2, phonenumber);
            stat.setString(3, no_anggota);

            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status = "0000";
    }

    public HashMap getCorpByCuidAndPhonenumber(String cu_id, String phonenumber, String no_anggota) {
        Connection conn = null;
        HashMap result = new HashMap();
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from am_corp where cu_id=? and phonenumber=? and no_anggota=?");
            stat.setString(1, cu_id);
            stat.setString(2, phonenumber);
            stat.setString(3, no_anggota);
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.cu_id, rs.getString("cu_id"));
                result.put(FieldParameter.phonenumber, rs.getString("phonenumber"));
                result.put(FieldParameter.no_anggota, rs.getString("no_anggota"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public ArrayList<Corporation> getAllKop() {
        ArrayList<Corporation> listKop = new ArrayList<Corporation>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT cu_id, nama_koperasi from profile_corp where status='1'");
            rs = stat.executeQuery();
            while (rs.next()) {
                Corporation corp = new Corporation();

                if (rs.getString("cu_id") == null) {
                    corp.setCu_id("-");
                } else {
                    corp.setCu_id(rs.getString("cu_id"));
                }
                if (rs.getString("nama_koperasi") == null) {
                    corp.setNama_koperasi("-");
                } else {
                    corp.setNama_koperasi(rs.getString("nama_koperasi"));
                }
                listKop.add(corp);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return listKop;
    }

    public String addKop(Corporation corp) {
        Connection conn = null;
        String status;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareStatement("select * from profile_corp where cu_id=?");
            stat.setString(1, corp.getCu_id());
            rs = stat.executeQuery();
            status1 = rs.next();
            if (status1 == true) {
                return status = "0002";
            } else {

                stat1 = conn.prepareStatement("INSERT INTO profile_corp(cu_id, nama_koperasi, password_corp) VALUES (?, ?, ?)");
                stat1.setString(1, corp.getCu_id());
                stat1.setString(2, corp.getNama_koperasi());
                stat1.setString(3, corp.getPassword_corp());
                stat1.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
            return status = "0002";
        } finally {
            clearAllConnStatRS(conn, stat, rs);
            clearStatment(stat1);
        }
        return status = "0000";
    }

    public String updateKop(Corporation corp) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE profile_corp SET nama_koperasi=? WHERE cu_id=? and status='1'");
            stat.setString(1, corp.getNama_koperasi());
            stat.setString(2, corp.getCu_id());
            stat.executeUpdate();

            status = "0000";
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public String deleteKop(String cu_id) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE profile_corp SET status='0' WHERE cu_id=?");
            stat.setString(1, cu_id);

            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status = "0000";
    }

    public HashMap getKopByCuid(String cu_id) {
        Connection conn = null;
        HashMap result = new HashMap();
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from profile_corp where cu_id=?");
            stat.setString(1, cu_id);
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.cu_id, rs.getString("cu_id"));
                result.put(FieldParameter.nama_koperasi, rs.getString("nama_koperasi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public ArrayList<Transactiontrancode> getAllproduk() {
        ArrayList<Transactiontrancode> listproduk = new ArrayList<Transactiontrancode>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * from trancode");
            rs = stat.executeQuery();
            while (rs.next()) {
                Transactiontrancode corp = new Transactiontrancode();
                if (rs.getString("trancodeid") == null) {
                    corp.setTrancodeid("-");
                } else {
                    corp.setTrancodeid(rs.getString("trancodeid"));
                }
                if (rs.getString("trancodename") == null) {
                    corp.setTrancodename("-");
                } else {
                    corp.setTrancodename(rs.getString("trancodename"));
                }
                if (rs.getString("billercode") == null) {
                    corp.setBillercode("-");
                } else {
                    corp.setBillercode(rs.getString("billercode"));
                }
                if (rs.getString("tctype") == null) {
                    corp.setTctype("-");
                } else {
                    corp.setTctype(rs.getString("tctype"));
                }
                if (rs.getString("available") == null) {
                    corp.setAvailable("-");
                } else {
                    corp.setAvailable(rs.getString("available"));
                }

                if (rs.getString("provider") == null) {
                    corp.setProvider("-");
                } else {
                    corp.setProvider(rs.getString("provider"));
                }
                if (rs.getString("category") == null) {
                    corp.setCategory("-");
                } else {
                    corp.setCategory(rs.getString("category"));
                }
                if (rs.getString("detailproduct") == null) {
                    corp.setDetailproduct("-");
                } else {
                    corp.setDetailproduct(rs.getString("detailproduct"));
                }
                listproduk.add(corp);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return listproduk;
    }

    public String addproduk(Transactiontrancode corp) {
        Connection conn = null;
        String status;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareStatement("select * from trancode where trancodeid=?");
            stat.setString(1, corp.getTrancodeid());
            rs = stat.executeQuery();
            status1 = rs.next();
            if (status1 == true) {
                return status = "0002";
            } else {

                stat1 = conn.prepareStatement("INSERT INTO trancode(trancodeid, trancodename, billercode, tctype, provider, category, detailproduct, available) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                stat1.setString(1, corp.getTrancodeid());
                stat1.setString(2, corp.getTrancodename());
                stat1.setString(3, corp.getBillercode());
                stat1.setString(4, corp.getTctype());
                stat1.setString(5, corp.getProvider());
                stat1.setString(6, corp.getCategory());
                stat1.setString(7, corp.getDetailproduct());
                stat1.setString(8, "1");

                stat1.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
            return status = "0002";
        } finally {
            clearStatment(stat);
            clearResultset(rs);
            clearStatment(stat1);
            clearDBConnection(conn);
        }
        return status = "0000";
    }

    public String updateproduk(Transactiontrancode corp) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE trancode SET trancodename=?, billercode=?, tctype=?, provider=?, category=?, detailproduct=? WHERE trancodeid=?");
            stat.setString(1, corp.getTrancodename());
            stat.setString(2, corp.getBillercode());
            stat.setString(3, corp.getTctype());
            stat.setString(4, corp.getProvider());
            stat.setString(5, corp.getCategory());
            stat.setString(6, corp.getDetailproduct());

            stat.setString(7, corp.getTrancodeid());
            stat.executeUpdate();

            status = "0000";
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public String deleteproduk(String trancodeid) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("DELETE FROM trancode WHERE trancodeid=?");
            stat.setString(1, trancodeid);
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status = "0000";
    }

    public HashMap getprodukbytrancodeid(String trancodeid) {
        Connection conn = null;
        HashMap result = new HashMap();
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from trancode where trancodeid=?");
            stat.setString(1, trancodeid);
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.trancodeid, rs.getString("trancodeid"));
                result.put(FieldParameter.trancodename, rs.getString("trancodename"));
                result.put(FieldParameter.billercode, rs.getString("billercode"));
                result.put(FieldParameter.tctype, rs.getString("tctype"));
                result.put(FieldParameter.provider, rs.getString("provider"));
                result.put(FieldParameter.category, rs.getString("category"));
                result.put(FieldParameter.detailproduct, rs.getString("detailproduct"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public ArrayList<am_trancode> getAllprodukuser() {
        ArrayList<am_trancode> listprodukuser = new ArrayList<am_trancode>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
        PreparedStatement stat2 = null;
        ResultSet rs2 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * from am_trancode where agent_id = 'ppob'");
            rs = stat.executeQuery();
            while (rs.next()) {
                am_trancode am = new am_trancode();
                if (rs.getString("trancodeid") == null) {
                    am.setTrancodeid("-");
                } else {
                    am.setTrancodeid(rs.getString("trancodeid"));
                }
                stat1 = conn.prepareStatement("SELECT trancodename from trancode where trancodeid = ?");
                stat1.setString(1, rs.getString("trancodeid"));
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    if (rs1.getString("trancodename") == null) {
                        am.setTrancodename("-");
                    } else {
                        am.setTrancodename(rs1.getString("trancodename"));
                    }
                }
                if (rs.getString("feejual") == null) {
                    am.setFeejual("-");
                } else {
                    am.setFeejual(rs.getString("feejual"));
                }
                if (rs.getString("hargajual") == null) {
                    am.setHargajual("-");
                } else {
                    am.setHargajual(rs.getString("hargajual"));
                }
                if (rs.getString("ppob_profit") == null) {
                    am.setPpob_profit("-");
                } else {
                    am.setPpob_profit(rs.getString("ppob_profit"));
                }
                if (rs.getString("ref_profit") == null) {
                    am.setRef_profit("-");
                } else {
                    am.setRef_profit(rs.getString("ref_profit"));
                }
//                stat2 = conn.prepareStatement("SELECT poin from am_trancode where trancodeid = ?");
//                stat2.setString(1, rs.getString("trancodeid"));
//                rs2 = stat2.executeQuery();
//                while (rs2.next()) {
                if (rs.getString("poin") == null) {
                    am.setPoin("-");
                } else {
                    am.setPoin(rs.getString("poin"));
                }
                if (rs.getString("poin_noncorp") == null) {
                    am.setPoin_noncorp("-");
                } else {
                    am.setPoin_noncorp(rs.getString("poin_noncorp"));
                }
//                }

                if (rs.getString("hargajual_noncorp") == null) {
                    am.setHargajual_noncorp("-");
                } else {
                    am.setHargajual_noncorp(rs.getString("hargajual_noncorp"));
                }
                if (rs.getString("ppob_profit_noncorp") == null) {
                    am.setPpob_profit_noncorp("-");
                } else {
                    am.setPpob_profit_noncorp(rs.getString("ppob_profit_noncorp"));
                }
                if (rs.getString("feejual_noncorp") == null) {
                    am.setFeejual_noncorp("-");
                } else {
                    am.setFeejual_noncorp(rs.getString("feejual_noncorp"));
                }
                listprodukuser.add(am);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearStatment(stat);
            clearResultset(rs);
            clearStatment(stat1);
            clearResultset(rs1);
            clearStatment(stat2);
            clearResultset(rs2);
            clearDBConnection(conn);

        }
        return listprodukuser;
    }

    public HashMap getAmTrancodeByTrancodeid(String trancodeid) {
        Connection conn = null;
        HashMap result = new HashMap();
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
//        PreparedStatement stat2 = null;
//        ResultSet rs2 = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from am_trancode where trancodeid=?");
            stat.setString(1, trancodeid);
            rs = stat.executeQuery();
            while (rs.next()) {
                stat1 = conn.prepareStatement("select trancodename,tctype from trancode where trancodeid = ?");
                stat1.setString(1, trancodeid);
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    result.put(FieldParameter.trancodeid, rs.getString("trancodeid"));
                    result.put(FieldParameter.trancodename, rs1.getString("trancodename"));
                    result.put(FieldParameter.tctype, rs1.getString("tctype"));
                    result.put(FieldParameter.feejual, rs.getString("feejual"));
                    result.put(FieldParameter.hargajual, rs.getString("hargajual"));
                    result.put(FieldParameter.ppob_profit, rs.getString("ppob_profit"));
                    result.put(FieldParameter.ref_profit, rs.getString("ref_profit"));
//                    stat2 = conn.prepareStatement("select poin from am_trancode where trancodeid = ?");
//                    stat2.setString(1, trancodeid);
//                    rs2 = stat2.executeQuery();
//                    while (rs2.next()) {
                    result.put(FieldParameter.poin, rs.getString("poin"));
//                    }
                    result.put(FieldParameter.hargajual_noncorp, rs.getString("hargajual_noncorp"));
                    result.put(FieldParameter.ppob_profit_noncorp, rs.getString("ppob_profit_noncorp"));
                    result.put(FieldParameter.feejual_noncorp, rs.getString("feejual_noncorp"));
                    result.put(FieldParameter.poin_noncorp, rs.getString("poin_noncorp"));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearStatment(stat);
            clearResultset(rs);
            clearStatment(stat1);
            clearResultset(rs1);
//            clearStatment(stat2);
//            clearResultset(rs2);
            clearDBConnection(conn);
        }
        return result;
    }

    public String addprodukuserprepaid(am_trancode am) {
        Connection conn = null;
        String status;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareStatement("select trancodeid from am_trancode where trancodeid=?");
            stat.setString(1, am.getTrancodeid());
            rs = stat.executeQuery();
            status1 = rs.next();
            if (status1 == true) {
                return status = "0002";
            } else {
                stat1 = conn.prepareStatement("INSERT INTO am_trancode(agent_id, trancodeid, hargajual, ppob_profit, ref_profit, poin, hargajual_noncorp, ppob_profit_noncorp, poin_noncorp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                stat1.setString(1, "ppob");
                stat1.setString(2, am.getTrancodeid());
//                stat1.setInt(3, Integer.valueOf(am.getFeejual()));
                stat1.setInt(3, Integer.valueOf(am.getHargajual()));
                stat1.setInt(4, Integer.valueOf(am.getPpob_profit()));
                stat1.setInt(5, Integer.valueOf(am.getRef_profit()));
                stat1.setInt(6, Integer.valueOf(am.getPoin()));
                stat1.setInt(7, Integer.valueOf(am.getHargajual_noncorp()));
                stat1.setInt(8, Integer.valueOf(am.getPpob_profit_noncorp()));
//                stat1.setInt(10, Integer.valueOf(am.getFeejual_noncorp()));
                stat1.setInt(9, Integer.valueOf(am.getPoin_noncorp()));
                stat1.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
            return status = "0002";
        } finally {
            clearAllConnStatRS(conn, stat, rs);
            clearStatment(stat1);

        }
        return status = "0000";
    }

    public String addprodukuserpayment(am_trancode am) {
        Connection conn = null;
        String status;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareStatement("select trancodeid from am_trancode where trancodeid=?");
            stat.setString(1, am.getTrancodeid());
            rs = stat.executeQuery();
            status1 = rs.next();
            if (status1 == true) {
                return status = "0002";
            } else {
                stat1 = conn.prepareStatement("INSERT INTO am_trancode(agent_id, trancodeid, hargajual, ppob_profit, ref_profit, poin, hargajual_noncorp, ppob_profit_noncorp, poin_noncorp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                stat1.setString(1, "ppob");
                stat1.setString(2, am.getTrancodeid());
//                stat1.setInt(3, Integer.valueOf(am.getFeejual()));
                stat1.setInt(3, Integer.valueOf(am.getHargajual()));
                stat1.setInt(4, Integer.valueOf(am.getPpob_profit()));
                stat1.setInt(5, Integer.valueOf(am.getRef_profit()));
                stat1.setInt(6, Integer.valueOf(am.getPoin()));
                stat1.setInt(7, Integer.valueOf(am.getHargajual_noncorp()));
                stat1.setInt(8, Integer.valueOf(am.getPpob_profit_noncorp()));
//                stat1.setInt(10, Integer.valueOf(am.getFeejual_noncorp()));
                stat1.setInt(10, Integer.valueOf(am.getPoin_noncorp()));
                stat1.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
            return status = "0002";
        } finally {
            clearAllConnStatRS(conn, stat, rs);
            clearStatment(stat1);

        }
        return status = "0000";
    }

    public String updateproduk(am_trancode am) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE am_trancode SET feejual=?, hargajual=?, ppob_profit=?, ref_profit=?, poin=?, hargajual_noncorp=?, ppob_profit_noncorp=?, feejual_noncorp=?, poin_noncorp=? WHERE trancodeid=?");
            stat.setInt(1, Integer.valueOf(am.getFeejual()));
            stat.setInt(2, Integer.valueOf(am.getHargajual()));
            stat.setInt(3, Integer.valueOf(am.getPpob_profit()));
            stat.setInt(4, Integer.valueOf(am.getRef_profit()));
            stat.setInt(5, Integer.valueOf(am.getPoin()));
            stat.setInt(6, Integer.valueOf(am.getHargajual_noncorp()));
            stat.setInt(7, Integer.valueOf(am.getPpob_profit_noncorp()));
            stat.setInt(8, Integer.valueOf(am.getFeejual_noncorp()));
            stat.setInt(9, Integer.valueOf(am.getPoin_noncorp()));

            stat.setString(10, am.getTrancodeid());
            stat.executeUpdate();
            status = "0000";
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status;
    }

    public String deleteuserproduk(String trancodeid) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("DELETE FROM am_trancode WHERE trancodeid=?");
            stat.setString(1, trancodeid);

            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "0000";
    }

    public ArrayList<Ads> getAllAds() throws ParseException {

        ArrayList<Ads> listAds = new ArrayList<Ads>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
        SimpleDateFormat requesttime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requesttime2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * from ads_list where status = '1'");
            rs = stat.executeQuery();
            while (rs.next()) {
                Ads ads = new Ads();
                if (rs.getString("ads_id") == null) {
                    ads.setAds_id("-");
                } else {
                    ads.setAds_id(rs.getString("ads_id"));
                }
                if (rs.getString("url") == null) {
                    ads.setUrl("-");
                } else {
                    ads.setUrl(rs.getString("url"));
                }
                if (rs.getString("start_date") == null) {
                    ads.setStart_date("-");
                } else {
                    Date start_date = requesttime1.parse(rs.getString("start_date"));
                    ads.setStart_date(requesttime2.format(start_date));
                }
                if (rs.getString("end_date") == null) {
                    ads.setEnd_date("-");
                } else {
                    Date end_date = requesttime1.parse(rs.getString("end_date"));
                    ads.setEnd_date(requesttime2.format(end_date));
                }
                if (rs.getString("spectator") == null) {
                    ads.setSpectator("-");
                } else if (rs.getString("spectator").equals("all")) {
                    ads.setSpectator("all");
                } else {
                    stat1 = conn.prepareStatement("SELECT nama_koperasi FROM profile_corp WHERE cu_id =?");
                    stat1.setString(1, rs.getString("spectator"));
                    rs1 = stat1.executeQuery();
                    while (rs1.next()) {
                        ads.setSpectator(rs1.getString("nama_koperasi"));
                    }
                }
                listAds.add(ads);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
            clearStatment(stat1);
            clearResultset(rs1);

        }
        return listAds;
    }

    public String addAds(Ads ads) throws ParseException {
        Connection conn = null;
        String status;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String start_date = ads.getStart_date();
        String end_date = ads.getEnd_date();
        SimpleDateFormat darihtml = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareStatement("select * from ads_list where ads_id=?");
            stat.setString(1, ads.getAds_id());
            rs = stat.executeQuery();
            status1 = rs.next();
            if (status1 == true) {
                return status = "0002";
            } else {

                stat = conn.prepareStatement("INSERT INTO ads_list(ads_id, url, start_date, end_date, spectator, status) VALUES (?, ?, ?, ?, ?, ?)");
                stat.setString(1, ads.getAds_id());
//                local
//                stat.setString(2, "http://127.0.0.1/backofficecuso_ppob/image/ads/" + ads.getUrl());
//                dev
//                stat.setString(2, "http://117.53.46.3/image/ads/"+ads.getUrl());
//                prod
                stat.setString(2, "http://202.73.25.93/image/ads/" + ads.getUrl());

                Date dateStart = darihtml.parse(start_date);
                stat.setTimestamp(3, Timestamp.valueOf(dateFormat.format(dateStart)));

                Date dateEnd = darihtml.parse(end_date);
                stat.setTimestamp(4, Timestamp.valueOf(dateFormat.format(dateEnd)));

                stat.setString(5, ads.getSpectator());
                stat.setInt(6, 1);
                stat.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
            return status = "0002";
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "0000";
    }

    public String updateAds(Ads ads) throws ParseException {

        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        String start_date = ads.getStart_date();
        String end_date = ads.getEnd_date();
        SimpleDateFormat darihtml = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE ads_list SET url=?, start_date=?, end_date=?, spectator=? WHERE ads_id=? and status=1");
//                local
//            stat.setString(1, "http://127.0.0.1/backofficecuso_ppob/image/ads/" + ads.getUrl());
//                dev
//                stat.setString(1, "http://117.53.46.3/image/ads/"+ads.getUrl());
//                prod
            if (ads.getUrl().substring(0, 4).equals("http")) {
                stat.setString(1, ads.getUrl());
            } else {
                stat.setString(1, "http://202.73.25.93/image/ads/" + ads.getUrl());
            }
            Date dateStart = darihtml.parse(start_date);
            stat.setTimestamp(2, Timestamp.valueOf(dateFormat.format(dateStart)));

            Date dateEnd = darihtml.parse(end_date);
            stat.setTimestamp(3, Timestamp.valueOf(dateFormat.format(dateEnd)));

//            Date dateStart = waktu1.parse(ads.getStart_date());
//            Timestamp start_date = Timestamp.valueOf(waktu2.format(dateStart));
//            stat.setTimestamp(2, start_date);
//            Date dateEnd = waktu1.parse(ads.getEnd_date());
//            Timestamp end_date = Timestamp.valueOf(waktu2.format(dateEnd));
//            stat.setTimestamp(3, end_date);
            stat.setString(4, ads.getSpectator());
            stat.setString(5, ads.getAds_id());
            stat.executeUpdate();
            status = "0000";
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public String deleteAds(String ads_id) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE ads_list SET status='0' WHERE ads_id=?");
            stat.setString(1, ads_id);
            stat.executeUpdate();
            status = "0000";
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public HashMap getAdsByAdsid(String ads_id) throws ParseException {
        Connection conn = null;
        HashMap result = new HashMap();
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
        SimpleDateFormat darihtml = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from ads_list where ads_id=? and status=1");
            stat.setString(1, ads_id);
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.ads_id, rs.getString("ads_id"));
                result.put(FieldParameter.url, rs.getString("url"));
                Date dateStart = dateFormat.parse(rs.getString("start_date"));
                result.put(FieldParameter.start_date, darihtml.format(dateStart));

                Date dateEnd = dateFormat.parse(rs.getString("end_date"));
                result.put(FieldParameter.end_date, darihtml.format(dateEnd));

                result.put(FieldParameter.spectator, rs.getString("spectator"));
                stat1 = conn.prepareStatement("SELECT nama_koperasi FROM profile_corp WHERE cu_id = ?");
                stat1.setString(1, rs.getString("spectator"));
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    result.put(FieldParameter.nama_koperasi, rs1.getString("nama_koperasi"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
            clearStatment(stat1);
            clearResultset(rs1);

        }
        return result;
    }

    public HashMap getPhotoByAgentid(String agent_id) throws ParseException {
        Connection conn = null;
        HashMap result = new HashMap();
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select agent_id, img_ktp, img_profile, img_self from am_user where agent_id=? and status='1'");
            stat.setString(1, agent_id);
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.agent_id, rs.getString("agent_id"));
                result.put(FieldParameter.img_ktp, rs.getString("img_ktp"));
                result.put(FieldParameter.img_profile, rs.getString("img_profile"));
                result.put(FieldParameter.img_self, rs.getString("img_self"));
//                if (rs.getString("biodata_dukcapil") == null) {
//                    result.put(FieldParameter.biodata_dukcapil, "Data Tidak Ada");
//                } else {
//                    result.put(FieldParameter.biodata_dukcapil, rs.getString("biodata_dukcapil"));
//
//                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public String getUserDetail(String userlogin) {
        Connection conn = null;
        String username = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT agent_name FROM am_user WHERE agent_id=?");
            stat.setString(1, userlogin);
            rs = stat.executeQuery();
            if (rs.next()) {
                username = rs.getString("agent_name");
            }
        } catch (SQLException e) {
            return username = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return username;
    }

    public String getUserEmail(String userlogin) {
        Connection conn = null;
        String username = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT email FROM am_user WHERE agent_id=?");
            stat.setString(1, userlogin);
            rs = stat.executeQuery();
            if (rs.next()) {
                username = rs.getString("email");
            }
        } catch (SQLException e) {
            return username = e.getMessage();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return username;
    }

    public ArrayList<Va> getAllVa() throws ParseException {
        ArrayList<Va> listVa = new ArrayList<Va>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        ResultSet rs1 = null;
        SimpleDateFormat requesttime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requesttime2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * from am_virtual_account where status = '1'");
            rs = stat.executeQuery();
            while (rs.next()) {
                Va va  = new Va();
                stat1 = conn.prepareStatement("SELECT * FROM am_user WHERE agent_id = ?");
                stat1.setString(1, rs.getString("phonenumber"));
                rs1 = stat1.executeQuery();
                while (rs1.next()) {
                    if (rs1.getString("agent_id") == null) {
                        va.setAgent_id("-");
                    } else {
                        va.setAgent_id(rs1.getString("agent_id"));
                    }
                    if (rs1.getString("agent_name") == null) {
                        va.setPhonenumber("-");
                    } else {
                        va.setPhonenumber(rs1.getString("agent_name"));
                    }
                }

                if (rs.getString("bank") == null) {
                    va.setBank("-");
                } else {
                    va.setBank(rs.getString("bank"));
                }
                if (rs.getString("no_va") == null) {
                    va.setNo_va("-");
                } else {
                    va.setNo_va("'" + rs.getString("no_va"));
                }
                if (rs.getString("aktivasi") == null) {
                    va.setAktivasi("-");
                } else {
                    Date start_date = requesttime1.parse(rs.getString("aktivasi"));
                    va.setAktivasi(requesttime2.format(start_date));
                }
                if (rs.getString("trx_id") == null) {
                    va.setTrx_id("-");
                } else {
                    va.setTrx_id(rs.getString("trx_id"));
                }
                if (rs.getString("expired") == null) {
                    va.setExpired("-");
                } else {
                    Date expired = requesttime1.parse(rs.getString("expired"));
                    va.setExpired(requesttime2.format(expired));
                }
                listVa.add(va);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
            clearStatment(stat1);
            clearResultset(rs1);
        }
        return listVa;
    }

    public ArrayList<HystoriCorp> getHystoriCorp() throws ParseException {
        ArrayList<HystoriCorp> list_hystori_corp = new ArrayList<HystoriCorp>();
        Connection conn = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat4 = null;
        PreparedStatement stat5 = null;

        ResultSet rs1 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        SimpleDateFormat requesttime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat requesttime2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat1 = conn.prepareStatement("select * from "
                    + "(select no_ba, rcinternal, fromagent, fromaccount, toaccount, proccode, requesttime, refer, noref, amount, productcode, prev_bal, curr_bal "
                    + "from tempmsg union all "
                    + "select no_ba, rcinternal, fromagent, fromaccount, toaccount,  proccode, requesttime, refer, noref, amount, productcode, prev_bal, curr_bal "
                    + "from tempmsg_backup) "
                    + "AS tempmsg where proccode IN ('400001') and fromagent IN ('sehati_coops@yahoo.co.id', 'kopkarpuspitakencana@gmail.com')");
            rs1 = stat1.executeQuery();
            while (rs1.next()) {
                HystoriCorp hystori = new HystoriCorp();
                stat4 = conn.prepareStatement("select * from trancode where trancodeid='" + rs1.getString("productcode") + "'");
                rs4 = stat4.executeQuery();
                while (rs4.next()) {
                    if (rs4.getString("trancodename") == null) {
                        hystori.setNameproduct("-");
                    } else {
                        hystori.setNameproduct(rs4.getString("trancodename"));
                    }
                }
                if (rs1.getString("proccode").equals("400001")) {
                    if (rs1.getString("fromaccount") == null) {
                        hystori.setFromaccount("-");
                    } else {
                        hystori.setFromaccount(rs1.getString("fromaccount"));
                    }
                    hystori.setToaccount(rs1.getString("toaccount"));
                } else {
                    hystori.setFromaccount("-");
                    hystori.setToaccount("-");
                }
                if (rs1.getString("noref") == null) {
                    hystori.setNoref("-");
                } else {
                    hystori.setNoref(rs1.getString("noref"));
                }
                if (rs1.getString("requesttime") == null) {
                    hystori.setRequesttime("-");
                } else {
                    Date requesttime = requesttime1.parse(rs1.getString("requesttime"));
                    hystori.setRequesttime(requesttime2.format(requesttime));
                }
                if (rs1.getString("amount") == null) {
                    hystori.setAmount("-");
                } else {
                    hystori.setAmount(rs1.getString("amount"));
                }
                if (rs1.getString("refer").equals("")) {
                    hystori.setRefer("-");
                } else if (rs1.getString("refer").equals("0")) {
                    hystori.setRefer("-");
                } else {
                    stat5 = conn.prepareStatement("select nama_koperasi from profile_corp where cu_id='" + rs1.getString("refer") + "'");
                    rs5 = stat5.executeQuery();
                    while (rs5.next()) {
                        hystori.setRefer(rs5.getString("nama_koperasi"));
                    }
                }
                if (rs1.getString("prev_bal") == null) {
                    hystori.setPrev_bal("-");
                } else {
                    hystori.setPrev_bal(rs1.getString("prev_bal"));
                }
                if (rs1.getString("no_ba") == null) {
                    hystori.setNo_ba("-");
                } else {
                    hystori.setNo_ba(rs1.getString("no_ba"));
                }
                if (rs1.getString("curr_bal") == null) {
                    hystori.setCurr_bal("-");
                } else {
                    hystori.setCurr_bal(rs1.getString("curr_bal"));
                }
                if (rs1.getString("rcinternal") == null) {
                    hystori.setStatus("-");
                } else if (rs1.getString("rcinternal").equals("0000")) {
                    hystori.setStatus("success");
                } else if (rs1.getString("rcinternal").equals("0020")) {
                    hystori.setStatus("error");
                } else if (rs1.getString("rcinternal").equals("0068")) {
                    hystori.setStatus("timeout");
                } else {
                    hystori.setStatus("error transaction");
                }
                list_hystori_corp.add(hystori);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearDBConnection(conn);
            clearStatment(stat1);
            clearStatment(stat4);
            clearStatment(stat5);
            clearResultset(rs1);
            clearResultset(rs4);
            clearResultset(rs5);
        }
        return list_hystori_corp;
    }

    public ArrayList<Warung> getAllQr() throws ParseException {
        ArrayList<Warung> listQr = new ArrayList<Warung>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * from warungqr_list where status = '1'");
            rs = stat.executeQuery();
            while (rs.next()) {
                Warung qr = new Warung();
                if (rs.getString("merchantid") == null) {
                    qr.setMerchantid("-");
                } else {
                    qr.setMerchantid(rs.getString("merchantid"));
                }
                if (rs.getString("merchantname") == null) {
                    qr.setMerchantname("-");
                } else {
                    qr.setMerchantname(rs.getString("merchantname"));
                }
                if (rs.getString("merchantbalance") == null) {
                    qr.setMerchantbalance("-");
                } else {
                    qr.setMerchantbalance(rs.getString("merchantbalance"));
                }
                if (rs.getString("biayaadmin") == null) {
                    qr.setBiayaadmin("-");
                } else {
                    qr.setBiayaadmin(rs.getString("biayaadmin"));
                }
                if (rs.getString("ppob_profit") == null) {
                    qr.setPpob_profit("-");
                } else {
                    qr.setPpob_profit(rs.getString("ppob_profit"));
                }
                if (rs.getString("ref_profit") == null) {
                    qr.setRef_profit("-");
                } else {
                    qr.setRef_profit(rs.getString("ref_profit"));
                }
                if (rs.getString("ppob_profit_noncorp") == null) {
                    qr.setPpob_profit_noncorp("-");
                } else {
                    qr.setPpob_profit_noncorp(rs.getString("ppob_profit_noncorp"));
                }
                listQr.add(qr);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return listQr;
    }

    public String addQr(Warung qr) throws ParseException {
        Connection conn = null;
        String status;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            boolean status1 = false;
            stat = conn.prepareStatement("select * from warungqr_list where merchantid=?");
            stat.setString(1, qr.getMerchantid());
            rs = stat.executeQuery();
            status1 = rs.next();
            if (status1 == true) {
                return status = "0002";
            } else {
                stat = conn.prepareStatement("INSERT INTO warungqr_list(merchantid, merchantname,  biayaadmin, ppob_profit, ref_profit, ppob_profit_noncorp) VALUES (?, ?, ?, ?, ?, ?)");
                stat.setString(1, qr.getMerchantid());
                stat.setString(2, qr.getMerchantname());
                stat.setInt(3, Integer.valueOf(qr.getBiayaadmin()));
                stat.setInt(4, Integer.valueOf(qr.getPpob_profit()));
                stat.setInt(5, Integer.valueOf(qr.getRef_profit()));
                stat.setInt(6, Integer.valueOf(qr.getPpob_profit_noncorp()));
                stat.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
            return status = "0002";
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return status = "0000";
    }

    public String updateQr(Warung qr) throws ParseException {

        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE warungqr_list SET merchantname=?, biayaadmin=?, ppob_profit=?, ref_profit=?, ppob_profit_noncorp=? WHERE merchantid=? and status=1");
            stat.setString(1, qr.getMerchantname());
//            stat.setString(2, qr.getMerchantbalance());
            stat.setInt(2, Integer.valueOf(qr.getBiayaadmin()));
            stat.setInt(3, Integer.valueOf(qr.getPpob_profit()));
            stat.setInt(4, Integer.valueOf(qr.getRef_profit()));
            stat.setInt(5, Integer.valueOf(qr.getPpob_profit_noncorp()));
            stat.setString(6, qr.getMerchantid());

            stat.executeUpdate();
            status = "0000";
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public String deleteQr(String merchantid) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE warungqr_list SET status='0' WHERE merchantid=?");
            stat.setString(1, merchantid);
            stat.executeUpdate();
            status = "0000";
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public HashMap getQrByMerchantid(String merchantid) throws ParseException {
        Connection conn = null;
        HashMap result = new HashMap();
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from warungqr_list where merchantid=? and status=1");
            stat.setString(1, merchantid);
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.merchantid, rs.getString("merchantid"));
                result.put(FieldParameter.merchantname, rs.getString("merchantname"));
                result.put(FieldParameter.merchantbalance, rs.getString("merchantbalance"));
                result.put(FieldParameter.biayaadmin, rs.getString("biayaadmin"));
                result.put(FieldParameter.ppob_profit, rs.getString("ppob_profit"));
                result.put(FieldParameter.ref_profit, rs.getString("ref_profit"));
                result.put(FieldParameter.ppob_profit_noncorp, rs.getString("ppob_profit_noncorp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getVersionUserByVersionCode(String versioncode) throws ParseException {
        Connection conn = null;
        HashMap result = new HashMap();
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from version where versioncode = ?");
            stat.setInt(1, Integer.valueOf(versioncode));
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.versioncode, rs.getString("versioncode"));
                result.put(FieldParameter.versionname, rs.getString("versionname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getVersionAgentByVersionCode(String versioncode) throws ParseException {
        Connection conn = null;
        HashMap result = new HashMap();
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from version_agent where versioncode = ?");
            stat.setInt(1, Integer.valueOf(versioncode));
            rs = stat.executeQuery();
            while (rs.next()) {
                result.put(FieldParameter.versioncode, rs.getString("versioncode"));
                result.put(FieldParameter.versionname, rs.getString("versionname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public ArrayList<Version> getUserVersion() throws ParseException {
        ArrayList<Version> ListUserVersion = new ArrayList<Version>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * from version");
            rs = stat.executeQuery();
            while (rs.next()) {
                Version vs = new Version();
                if (rs.getString("versioncode") == null) {
                    vs.setVersioncode("-");
                } else {
                    vs.setVersioncode(rs.getString("versioncode"));
                }
                if (rs.getString("versionname") == null) {
                    vs.setVersionname("-");
                } else {
                    vs.setVersionname(rs.getString("versionname"));
                }

                ListUserVersion.add(vs);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return ListUserVersion;
    }

    public ArrayList<Version> getAgentVersion() throws ParseException {
        ArrayList<Version> ListAgentVersion = new ArrayList<Version>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT * from version_agent");
            rs = stat.executeQuery();
            while (rs.next()) {
                Version vs = new Version();
                if (rs.getString("versioncode") == null) {
                    vs.setVersioncode("-");
                } else {
                    vs.setVersioncode(rs.getString("versioncode"));
                }
                if (rs.getString("versionname") == null) {
                    vs.setVersionname("-");
                } else {
                    vs.setVersionname(rs.getString("versionname"));
                }

                ListAgentVersion.add(vs);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return ListAgentVersion;
    }

    public String updateVersionUser(Version vr) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE version SET versioncode = ?, versionname = ?");
            stat.setInt(1, Integer.valueOf(vr.getVersioncode()));
            stat.setString(2, vr.getVersionname());
            stat.executeUpdate();

            status = "0000";
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public String updateVersionAgent(Version vr) {
        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE version_agent SET versioncode = ?, versionname = ?");
            stat.setInt(1, Integer.valueOf(vr.getVersioncode()));
            stat.setString(2, vr.getVersionname());
            stat.executeUpdate();

            status = "0000";
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public String activeProduct(Transactiontrancode trancode) throws ParseException {

        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE trancode SET available=? WHERE trancodeid=?");
            stat.setString(1, "1");
            stat.setString(2, trancode.getTrancodeid());

            stat.executeUpdate();
            status = "0000";
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public String deactiveProduct(Transactiontrancode trancode) throws ParseException {

        Connection conn = null;
        PreparedStatement stat = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("UPDATE trancode SET available=? WHERE trancodeid=?");
            stat.setString(1, "0");
            stat.setString(2, trancode.getTrancodeid());

            stat.executeUpdate();
            status = "0000";
        } catch (SQLException e) {
            e.printStackTrace();
            return status = e.getMessage();
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
        }
        return status;
    }

    public String PengembalianDana(pengembalian dana) {
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;
        PreparedStatement stat3 = null;
        PreparedStatement stat4 = null;
        PreparedStatement stat5 = null;

        ResultSet rs1 = null;
        ResultSet rs = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT rcinternal, fromagent, noref, amount, usr_cashback, refer, no_ba, custno FROM tempmsg where noref = ? UNION SELECT rcinternal, fromagent, noref, amount, usr_cashback, refer, no_ba, custno FROM tempmsg_backup where noref = ?");
            stat.setString(1, dana.getRrn());
            stat.setString(2, dana.getRrn());
            rs = stat.executeQuery();
            while (rs.next()) {
                stat3 = conn.prepareStatement("SELECT * from am_balance where agent_id = ?");
                stat3.setString(1, rs.getString("fromagent"));
                rs1 = stat3.executeQuery();
                while (rs1.next()) {
                    
//                    update log lama
                    stat1 = conn.prepareStatement("UPDATE tempmsg set responsecode = ?, rcinternal = ? where noref = ?;UPDATE tempmsg_backup set responsecode = ?, rcinternal = ? where noref = ?");
                    stat1.setString(1, "0001");
                    stat1.setString(2, "0001");
                    stat1.setString(3, rs.getString("noref"));
                    stat1.setString(4, "0001");
                    stat1.setString(5, "0001");
                    stat1.setString(6, rs.getString("noref"));
                    stat1.executeUpdate();

//                    update balance and poin
                    stat4 = conn.prepareStatement("UPDATE am_balance set curr_bal = ?, poin_bal = ? where agent_id = ?");
                    stat4.setDouble(1, Double.valueOf(rs1.getString("curr_bal")) + Double.valueOf(rs.getString("amount")));
                    stat4.setDouble(2, Double.valueOf(rs1.getString("poin_bal")) - Double.valueOf(rs.getString("usr_cashback")));
                    stat4.setString(3, rs.getString("fromagent"));
                    stat4.executeUpdate();
                    stat4.close();
                    
//                    update balance di biller
                    stat4 = conn.prepareStatement("UPDATE am_balance set curr_bal = ?, poin_bal = ? where agent_id = ?");
                    stat4.setDouble(1, Double.valueOf(rs1.getString("curr_bal")) + Double.valueOf(rs.getString("amount")));
                    stat4.setDouble(2, Double.valueOf(rs1.getString("poin_bal")) - Double.valueOf(rs.getString("usr_cashback")));
                    stat4.setString(3, rs.getString("fromagent"));
                    stat4.executeUpdate();

//                    create log baru
                    stat2 = conn.prepareStatement("INSERT INTO tempmsg(msgid, noref, billercode, proccode, transactionid, productcode, amount, refer, prev_bal, curr_bal, responsecode, rcinternal, no_ba, fromagent, custno, transactioncode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    String rrn = StringFunction.getCurrentDateMMDDHHMMSSSS();
                    stat2.setString(1, StringFunction.getCurrentDateYYYYMMDDHHMM() + rs.getString("fromagent") + rrn);
                    stat2.setString(2, rrn);
                    stat2.setString(3, "QMA");
                    stat2.setString(4, "400011");
                    stat2.setString(5, "400011");
                    stat2.setString(6, "400011");
                    stat2.setString(7, rs.getString("amount"));
                    stat2.setString(8, rs.getString("refer"));
                    stat2.setDouble(9, Double.valueOf(rs1.getString("curr_bal")));
                    stat2.setDouble(10, Double.valueOf(rs1.getString("curr_bal")) + Double.valueOf(rs.getString("amount")));
                    stat2.setString(11, "0000");
                    stat2.setString(12, "0000");
                    stat2.setString(13, rs.getString("no_ba"));
                    stat2.setString(14, rs.getString("fromagent"));
                    stat2.setString(15, rs.getString("custno"));
                    stat2.setString(16, rs.getString("noref"));
                    stat2.executeUpdate();

//                    hapus log di gystory poin
                    stat5 = conn.prepareStatement("DELETE from am_update_poin where noref = ?");
                    stat5.setString(1, rs.getString("noref"));
                    stat5.executeUpdate();
                    status = "0000";
                }
            }
        } catch (SQLException e) {
            status = "0001";
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
            clearStatment(stat1);
            clearStatment(stat2);
            clearStatment(stat3);
            clearStatment(stat4);
            clearStatment(stat5);
            clearResultset(rs);
            clearResultset(rs1);
        }
        return status;
    }

    public String Timeout_sukses_dibiller(timeout dana) {
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;
        PreparedStatement stat3 = null;
        PreparedStatement stat4 = null;

        ResultSet rs1 = null;
        ResultSet rs = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT rcinternal, fromagent, noref, amount, usr_cashback, refer, no_ba, custno, productcode FROM tempmsg where noref = ? UNION SELECT rcinternal, fromagent, noref, amount, usr_cashback, refer, no_ba, custno, productcode FROM tempmsg_backup where noref = ?");
            stat.setString(1, dana.getRrn());
            stat.setString(2, dana.getRrn());
            rs = stat.executeQuery();
            while (rs.next()) {
                stat3 = conn.prepareStatement("SELECT * from am_balance where agent_id = ?");
                stat3.setString(1, rs.getString("fromagent"));
                rs1 = stat3.executeQuery();
                while (rs1.next()) {
                    stat1 = conn.prepareStatement("UPDATE tempmsg set responsecode = ?, rcinternal = ? where noref = ?;UPDATE tempmsg_backup set responsecode = ?, rcinternal = ? where noref = ?");
                    stat1.setString(1, "0000");
                    stat1.setString(2, "0000");
                    stat1.setString(3, rs.getString("noref"));
                    stat1.setString(4, "0000");
                    stat1.setString(5, "0000");
                    stat1.setString(6, rs.getString("noref"));
                    stat1.executeUpdate();

//                    stat4 = conn.prepareStatement("UPDATE am_balance set poin_bal = ? where agent_id = ?");
//                    stat4.setDouble(1, Double.valueOf(rs1.getString("poin_bal")) + Double.valueOf(rs.getString("usr_cashback")));
//                    stat4.setString(2, rs.getString("fromagent"));
//                    stat4.executeUpdate();
//                    stat2 = conn.prepareStatement("INSERT INTO am_update_poin(agent_id, noref, product_code, poin, before_poin, current_poin, process_exe, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
//                    stat2.setString(1, rs.getString("fromagent"));
//                    stat2.setString(2, rs.getString("noref"));
//                    stat2.setString(3, rs.getString("productcode"));
//                    stat2.setDouble(4, Double.valueOf(rs.getString("usr_cashback")));
//                    stat2.setDouble(5, Double.valueOf(rs1.getString("poin_bal")));
//                    stat2.setDouble(6, Double.valueOf(rs1.getString("poin_bal")) + Double.valueOf(rs.getString("usr_cashback")));
//                    stat2.setBoolean(7, true);
//                    stat2.setString(8, "RECEIVE POIN");
//                    stat2.executeUpdate();
                    status = "0000";
                }
            }
        } catch (SQLException e) {
            status = "0001";
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
            clearStatment(stat1);
            clearStatment(stat2);
            clearStatment(stat3);
            clearStatment(stat4);
            clearResultset(rs);
            clearResultset(rs1);
        }
        return status;
    }

    public String Timeout_gagal_dibiller(timeoutgagal dana) {
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;
        PreparedStatement stat3 = null;
        PreparedStatement stat4 = null;

        ResultSet rs1 = null;
        ResultSet rs = null;
        String status = "0001";
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT rcinternal, fromagent, noref, amount, usr_cashback, refer, no_ba, custno FROM tempmsg where noref = ? UNION SELECT rcinternal, fromagent, noref, amount, usr_cashback, refer, no_ba, custno FROM tempmsg_backup where noref = ?");
            stat.setString(1, dana.getRrn());
            stat.setString(2, dana.getRrn());
            rs = stat.executeQuery();
            while (rs.next()) {
                stat3 = conn.prepareStatement("SELECT * from am_balance where agent_id = ?");
                stat3.setString(1, rs.getString("fromagent"));
                rs1 = stat3.executeQuery();
                while (rs1.next()) {
                    stat1 = conn.prepareStatement("UPDATE tempmsg set responsecode = ?, rcinternal = ? where noref = ?;UPDATE tempmsg_backup set responsecode = ?, rcinternal = ? where noref = ?");
                    stat1.setString(1, "0001");
                    stat1.setString(2, "0001");
                    stat1.setString(3, rs.getString("noref"));
                    stat1.setString(4, "0001");
                    stat1.setString(5, "0001");
                    stat1.setString(6, rs.getString("noref"));
                    stat1.executeUpdate();

                    stat4 = conn.prepareStatement("UPDATE am_balance set curr_bal = ? where agent_id = ?");
                    stat4.setDouble(1, Double.valueOf(rs1.getString("curr_bal")) + Double.valueOf(rs.getString("amount")));
                    stat4.setString(2, rs.getString("fromagent"));
                    stat4.executeUpdate();

                    stat2 = conn.prepareStatement("INSERT INTO tempmsg(msgid, noref, billercode, proccode, transactionid, productcode, amount, refer, prev_bal, curr_bal, responsecode, rcinternal, no_ba, fromagent, custno, transactioncode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    String rrn = StringFunction.getCurrentDateMMDDHHMMSSSS();
                    stat2.setString(1, StringFunction.getCurrentDateYYYYMMDDHHMM() + rs.getString("fromagent") + rrn);
                    stat2.setString(2, rrn);
                    stat2.setString(3, "QMA");
                    stat2.setString(4, "400011");
                    stat2.setString(5, "400011");
                    stat2.setString(6, "400011");
                    stat2.setString(7, rs.getString("amount"));
                    stat2.setString(8, rs.getString("refer"));
                    stat2.setDouble(9, Double.valueOf(rs1.getString("curr_bal")));
                    stat2.setDouble(10, Double.valueOf(rs1.getString("curr_bal")) + Double.valueOf(rs.getString("amount")));
                    stat2.setString(11, "0000");
                    stat2.setString(12, "0000");
                    stat2.setString(13, rs.getString("no_ba"));
                    stat2.setString(14, rs.getString("fromagent"));
                    stat2.setString(15, rs.getString("custno"));
                    stat2.setString(16, rs.getString("noref"));
                    stat2.executeUpdate();

                    stat4 = conn.prepareStatement("UPDATE am_balance set poin_bal = ? where agent_id = ?");
                    stat4.setDouble(1, Double.valueOf(rs1.getString("poin_bal")) - Double.valueOf(rs.getString("usr_cashback")));
                    stat4.setString(2, rs.getString("fromagent"));
                    stat4.executeUpdate();

//                    stat2 = conn.prepareStatement("INSERT INTO am_update_poin(agent_id, noref, product_code, poin, before_poin, current_poin, process_exe, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
//                    stat2.setString(1, rs.getString("fromagent"));
//                    stat2.setString(2, rs.getString("noref"));
//                    stat2.setString(3, rs.getString("productcode"));
//                    stat2.setDouble(4, Double.valueOf(rs.getString("usr_cashback")));
//                    stat2.setDouble(5, Double.valueOf(rs1.getString("poin_bal")));
//                    stat2.setDouble(6, Double.valueOf(rs1.getString("poin_bal")) + Double.valueOf(rs.getString("usr_cashback")));
//                    stat2.setBoolean(7, true);
//                    stat2.setString(8, "RECEIVE POIN");
//                    stat2.executeUpdate();
                    status = "0000";
                }
            }
        } catch (SQLException e) {
            status = "0001";
        } finally {
            clearDBConnection(conn);
            clearStatment(stat);
            clearStatment(stat1);
            clearStatment(stat2);
            clearStatment(stat3);
            clearStatment(stat4);
            clearResultset(rs);
            clearResultset(rs1);
        }
        return status;
    }

    public HashMap getOtp(String agent_id) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        result.put(FieldParameter.resp_code, "0001");
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT agent_id, otp_pure, otp_lastupdate FROM am_user WHERE agent_id = ?");
            stat.setString(1, agent_id);
            rs = stat.executeQuery();
            if (rs.next()) {
                result.put(FieldParameter.agent_id, rs.getString("agent_id"));
                result.put(FieldParameter.otp_pure, Func5.prode(rs.getString("otp_pure")));
                result.put(FieldParameter.otp_lastupdate, rs.getString("otp_lastupdate"));
                result.put(FieldParameter.resp_code, "0000");
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap getUpgrade(String agent_id, String nama_koperasi) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;
        PreparedStatement stat3 = null;

        ResultSet rs1 = null;
        HashMap result = new HashMap();
        result.put(FieldParameter.resp_code, "0001");
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("SELECT agent_id, agent_name, userlevel, reference_id FROM am_user WHERE agent_id = ?");
            stat.setString(1, agent_id);
            rs = stat.executeQuery();
            while (rs.next()) {
                if (rs.getString("userlevel").equals("0")) {
                    stat1 = conn.prepareStatement("UPDATE am_user set userlevel = ?, reference_id = ? where agent_id = ?");
                    stat1.setInt(1, 1);
                    stat1.setString(2, agent_id);
                    stat1.setString(3, agent_id);
                    stat1.executeUpdate();

                    stat2 = conn.prepareStatement("INSERT INTO profile_corp(cu_id, nama_koperasi) VALUES(?, ?)");
                    stat2.setString(1, agent_id);
                    stat2.setString(2, nama_koperasi);
                    stat2.executeUpdate();

                    stat2 = conn.prepareStatement("SELECT agent_id, agent_name, userlevel, reference_id FROM am_user WHERE agent_id = ?");
                    stat2.setString(1, agent_id);
                    rs1 = stat2.executeQuery();
                    while (rs1.next()) {
                        result.put(FieldParameter.agent_id, rs1.getString("agent_id"));
                        result.put(FieldParameter.agent_name, rs1.getString("agent_name"));
                        result.put(FieldParameter.userlevel, rs1.getString("userlevel"));
                        result.put(FieldParameter.reference_id, rs1.getString("reference_id"));
                        result.put(FieldParameter.nama_koperasi, nama_koperasi);
                        result.put(FieldParameter.resp_code, "0000");
                        result.put(FieldParameter.resp_desc, "Pendaftaran Leader Berhasil");
                    }
                } else {
                    result.put(FieldParameter.resp_code, "0002");
                    result.put(FieldParameter.resp_desc, "gagal, ID Sudah pernah terdaftar sebagai Leader");
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap toMember(String agent_id_user, String agent_id) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat1 = null;

        ResultSet rs1 = null;
        HashMap result = new HashMap();
        result.put(FieldParameter.resp_code, "0001");
        result.put(FieldParameter.resp_desc, "Gagal");
        try {
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select agent_id, agent_name, reference_id from am_user where agent_id = ?");
            stat.setString(1, agent_id_user);
            rs = stat.executeQuery();
            while (rs.next()) {
                long reff_id = rs.getLong("reference_id");
                if (reff_id == 0) {
                    stat1 = conn.prepareStatement("update am_user set reference_id = ? where agent_id = ?");
                    stat1.setString(1, agent_id);
                    stat1.setString(2, agent_id_user);
                    stat1.executeUpdate();
                    result.put(FieldParameter.agent_id_user, rs.getString("agent_id"));
                    result.put(FieldParameter.agent_name, rs.getString("agent_name"));
                    result.put(FieldParameter.agent_id, agent_id);
                    result.put(FieldParameter.resp_code, "0000");
                    result.put(FieldParameter.resp_desc, "Pendaftaran Member Berhasil");
                } else if (reff_id != 0) {
                    result.put(FieldParameter.resp_code, "0002");
                    result.put(FieldParameter.resp_desc, "Gagal, User Sudah Punya Leader");
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

}
