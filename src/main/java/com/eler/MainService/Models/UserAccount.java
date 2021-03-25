package com.eler.MainService.Models;

public class UserAccount {


    private int idUser;

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

    public UserAccount() {
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public UserAccount(int idUser, String email, String password) {
        this.idUser = idUser;
        this.email = email;
        this.password = password;
    }

    
}
