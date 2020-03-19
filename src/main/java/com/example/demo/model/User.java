package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "User")
public class User {

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    private long id;

    @NotBlank(message = "First name cant be blank")
    @Size(max = 100)
    @Indexed(unique = true)
    private String firstName;

    @NotBlank(message = "Last name cant be blank")
    @Size(max = 100)
    @Indexed(unique = true)
    private String lastName;

    @NotBlank(message = "Username cant be blank")
    @Size(max = 100)
    @Email
    @Indexed(unique = true)
    private String username;

    @NotBlank(message = "Password cant be blank")
    @Indexed(unique = true)
    private String password;

    @Override
    public String toString(){
        return "Employee [id = " + id
                + ", firstName = " + firstName
                + ", lastName = " + lastName
                + ", username = " + username
                + ", password = " + password
                + "]";
    }
}
