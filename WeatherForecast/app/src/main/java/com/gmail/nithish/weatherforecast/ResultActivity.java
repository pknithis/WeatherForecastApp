package com.gmail.nithish.weatherforecast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {

    protected  void processJson(String jsonData)
    {
        try {
            JSONObject jsonObj = new JSONObject(jsonData);
            JSONObject currently= jsonObj.getJSONObject("currently");
            String summary = currently.getString("summary");
            Log.d("Summary Json " , summary);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent it = getIntent();
        if(it!=null)
        {
            String jsonData = it.getStringExtra("JSONDATA");
            Log.d("JSONDATA RECEIVED",jsonData);
            processJson(jsonData);
        }
        else
        {
            Log.d("Error Intent ","itent received is null");
        }

    }
}
