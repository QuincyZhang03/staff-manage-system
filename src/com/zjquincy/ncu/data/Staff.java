package com.zjquincy.ncu.data;

import java.util.Date;

public class Staff {
    final private int id;
    final private String name;
    final private char gender;
    final private Date birthday;
    final private String telephone;
    final private String department;
    final private String position;
    final private double salary;

    public Staff(int id, String name, char gender, Date birthday, String telephone, String department, String position, double salary) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.telephone = telephone;
        this.department = department;
        this.position = position;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public char getGender() {
        return gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public boolean equals(Object obj) {
        return id == ((Staff) obj).id;
    }

    @Override
    public String toString() {
        return String.format("%d号员工%s，%c，出生于%s，联系电话为%s，是%s%s，月薪%f",
                id, name, gender, birthday, telephone, department, position, salary);
    }
}
