package com.zjquincy.ncu.data;

import com.google.gson.annotations.SerializedName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class Department implements IData {
    @SerializedName("id")
    final private int id;
    @SerializedName("name")
    final private String name;

    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }


    @Override
    public int create(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO department(id,name) VALUES(?,?)");
        statement.setInt(1, id);
        statement.setString(2, name);
        int result = statement.executeUpdate();
        statement.close();
        return result;
    }

    @Override
    public int delete(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM department WHERE id=?");
        statement.setInt(1, id);
        int result = statement.executeUpdate();
        statement.close();
        return result;
    }

    @Override
    public int update(Connection connection, IData to) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE department SET id=?,name=? WHERE id=?");
        Department after = (Department) to;
        statement.setInt(1, after.id);
        statement.setString(2, after.name);
        statement.setInt(3, id);//原编号
        int result = statement.executeUpdate();
        statement.close();
        return result;
    }

    @Override
    public String toString() {
        return String.format("部门{%d,%s}", id, name);
    }
}
