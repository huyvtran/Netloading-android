package com.ketnoivantai.model.pojo;

/**
 * Created by Dandoh on 2/10/16.
 */
public class LoginPOJO {

    public String username;
    public String password;

    public LoginPOJO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
