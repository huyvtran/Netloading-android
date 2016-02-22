package com.netloading.model.pojo;

/**
 * Created by Dandoh on 2/20/16.
 */
public class RequestPOJO {
    private final String pickUpDate;
    private final String goodsWeightDimension;
    private final String goodsWeightNumber;
    private final int startDistrictCode;
    private final int arriveDistrictCode;
    private final String vehicleType;
    private final String expectedPrice;

    public RequestPOJO(String pickUpDate, String goodsWeightDimension, String goodsWeightNumber, int startDistrictCode, int arriveDistrictCode, String vehicleType, String expectedPrice) {

        this.pickUpDate = pickUpDate;
        this.goodsWeightDimension = goodsWeightDimension;
        this.goodsWeightNumber = goodsWeightNumber;
        this.startDistrictCode = startDistrictCode;
        this.arriveDistrictCode = arriveDistrictCode;
        this.vehicleType = vehicleType;
        this.expectedPrice = expectedPrice;
    }

    public String getGoodsWeightNumber() {
        return goodsWeightNumber;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public String getGoodsWeightDimension() {
        return goodsWeightDimension;
    }

    public int getStartDistrictCode() {
        return startDistrictCode;
    }

    public int getArriveDistrictCode() {
        return arriveDistrictCode;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getExpectedPrice() {
        return expectedPrice;
    }
}
