package com.bignerdranch.android.pothole;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class PotHoleListFragment extends Fragment{
    private RecyclerView mPotHoleRecycleView;
    private PotHoleAdapter mAdapter;
    private List<PotHole> mItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState){
        View view = inflater.inflate(R.layout.fragment_pothole_list, container, false);
        mPotHoleRecycleView = (RecyclerView) view.findViewById(R.id.pothole_recyler_view);
        mPotHoleRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void setupAdapter(){
        if(isAdded()) {
            mPotHoleRecycleView.setAdapter(new PotHoleAdapter(mItems));
        }
    }

    private class PotHoleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mUid;
        private TextView mLocation;
        private TextView mDate;
        private PotHole mPotHole;

        public PotHoleHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mUid = (TextView)itemView.findViewById(R.id.list_item_pothole_userid);
            mLocation = (TextView) itemView.findViewById(R.id.list_item_posthole_location);
            mDate = (TextView) itemView.findViewById(R.id.list_item_pothole_date);
        }

        @Override
        public void onClick(View v) {
            Intent intent = SubmitPotHoleActivity.newIntent(getActivity(), mPotHole.getId());
            // pass pothole as extra
            startActivity(intent);
        }

        public void bindPotHole(PotHole pothole){
            mPotHole = pothole;
            mUid.setText(mPotHole.getId());
            mLocation.setText(mPotHole.getLatitute());
            mDate.setText(mPotHole.getDate());
        }
    }

    private class PotHoleAdapter extends RecyclerView.Adapter<PotHoleHolder>{
        private List<PotHole> mPotHoles;

        public PotHoleAdapter(List<PotHole> potholes) {
            mPotHoles = potholes;
        }

        @Override
        public PotHoleHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_pothole, parent, false);

            return new PotHoleHolder(view);
        }

        @Override
        public void onBindViewHolder(PotHoleHolder holder, int position) {
            PotHole pothole = mPotHoles.get(position);
            holder.bindPotHole(pothole);
        }

        @Override
        public int getItemCount() {
            return mPotHoles.size();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,List<PotHole>>{

        @Override
        protected List<PotHole> doInBackground(Void... params) {
            return new PotHoleFetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(List<PotHole> items){
            mItems = items;
            setupAdapter();
        }
    }

    private void updateUI(){
        PotHoleTracker potHoleTracker = PotHoleTracker.get(getActivity());
        List<PotHole> potHoles = potHoleTracker.getPotHoles();

        if(mAdapter == null){
            mAdapter = new PotHoleAdapter(potHoles);
            mPotHoleRecycleView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mAdapter =  new PotHoleAdapter(potHoles);
        mPotHoleRecycleView.setAdapter(mAdapter);
    }

}
