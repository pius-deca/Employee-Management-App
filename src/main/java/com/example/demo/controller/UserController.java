package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.payload.LoginRequest;
import com.example.demo.service.MapValidationErrorService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @GetMapping("")
    public String hello(){
        return "Hello Aremeiye Ebi";
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result){
        ResponseEntity<?> error = mapValidationErrorService.MapValidationError(result);
        if (error != null) {
            return error;
        }
        User createdUser = userService.create(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        ResponseEntity<?> error = mapValidationErrorService.MapValidationError(result);
        if (error != null) {
            return error;
        }
        ResponseEntity<?> loggedUser = userService.login(loginRequest);
        return new ResponseEntity<>(loggedUser, HttpStatus.OK);
    }

    @PostMapping("/verifyEmail/{token}")
    public ResponseEntity<?> verifyUser(@PathVariable String token){
        User user = userService.verifyEmail(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") Long userId){
        User user = userService.getById(userId);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User userToUpdate, @PathVariable(value = "id") Long userId, BindingResult result){
        ResponseEntity<?> error = mapValidationErrorService.MapValidationError(result);
        if (error != null) {
            return error;
        }
        User updatedUser = userService.update(userToUpdate, userId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long userId){
        userService.delete(userId);
        return new ResponseEntity<String>("Deleted successfully", HttpStatus.OK);
    }
}
