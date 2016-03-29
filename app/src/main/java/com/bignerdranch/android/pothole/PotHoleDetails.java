package com.bignerdranch.android.pothole;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class PotHoleDetails extends AppCompatActivity {

    TextView mId;
    TextView mLatitude;
    TextView mLongitude;
    TextView mDate;
    TextView mDescription;
    ImageView mImageView;
    private static final String TAG = "PotHoleDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pot_hole_details);

        mId = (TextView)findViewById(R.id.detail_id);
        mLatitude = (TextView)findViewById(R.id.detail_latitude);
        mLongitude = (TextView)findViewById(R.id.detail_longitude);
        mDate = (TextView)findViewById(R.id.detail_date);
        mDescription = (EditText) findViewById(R.id.detail_description);
        mImageView = (ImageView) findViewById(R.id.detail_pothole_photo);

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
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i(TAG, "You have internet connection, downloading your stuff!");
            requestImage(userId, mImageView);
        } else {
            // display error
            Toast.makeText(this, "Error No Internet Connection!", Toast.LENGTH_LONG).show();
            Log.i(TAG, "ERROR No Internet Connection!");
        }

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
                Drawable placeholder = getResources().getDrawable(R.drawable.pothole_default);
                imageView.setImageDrawable(placeholder);
            }
        });
        VolleySingleton.getInstance().addToRequestQueue(imageRequest);
    }
}
