package de.hbrs.ia.controller;

import de.hbrs.ia.model.SalesMan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/")
@ComponentScan
public class OrangeHRMRestControllerImpl {

    @Value("${orangeHRM.baseUrl}") // Define this property in your application.properties or application.yml
    private String orangeHRMBaseUrl;

    @GetMapping("/{id}")
    public ResponseEntity<SalesMan> getEmployeeById(@PathVariable("id") Long id) {
        String apiEndpoint = orangeHRMBaseUrl + "/api/v1/employee/" + id;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SalesMan> response = restTemplate.getForEntity(apiEndpoint, SalesMan.class);

        SalesMan employee = response.getBody();
        SalesMan salesMan = new SalesMan(employee.getFirstname(), employee.getLastname(), employee.getSid());

        return new ResponseEntity<>(salesMan, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<SalesMan>> getAllEmployeeSummaries() {
        String apiEndpoint = orangeHRMBaseUrl + "/api/v1/employee";

        RestTemplate restTemplate = new RestTemplate();

        // Use RestTemplate to make the HTTP GET request
        ResponseEntity<List<SalesMan>> response = restTemplate.exchange(
                apiEndpoint,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // Transform the response into EmployeeSummary objects
        List<SalesMan> employeeSummaries = response.getBody()
                .stream()
                .map(employee -> new SalesMan(employee.getFirstname(), employee.getLastname(),employee.getSid()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(employeeSummaries, HttpStatus.OK);
    }
}










