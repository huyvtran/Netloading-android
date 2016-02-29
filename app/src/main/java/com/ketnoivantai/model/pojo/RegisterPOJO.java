package com.ketnoivantai.model.pojo;

/**
 * Created by Dandoh on 2/13/16.
 */
public class RegisterPOJO {


    private final String username;
    private final String password;
    private final String phone;
    private final String email;
    private final String address;
    private final String socialId;
    private final String name;

    public RegisterPOJO(String username, String password, String phone, String email, String address, String socialId, String name) {

        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.socialId = socialId;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getSocialId() {
        return socialId;
    }

    public String getName() {
        return name;
    }
}
