# Coding Challenge
## What's Provided
A simple [Spring Boot](https://projects.spring.io/spring-boot/) web application has been created and bootstrapped 
with data. The application contains information about all employees at a company. On application start-up, an in-memory 
Mongo database is bootstrapped with a serialized snapshot of the database. While the application runs, the data may be
accessed and mutated in the database without impacting the snapshot.

### How to Run
The application may be executed by running `./gradlew bootRun` (note that `gradlew` may need to be given permission,
e.g., via `chmod +x ./gradlew`).

### How to Use
The following endpoints are available to use:
```
* CREATE
    * HTTP Method: POST 
    * URL: localhost:8080/employee
    * PAYLOAD: Employee
    * RESPONSE: Employee
* READ
    * HTTP Method: GET 
    * URL: localhost:8080/employee/{id}
    * RESPONSE: Employee
* UPDATE
    * HTTP Method: PUT 
    * URL: localhost:8080/employee/{id}
    * PAYLOAD: Employee
    * RESPONSE: Employee
```
The Employee has a JSON schema of:
```json
{
  "type":"Employee",
  "properties": {
    "employeeId": {
      "type": "string"
    },
    "firstName": {
      "type": "string"
    },
    "lastName": {
          "type": "string"
    },
    "position": {
          "type": "string"
    },
    "department": {
          "type": "string"
    },
    "directReports": {
      "type": "array",
      "items" : "string"
    }
  }
}
```
For all endpoints that require an "id" in the URL, this is the "employeeId" field.

## What to Implement
Clone or download the repository, do not fork it.

### Task 1
Create a new type, ReportingStructure, that has two properties: employee and numberOfReports.

For the field "numberOfReports", this should equal the total number of reports under a given employee. The number of 
reports is determined to be the number of directReports for an employee and all of their distinct reports. For example, 
given the following employee structure:
```
                    John Lennon
                   /            \
         Paul McCartney       Ringo Starr
                               /        \
                          Pete Best     George Harrison
```
The numberOfReports for employee John Lennon (employeeId: 16a596ae-edd3-4847-99fe-c4518e82c86f) would be equal to 4. 

This new type should have a new REST endpoint created for it. This new endpoint should accept an employeeId and return 
the fully filled out ReportingStructure for the specified employeeId. The values should be computed on the fly and will 
not be persisted.

### Task 2
Create a new type, Compensation. A Compensation has the following fields: employee, salary, and effectiveDate. Create 
two new Compensation REST endpoints. One to create and one to read by employeeId. These should persist and query the 
Compensation from the persistence layer.

## Delivery
Please upload your results to a publicly accessible Git repo. Free ones are provided by GitHub and Bitbucket.


-------------------------------------------------------------------------------
-------------------------------------------------------------------------------

# Solution


In this section we describe our approach to the solution, step by step, one task at a time.

## Task 1

1. We first create the new class [ReportingStructure](src/main/java/com/mindex/challenge/data/ReportingStructure.java) 
with the `employee` and `numberOfReports` fields, as requested.

2. We then create the 
[ReportingStructureController](src/main/java/com/mindex/challenge/controller/ReportingStructureController.java) class, 
from which we get the endpoint
    ```
    * READ
        * HTTP Method: GET 
        * URL: localhost:8080/{id}/reports
        * RESPONSE: Employee
    ```
   
3. In the above constructed `ReportingStructureController` class, we make use of `ReportingStructureService`, which is
declared as an interface in 
[ReportingStructureService](src/main/java/com/mindex/challenge/service/ReportingStructureService.java), and 
implemented in
[ReportingStructureServiceImpl](src/main/java/com/mindex/challenge/service/impl/ReportingStructureServiceImpl.java). 
It is here where we calculate the total number of (direct and indirect) reports done by the `employee` 
with `"employeeId" : {id}`.

4. Lastly, we test our implementation in 
[ReportingStructureServiceImplTest](src/test/java/com/mindex/challenge/service/impl/ReportingStructureServiceImplTest.java).
The subject chosen for the test is`Ringo Starr`, though any other `employee` would do. 


We can now see the number of reports by each employee either via a browser or a terminal. For instance, for 
`John Lennon`, we can either paste on the browser
```
localhost:8080/16a596ae-edd3-4847-99fe-c4518e82c86f/reports
```
or on the command line
```bash
$ curl -X GET localhost:8080/16a596ae-edd3-4847-99fe-c4518e82c86f/reports
```
Either way we get the following `json` data:
```json
{
  "employee": {
    "employeeId": "16a596ae-edd3-4847-99fe-c4518e82c86f",
    "firstName": "John","lastName":"Lennon",
    "position": "Development Manager",
    "department": "Engineering",
    "directReports": [
      {
        "employeeId":"b7839309-3348-463b-a7e3-5de1c168beb3",
        "firstName":null,
        "lastName":null,
        "position":null,
        "department":null,
        "directReports":null
      },
      {
        "employeeId":"03aa1462-ffa9-4978-901b-7c001562cf6f",
        "firstName":null,
        "lastName":null,
        "position":null,
        "department":null,
        "directReports":null
      }
    ]
  },
  "numberOfReports":4
}
```

Moreover, if the only output we desire is just the number of reports itself, we can use a tool such as 
[jq](https://stedolan.github.io/jq/) to parse `json` data; thus typing on the terminal
   ```bash
   $ curl -X GET localhost:8080/16a596ae-edd3-4847-99fe-c4518e82c86f/reports | jq -r .numberOfReports
   ```

yields the output `4`, as expected. 



-------------------------------------------------------------------------------


## Task 2

1. We start off by creating the [Compensation](src/main/java/com/mindex/challenge/data/Compensation.java) class
with the relevant fields and methods.

2. We then create the
[CompensationController](src/main/java/com/mindex/challenge/controller/CompensationController.java) class, from 
which we get the with the following endpoints:
   ```
   * CREATE
       * HTTP Method: POST 
       * URL: localhost:8080/compensation
       * PAYLOAD: Employee
       * RESPONSE: Employee
   * READ
       * HTTP Method: GET 
       * URL: localhost:8080/{id}/compensation
       * RESPONSE: Employee
   ```

3. In the above constructed `CompensationController` class, we make use of `CompensationService`, which is
declared as an interface in
[CompensationService](src/main/java/com/mindex/challenge/service/CompensationService.java) and implemented in
[CompensationServiceImpl](src/main/java/com/mindex/challenge/service/impl/CompensationServiceImpl.java). Here we 
create and read salary entries from 
[CompensationRepository](src/main/java/com/mindex/challenge/dao/CompensationRepository.java). 

4. Lastly, we test our implementations in 
[CompensationServiceImplTest](src/test/java/com/mindex/challenge/service/impl/CompensationServiceImplTest.java).
The subject chosen for the test this time is`Pete Best`, though, again, any other `employee` would do.


We can now set and read salary entries for any `employee`. For instance, we may set the salary for `Paul McCartney`
to be `$500,000` as follows:
```bash
$ curl -X POST localhost:8080/compensation -H 'Content-type:application/json' -d '{"employeeId" : "b7839309-3348-463b-a7e3-5de1c168beb3", "effectiveDate" : "2022-03-30T19:41:01.352+0000", "salary" : 500000.00}'
```
We then may then read the data by typing
```bash
$ curl -X GET localhost:8080/b7839309-3348-463b-a7e3-5de1c168beb3/compensation
```

The output is then
```json
{
   "employeeId" : "b7839309-3348-463b-a7e3-5de1c168beb3", 
   "effectiveDate" : "2022-03-30T19:41:01.352+0000",
   "salary" : 500000.00
}
```