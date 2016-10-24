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

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public void setBudget_request(int budget_request) {
        this.budget_request = budget_request;
    }

    public void setBudget_bonus(int budget_bonus) {
        this.budget_bonus = budget_bonus;
    }
}
