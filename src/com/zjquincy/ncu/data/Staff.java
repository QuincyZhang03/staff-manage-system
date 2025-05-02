package com.zjquincy.ncu.data;

import com.google.gson.annotations.SerializedName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Objects;

public class Staff implements IData {
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

    @Override
    public int create(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO " +
                "staff(id,name,gender,birthday,telephone,department,position,salary) VALUES(?,?,?,?,?,?,?,?)");
        statement.setInt(1, id);
        statement.setString(2, name);
        statement.setString(3, gender);
        statement.setDate(4, birthday);
        statement.setString(5, telephone);
        statement.setInt(6, department);
        statement.setString(7, position);
        statement.setDouble(8, salary);
        int result = statement.executeUpdate();
        statement.close();
        return result;
    }

    @Override
    public int delete(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM staff WHERE id=?");
        statement.setInt(1, id);
        int result = statement.executeUpdate();
        statement.close();
        return result;
    }

    @Override
    public int update(Connection connection, IData to) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE staff SET " +
                "id=?,name=?,gender=?,birthday=?,telephone=?,department=?,position=?,salary=? WHERE id=?");
        Staff after = (Staff) to;
        statement.setInt(1, after.id);
        statement.setString(2, after.name);
        statement.setString(3, after.gender);
        statement.setDate(4, after.birthday);
        statement.setString(5, after.telephone);
        statement.setInt(6, after.department);
        statement.setString(7, after.position);
        statement.setDouble(8, after.salary);
        statement.setInt(9, id);//原编号
        int result = statement.executeUpdate();
        statement.close();
        return result;
    }

    @Override
    public String toString() {
        return String.format("员工{%d,%s,%s,%s,%s,%d,%s,%f}", id, name, gender, birthday, telephone, department, position, salary);
    }
}
