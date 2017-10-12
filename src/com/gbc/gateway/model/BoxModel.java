/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.model;

import com.gbc.gateway.data.Box;
import com.gbc.gateway.database.MySqlFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            if(result > 0) {
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
            if(result > 0) {
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
            if(result > 0) {
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
            if(result > 0) {
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
    
    public int getListBoxbyCabinetId(List<Box> list_box, int cabinet_id) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            String accountTableName = "box";
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            Box box;
            queryStr = String.format("SELECT `box_id`, `box_label`, `box_status`, `cabinet_id`, `opencode`, `locktime`, `attacheddata` FROM %1$s WHERE `cabinet_id` = %2$d", accountTableName, cabinet_id);
            System.out.println("Query getListBoxbyCabinetId: " + queryStr);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            if (rs != null) {
                while (rs.next()) {
                    box = new Box();
                    box.setId(rs.getInt("box_id"));
                    box.setLabel(rs.getString("box_label"));
                    box.setStatus(rs.getInt("box_status"));
                    box.setCabinet_id(rs.getInt("cabinet_id"));
                    box.setOpencode(rs.getString("opencode"));
                    box.setLocktime(rs.getString("locktime"));
                    box.setAttacheddata(rs.getString("attacheddata"));
                    list_box.add(box);
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
}
