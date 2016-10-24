package com.ketnoivantai.companies.models.webservice;

import com.ketnoivantai.companies.models.entities.Company;
import com.ketnoivantai.companies.models.webservice.json.GenericResult;
import com.ketnoivantai.companies.models.webservice.json.LoginResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Dandoh on 10/22/16.
 */

public interface CompaniesAccountService {
    // login
    @POST("/companies/sessions")
    Call<LoginResult> login(@Body Company company);

}
