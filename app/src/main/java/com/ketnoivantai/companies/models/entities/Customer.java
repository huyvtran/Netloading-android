package com.ketnoivantai.companies.models.entities;

/**
 * Created by AnhVu on 10/15/16.
 */

public class Customer {

    int id;
    String username;
    String password;
    String token;
    String phone;
    String email;
    String address;
    String social_id;
    String gcm_token;
    String name;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getSocial_id() {
        return social_id;
    }

    public String getGcm_token() {
        return gcm_token;
    }

    public String getName() {
        return name;
    }
}
