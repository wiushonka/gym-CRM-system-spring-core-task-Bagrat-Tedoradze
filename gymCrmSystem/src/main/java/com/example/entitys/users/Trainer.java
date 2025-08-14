package com.example.entitys.users;

public class Trainer extends User {

    private Long id;
    private String spec;

    public Trainer(Long id, String firstName, String lastName, String password, String username, String spec) {
        super(firstName, lastName, username, password);
        this.id=id;
        this.spec=spec;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getSpec() { return spec; }

    public void setSpec(String spec) { this.spec = spec; }
}
