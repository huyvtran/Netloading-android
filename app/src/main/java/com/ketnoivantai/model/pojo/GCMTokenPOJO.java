package com.ketnoivantai.model.pojo;

/**
 * Created by Dandoh on 2/15/16.
 */
public class GCMTokenPOJO {

    int id;
    String registration_token;

    public GCMTokenPOJO(int id, String registration_token) {
        this.id = id;
        this.registration_token = registration_token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegistration_token() {
        return registration_token;
    }

    public void setRegistration_token(String registration_token) {
        this.registration_token = registration_token;
    }
}
