package com.mindex.challenge.data;

import java.util.Date;

public class Compensation {

    // fields
    private String employeeId;
    private Date effectiveDate;
    private double salary;

    // getters
    public String getEmployeeId() { return employeeId; }
    public Date getEffectiveDate() {
        return effectiveDate;
    }
    public double getSalary() {
        return salary;
    }

    // setters
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
}