package com.bignerdranch.android.pothole;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class PotHoleTracker {
    private static PotHoleTracker mPotHoleTracker;
    private List<PotHole> mPotHoles;
    private String mDate;

    public static PotHoleTracker get(Context context){
        if( mPotHoleTracker == null){
            mPotHoleTracker = new PotHoleTracker(context);
        }
        return mPotHoleTracker;
    }

    private PotHoleTracker(Context context){
        mPotHoles = new ArrayList<>();

        for( int i = 0; i < 100; i ++) {
            PotHole potHole = new PotHole();
            potHole.setId("User Id: " + i);
            potHole.setDate(mDate);
            potHole.setLongtitute("47");
            potHole.setLatitude("68");
            mPotHoles.add(potHole);
        }
    }

    public List<PotHole> getPotHoles(){
        return mPotHoles;
    }

    public PotHole getPotHole(String id ){
        for(PotHole potHole: mPotHoles){
            if(potHole.getId().equals(id)){
                return potHole;
            }
        }
        return null;
    }

}
