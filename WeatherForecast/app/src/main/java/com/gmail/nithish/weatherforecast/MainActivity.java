package com.gmail.nithish.weatherforecast;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.transform.Result;

class JsonDataDownloader extends AsyncTask<String,Void,Void> {

    private Activity parentActivity;
    JsonDataDownloader(Activity activity)
    {
        parentActivity = activity;
    }
    @Override
    protected Void doInBackground(String... params) {

        Log.d("JsonData","Async entered");
        String urlDetails = params[0];
        Log.d("JsonData url",urlDetails);
        String urlParams[] = urlDetails.split(":");
        String streetAddress= urlParams[0];
        String city=urlParams[1];
        String state=urlParams[2];
        String temp=urlParams[3];
        String url="http://nithishkp.elasticbeanstalk.com/index/processhw8.php?Address="+URLEncoder.encode(streetAddress)+"&City="+URLEncoder.encode(city)+"&State="+URLEncoder.encode(state)+"&Temperature="+temp;


        Log.d("JsonData URL string",url);
        URL urlOb= null;
        try {
            urlOb = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONObject job=null;
        StringBuilder jsonData=new StringBuilder();
        BufferedInputStream bin = null;
        try {
            HttpURLConnection httpconnection = (HttpURLConnection)urlOb.openConnection();
            httpconnection.connect();
            int response = httpconnection.getResponseCode();
            Log.d("Http Response",response+"");
            InputStream ip = httpconnection.getInputStream();
            bin = new BufferedInputStream(ip);
            int  data = bin.read();

            while (data!=-1)
            {
                jsonData.append((char)data);
                data = bin.read();
            }

            Intent toResultActivity = new Intent(this.parentActivity, ResultActivity.class);
            toResultActivity.putExtra("JSONDATA",jsonData.toString());
            parentActivity.startActivity(toResultActivity);


            Log.d("JSON DATA",jsonData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }



        return null;
    }


}
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void aboutClickHandler(View view) {
        startActivity(new Intent(this,AboutActivity.class));
    }

    public void searchClickHandler(View view) {
        String streetAddress = ((EditText)findViewById(R.id.edtAddress)).getText()+"";
        String city = ((EditText)findViewById(R.id.edtCity)).getText()+"";
        String state = ((Spinner)findViewById(R.id.spState)).getSelectedItem().toString();
        String temp="";
        if(((RadioButton)findViewById(R.id.fah)).isChecked())
        {
           temp="Fahrenheit";
        }
        else
        {
            temp="Celsius";
        }
        TextView errorText = (TextView)findViewById(R.id.txtError);
        if(streetAddress.length() == 0)
        {
            errorText.setText("Please enter Street Address");
            return;
        }
        else if(city.length() == 0)
        {
            errorText.setText("Please enter City");
            return;
        }
        else if((((RadioButton)findViewById(R.id.fah)).isChecked()) == false && (((RadioButton)findViewById(R.id.cel)).isChecked())==false)
        {
            errorText.setText("Please select the Temperature");
            return;
        }
        String finalUrl = streetAddress+":"+city+":"+"CA:"+temp;
        Toast.makeText(getApplicationContext(), finalUrl,Toast.LENGTH_LONG).show();
        new JsonDataDownloader(this).execute(finalUrl);
    }
}
