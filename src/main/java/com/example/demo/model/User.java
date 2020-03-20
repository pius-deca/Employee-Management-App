package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "Users")
public class User implements UserDetails {

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    private long id;

    @NotBlank(message = "First name cant be blank")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name cant be blank")
    @Size(max = 100)
    private String lastName;

    @NotBlank(message = "Username cant be blank")
    @Size(max = 100)
    @Email
    @Indexed(unique = true)
    private String username;

    @NotBlank(message = "Password cant be blank")
    @Indexed(unique = true)
    private String password;

    @JsonIgnore
    private String emailVerificationToken;

    private EmailVerificationStatus emailVerificationStatus = EmailVerificationStatus.UNVERIFIED;

    @Override
    public String toString(){
        return "Employee [id = " + id
                + ", firstName = " + firstName
                + ", lastName = " + lastName
                + ", username = " + username
                + ", password = " + password
                + "]";
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
