package com.zjquincy.ncu.data;

import com.google.gson.annotations.SerializedName;
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
}
