package com.ketnoivantai.companies.models.webservice;

import com.ketnoivantai.companies.models.entities.Vehicle;
import com.ketnoivantai.companies.models.webservice.json.AddVehicleResult;
import com.ketnoivantai.companies.models.webservice.json.GenericResult;
import com.ketnoivantai.companies.models.webservice.json.VehiclesResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by AnhVu on 10/15/16.
 */

public interface AuthenticatedService {

    /**
     ******************** Vehicle ******************************
     */
    @GET("/vehicles")
    Call<VehiclesResult> getVehicles();

    @POST("/vehicles")
    Call<AddVehicleResult> addVehicle(@Body Vehicle vehicle);

    @DELETE("/vehicles/{id}")
    Call<GenericResult> deleteVehicle(@Path("id") int id);

    @GET("/vehicles/{id}")
    Call<Vehicle> getVehicleById(@Path("id") int id);

    @PATCH("/vehicles/{id}")
    Call<GenericResult> updateVehicle(@Path("id") int id, @Body Vehicle vehicle);


}
