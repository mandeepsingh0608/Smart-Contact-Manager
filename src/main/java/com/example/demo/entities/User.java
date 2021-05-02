package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data

@Entity
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
@NotBlank(message = "Name must not be blank!!")
@Size(min = 2,max = 30, message = "min 2 to 30 character required")
private String name;
@Column(unique = true)
@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "invalid")
@NotBlank(message = "should not be blank")
private String email;
@Size(min = 6,message = "must be 6 char at least")
@NotBlank(message = "should no be blank")
private String password;
private String role;
private String imageURL;
@Column(length = 500)
private String aboutuser;
private boolean enabled;

@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
List<Contact> contacts=new ArrayList<>();

    public User() {
    }

}
