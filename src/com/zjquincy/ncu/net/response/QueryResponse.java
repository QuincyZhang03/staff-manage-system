package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;
import com.zjquincy.ncu.data.Department;
import com.zjquincy.ncu.data.Staff;

import java.util.ArrayList;

/*
 * 查询响应格式：
 * String response_type = "query"
 * boolean isSuccess 是否成功
 * Json staff 成功返回职工List，失败无该字段
 * Json department 成功返回部门List，失败无该字段
 * */
public class QueryResponse extends AbstractResponse {
    @SerializedName("isSuccess")
    private boolean isSuccess;
    @SerializedName("staff")
    private ArrayList<Staff> staff;
    @SerializedName("department")
    private ArrayList<Department> department;

    public QueryResponse(boolean isSuccess) {
        response_type = "query";
        this.isSuccess = isSuccess;
    }

    public QueryResponse(ArrayList<Staff> staffList, ArrayList<Department> departmentList) {
        isSuccess = !(staffList.isEmpty() || departmentList.isEmpty());//有一个集合为空则查询失败
        staff = isSuccess ? staffList : null;
        department = isSuccess ? departmentList : null;
    }
}
