package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "Employees")
public class Employee {

    @Transient
    public static final String SEQUENCE_NAME = "employees_sequence";

    @Id
    private long id;

    @NotBlank
    @Size(max = 100)
    private String firstName;
    private String lastName;

    @NotBlank
    @Size(max = 100)
    @Indexed(unique = true)
    private String emailId;

    private String employer;

    @BsonProperty(value = "user_id")
    @JsonIgnore
    private User user;

    @Override
    public String toString(){
        return "Employee [id = " + id
                + ", firstName = " + firstName
                + ", lastName = " + lastName
                + ", emailId = " + emailId
                + "]";
    }

}
