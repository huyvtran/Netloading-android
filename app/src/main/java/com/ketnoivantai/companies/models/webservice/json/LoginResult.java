package com.ketnoivantai.companies.models.webservice.json;

/**
 * Created by Dandoh on 10/22/16.
 */
public class LoginResult extends GenericResult {

    int id;
    String token;

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
