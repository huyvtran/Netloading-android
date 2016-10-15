package com.ketnoivantai.companies.models.entities;

/**
 * Created by AnhVu on 10/15/16.
 */

public class Order {

    int id;
    int trip_id;
    int request_id;
    int status;

    public int getId() {
        return id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public int getRequest_id() {
        return request_id;
    }

    public int getStatus() {
        return status;
    }
}
