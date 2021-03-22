package com.eler.MainService.Models;

import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public class TeacherAccount {


    private int id;

    private String email;

    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TeacherAccount() {
    }

    
}
