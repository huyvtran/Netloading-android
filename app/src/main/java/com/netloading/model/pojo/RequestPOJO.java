package com.netloading.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Dandoh on 2/20/16.
 */
public class RequestPOJO implements Serializable, Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pickup_date);
        dest.writeString(this.goods_weight_dimension);
        dest.writeInt(this.goods_weight_number);
        dest.writeInt(this.start_address);
        dest.writeInt(this.arrive_address);
        dest.writeString(this.vehicle_type);
        dest.writeString(this.expected_price);
        dest.writeString(this.goods_name);
    }

    private RequestPOJO(Parcel in) {
        this.pickup_date = in.readString();
        this.goods_weight_dimension = in.readString();
        this.goods_weight_number = in.readInt();
        this.start_address = in.readInt();
        this.arrive_address = in.readInt();
        this.vehicle_type = in.readString();
        this.expected_price = in.readString();
        this.goods_name = in.readString();
    }

    public static final Parcelable.Creator<RequestPOJO> CREATOR = new Parcelable.Creator<RequestPOJO>() {
        public RequestPOJO createFromParcel(Parcel source) {
            return new RequestPOJO(source);
        }

        public RequestPOJO[] newArray(int size) {
            return new RequestPOJO[size];
        }
    };
}
