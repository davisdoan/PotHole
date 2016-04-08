package com.bignerdranch.android.pothole;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PotHoleListActivity extends SingleFragmentActivity implements PotHoleListFragment.Callbacks{

    @Override
    protected Fragment createFragment(){
        return new PotHoleListFragment();
    }

    @Override
    protected int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onPotholeSelected(PotHole potHole){
        if(findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = PotHoleDetails.newIntent(this, potHole.getId());
            //Intent intent = new Intent(this, PotHoleDetails.class);
            intent.putExtra("id", potHole.getId());
            intent.putExtra("latitude", potHole.getLatitute());
            intent.putExtra("longitude", potHole.getLatitute());
            intent.putExtra("description", potHole.getDescription());
            intent.putExtra("date", potHole.getDate());
            startActivity(intent);
        } else {
            Fragment newDetail = PotHoleDetailFragment.newInstance(potHole);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newDetail).commit();
        }
    }
}
