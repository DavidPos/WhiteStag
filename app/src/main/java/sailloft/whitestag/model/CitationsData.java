package sailloft.whitestag.model;

import java.io.Serializable;

/**
 * Created by David's Laptop on 4/25/2015.
 */
public class CitationsData implements Serializable{


    private int mOwner;
    private String mOfficer;
    private String mCitationType;
    private String mTimeDate;
    private int mBoot;
    private int mVehicle;
    private String mAddInfo;
    private String mLocation;

    public CitationsData(int owner, String officer, String citationType, String timeDate, int boot, int vehicle, String addInfo, String location){

        mOwner = owner;
        mOfficer = officer;
        mCitationType = citationType;
        mTimeDate = timeDate;
        mBoot = boot;//0 means not signed boot form :1 means form signed
        mVehicle = vehicle;
        mAddInfo = addInfo;
        mLocation = location;
    }



    public int getOwner() {
        return mOwner;
    }

    public void setOwner(int owner) {
        mOwner = owner;
    }

    public String getOfficer() {
        return mOfficer;
    }

    public void setOfficer(String officer) {
        mOfficer = officer;
    }

    public String getCitationType() {
        return mCitationType;
    }

    public void setCitationType(String citationType) {
        mCitationType = citationType;
    }

    public String getTimeDate() {
        return mTimeDate;
    }

    public void setTimeDate(String timeDate) {
        mTimeDate = timeDate;
    }

    public int getBoot() {
        return mBoot;
    }

    public void setBoot(int boot) {
        mBoot = boot;
    }

    public int getVehicle() {
        return mVehicle;
    }

    public void setVehicle(int vehicle) {
        mVehicle = vehicle;
    }

    public String getAddInfo() {
        return mAddInfo;
    }

    public void setAddInfo(String addInfo) {
        mAddInfo = addInfo;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }
}
