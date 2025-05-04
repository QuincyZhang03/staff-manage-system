package com.zjquincy.ncu.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

public interface IData extends Serializable {
    int create(Connection connection) throws SQLException;
    int delete(Connection connection) throws SQLException;
    int update(Connection connection,IData to) throws SQLException;
}
