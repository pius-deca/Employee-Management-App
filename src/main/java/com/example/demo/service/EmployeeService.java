package com.example.demo.service;

import com.example.demo.exception.EmployeeAlreadyExistsException;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public void getEmployeeByEmailId(String emailId){
        Employee employee = employeeRepository.findByEmailId(emailId);
        if (employee != null) {
            throw new EmployeeAlreadyExistsException("Sorry, Employee already exists");
        }
    }

    public Employee create(Employee employee){
        getEmployeeByEmailId(employee.getEmailId());
        employee.setId(sequenceGeneratorService.generateSequence(Employee.SEQUENCE_NAME));
        return employeeRepository.save(employee);
    }

    public Employee getById(Long employeeId) throws Exception {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if(employee == null){
            throw new EmployeeNotFoundException("Employee not found");
        }
        return employee;
    }

    public List<Employee> getAll(){
        return employeeRepository.findAll();
    }
}
