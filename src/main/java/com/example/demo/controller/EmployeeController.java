package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody Employee employee){
        return employeeService.create(employee);
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeService.getAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId){
        Employee employee = employeeService.getById(employeeId);
        return ResponseEntity.ok().body(employee);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable(value = "id") Long employeeId){
        employeeService.delete(employeeId);
        return ResponseEntity.ok().body("Employee deleted successfully");
    }

    @PatchMapping("/employees/{id}")
    public Employee updateEmployee(@Valid @RequestBody Employee employee, @PathVariable(value = "id") Long employeeId){
        return employeeService.update(employee, employeeId);
    }

}
