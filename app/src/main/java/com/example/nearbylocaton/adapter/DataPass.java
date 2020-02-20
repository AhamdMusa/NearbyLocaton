package com.example.nearbylocaton.adapter;

import java.io.Serializable;

public class DataPass implements Serializable {
    private String name, phone;

    public DataPass(String name) {
        this.name = name;
    }

    public DataPass() {
    }



    public DataPass(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
