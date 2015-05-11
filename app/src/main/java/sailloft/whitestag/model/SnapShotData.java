package sailloft.whitestag.model;

import java.io.Serializable;

/**
 * Created by David's Laptop on 5/3/2015.
 */
public class SnapShotData implements Serializable {
    private int ownerId;
    private long dateTime;
    private int vehicleId;
    private String mLocation;

    public SnapShotData(int owner, long date, int vehicle, String location){
        ownerId = owner;
        dateTime = date;
        vehicleId = vehicle;
        mLocation = location;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(int dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }
}
