package sailloft.whitestag.model;

import java.io.Serializable;

/**
 * Created by David's Laptop on 6/15/2015.
 */
public class LocationData implements Serializable {

    private String mLocation;

    public LocationData(String location){
        mLocation = location;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }
}
