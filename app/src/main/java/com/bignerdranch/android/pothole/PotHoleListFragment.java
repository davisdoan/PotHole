package com.bignerdranch.android.pothole;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PotHoleListFragment extends Fragment{
    private RecyclerView mPotHoleRecycleView;
    private Button mNewReportButton;
    private List<PotHole> mItems = new ArrayList<>();
    private List<PotHole> potHoleListItems;
    private static final String TAG = "PotHoleListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //new FetchItemsTask().execute();
        requestJsonObject();
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
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        setupAdapter();
    }

    private void setupAdapter(){
        if(isAdded()) {
            //mPotHoleRecycleView.setAdapter(new PotHoleAdapter(mItems));
            mPotHoleRecycleView.setAdapter(new PotHoleAdapter(potHoleListItems));
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
            mUid.setText(getString(R.string.pothole_title_id) + mPotHole.getId());
            mLatitude.setText(getString(R.string.pothole_title_latitude) + mPotHole.getLatitute());
            mLongitude.setText(getString(R.string.pothole_title_longitude) + mPotHole.getLongitute());
            mDate.setText(getString(R.string.pothole_title_date) + mPotHole.getDate());
        }
    }


    private void requestJsonObject() {
        String url = Uri.parse("http://bismarck.sdsu.edu/city/batch")
                .buildUpon()
                .appendQueryParameter("type","street")
                .build()
                .toString();
        RequestQueue mainQueue = Volley.newRequestQueue(getContext());
        //JsonArrayRequest jsonArray = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener())

        potHoleListItems = new ArrayList<>();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray jsonBody) {
                try {
                    //String name = response.getString("name");
                    for(int i = 0; i < jsonBody.length(); i++){
                        JSONObject myJson = jsonBody.getJSONObject(i);
                        String id = myJson.getString("id");
                        String latitude = myJson.getString("latitude");
                        String longitude = myJson.getString("longitude");
                        String description = myJson.getString("description");
                        String date = myJson.getString("created");

                        Log.i(TAG, "You have from VOLLEY: " + id);
                        Log.i(TAG, "You have from VOLLEY: " + latitude );

                        PotHole potholeItem = new PotHole();

                        potholeItem.setId(id);
                        potholeItem.setLatitude(latitude);
                        potholeItem.setLongtitute(longitude);
                        potholeItem.setDescription(description);
                        potholeItem.setDate(date);

                        potHoleListItems.add(potholeItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
            });
        mainQueue.add(jsonObjReq);
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

}
