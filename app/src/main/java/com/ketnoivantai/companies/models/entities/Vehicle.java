package com.ketnoivantai.companies.models.entities;

import com.ketnoivantai.companies.models.webservice.json.GenericResult;

/**
 * Created by AnhVu on 10/15/16.
 */

public class Vehicle extends GenericResult {

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

    public Vehicle() {
    }

    public Vehicle(String vehicle_type, int capacity_number, String plate_number, String information) {
        this.vehicle_type = vehicle_type;
        this.capacity_number = capacity_number;
        this.plate_number = plate_number;
        this.information = information;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public void setCapacity_number(int capacity_number) {
        this.capacity_number = capacity_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }
}
