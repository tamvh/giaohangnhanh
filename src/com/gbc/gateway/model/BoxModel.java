/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.model;

import com.gbc.gateway.data.Box;
import com.gbc.gateway.database.MySqlFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvh
 */
public class BoxModel {

    private static BoxModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());

    public static BoxModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new BoxModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    public int insertBox(Box box) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            String accountTableName = "box";
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            queryStr = String.format("INSERT INTO %1$s (box_label, cabinet_id, opencode, attacheddata) VALUES ('%2$s', %3$d, %4$s, %5$s)",
                    accountTableName, box.getLabel(), box.getCabinet_id(), box.getOpencode(), box.getAttacheddata());
            System.out.println("Query insertBox: " + queryStr);
            int result = stmt.executeUpdate(queryStr);
            if (result > 0) {
                ret = 0;
            }
            if (ret == 0) {
                queryStr = String.format("SELECT `box_id`, `box_status`, `locktime` FROM `box` WHERE `box_label` = '%1$s' ORDER BY `box_id` DESC LIMIT 0,1", box.getLabel());
                if (stmt.execute(queryStr)) {
                    rs = stmt.getResultSet();
                    if (rs != null) {
                        if (rs.next()) {
                            box.setId(rs.getInt("box_id"));
                            box.setStatus(rs.getInt("box_status"));
                            box.setLocktime(rs.getString("locktime"));
                            System.out.println("get box information success");
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(BoxModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int deleteBoxbyBoxId(int box_id) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            queryStr = String.format("DELETE FROM `box` WHERE `box_id` = %1$d", box_id);
            System.out.println("Query deleteBoxbyBoxId: " + queryStr);
            int result = stmt.executeUpdate(queryStr);
            if (result > 0) {
                ret = 0;
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(BoxModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int deleteBoxbyBoxId(JsonArray arrItemIDDel) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            queryStr = String.format("DELETE FROM `box` WHERE `box_id` IN (%1$s)", getWhereClauseDelete(arrItemIDDel));
            System.out.println("Query deleteBoxbyBoxId: " + queryStr);
            int result = stmt.executeUpdate(queryStr);
            if (result > 0) {
                ret = 0;
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(BoxModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int delelteBoxByCabinetId(int cabinet_id) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            queryStr = String.format("DELETE FROM `box` WHERE `cabinet_id` = %1$d", cabinet_id);
            System.out.println("Query delelteBoxByCabinetId: " + queryStr);
            int result = stmt.executeUpdate(queryStr);
            if (result > 0) {
                ret = 0;
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(BoxModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int delelteBoxByListCabinetId(JsonArray arrItemIDDel) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            queryStr = String.format("DELETE FROM `box` WHERE `cabinet_id` IN (%1$s)", getWhereClauseDelete(arrItemIDDel));
            System.out.println("Query delelteBoxByCabinetId: " + queryStr);
            int result = stmt.executeUpdate(queryStr);
            if (result > 0) {
                ret = 0;
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(BoxModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int updateBoxInfo(Box box) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            queryStr = String.format("UPDATE `box` SET `box_label`='%1$s', `attacheddata`='%2$s' WHERE `box_id` = %3$d", box.getLabel(), box.getAttacheddata(), box.getId());
            System.out.println("Query updateBoxInfo: " + queryStr);
            int result = stmt.executeUpdate(queryStr);
            if (result > 0) {
                ret = 0;
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(BoxModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int updateBoxStatus(int box_id, int box_status) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            queryStr = String.format("UPDATE `box` SET `box_status`= %1$d WHERE `box_id` = %2$d", box_status, box_id);
            System.out.println("Query updateBoxStatus: " + queryStr);
            int result = stmt.executeUpdate(queryStr);
            if (result > 0) {
                ret = 0;
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(BoxModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int getListBoxbyCabinetId(List<Box> list_box_master, List<JsonObject> list_box_module, int cabinet_id) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        Connection connection_modelid = null;
        Statement stmt_modelid = null;
        ResultSet rs_modelid = null;

        try {
            String queryStr;
            String queryStr_modelid;
            String accountTableName = "box";
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            connection_modelid = MySqlFactory.getConnection();
            stmt_modelid = connection_modelid.createStatement();
            Box box;

            queryStr = String.format("SELECT `box_id`, `box_label`, `box_status`, `box_type`, `module_id`, `cabinet_id`, `opencode`, `locktime`, `attacheddata` FROM %1$s WHERE `cabinet_id` = %2$d AND `box_type` = 1", accountTableName, cabinet_id);
            System.out.println("Query getListBoxbyCabinetId: " + queryStr);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            if (rs != null) {
                while (rs.next()) {
                    box = new Box();
                    box.setId(rs.getInt("box_id"));
                    box.setLabel(rs.getString("box_label"));
                    box.setStatus(rs.getInt("box_status"));
                    box.setModule_id(rs.getInt("module_id"));
                    box.setCabinet_id(rs.getInt("cabinet_id"));
                    box.setOpencode(rs.getString("opencode"));
                    box.setLocktime(rs.getString("locktime"));
                    box.setAttacheddata(rs.getString("attacheddata"));
                    list_box_master.add(box);
                    ret = 0;
                }
            }

            if (ret == 0) {
                queryStr = String.format("SELECT DISTINCT(`module_id`) AS 'module_id' FROM %1$s WHERE `cabinet_id` = %2$d AND `box_type` = 0", accountTableName, cabinet_id);
                System.out.println("Query get model_id: " + queryStr);
                stmt.execute(queryStr);
                rs = stmt.getResultSet();
                if (rs != null) {
                    while (rs.next()) {
                        int model_id = rs.getInt("module_id");
                        if (model_id > 0) {
                            JsonObject list_model = new JsonObject();
                            list_model.addProperty("id", String.valueOf(model_id));
                            queryStr_modelid = String.format("SELECT `box_id`, `box_label`, `box_status`, `box_type`, `module_id`, `cabinet_id`, `opencode`, `locktime`, `attacheddata` FROM %1$s WHERE `cabinet_id` = %2$d AND `module_id` = %3$d", accountTableName, cabinet_id, model_id);
                            System.out.println("Query get list model_id: " + queryStr_modelid);
                            stmt_modelid.execute(queryStr_modelid);
                            rs_modelid = stmt_modelid.getResultSet();
                            List<Box> models = new ArrayList<>();
                            Box box_models;
                            if (rs_modelid != null) {
                                while (rs_modelid.next()) {
                                    box_models = new Box();
                                    box_models.setId(rs_modelid.getInt("box_id"));
                                    box_models.setLabel(rs_modelid.getString("box_label"));
                                    box_models.setStatus(rs_modelid.getInt("box_status"));
                                    box_models.setModule_id(rs_modelid.getInt("module_id"));
                                    box_models.setCabinet_id(rs_modelid.getInt("cabinet_id"));
                                    box_models.setOpencode(rs_modelid.getString("opencode"));
                                    box_models.setLocktime(rs_modelid.getString("locktime"));
                                    box_models.setAttacheddata(rs_modelid.getString("attacheddata"));
                                    models.add(box_models);   
                                }
                            }
                            list_model.add("dt", new Gson().toJsonTree(models));
                            list_box_module.add(list_model);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(BoxModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
            MySqlFactory.safeClose(rs_modelid);
            MySqlFactory.safeClose(stmt_modelid);
            MySqlFactory.safeClose(connection_modelid);
        }

        return ret;
    }

    public int getListHistoryOpenBox(List<JsonObject> list_history, int fromRecord, int total_item_per_page, String fromDate, String toDate, List<Integer> lengthList) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            String accountTableName = "history_open_box";
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();

            queryStr = String.format("SELECT COUNT(*) AS `total` FROM %s %s", accountTableName, getWhereClauseSearch(fromDate, toDate));

            stmt.execute(queryStr);
            rs = stmt.getResultSet();

            if (rs != null) {
                while (rs.next()) {
                    lengthList.add(rs.getInt("total"));
                }
                MySqlFactory.safeClose(rs);
            }

            queryStr = String.format("SELECT `box_id`, `cabinet_id`, `opencode`, `locktime`, `open_status` FROM `%1$s` %2$s  ORDER BY `locktime` DESC LIMIT %3$d,%4$d",
                    accountTableName, getWhereClauseSearch(fromDate, toDate), fromRecord, total_item_per_page);
            System.out.println("Query getListHistoryOpenBox: " + queryStr);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            JsonObject his;
            if (rs != null) {
                while (rs.next()) {
                    his = new JsonObject();
                    his.addProperty("box_id", rs.getInt("box_id"));
                    his.addProperty("cabinet_id", rs.getInt("cabinet_id"));
                    his.addProperty("opencode", rs.getString("opencode"));
                    his.addProperty("locktime", rs.getString("locktime"));
                    his.addProperty("open_status", rs.getInt("open_status"));
                    list_history.add(his);
                    ret = 0;
                }
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(BoxModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }

        return ret;
    }

    public String getWhereClauseSearch(String fromDate, String toDate) {
        StringBuilder result = new StringBuilder();
        String and = "WHERE";
        if (fromDate != null && !fromDate.isEmpty()) {
            result.append(String.format("%s (`locktime` >= '%s')", and, fromDate));
            and = " AND ";
        }
        if (toDate != null && !toDate.isEmpty()) {
            result.append(String.format("%s (`locktime` <= '%s 23:59:59')", and, toDate));
        }
        return result.toString();
    }

    public String getWhereClauseDelete(JsonArray a) {
        StringBuilder result = new StringBuilder();
        String s = ",";
        for (int i = 0; i < a.size(); i++) {
            if (i < (a.size() - 1)) {
                result.append(String.format("%d %s", a.get(i).getAsInt(), s));
            } else {
                result.append(a.get(i).getAsInt());
            }
        }
        return result.toString();
    }
}
