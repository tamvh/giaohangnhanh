/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.data;

import java.util.List;

/**
 *
 * @author tamvh
 */
public class Cabinet {
    private int id;
    private String name;
    private long nlat;
    private long nlong;
    private String address;
    private List<Box> list_box;

    public List<Box> getList_box() {
        return list_box;
    }

    public void setList_box(List<Box> list_box) {
        this.list_box = list_box;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNlat() {
        return nlat;
    }

    public void setNlat(long nlat) {
        this.nlat = nlat;
    }

    public long getNlong() {
        return nlong;
    }

    public void setNlong(long nlong) {
        this.nlong = nlong;
    }
}
