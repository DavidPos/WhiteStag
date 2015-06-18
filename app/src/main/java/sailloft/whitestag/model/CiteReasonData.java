package sailloft.whitestag.model;

import java.io.Serializable;

/**
 * Created by David's Laptop on 6/15/2015.
 */
public class CiteReasonData implements Serializable {
    private String mReason;

    public CiteReasonData(String reason){
        mReason = reason;
    }

    public String getReason() {
        return mReason;
    }

    public void setReason(String reason) {
        mReason = reason;
    }
}
