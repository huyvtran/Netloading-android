package com.ketnoivantai.companies.models.entities;

/**
 * Created by AnhVu on 10/15/16.
 */

public class Company {

    int id;
    String username;
    String password;
    String token;
    String email;
    String business_id;
    String fax;
    String website;
    String name;
    String phone;
    String address;
    String budget;
    int budget_request;
    int budget_bonus;

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

    public String getEmail() {
        return email;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public String getFax() {
        return fax;
    }

    public String getWebsite() {
        return website;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getBudget() {
        return budget;
    }

    public int getBudget_request() {
        return budget_request;
    }

    public int getBudget_bonus() {
        return budget_bonus;
    }
}
