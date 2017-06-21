package com.imceits.android.imagerecycler;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView imgView;
    TextView txtView;
    TextView txtMarker;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ProgressDialog progressDialog;
    ArrayList<RecycleData> imageList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        getImageUrlData();

    }

    private void setRecyclerView(){
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        final MyAdapter adapter = new MyAdapter(getApplicationContext(), imageList);
        recyclerView.setAdapter(adapter);
    }

    private void getImageUrlData(){
        String publicUrl = "https://api.unsplash.com/photos/?client_id=058d27d7a23d475453a0dc5f145844c3a8fd19fdc47b902086f01bc2c805fb06";
        new BackGroundTask().execute(publicUrl);
    }

    private class BackGroundTask extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... publicUrl) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(publicUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null){
                    builder.append(line);
                }
                reader.close();
               return builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(result);
                imageList = new ArrayList<>();
                for(int i=0; i<jsonArray.length();i++){
                    RecycleData data = new RecycleData();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject jsonUrls = jsonObject.getJSONObject("urls");
                   String imageUrl = jsonUrls.getString("regular");
                    Log.i("test", imageUrl);
                    data.setImageUrl(imageUrl);
                    data.setLabel(" Image : " + (i+1));
                    imageList.add(data);
                }
                setRecyclerView();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
