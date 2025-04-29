package com.zjquincy.ncu.data;

import com.google.gson.annotations.SerializedName;

public class Department {
    @SerializedName("id")
    final private int id;
    @SerializedName("name")
    final private String name;

    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%d号部门%s",id,name);
    }
}
