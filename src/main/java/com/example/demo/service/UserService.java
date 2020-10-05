package com.example.demo.service;

import com.example.demo.exception.AlreadyExistsException;
import com.example.demo.exception.EmailNotVerifiedException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.EmailVerificationStatus;
import com.example.demo.model.User;
import com.example.demo.payload.LoginRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.LoginResponse;
import com.example.demo.security.JwtProvider;
import com.example.demo.utils.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.demo.security.SecurityConstants.PORT;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()){
            throw new AlreadyExistsException("User with username of : " + username + " already exists");
        }
        return null;
    }

    public User create(User newUser){
        getByUsername(newUser.getUsername());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));

        String token = jwtProvider.generateToken(newUser.getUsername());
        newUser.setEmailVerificationToken(token);

        String url = "http://localhost:" + PORT + "/api/v1/users/verifyEmail/" + token;
        String message = "Hello, "+ newUser.getFirstName() + " "+ newUser.getLastName() +", \n"+
                "Click this link below to verify your email \n" +
                url;
        emailSender.sendMail(newUser.getUsername(), "Testing Project email verification", message);
        return userRepository.save(newUser);
    }

    public User verifyEmail(String token){
        User user = userRepository.findByEmailVerificationToken(token);
        if (user == null){
           throw new EmailNotVerifiedException("Email is not verified, please register with a valid email");
        }
        user.setEmailVerificationStatus(EmailVerificationStatus.VERIFIED);
        return userRepository.save(user);
    }

    public ResponseEntity<?> login(LoginRequest loginRequest){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User myUserDetails = (User) authentication.getPrincipal();
        String token = jwtProvider.generateToken(username);
        if (myUserDetails.getEmailVerificationStatus().equals(EmailVerificationStatus.VERIFIED)){
            return ResponseEntity.ok().body(new LoginResponse("Bearer " + token, myUserDetails.getId(), myUserDetails.getUsername(), myUserDetails.getFirstName(), myUserDetails.getLastName()));
        }
        throw new NotFoundException("Email is not verified");
    }

    public User getById(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        if (user == null){
            throw new NotFoundException("User not found");
        }
        return user;
    }

    public User update(User updateUser, Long userId){
        User user = getById(userId);
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setUsername(updateUser.getUsername());
        user.setPassword(updateUser.getPassword());
        return userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public void delete(Long userId){
        userRepository.delete(getById(userId));
    }
}
