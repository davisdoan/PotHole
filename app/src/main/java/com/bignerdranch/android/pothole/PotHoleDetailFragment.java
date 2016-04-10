package com.bignerdranch.android.pothole;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class PotHoleDetailFragment extends Fragment{

    TextView mId;
    TextView mLatitude;
    TextView mLongitude;
    TextView mDate;
    TextView mDescription;
    ImageView mImageView;
    private static final String TAG = "PotHoleDetailsFrag,emt";
    private static final String ARG_POTHOLE_ID = "pothole_id";
    private static final String ARG_POTHOLE_LATITUDE = "pothole_latitude";
    private static final String ARG_POTHOLE_LONGITUDE = "pothole_longitude";
    private static final String ARG_POTHOLE_DATE = "pothole_date";
    private static final String ARG_POTHOLE_DESCRIPTION = "pothole_description";

    public static PotHoleDetailFragment newInstance(PotHole pothole) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_POTHOLE_ID, pothole.getId());
        args.putSerializable(ARG_POTHOLE_DATE, pothole.getDate());
        args.putSerializable(ARG_POTHOLE_LATITUDE, pothole.getLatitute());
        args.putSerializable(ARG_POTHOLE_LONGITUDE, pothole.getLongitute());
        args.putSerializable(ARG_POTHOLE_DESCRIPTION, pothole.getDescription());

        PotHoleDetailFragment fragment = new PotHoleDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pot_hole_details, container, false);

        mId = (TextView) view.findViewById(R.id.detail_id);
        mLatitude = (TextView) view.findViewById(R.id.detail_latitude);
        mLongitude = (TextView) view.findViewById(R.id.detail_longitude);
        mDate = (TextView) view.findViewById(R.id.detail_date);
        mDescription = (TextView) view.findViewById(R.id.detail_description);
        mImageView = (ImageView) view.findViewById(R.id.detail_pothole_photo);

        String potholeId = getArguments().getString(ARG_POTHOLE_ID);
        String potholeLongitude = getArguments().getString(ARG_POTHOLE_LONGITUDE);
        String potholeLatitude = getArguments().getString(ARG_POTHOLE_LATITUDE);
        String potholeDescription = getArguments().getString(ARG_POTHOLE_DESCRIPTION);
        String potholeDate = getArguments().getString(ARG_POTHOLE_DATE);

        mId.setText(getString(R.string.pothole_title_id) + " " + potholeId);
        mLatitude.setText(getString(R.string.pothole_title_latitude) + " " + potholeLatitude);
        mLongitude.setText(getString(R.string.pothole_title_longitude) + " " + potholeLongitude);
        mDate.setText(getString(R.string.pothole_title_date) + " " + potholeDate);
        mDescription.setText(potholeDescription);

        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i(TAG, "You have internet connection, downloading your stuff!");
            requestImage(potholeId, mImageView);
        } else {
            Toast.makeText(getContext(), "Error No Internet Connection!", Toast.LENGTH_LONG).show();
            Log.i(TAG, "ERROR No Internet Connection!");
        }

        return view;
    }

    private void requestImage(String postId, final ImageView imageView){
        String id = postId;
        String url = "http://bismarck.sdsu.edu/city/image?id=" + id;
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                Log.i(TAG, "Downloading Image Via VOlley!");
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "No Image available via volley!");
                if(isAdded()) {
                    Drawable placeholder = getResources().getDrawable(R.drawable.pothole_default);
                    imageView.setImageDrawable(placeholder);
                }
            }
        });
        VolleySingleton.getInstance().addToRequestQueue(imageRequest);
    }

}
