package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.MapValidationErrorService;
import com.example.demo.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping
    public ResponseEntity<?> createEmployee(@Valid @RequestBody Employee employee, BindingResult result, Principal principal){
        ResponseEntity<?> error = mapValidationErrorService.MapValidationError(result);
        if (error != null) {
            return error;
        }
        Employee createdEmployee =  employeeService.create(employee, principal.getName());
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<Employee> getAllEmployees(Principal principal){
        return employeeService.getAll(principal.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId, Principal principal){
        Employee employee = employeeService.getById(employeeId, principal.getName());
        return ResponseEntity.ok().body(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable(value = "id") Long employeeId, Principal principal){
        employeeService.delete(employeeId, principal.getName());
        return ResponseEntity.ok().body("Employee deleted successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@Valid @RequestBody Employee employee, @PathVariable(value = "id") Long employeeId, BindingResult result, Principal principal){
        ResponseEntity<?> error = mapValidationErrorService.MapValidationError(result);
        if (error != null){
            return error;
        }
        Employee updatedEmployee = employeeService.update(employee, employeeId, principal.getName());
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

}
