package com.example.demo.service;

import com.example.demo.exception.AlreadyExistsException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public void getByUsername(String username){
        User user = userRepository.findByUsername(username);
        if (user != null){
            throw new AlreadyExistsException("User with username of : " + username + " already exists");
        }
    }

    public User create(User newUser){
        getByUsername(newUser.getUsername());
        newUser.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
        return userRepository.save(newUser);
    }

    public User getById(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        if (user == null){
            throw new NotFoundException("User not found");
        }
        return user;
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public void delete(Long userId){
        userRepository.delete(getById(userId));
    }
}
