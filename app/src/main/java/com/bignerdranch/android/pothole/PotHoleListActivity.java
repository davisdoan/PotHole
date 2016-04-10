package com.bignerdranch.android.pothole;

import android.support.v4.app.Fragment;

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
            Fragment newDetailPhone = PotHoleDetailFragment.newInstance(potHole);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,newDetailPhone).addToBackStack(null).commit();
        } else {
            Fragment newDetail = PotHoleDetailFragment.newInstance(potHole);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newDetail).commit();
        }
    }
}
