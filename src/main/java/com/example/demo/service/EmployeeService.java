package com.example.demo.service;

import com.example.demo.exception.AlreadyExistsException;
import com.example.demo.exception.NotFoundException;
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
            throw new AlreadyExistsException("Sorry, Employee already exists");
        }
    }

    public Employee create(Employee employee){
        getEmployeeByEmailId(employee.getEmailId());
        employee.setId(sequenceGeneratorService.generateSequence(Employee.SEQUENCE_NAME));
        return employeeRepository.save(employee);
    }

    public Employee update(Employee updateEmployee, Long employeeId){
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee != null){
            employee.setFirstName(updateEmployee.getFirstName());
            employee.setLastName(updateEmployee.getLastName());
            employee.setEmailId(updateEmployee.getEmailId());
            return employeeRepository.save(employee);
        }
        throw new NotFoundException("Employee not found");
    }

    public Employee getById(Long employeeId){
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if(employee == null){
            throw new NotFoundException("Employee not found");
        }
        return employee;
    }

    public List<Employee> getAll(){
        return employeeRepository.findAll();
    }

    public void delete(Long employeeId){
        employeeRepository.delete(getById(employeeId));
    }
}
