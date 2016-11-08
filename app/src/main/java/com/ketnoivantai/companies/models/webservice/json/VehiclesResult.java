package com.ketnoivantai.companies.models.webservice.json;

import com.ketnoivantai.companies.models.entities.Vehicle;

import java.util.List;

/**
 * Created by Dandoh on 10/24/16.
 */

public class VehiclesResult extends GenericResult {
    List<Vehicle> list;

    public List<Vehicle> getList() {
        return list;
    }
}
