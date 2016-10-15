package com.ketnoivantai.companies.models.entities;

/**
 * Created by AnhVu on 10/15/16.
 */

public class Vehicle {

    int id;
    int company_id;
    String vehicle_type;
    int capacity_number;
    String plate_number;
    String information;

    public int getId() {
        return id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public int getCapacity_number() {
        return capacity_number;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public String getInformation() {
        return information;
    }
}
