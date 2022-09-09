package com.fara.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Table(name = "user")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotBlank(message = "first name name is required but not provided")
    @NotEmpty(message = "first name is required but not provided")
    @NotNull(message = "first name name is required but not provided")
    @Column(name = "first_name", nullable = false)
    private String firstName;


    @NotBlank(message = "last name name is required but not provided")
    @NotEmpty(message = "last name is required but not provided")
    @NotNull(message = "last name name is required but not provided")
    @Column(name = "last_name", nullable = false)
    private String lastName;


    @NotBlank(message = "email name is required but not provided")
    @NotEmpty(message = "email is required but not provided")
    @NotNull(message = "email name is required but not provided")
    @javax.validation.constraints.Email(message = "should be email")
//    @Pattern(message = "must be at correct format", regexp = "^[A-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[A-Z0-9_!#$%&'*+/=?`{|}~^-]+\u21B5\n)*@[A-Z0-9-]+(?:\\.[A-Z0-9-]+)*$")
    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @Enumerated(EnumType.STRING)
    @NotNull(message = "role is required but not provided")
    @Column(name = "roles", nullable = false)
    private Roles roles;

    @NotEmpty(message = "user name is required but not provided")
    @NotBlank(message = "user name is required but not provided")
    @Column(name = "username", nullable = false, unique = true)
    @NotNull(message = "user name is required but not provided")
    private String username;

    @Column(name = "enable", nullable = false)
    private Boolean enable = false;


    @NotBlank(message = "password name is required but not provided")
    @NotEmpty(message = "password name is required but not provided")
    @NotNull(message = "password name is required but not provided")

    @Column(name = "password", nullable = false)
    private String password;

    public String getFullName() {
        return firstName+" "+lastName;
    }

    public User(
            String firstName,
            String lastName,
            String username,
            String password,
            String email,
            Roles roles,
            Boolean enable
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
        this.username = username;
        this.enable = enable;
        this.password = password;
    }



    //    @NotNull(message = "last name is required but not provided")
//    @Column(name = "birth", nullable = false)
//    private Date birth;
//    @NotNull(message = "gender is required but not provided")
//    @Enumerated(EnumType.STRING)
//    @Column(name = "gender", nullable = false)
//    private Gender gender = Gender.MALE;
//    @NotBlank(message = "address name is required but not provided")
//    @NotEmpty(message = "address is required but not provided")
//    @NotNull(message = "address name is required but not provided")
//    @Column(name = "address", nullable = false)
//    private String address;
//    @NotBlank(message = "phone name is required but not provided")
//    @NotEmpty(message = "phone is required but not provided")
//    @NotNull(message = "phone name is required but not provided")
//    @Column(name = "phone", nullable = false, unique = true, length = 20)
//    private String phone;
//    @NotBlank(message = "city name is required but not provided")
//    @NotEmpty(message = "city is required but not provided")
//    @NotNull(message = "city name is required but not provided")
//    @Column(name = "city")
//    private String city;
//    @NotBlank(message = "state name is required but not provided")
//    @NotEmpty(message = "state name is required but not provided")
//    @NotNull(message = "state name is required but not provided")
//    @Column(name = "state")
//    private String state;
//    public User(String firstName, String lastName, Date birth, Gender gender, String email, String address, String phone, Roles roles, String username, Boolean enable, String city, String state, String password) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.birth = birth;
//        this.gender = gender;
//        this.email = email;
//        this.address = address;
//        this.phone = phone;
//        this.roles = roles;
//        this.username = username;
//        this.enable = enable;
//        this.city = city;
//        this.state = state;
//        this.password = password;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && roles == user.roles && Objects.equals(username, user.username) && Objects.equals(enable, user.enable) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, roles, username, enable, password);
    }
}
