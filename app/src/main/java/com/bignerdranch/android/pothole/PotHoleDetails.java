package com.bignerdranch.android.pothole;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class PotHoleDetails extends AppCompatActivity {

    TextView mId;
    TextView mLatitude;
    TextView mLongitude;
    TextView mDate;
    TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pot_hole_details);

        mId = (TextView)findViewById(R.id.detail_id);
        mLatitude = (TextView)findViewById(R.id.detail_latitude);
        mLongitude = (TextView)findViewById(R.id.detail_longitude);
        mDate = (TextView)findViewById(R.id.detail_date);
        mDescription = (EditText) findViewById(R.id.detail_description);

        Bundle mainData = getIntent().getExtras();
        String userId = mainData.getString("id");
        String date = mainData.getString("date");
        String latitude = mainData.getString("latitude");
        String longitude = mainData.getString("longitude");
        String description = mainData.getString("description");

        mId.setText(getString(R.string.pothole_title_id) + userId);
        mDate.setText(getString(R.string.pothole_title_date) + date );
        mLongitude.setText(getString(R.string.pothole_title_latitude) + longitude);
        mLatitude.setText(getString(R.string.pothole_title_latitude) + latitude);
        mDescription.setText(description);
    }
}
