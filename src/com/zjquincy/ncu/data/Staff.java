package com.zjquincy.ncu.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Staff {
    @SerializedName("id")
    final private int id;
    @SerializedName("name")
    final private String name;
    @SerializedName("gender")
    final private String gender;
    @SerializedName("birthday")
    final private Date birthday;
    @SerializedName("telephone")
    final private String telephone;
    @SerializedName("department")
    final private int department;
    @SerializedName("position")
    final private String position;
    @SerializedName("salary")
    final private double salary;

    public Staff(int id, String name, String gender, Date birthday, String telephone, int department, String position, double salary) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.telephone = telephone;
        this.department = department;
        this.position = position;
        this.salary = salary;
    }

    @Override
    public boolean equals(Object obj) {
        return id == ((Staff) obj).id;
    }

    @Override
    public String toString() {
        return String.format("%d号员工%s，%s，出生于%s，联系电话为%s，是%d%s，月薪%f",
                id, name, gender, birthday, telephone, department, position, salary);
    }
}
