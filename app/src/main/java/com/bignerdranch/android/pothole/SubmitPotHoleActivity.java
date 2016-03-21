package com.bignerdranch.android.pothole;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SubmitPotHoleActivity extends AppCompatActivity {


    private static final String EXTRA_POTHOLE_ID = "com.bignerdranch.android.pothole.pothole_id";

    public static Intent newIntent(Context packageContext, String potholeId){
        Intent intent = new Intent(packageContext, SubmitPotHoleActivity.class);
        intent.putExtra(EXTRA_POTHOLE_ID, potholeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_pot_hole);
    }
}
