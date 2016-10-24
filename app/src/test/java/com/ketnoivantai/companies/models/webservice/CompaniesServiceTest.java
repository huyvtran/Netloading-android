package com.ketnoivantai.companies.models.webservice;

import com.ketnoivantai.companies.models.entities.Company;
import com.ketnoivantai.companies.models.entities.Vehicle;
import com.ketnoivantai.companies.models.webservice.json.AddVehicleResult;
import com.ketnoivantai.companies.models.webservice.json.GenericResult;
import com.ketnoivantai.companies.models.webservice.json.LoginResult;
import com.ketnoivantai.utils.Constants;
import com.ketnoivantai.utils.Utils;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by Dandoh on 10/24/16.
 */
public class CompaniesServiceTest {
    private CompaniesAccountService mCompaniesAccountService;

    private CompaniesService mCompaniesService;

    @Before
    public void setUp() throws Exception {
        mCompaniesAccountService = ServiceGenerator.getCompaniesAccountService();
        Company company = new Company();
        company.setUsername("dandoh");
        company.setPassword("dandoh");
        LoginResult res = mCompaniesAccountService.login(company).execute().body();
        ServiceGenerator.initialize(res.getToken(), res.getId());


        mCompaniesService = ServiceGenerator.getCompaniesService();
    }


    @Test
    public void getVehicles() throws Exception {
        List<Vehicle> vehicles = mCompaniesService.getVehicles().execute()
                .body().getList();

        assertNotNull(vehicles);
        Utils.log("CompaniesServiceTest", "getVehicles: " + vehicles.size());

    }

    @Test
    public void testCreateNewValidVehicleAndDelete() throws Exception {

        // add new random vehicle
        AddVehicleResult res = mCompaniesService.addVehicle(new Vehicle("xeTai", 1000,
                UUID.randomUUID().toString(), "ADSD"))
                .execute().body();
        assertEquals(Constants.NO_ERROR, res.getError());

        // get all vehicles
        List<Vehicle> vehicles = mCompaniesService.getVehicles().execute()
                .body().getList();
        boolean contain = false;
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId() == res.getVehicle_id())
                contain = true;
        }
        assertTrue("List should contain previously added vehicle", contain);

        // delete previously added vehicle
        GenericResult deleteRes = mCompaniesService.deleteVehicle(res.getVehicle_id())
                .execute().body();
        assertEquals(Constants.NO_ERROR, deleteRes.getError());

        vehicles = mCompaniesService.getVehicles().execute()
                .body().getList();
        contain = false;
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId() == res.getVehicle_id())
                contain = true;
        }
        assertFalse("List should not contain previously added vehicle", contain);

    }

    @Test
    public void testAddDuplicateVehicle() throws Exception {
        // add random vehicle
        Vehicle randomVehicle = new Vehicle("xeTai", 1000,
                UUID.randomUUID().toString().substring(0, 10), "ADSD");
        int vehicleId = mCompaniesService.addVehicle(randomVehicle).execute()
                .body().getVehicle_id();

        GenericResult dupResult = mCompaniesService.addVehicle(randomVehicle).execute()
                .body();
        assertNotEquals(Constants.NO_ERROR, dupResult.getError());

        mCompaniesService.deleteVehicle(vehicleId).execute();
    }

    @Test
    public void testGetVehicleInformation() throws Exception {
        // add random vehicle
        Vehicle randomVehicle = new Vehicle("xeTai", 1000,
                UUID.randomUUID().toString().substring(0, 10), "ADSD");
        int vehicleId = mCompaniesService.addVehicle(randomVehicle).execute()
                .body().getVehicle_id();

        // get information
        Vehicle vehicle = mCompaniesService.getVehicleById(vehicleId).execute()
                .body();

        // assert
        assertEquals(vehicle.getPlate_number(), randomVehicle.getPlate_number());
        assertEquals(vehicle.getVehicle_type(), randomVehicle.getVehicle_type());

        // delete
        mCompaniesService.deleteVehicle(vehicleId).execute();
    }

    @Test
    public void testUpdateVehicleInformation() throws Exception {
        // add random vehicle
        Vehicle randomVehicle = new Vehicle("xeTai", 1000,
                UUID.randomUUID().toString().substring(0, 10), "ADSD");
        int vehicleId = mCompaniesService.addVehicle(randomVehicle).execute()
                .body().getVehicle_id();

        // update information
        Vehicle updateVehicle = new Vehicle();
        updateVehicle.setCapacity_number(1023);
        updateVehicle.setPlate_number(UUID.randomUUID().toString().substring(0, 10));
        updateVehicle.setVehicle_type("xeTai");
        GenericResult updateResult = mCompaniesService.updateVehicle(vehicleId, updateVehicle)
                .execute().body();
        assertEquals(Constants.NO_ERROR, updateResult.getError());

        // get information
        Vehicle vehicle = mCompaniesService.getVehicleById(vehicleId).execute()
                .body();

        // check
        assertEquals(updateVehicle.getCapacity_number(), vehicle.getCapacity_number());
        assertEquals(updateVehicle.getPlate_number(), vehicle.getPlate_number());
        assertEquals(updateVehicle.getVehicle_type(), vehicle.getVehicle_type());

        // delete
        mCompaniesService.deleteVehicle(vehicleId).execute();

    }

}