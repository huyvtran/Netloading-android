package com.ketnoivantai.customers.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AnhVu on 3/5/16.
 */
public class VehiclePOJO implements Parcelable {

    private final int id;
    private final int company_id;
    private final String vehicle_type;
    private final int capacity_number;
    private final String plate_number;
    private final String information;

    public VehiclePOJO(int id, int company_id, String vehicle_type, int capacity_number, String plate_number,
                       String information) {
        this.id = id;
        this.company_id = company_id;
        this.vehicle_type = vehicle_type;
        this.capacity_number = capacity_number;
        this.plate_number = plate_number;
        this.information = information;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public int getCapacity_number() {
        return capacity_number;
    }

    public String getInformation() {
        return information;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.company_id);
        dest.writeString(this.vehicle_type);
        dest.writeInt(this.capacity_number);
        dest.writeString(this.plate_number);
        dest.writeString(this.information);
    }

    private VehiclePOJO(Parcel in) {
        this.id = in.readInt();
        this.company_id = in.readInt();
        this.vehicle_type = in.readString();
        this.capacity_number = in.readInt();
        this.plate_number = in.readString();
        this.information = in.readString();
    }

    public static final Parcelable.Creator<VehiclePOJO> CREATOR = new Parcelable.Creator<VehiclePOJO>() {
        public VehiclePOJO createFromParcel(Parcel source) {
            return new VehiclePOJO(source);
        }

        public VehiclePOJO[] newArray(int size) {
            return new VehiclePOJO[size];
        }
    };
}
