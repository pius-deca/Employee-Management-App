package com.example.demo.service;

import com.example.demo.exception.AlreadyExistsException;
import com.example.demo.exception.EmailNotVerifiedException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.EmailVerificationStatus;
import com.example.demo.model.Employee;
import com.example.demo.model.User;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public void getEmployeeByEmailId(String emailId){
        Employee employee = employeeRepository.findByEmailId(emailId);
        if (employee != null) {
            throw new AlreadyExistsException("Sorry, Employee with emailId of : "+emailId+" already exists");
        }
    }

    public Employee create(Employee employee, String username){
        getEmployeeByEmailId(employee.getEmailId());
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getEmailVerificationStatus().equals(EmailVerificationStatus.VERIFIED)){
            employee.setUser(user.get());
            employee.setEmployer(user.get().getUsername());
            employee.setId(sequenceGeneratorService.generateSequence(Employee.SEQUENCE_NAME));
            System.out.println(user.get().getUsername() +" === "+username);
            return employeeRepository.save(employee);
        }
        throw new EmailNotVerifiedException(username + " is not verified, therefore cannot add employees");
    }

    public Employee update(Employee updateEmployee, Long employeeId, String username){
        Employee employee = getById(employeeId, username);
        employee.setFirstName(updateEmployee.getFirstName());
        employee.setLastName(updateEmployee.getLastName());
        employee.setEmailId(updateEmployee.getEmailId());
        return employeeRepository.save(employee);
    }

    public Employee getById(Long employeeId, String username){
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if(employee == null){
            throw new NotFoundException("Employee not found");
        }
        if (!employee.getEmployer().equals(username)){
            throw new NotFoundException(username + " does not have employee of id : " + employeeId);
        }
        return employee;
    }

    public List<Employee> getAll(String username){
        return employeeRepository.findAllByEmployer(username);
    }

    public void delete(Long employeeId, String username){
        employeeRepository.delete(getById(employeeId, username));
    }
}
