package com.netloading.model.pojo;

/**
 * Created by Dandoh on 2/20/16.
 */
public class RequestPOJO {
    private final String pickup_date;
    private final String goods_weight_dimension;
    private final String goods_weight_number;
    private final int start_address;
    private final int arrive_address;
    private final String vehicle_type;
    private final String expected_price;
    private final String goods_name;

    public RequestPOJO(String pickUpDate, String goodsWeightDimension, String goodsWeightNumber, int startDistrictCode, int arriveDistrictCode, String vehicleType, String expectedPrice, String goodsName) {

        this.pickup_date = pickUpDate;
        this.goods_weight_dimension = goodsWeightDimension;
        this.goods_weight_number = goodsWeightNumber;
        this.start_address = startDistrictCode;
        this.arrive_address = arriveDistrictCode;
        this.vehicle_type = vehicleType;
        this.expected_price = expectedPrice;
        this.goods_name = goodsName;
    }

    public String getGoods_weight_number() {
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
}
