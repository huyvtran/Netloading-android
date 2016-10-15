package com.ketnoivantai.customers.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dandoh on 2/24/16.
 */
public class CompanyTripPOJO implements Parcelable {

    private final int id;
    private final int vehicle_id;
    private final String start_date;
    private final String arrive_date;
    private final String start_address;
    private final String arrive_address;
    private final int status;
    private final int price_per_unit;
    private final int driver_id;
    private final int company_id;
    private String name = "Công ty THHH Vũ Bình Nè";
    private int price = 1500000;
    private VehiclePOJO vehicle;


    public CompanyTripPOJO(int id, int vehicle_id, String start_date, String arrive_date, String start_address,
                           String arrive_address, int status, int price_per_unit, int driver_id, int company_id,
                           String name, int price) {

        this.id = id;
        this.vehicle_id = vehicle_id;
        this.start_date = start_date;
        this.arrive_date = arrive_date;
        this.start_address = start_address;
        this.arrive_address = arrive_address;
        this.status = status;
        this.price_per_unit = price_per_unit;
        this.driver_id = driver_id;
        this.company_id = company_id;
        this.name = name;
        this.price = price;
    }



    @Override
    public String toString() {
        return "CompanyTripPOJO{" +
                "id=" + id +
                ", vehicle_id=" + vehicle_id +
                ", start_date='" + start_date + '\'' +
                ", arrive_date='" + arrive_date + '\'' +
                ", start_address='" + start_address + '\'' +
                ", arrive_address='" + arrive_address + '\'' +
                ", status=" + status +
                ", price_per_unit=" + price_per_unit +
                ", driver_id=" + driver_id +
                ", company_id=" + company_id +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.vehicle_id);
        dest.writeString(this.start_date);
        dest.writeString(this.arrive_date);
        dest.writeString(this.start_address);
        dest.writeString(this.arrive_address);
        dest.writeInt(this.status);
        dest.writeInt(this.price_per_unit);
        dest.writeInt(this.driver_id);
        dest.writeInt(this.company_id);
        dest.writeString(this.name);
        dest.writeInt(this.price);
    }

    private CompanyTripPOJO(Parcel in) {
        this.id = in.readInt();
        this.vehicle_id = in.readInt();
        this.start_date = in.readString();
        this.arrive_date = in.readString();
        this.start_address = in.readString();
        this.arrive_address = in.readString();
        this.status = in.readInt();
        this.price_per_unit = in.readInt();
        this.driver_id = in.readInt();
        this.company_id = in.readInt();
        this.name = in.readString();
        this.price = in.readInt();
    }

    public static final Parcelable.Creator<CompanyTripPOJO> CREATOR = new Parcelable.Creator<CompanyTripPOJO>() {
        public CompanyTripPOJO createFromParcel(Parcel source) {
            return new CompanyTripPOJO(source);
        }

        public CompanyTripPOJO[] newArray(int size) {
            return new CompanyTripPOJO[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getCompany_id() {
        return company_id;
    }

    public int getId() {
        return id;
    }

    public VehiclePOJO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehiclePOJO vehicle) {
        this.vehicle = vehicle;
    }
}
