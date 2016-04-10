package com.bignerdranch.android.pothole;

public class PotHole {

    private String mId;
    private String mLatitude;
    private String mLongitute;
    private String mDate;
    private String mDescription;

    public PotHole(){
    }

    public String getId() { return mId;
    }

    public void setId(String id){ mId = id;
    }

    public String getLatitute() { return mLatitude;
    }

    public void setLatitude(String latitute) {mLatitude = latitute;
    }

    public String getLongitute(){ return mLongitute;
    }

    public void setLongtitute(String longitute) { mLongitute = longitute;
    }

    public String getDate() { return mDate;
    }

    public void setDate(String date){ mDate = date;
    }

    public String getDescription() { return mDescription;
    }

    public void setDescription(String description) { mDescription = description;
    }
}
