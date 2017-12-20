/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.model;

import com.gbc.gateway.data.Box;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import com.gbc.gateway.data.Cabinet;
import com.gbc.gateway.database.MySqlFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    
    public int insertCabinet(Cabinet cabinet) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            String accountTableName = "cabinet";
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            queryStr = String.format("INSERT INTO %1$s (cabinet_name, nlat, nlong, address) VALUES ('%2$s', %3$d, %4$d, '%5$s')",
                    accountTableName, cabinet.getName().trim(), cabinet.getNlat(), cabinet.getNlong(), cabinet.getAddress().trim());
            System.out.println("Query insertCabinet: " + queryStr);
            int result = stmt.executeUpdate(queryStr);
            if(result > 0) {
                ret = 0;
            }
            if(ret == 0) {
                queryStr = String.format("SELECT `cabinet_id` FROM `cabinet` WHERE `cabinet_name` = '%1$s' ORDER BY `cabinet_id` DESC LIMIT 0,1", cabinet.getName());
                if (stmt.execute(queryStr)) {
                    rs = stmt.getResultSet();
                    if (rs != null) {
                         if (rs.next()) {
                            cabinet.setId(rs.getInt("cabinet_id"));
                            System.out.println("get cabinet id success");
                         } 
                    }
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
    
    public int deleteCabinetById(int cabinet_id) throws IOException {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            queryStr = String.format("DELETE FROM `cabinet` WHERE `cabinet_id` = %1$d", cabinet_id);
            System.out.println("Query deleteCabinetById: " + queryStr);
            int result = stmt.executeUpdate(queryStr);
            if(result > 0) {
                BoxModel.getInstance().delelteBoxByCabinetId(cabinet_id);
                ret = 0;
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
    
    public int editCabinet(Cabinet cabinet) throws IOException {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            queryStr = String.format("UPDATE `cabinet` SET `cabinet_name` = '%1$s', `nlat` = %2$d, `nlong` = %3$d, `address` = '%4$s' WHERE `cabinet_id` = %5$d", 
                    cabinet.getName(),
                    cabinet.getNlat(),
                    cabinet.getNlong(),
                    cabinet.getAddress(),
                    cabinet.getId());
            System.out.println("Query editCabinet: " + queryStr);
            int result = stmt.executeUpdate(queryStr);
            if(result > 0) {
                System.out.println("update cabinet success");
                ret = 0;
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
    
    
    public int deleteCabinetByListId(JsonArray arrItemIDDel) throws IOException {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            queryStr = String.format("DELETE FROM `cabinet` WHERE `cabinet_id` IN (%1$s)", getWhereClauseDelete(arrItemIDDel));
            System.out.println("Query deleteCabinetById: " + queryStr);
            int result = stmt.executeUpdate(queryStr);
            if(result > 0) {
                BoxModel.getInstance().delelteBoxByListCabinetId(arrItemIDDel);
                ret = 0;
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
    
    
    public int getListCabinet(List<Cabinet> list_cabinet) throws IOException {
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
            queryStr = String.format("SELECT `cabinet_id`, `cabinet_name`, `nlat`, `nlong`, `address` FROM %1$s", accountTableName);
            System.out.println("Query getListCabinet: " + queryStr);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            List<Box> list_box;
            if (rs != null) {
                while (rs.next()) {
                    cb = new Cabinet();
                    list_box = new ArrayList<>();
                    List<Box> list_box_master = new ArrayList<>();
                    List<JsonObject> list_box_module = new ArrayList<>();
                    int cabinet_id = rs.getInt("cabinet_id");
                    BoxModel.getInstance().getListBoxbyCabinetId(list_box_master, list_box_module, cabinet_id);
                    cb.setId(rs.getInt("cabinet_id"));
                    cb.setName(rs.getString("cabinet_name"));
                    cb.setNlat(rs.getLong("nlat"));
                    cb.setNlong(rs.getLong("nlong"));
                    cb.setAddress(rs.getString("address"));
                    cb.setList_box(list_box);
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
    
    public String getWhereClauseDelete(JsonArray a){
        StringBuilder result = new StringBuilder();
        String s = ",";
        for(int i = 0; i < a.size(); i++){
            if(i < (a.size() - 1)){
                result.append(String.format("%d %s", a.get(i).getAsInt(), s));
            } else 
                result.append(a.get(i).getAsInt());
        }        
        return result.toString();
    }
    
}
