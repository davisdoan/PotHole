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
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class PotHoleListFragment extends Fragment{
    private RecyclerView mPotHoleRecycleView;
    private Button mNewReportButton;
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
        mNewReportButton = (Button)view.findViewById(R.id.new_report_button);

        mNewReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reportIntent = new Intent(getActivity(), SubmitPotHoleActivity.class);
                startActivity(reportIntent);
            }
        });

        //UI();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        //updateUI();
    }

    private void setupAdapter(){
        if(isAdded()) {
            mPotHoleRecycleView.setAdapter(new PotHoleAdapter(mItems));
        }
    }

    private class PotHoleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mUid;
        private TextView mLatitude;
        private TextView mLongitude;
        private TextView mDate;
        private PotHole mPotHole;

        public PotHoleHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mUid = (TextView)itemView.findViewById(R.id.list_item_pothole_userid);
            mLatitude = (TextView) itemView.findViewById(R.id.list_item_pothole_latitude);
            mLongitude = (TextView) itemView.findViewById(R.id.list_item_posthole_longitute);
            mDate = (TextView) itemView.findViewById(R.id.list_item_pothole_date);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), PotHoleDetails.class);
            intent.putExtra("id", mPotHole.getId());
            intent.putExtra("latitude", mPotHole.getLatitute());
            intent.putExtra("longitude", mPotHole.getLatitute());
            intent.putExtra("description", mPotHole.getDescription());
            intent.putExtra("date",mPotHole.getDate());

            startActivity(intent);
        }

        public void bindPotHole(PotHole pothole){
            mPotHole = pothole;
            mUid.append(mPotHole.getId());
            mLatitude.append(mPotHole.getLatitute());
            mLongitude.append(mPotHole.getLongitute());
            mDate.append(mPotHole.getDate());
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
