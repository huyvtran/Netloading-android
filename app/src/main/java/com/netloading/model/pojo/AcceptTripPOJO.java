package com.netloading.model.pojo;

/**
 * Created by AnhVu on 2/25/16.
 */
public class AcceptTripPOJO {

    private final int request_id;
    private final int trip_id;
    private int id;
    private int status;

    public AcceptTripPOJO(int requestId, int tripId) {
        this.request_id = requestId;
        this.trip_id = tripId;
    }

    @Override
    public String toString() {
        return "AcceptTripPOJO{" +
                "request_id=" + request_id +
                ", trip_id=" + trip_id +
                ", id=" + id +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
