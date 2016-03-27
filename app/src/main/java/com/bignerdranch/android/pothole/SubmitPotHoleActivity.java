package com.bignerdranch.android.pothole;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubmitPotHoleActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ImageButton mCameraButton;
    private ImageView mImageView;
    private TextView mLatitudeView;
    private TextView mLongitudeView;
    private TextView mDate;
    private EditText mDescription;
    private TextView mId;
    private GoogleApiClient mClient;
    private Location mLastLocation;
    private String mLatitude;
    private String mLongitude;
    private Button mSubmitButton;

    private LocationRequest mLocationRequest;

    private int permissionCheck;
    private static final String EXTRA_POTHOLE_ID = "com.bignerdranch.android.pothole.pothole_id";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_ERROR = 0;
    private static final String TAG = "SubmitPotHoleActivity";

    public static Intent newIntent(Context packageContext, String potholeId) {
        Intent intent = new Intent(packageContext, SubmitPotHoleActivity.class);
        intent.putExtra(EXTRA_POTHOLE_ID, potholeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_pot_hole);
        mCameraButton = (ImageButton) findViewById(R.id.pothole_camera_button);
        mSubmitButton = (Button) findViewById(R.id.submission_button);
        mImageView = (ImageView) findViewById(R.id.pothole_photo_submission);
        mLatitudeView = (TextView) findViewById(R.id.pothole_submit_latitude);
        mLongitudeView = (TextView) findViewById(R.id.pothole_submit_longitude);
        mDate = (TextView) findViewById(R.id.pothole_submit_date);
        mDescription = (EditText) findViewById(R.id.pothole_submit_description);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
            }
        });

        mLocationRequest = LocationRequest.create();

        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        mClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.i(TAG, "You have connected! ");

                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            LocationRequest accurateRequest = new LocationRequest();
                            accurateRequest.setInterval(3000);
                            accurateRequest.setFastestInterval(2000);
                            accurateRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                    mClient);
                            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, mLocationRequest, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    mLatitude = String.valueOf(location.getLatitude());
                                    mLongitude = String.valueOf(location.getLongitude());
                                    Log.i(TAG, "You have latitude: " + mLatitude);
                                    mLatitudeView.setText(mLatitude);
                                    mLongitudeView.setText(mLongitude);
                                }
                            });
                        }

                        if (mLastLocation != null) {
                            mLatitude = String.valueOf(mLastLocation.getLatitude());
                            mLongitude = String.valueOf(mLastLocation.getLongitude());
                            Log.i(TAG, "You have latitude: " + mLatitude);
                            mLatitudeView.setText(mLatitude);
                            mLongitudeView.setText(mLongitude);
                        } else {
                            Log.i(TAG, "You have to keep trying!");
                        }
                    }

                    private void handleNewLocation(Location location) {
                        Log.d(TAG, location.toString());
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(mCameraButton);
            }
        });

        mLatitudeView.setText("777");
        mLongitudeView.setText("888");
        mDate.setText(currentDate);
        mDescription.setText("Pothole near Starbucks");
        //mId.setText("0989");

    }

    public void sendDataToServer() {
        JSONObject post_dict = new JSONObject();
        try {
            post_dict.put("user", "0989");
            post_dict.put("latitude", 777);
            post_dict.put("longitude", 888);
            post_dict.put("type", "street");
            post_dict.put("description", mDescription.getText().toString());
            post_dict.put("imagetype", "jpg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (post_dict.length() > 0) {
            new SendDataToServer().execute(String.valueOf(post_dict));
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public String filename() {
        return "JPEG_FILE.jpg";
    }

    private File imageFile() {
        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) return null;
        return new File(externalFilesDir, filename());
    }

    public void dispatchTakePictureIntent(View button) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = imageFile();
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File photoFile = imageFile();
            if (photoFile != null) {
                Log.i("SubmitPotHoleActivity", "About to add photo");

                mImageView.setImageDrawable(null);
                mImageView.setImageURI(Uri.fromFile(imageFile()));
            }
        }
    }

    protected void onStart() {
        mClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }

    public void onResume() {
        super.onResume();
        mImageView.invalidate();

        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GooglePlayServicesUtil
                    .getErrorDialog(errorCode, this, REQUEST_ERROR,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    finish();
                                }
                            });

            errorDialog.show();
        }
    }

    private class SendDataToServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            return null;

        }
    }
}

