package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;
import com.zjquincy.ncu.access.User;
import com.zjquincy.ncu.data.Department;
import com.zjquincy.ncu.data.Staff;

import java.util.ArrayList;

/*
 * 查询响应格式：
 * String response_type = "QUERY"
 * boolean isSuccess 是否成功
 * Json staff 成功返回职工List，失败无该字段
 * Json department 成功返回部门List，失败无该字段
 * Json user 成功且身份为管理员返回用户List，失败或普通用户无该字段
 * */
public class QueryResponse extends AbstractResponse {
    @SerializedName("isSuccess")
    private boolean isSuccess;
    @SerializedName("staff")
    private ArrayList<Staff> staff;
    @SerializedName("department")
    private ArrayList<Department> department;
    @SerializedName("user")
    private ArrayList<User> user;

    private QueryResponse() {
        response_type = "QUERY";
    }

    public QueryResponse(boolean isSuccess) {
        this();
        this.isSuccess = isSuccess;
    }

    @Override
    public String getLogMessage() {
        return isSuccess + "";
    }

    public QueryResponse(ArrayList<Staff> staffList, ArrayList<Department> departmentList, ArrayList<User> user) {
        this();
        isSuccess = !(staffList.isEmpty() || departmentList.isEmpty());//有一个集合为空则查询失败
        staff = isSuccess ? staffList : null;
        department = isSuccess ? departmentList : null;
        this.user = user;
    }
}
