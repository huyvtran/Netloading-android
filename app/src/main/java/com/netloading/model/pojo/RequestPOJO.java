package com.netloading.model.pojo;

import java.io.Serializable;

/**
 * Created by Dandoh on 2/20/16.
 */
public class RequestPOJO implements Serializable{
    private final String pickup_date;
    private final String goods_weight_dimension;
    private final int goods_weight_number;
    private final int start_address;
    private final int arrive_address;
    private final String vehicle_type;
    private final String expected_price;
    private final String goods_name;

    public RequestPOJO(String pickUpDate, String goodsWeightDimension,
                       int goodsWeightNumber, int startDistrictCode,
                       int arriveDistrictCode, String vehicleType,
                       String expectedPrice, String goodsName) {

        this.pickup_date = pickUpDate;
        this.goods_weight_dimension = goodsWeightDimension;
        this.goods_weight_number = goodsWeightNumber;
        this.start_address = startDistrictCode;
        this.arrive_address = arriveDistrictCode;
        this.vehicle_type = vehicleType;
        this.expected_price = expectedPrice;
        this.goods_name = goodsName;
    }

    public int getGoods_weight_number() {
        return goods_weight_number;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public String getGoods_weight_dimension() {
        return goods_weight_dimension;
    }

    public int getStart_address() {
        return start_address;
    }

    public int getArrive_address() {
        return arrive_address;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public String getExpected_price() {
        return expected_price;
    }

    @Override
    public String toString() {
        return "RequestPOJO{" +
                "pickup_date='" + pickup_date + '\'' +
                ", goods_weight_dimension='" + goods_weight_dimension + '\'' +
                ", goods_weight_number=" + goods_weight_number +
                ", start_address=" + start_address +
                ", arrive_address=" + arrive_address +
                ", vehicle_type='" + vehicle_type + '\'' +
                ", expected_price='" + expected_price + '\'' +
                ", goods_name='" + goods_name + '\'' +
                '}';
    }
}
