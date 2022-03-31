package com.mindex.challenge.data;

public class ReportingStructure {

    // fields
    private Employee employee;
    private byte numberOfReports;

    // default constructor (unused in project, but needed for testing)
    public ReportingStructure() {}

    // one-arg constructor
    public ReportingStructure(Employee employee) {
        this.employee = employee;
    }

    // getters
    public Employee getEmployee() { return this.employee; }
    public byte getNumberOfReports() {
        return numberOfReports;
    }

    // setters
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public void setNumberOfReports(byte numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}