package com.ketnoivantai.companies.models.entities;

/**
 * Created by AnhVu on 10/15/16.
 */

public class Request {

    int id;
    int customer_id;
    String pickup_date;
    String deliver_date;
    String goods_type;
    int goods_weight_number;
    String goods_size;
    String start_address;
    String arrive_address;
    String goods_description;
    String vehicle_type;
    String goods_name;
    String start_province_name;
    String arrive_province_name;
    String start_district_name;
    String arrive_district_name;
    int status;

    public int getId() {
        return id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public String getDeliver_date() {
        return deliver_date;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public int getGoods_weight_number() {
        return goods_weight_number;
    }

    public String getGoods_size() {
        return goods_size;
    }

    public String getStart_address() {
        return start_address;
    }

    public String getArrive_address() {
        return arrive_address;
    }

    public String getGoods_description() {
        return goods_description;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public String getStart_province_name() {
        return start_province_name;
    }

    public String getArrive_province_name() {
        return arrive_province_name;
    }

    public String getStart_district_name() {
        return start_district_name;
    }

    public String getArrive_district_name() {
        return arrive_district_name;
    }

    public int getStatus() {
        return status;
    }
}
