package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private EmployeeRepository employeeRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/{id}/compensation";
    }

    // test done for Pete Best; any other would do just as well
    @Test
    public void testCreateRead() {
        Compensation testCompensation = new Compensation();
        Employee peteBest = employeeRepository.findByEmployeeId("62c1084e-6e34-4630-93fd-9153afb65309");
        testCompensation.setEmployeeId(peteBest.getEmployeeId());
        testCompensation.setSalary(1_000_000.00);
        testCompensation.setEffectiveDate(new Date());

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl,
                                                                      testCompensation,
                                                                      Compensation.class
                                                                     ).getBody();
        assertNotNull(createdCompensation);
        assertCompensationEquivalence(testCompensation, createdCompensation);

        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl,
                                                                  Compensation.class,
                                                                  peteBest.getEmployeeId()
                                                                 ).getBody();
        assertNotNull(readCompensation);
        assertCompensationEquivalence(readCompensation, createdCompensation);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary(), 0.0);
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}