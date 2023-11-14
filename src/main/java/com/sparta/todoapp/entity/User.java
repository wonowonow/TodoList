package com.sparta.todoapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigInteger;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private BigInteger id;
    @NotEmpty
    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]$")
    @Column(nullable = false, unique = true)
    private String username;
    @NotEmpty
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^[a-zA-Z0-9]$")
    @Column(nullable = false)
    private String password;
    @OneToMany
    private Set<Card> cards;
    @OneToMany
    private Set<Comment> comments;
}
