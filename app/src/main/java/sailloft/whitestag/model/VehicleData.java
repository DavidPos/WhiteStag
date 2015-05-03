package sailloft.whitestag.model;

import java.io.Serializable;

/**
 * Created by David's Laptop on 4/23/2015.
 */
public class VehicleData implements Serializable {

    private String mMake;
    private String mModel;
    private String mPlateNumber;
    private String mPlateState;
    private String mYear;
    private int mOwner;


    public VehicleData(String make, String model, String plateNumber, String plateState, String year, int owner){

        mMake = make;
        mModel = model;
        mPlateNumber = plateNumber;
        mPlateState = plateState;
        mYear = year;
        mOwner = owner;

    }



    public String getMake() {
        return mMake;
    }

    public void setMake(String make) {
        this.mMake = make;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        this.mModel = model;
    }

    public String getPlateNumber() {
        return mPlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.mPlateNumber = plateNumber;
    }

    public String getPlateState() {
        return mPlateState;
    }

    public void setPlateState(String plateState) {
        this.mPlateState = plateState;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String year) {
        this.mYear = year;
    }

    public int getOwner() {
        return mOwner;
    }

    public void setOwner(int owner) {
        this.mOwner = owner;
    }


}
