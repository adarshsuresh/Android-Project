package com.csci571.weatherforecast.forecastsearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends FragmentActivity implements View.OnClickListener {

    JSONObject resObj = null;
    Button moredetails,viewmap;
    ImageView fbicon;
    Bundle extras;
    CallbackManager callbackManager;
    ShareDialog shardialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shardialog= new ShareDialog(this);
        shardialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

                if(result.getPostId()==null)
                    Toast.makeText(ResultActivity.this,"Post Cancelled",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ResultActivity.this,"Facebook Post Sucessful",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(ResultActivity.this,"Post Unsucessful",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(ResultActivity.this,"Post Cancelled",Toast.LENGTH_SHORT).show();
            }
        });
        moredetails=(Button)findViewById(R.id.moredetails);
        viewmap=(Button)findViewById(R.id.viewmap);
        fbicon=(ImageView)findViewById(R.id.fbicon);
        moredetails.setOnClickListener(this);
        viewmap.setOnClickListener(this);
        fbicon.setOnClickListener(this);
        try {
            Intent intent = getIntent();
             extras = intent.getExtras();
            if(extras!=null){
                resObj=new JSONObject(extras.getString("json"));
                Log.v("Adalog:", String.valueOf(resObj));
                setData(extras.getString("city"),extras.getString("state"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.moredetails:
                getMoreDetails();break;
            case R.id.viewmap:break;
            case R.id.fbicon:postfeedback();break;
        }
    }

    public void getMoreDetails()
    {
        Intent resultIntent = new Intent(getApplicationContext(), DetailsActivity.class);
        resultIntent.putExtra("json", resObj.toString());
        resultIntent.putExtra("city",extras.getString("city"));
        resultIntent.putExtra("state", extras.getString("state"));
        startActivity(resultIntent);
    }
    public void setData(String city,String state) throws JSONException {
        JSONObject curObj=null;
        curObj=resObj.getJSONObject("Right Now");
        ImageView icon=(ImageView)findViewById(R.id.imgsummary);
        icon.setImageResource(getResources().getIdentifier(curObj.getString("icon"), "drawable", getPackageName()));
        TextView summary=(TextView)findViewById(R.id.summary);
        TextView temp=(TextView)findViewById(R.id.temp);
        TextView unit=(TextView)findViewById(R.id.unit);
        TextView highlo=(TextView)findViewById(R.id.temphilo);
        summary.setText(curObj.getString("summary")+" in "+city+", "+state);
        temp.setText(curObj.getString("temp"));
        unit.setText(curObj.getString("unit"));
        highlo.setText("L:"+curObj.getString("tempmin")+" | H:"+curObj.getString("tempmax"));
        TextView preccep=(TextView)findViewById(R.id.precepvalue);
        TextView chanceofrain=(TextView)findViewById(R.id.chanceofrainvalue);
        TextView windspeed=(TextView)findViewById(R.id.windspeedvalue);
        TextView dewpoint=(TextView)findViewById(R.id.dewpointvalue);
        TextView humidity=(TextView)findViewById(R.id.humidityvalue);
        TextView visibility=(TextView)findViewById(R.id.visibilityvalue);
        TextView sunrisr=(TextView)findViewById(R.id.sunrisevalue);
        TextView sunset=(TextView)findViewById(R.id.sunsetvalue);
        curObj=curObj.getJSONObject("table");
        preccep.setText(curObj.getString("Precipitation"));
        chanceofrain.setText(curObj.getString("Chance of Rain"));
        windspeed.setText(curObj.getString("Wind Speed"));
        dewpoint.setText(curObj.getString("Dew Point"));
        humidity.setText(curObj.getString("Humidity"));
        visibility.setText(curObj.getString("Visibility"));
        sunrisr.setText(curObj.getString("Sunrise"));
        sunset.setText(curObj.getString("Sunset"));
    }
    public void postfeedback() {

            JSONObject curObj=null;
            try {
                curObj=resObj.getJSONObject("Right Now");

            String contenttitle="Current Weather in " + extras.getString("city")+", "+extras.getString("state");
            String desc=curObj.getString("summary") +", "+ curObj.getString("temp")+curObj.getString("unit");
            ShareLinkContent linkContent= new ShareLinkContent.Builder()
                    .setContentTitle(contenttitle)
                    .setContentDescription(desc)
                    .setContentUrl(Uri.parse("http://forecast.io"))
                    .setImageUrl(Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/" +curObj.getString("icon")+".png")).build();
            shardialog.show(linkContent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
