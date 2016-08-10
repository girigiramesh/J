package com.example.apple.json_asynctask;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private class AsyncHttpTask extends AsyncTask<String, String, String> {
        InputStream is = null;
        int len = 500;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                Log.d(TAG, "onPostExecute: " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    responseinfo.setText(jsonObject.optString("city"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://ip-api.com/json");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("GET");

                if (200 == httpURLConnection.getResponseCode()) {
                    return readInputStream(httpURLConnection.getInputStream());
                } else {
                    return "";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }
    }

    private static final String TAG = "MainActivity";
    private TextView responseinfo;
    private Button getinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        responseinfo = (TextView) findViewById(R.id.responseinfo);
        getinfo = (Button) findViewById(R.id.getinfo);
        getinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpTask getInfo = new AsyncHttpTask();
                getInfo.execute();
            }
        });

    }

    public static String readInputStream(InputStream stream) throws IOException {
        try {
            int intChar;
            StringBuilder responseBuilder = new StringBuilder();
            while ((intChar = stream.read()) != -1) responseBuilder.append((char) intChar);
            return responseBuilder.toString();
        } finally {
            if (stream != null) stream.close();
        }
    }
}