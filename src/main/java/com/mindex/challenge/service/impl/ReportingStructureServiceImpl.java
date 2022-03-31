package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Creating reporting structure for employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        // determine the number of reports for all employees
        ReportingStructure reportingStructure = new ReportingStructure(employee);
        byte numberOfReports = determineNumberOfReports(employee);  // see function implementation below
        reportingStructure.setNumberOfReports(numberOfReports);

        return reportingStructure;
    }


    /* The `determineNumberOfReports` function first adds a single report to the total report count
     * for each (direct) report done by `employee`. Then, recursion is used to determine the number of
     * indirect reports down the tree.
     */
    private byte determineNumberOfReports(Employee employee) {

        List<Employee> employeeDirectReports = employee.getDirectReports();
        byte numberOfReports = 0;

        if (employeeDirectReports == null || employeeDirectReports.isEmpty())
            return numberOfReports;

        for (Employee employeeDirectReport : employeeDirectReports) {
            numberOfReports++;
            Employee reportedEmployee = employeeRepository.findByEmployeeId(employeeDirectReport.getEmployeeId());
            numberOfReports += determineNumberOfReports(reportedEmployee);   // recursion step
        }

        return numberOfReports;
    }
}