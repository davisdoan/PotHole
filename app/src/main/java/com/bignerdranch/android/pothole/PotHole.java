package com.bignerdranch.android.pothole;

import java.util.Date;

public class PotHole {

    private String mId;
    private String mLocation;
    private Date mDate;

    public PotHole(){
        mId = "989";
        mDate = new Date();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id){
        mId = id;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location){
        mLocation = location;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date){
        mDate = date;
    }
}
