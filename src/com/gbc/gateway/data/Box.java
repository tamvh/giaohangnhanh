/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.data;

/**
 *
 * @author tamvh
 */
public class Box {
    private int id;
    private String label;
    private int status;
    private int box_type;
    private int module_id;
    private int cabinet_id;
    private String opencode;
    private String locktime;
    private String attacheddata;
    
    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }
    
    public int getBox_type() {
        return box_type;
    }

    public void setBox_type(int box_type) {
        this.box_type = box_type;
    }
    
    public int getCabinet_id() {
        return cabinet_id;
    }

    public void setCabinet_id(int cabinet_id) {
        this.cabinet_id = cabinet_id;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOpencode() {
        return opencode;
    }

    public void setOpencode(String opencode) {
        this.opencode = opencode;
    }

    public String getLocktime() {
        return locktime;
    }

    public void setLocktime(String locktime) {
        this.locktime = locktime;
    }

    public String getAttacheddata() {
        return attacheddata;
    }

    public void setAttacheddata(String attacheddata) {
        this.attacheddata = attacheddata;
    }
    
}
