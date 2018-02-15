package com.example.ingreens.firebasedemo;

/**
 * Created by ingreens on 14/2/18.
 */

public class UserModel {

    String name,email,password;


    public UserModel() {
    }
    public UserModel(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public UserModel(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
}
