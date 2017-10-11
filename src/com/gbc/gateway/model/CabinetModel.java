/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.model;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import com.gbc.gateway.data.Cabinet;
import com.gbc.gateway.database.MySqlFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author tamvh
 */
public class CabinetModel {
    private static CabinetModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());

    public static CabinetModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new CabinetModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
    
    public int getListCabinet(List<Cabinet> list_cabinet) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            String accountTableName = "cabinet";
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            Cabinet cb;
            queryStr = String.format("SELECT `cabinet_id`, `cabinet_name`, `nlat`, `nlong` FROM %1$s", accountTableName);
            System.out.println("Query getListCabinet: " + queryStr);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            if (rs != null) {
                while (rs.next()) {
                    cb = new Cabinet();
                    cb.setId(rs.getInt("cabinet_id"));
                    cb.setName(rs.getString("cabinet_name"));
                    cb.setNlat(rs.getLong("nlat"));
                    cb.setNlong(rs.getLong("nlong"));
                    list_cabinet.add(cb);
                    ret = 0;
                }
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(CabinetModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        
        return ret;
    }
    
}