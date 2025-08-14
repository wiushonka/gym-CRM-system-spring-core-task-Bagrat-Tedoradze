package com.example.entitys.users;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Trainee extends User {

    private Long id;
    private String addr;
    private final Date birthDate;

    public Trainee(Long id, @NotNull Date birthDate, String addr,
                   String firstName, String lastName, String username, String password) {
        super(firstName, lastName, username, password);
        this.id = id;
        this.birthDate = new Date(birthDate.getTime());
        this.addr = addr;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Date getBirthDate() { return (Date)birthDate.clone(); }

    public String getAddr() { return addr; }

    public void setAddr(String addr) { this.addr = addr; }
}
