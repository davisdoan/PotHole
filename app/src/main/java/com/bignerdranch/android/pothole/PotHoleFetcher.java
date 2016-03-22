package com.bignerdranch.android.pothole;

import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PotHoleFetcher {

    private static final String TAG = "PotHoleFetcher";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage()+ ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    private void parseItems(List<PotHole> items, JSONArray jsonBody) throws IOException, JSONException {
        for(int i = 0; i < jsonBody.length(); i++){
            JSONObject myJson = jsonBody.getJSONObject(i);
            String id = myJson.getString("id");
            String latitude = myJson.getString("latitude");
            String longitute = myJson.getString("longitude");
            String description = myJson.getString("description");
            String date = myJson.getString("created");

            Log.i(TAG,"You have *******: " + id );
            Log.i(TAG,"You have *******: " + latitude );

            PotHole potholeItem = new PotHole();

            potholeItem.setId(id);
            potholeItem.setLatitude(latitude);
            potholeItem.setLongtitute(longitute);
            potholeItem.setDescription(description);
            potholeItem.setDate(date);

            items.add(potholeItem);
        }
    }

    public List<PotHole> fetchItems(){
        List<PotHole> items = new ArrayList<>();

        try{
            String url = Uri.parse("http://bismarck.sdsu.edu/city/batch")
                    .buildUpon()
                    .appendQueryParameter("type","street")
                    .appendQueryParameter("user", "rew")
                    .appendQueryParameter("size","10")
                    .appendQueryParameter("batch-number","0")
                    .appendQueryParameter("end-id","15")
                    .build()
                    .toString();

            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONArray jsonBody = new JSONArray(jsonString);
            parseItems(items, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "FAiled to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG,"Failed to parse JSON", je);
        }
        return items;
    }

}
