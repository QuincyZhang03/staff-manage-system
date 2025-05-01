package com.zjquincy.ncu.data;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.Objects;

public class Staff implements IData{
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
    public int hashCode() {
        return Objects.hash(id, name, gender, birthday, telephone, department, position, salary);
    }
}
