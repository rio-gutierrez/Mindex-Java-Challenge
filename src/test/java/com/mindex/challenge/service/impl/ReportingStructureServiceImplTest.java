package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String reportingStructureUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureUrl = "http://localhost:" + port + "/{id}/reports";
    }

    // test done for Ringo Starr; any other would do just as well
    @Test
    public void testRead() {

        Employee ringoStarr = employeeService.read("03aa1462-ffa9-4978-901b-7c001562cf6f");
        ReportingStructure testReportingStructure = new ReportingStructure(ringoStarr);

        // Read checks
        ReportingStructure readReportingStructure = restTemplate.getForEntity(reportingStructureUrl,
                                                                              ReportingStructure.class,
                                                                              ringoStarr.getEmployeeId()
                                                                             ).getBody();
        assertNotNull(readReportingStructure);
        assertEmployeeEquivalence(readReportingStructure.getEmployee(), testReportingStructure.getEmployee());
        assertEquals(2, readReportingStructure.getNumberOfReports());

    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}