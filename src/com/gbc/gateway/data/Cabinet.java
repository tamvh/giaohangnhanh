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
public class Cabinet {
    private int id;
    private String name;
    private long nlat;
    private long nlong;

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
