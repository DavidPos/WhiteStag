package sailloft.whitestag.model;

import java.io.Serializable;

/**
 * Created by David's Laptop on 4/23/2015.
 */
public class OwnerData implements Serializable {

    private String mFirstName;
    private String mLastName;
    private String mPermit;
    private String mDepartment;
    private String mWorkLocation;

    public OwnerData(String firstName, String lastName, String permit, String department, String workLocation){
        mFirstName = firstName;
        mLastName = lastName;
        mPermit = permit;
        mDepartment = department;
        mWorkLocation = workLocation;
    }



    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getPermit() {
        return mPermit;
    }

    public void setPermit(String permit) {
        mPermit = permit;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public void setDepartment(String department) {
        mDepartment = department;
    }

    public String getWorkLocation() {
        return mWorkLocation;
    }

    public void setWorkLocation(String workLocation) {
        mWorkLocation = workLocation;
    }
}

