package com.ketnoivantai.companies.models.entities;

/**
 * Created by AnhVu on 10/15/16.
 */

public class Trip {

    int id;
    int vehicle_id;
    String start_date;
    String arrive_date;
    String start_address;
    String arrive_address;
    int status;
    int price_per_unit;
    int driver_id;
    int company_id;
    int cur_request_id;

    public int getId() {
        return id;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getArrive_date() {
        return arrive_date;
    }

    public String getStart_address() {
        return start_address;
    }

    public String getArrive_address() {
        return arrive_address;
    }

    public int getStatus() {
        return status;
    }

    public int getPrice_per_unit() {
        return price_per_unit;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public int getCur_request_id() {
        return cur_request_id;
    }
}
