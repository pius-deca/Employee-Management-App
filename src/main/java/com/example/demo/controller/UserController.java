package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.MapValidationErrorService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;dd

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") Long userId){
        User user = userService.getById(userId);
        return ResponseEntity.ok().body(user);
    }
}
