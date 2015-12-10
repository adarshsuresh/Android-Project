package com.csci571.weatherforecast.forecastsearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.net.Uri;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Spinner spinner;
    private Button about,clear,search;
    private ImageView forecastimg;
    private TextView errmsg;
    private EditText street,city;
    private RadioButton  seldegree;
    private RadioGroup degree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        street=(EditText) findViewById(R.id.editText);
        city=(EditText) findViewById(R.id.editText2);
        spinner=(Spinner) findViewById(R.id.spinner);
        about=(Button)findViewById(R.id.about);
        search=(Button)findViewById(R.id.search);
        forecastimg = (ImageView)findViewById(R.id.forecastlogo);
        clear=(Button)findViewById(R.id.clear);
        degree=(RadioGroup) findViewById(R.id.degree);
        errmsg=(TextView)findViewById(R.id.errormsg);
        about.setOnClickListener(this);
        search.setOnClickListener(this);
        forecastimg.setOnClickListener(this);
        clear.setOnClickListener(this);
        street.setText("720 w 27th");
        city.setText("Los Angeles");
        spinner.setSelection(5, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    private void aboutButtonClick(){

        startActivity(new Intent(getApplicationContext(),AboutActivity.class));
    }
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
    private void clearButtonClick()
    {
        street.setText("");
        city.setText("");
        errmsg.setText("");
        spinner.setSelection(0, true);
        degree.check(R.id.farenheitradio);
    }
    private void searchButtonClick()
    {
        String streetstr=street.getText().toString();
        streetstr.trim();
        String citystr=city.getText().toString();
        citystr.trim();
        String statestr=spinner.getSelectedItem().toString();
        int selectedId = degree.getCheckedRadioButtonId();
        seldegree = (RadioButton) findViewById(selectedId);
        String degstr;
        degstr = seldegree.getText().toString();
        Log.v("Adastreet:", streetstr);
        Log.v("Adacirt:", citystr);
        Log.v("Adastate:", statestr);
        Log.v("Adadegree:", degstr);
       if( validateInput(streetstr, citystr, statestr))
       {getForecast(streetstr,citystr,statestr,degstr);
           errmsg.setText("");
       }
    }
    private boolean validateInput(String street,String city,String state)
    {
        if(street.matches(""))
        {
            errmsg.setText(getResources().getString(R.string.errorstreet));
            Log.v("Adalog:", "Please Enter a street address");
            return false;}
        if(city.matches("")){
            errmsg.setText(getResources().getString(R.string.errorcity));
            Log.v("Adalog:", "Please Enter a city");
            return false;
        }
        if(state.matches("Select a State"))
        {
            errmsg.setText(getResources().getString(R.string.errorstate));
            Log.v("Adalog:", "Please select a state");
            return false;
        }
    return true;
    }
    private void getForecast(String street,String city,String state,String degstr){
      String url="http://cs-server.usc.edu:36252/weatherforecast/getResult.php";
        try {
            street = URLEncoder.encode(street, "utf-8");
            city = URLEncoder.encode(city, "utf-8");
            state= URLEncoder.encode(state, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(degstr.matches("Farenheit"))
            degstr="us";
        else
            degstr="si";
        url+="?&saddress="+street +"&city="+city+"&state="+state+"&degree="+degstr+"&submit=Search";
        Log.v("Adalog:", url);
    new JSONTask().execute(url,city,state);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.about:
                aboutButtonClick();break;
            case R.id.forecastlogo:goToUrl("http://forecast.io");break;
            case R.id.clear:
                clearButtonClick();break;
            case R.id.search:searchButtonClick();break;
        }
    }
    public String getState(String state)
    {
         if(state.matches("Alabama"))return "AL";
          if(state.matches("Alaska"))return "AK";
         if(state.matches("Arizona"))return "AZ";
        if(state.matches("Arkansas"))return "AR";
        if(state.matches("California"))return "CA";
        if(state.matches("Colorado"))return "CO";
        if(state.matches("Connecticut"))return "CT";
        if(state.matches("Delaware"))return "DE";
        if(state.matches("District Of Columbia"))return "DC";
        if(state.matches("Florida"))return "FL";
        if(state.matches("Georgia"))return "GA";
        if(state.matches("Hawaii"))return "HI";
        if(state.matches("Idaho"))return "ID";
        if(state.matches("Illinois"))return "IL";
        if(state.matches("Indiana"))return "IN";
        if(state.matches("Iowa"))return "IA";
        if(state.matches("Kansas"))return "KS";
        if(state.matches("Kentucky"))return "KY";
        if(state.matches("Louisiana"))return "LA";
        if(state.matches("Maine"))return "ME";
        if(state.matches("Maryland"))return "MD";
        if(state.matches("Massachusetts"))return "MA";
        if(state.matches("Michigan"))return "MI";
        if(state.matches("Minnesota"))return "MN";
        if(state.matches("Mississippi"))return "MS";
        if(state.matches("Missouri"))return "MO";
        if(state.matches("Montana"))return "MT";
        if(state.matches("Nebraska"))return "NE";
        if(state.matches("Nevada"))return "NV";
        if(state.matches("New Hampshire"))return "NH";
        if(state.matches("New Jersey"))return "NJ";
        if(state.matches("New Mexico"))return "NM";
        if(state.matches("New York"))return "NY";
        if(state.matches("North Carolina"))return "NC";
        if(state.matches("North Dakota"))return "ND";
        if(state.matches("Ohio"))return "OH";
        if(state.matches("Oklahoma"))return "OK";
        if(state.matches("Oregon"))return "OR";
        if(state.matches("Pennsylvania"))return "PA";
        if(state.matches("Rhode Island"))return "RI";
        if(state.matches("South Carolina"))return "SC";
        if(state.matches("South Dakota"))return "SD";
        if(state.matches("Tennessee"))return "TN";
        if(state.matches("Texas"))return "TX";
        if(state.matches("Utah"))return "UT";
        if(state.matches("Vermont"))return "VT";
        if(state.matches("Virginia"))return "VA";
        if(state.matches("Washington"))return "WA";
        if(state.matches("West Virginia"))return "WV";
        if(state.matches("Wisconsin"))return "WI";
        if(state.matches("Wyoming"))return "WY";
        return null;
    }
    public class JSONTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection=null;
            BufferedReader reader=null;
            try {
                URL url=new URL(params[0]);
                connection=(HttpURLConnection)url.openConnection();
                connection.setConnectTimeout(15 * 1000);
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream istream=connection.getInputStream();
                reader=new BufferedReader(new InputStreamReader(istream));
                String line="";
                StringBuffer buffer=new StringBuffer();
                while((line=reader.readLine())!=null)
                {buffer.append(line);
                }

                return buffer.toString();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
            finally {
                if (connection!=null)
                    connection.disconnect();
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected  void onPostExecute(String s){
            super.onPostExecute(s);
            String finalJSON=s;
            JSONObject pObject=null;
            try {
                 pObject=new JSONObject(finalJSON);
                if(pObject.has("error")) {
                    String error = pObject.getString("error");
                    errmsg.setText(error);
                }
                else {
                    Intent resultIntent = new Intent(getApplicationContext(), ResultActivity.class);
                    resultIntent.putExtra("json", finalJSON.toString());
                    resultIntent.putExtra("city",city.getText().toString());
                    resultIntent.putExtra("state", getState(spinner.getSelectedItem().toString()));
                    startActivity(resultIntent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
