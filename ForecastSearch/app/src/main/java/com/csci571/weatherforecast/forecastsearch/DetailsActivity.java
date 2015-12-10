package com.csci571.weatherforecast.forecastsearch;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button next24,next7;
    Button btn;
    private RelativeLayout tab1,tab2;
    Bundle extras;
    JSONObject resObj = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent ();
         extras = intent.getExtras();
        next24=(Button)findViewById(R.id.next24);
        next7=(Button)findViewById(R.id.next7);
        tab1=(RelativeLayout)findViewById(R.id.tab1);
        tab2=(RelativeLayout)findViewById(R.id.tab2);
        next24.setOnClickListener(this);
        next7.setOnClickListener(this);
        TextView moredetails=(TextView)findViewById(R.id.moredetails);
        moredetails.setText("More Details for "+extras.getString("city")+", "+extras.getString("state"));
        try {
            resObj=new JSONObject(extras.getString("json"));
            createlayoutfor24();
            createlayoutfor7();
            tab2.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
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
            case R.id.next24:tab1.setVisibility(View.VISIBLE);tab2.setVisibility(View.GONE);next24.setBackground(this.getResources().getDrawable(R.drawable.custombuttonclicked));next7.setBackground(this.getResources().getDrawable(R.drawable.custombutton));break;
            case R.id.next7:tab2.setVisibility(View.VISIBLE);tab1.setVisibility(View.GONE);next7.setBackground(this.getResources().getDrawable(R.drawable.custombuttonclicked));next24.setBackground(this.getResources().getDrawable(R.drawable.custombutton));break;
        }
    }

    public void createlayoutfor24 () throws JSONException {
        JSONArray curObj = resObj.getJSONArray("Next 24 Hours");
        JSONObject myobj;
        int i;
        myobj=resObj.getJSONObject("Right Now");
        TextView temp=(TextView)findViewById(R.id.temp);
        temp.setText("Temp(" + myobj.getString("unit") + ")");
        TableLayout next24table=(TableLayout)findViewById(R.id.next24table);
        for ( i = 0; i <curObj.length()&& i<24; i++) {

             myobj=curObj.getJSONObject(i);
            JSONObject mainobj=myobj.getJSONObject("maindata");
            TableRow row= new TableRow(this);
            TableLayout.LayoutParams tableRowParams=
                    new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            tableRowParams.setMargins(0, 5, 0, 5);
            row.setPadding(0, 2, 0, 2);
            row.setLayoutParams(tableRowParams);


            TextView tv = new TextView(getApplicationContext());
            tv.setText(mainobj.getString("Time"));
            tv.setGravity(Gravity.LEFT);
            tv.setTextSize(20);
            tv.setTextColor(Color.parseColor("#5D5D5D"));
            tv.setTypeface(null, Typeface.BOLD);
            row.addView(tv);

            ImageView imgview = new ImageView(getApplicationContext());
            imgview.setImageResource(getResources().getIdentifier(mainobj.getString("Summary"), "mipmap", getPackageName()));
            TableRow.LayoutParams imagelayout=new TableRow.LayoutParams(250, 150);
            imagelayout.gravity=Gravity.CENTER;
            row.addView(imgview, imagelayout);

            TextView tv2 = new TextView(getApplicationContext());
            tv2.setGravity(Gravity.RIGHT);
            tv2.setWidth(ActionBar.LayoutParams.FILL_PARENT);
            tv2.setTextSize(20);
            tv2.setTextColor(Color.parseColor("#CBCBCB"));
            tv2.setText(mainobj.getString("Temp"));
            row.addView(tv2);
            Log.v("Adalog:", mainobj.getString("Summary"));
            if (i%2==0) {
                row.setBackgroundResource(R.color.gray);
            }
            else {
                row.setBackgroundResource(R.color.white);
            }
            next24table.addView(row,i+1);
        }
        TableRow row= new TableRow(this);
        TableLayout.LayoutParams tableRowParams=
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
        tableRowParams.setMargins(0, 5, 0, 5);
        row.setPadding(0, 2, 0, 2);
        row.setLayoutParams(tableRowParams);
        TextView dummy = new TextView(getApplicationContext());
        dummy.setGravity(Gravity.LEFT);
        row.addView(dummy);
         btn =  new Button(this, null, android.R.attr.buttonStyleSmall);
        TableRow.LayoutParams param = new TableRow.LayoutParams(100, 100);
        btn.setText("+");
        btn.setLayoutParams(param);
        btn.setGravity(Gravity.CENTER);
        btn.setTextSize(20);
        btn.setTextColor(Color.parseColor("#000000"));
        btn.setBackgroundColor(Color.parseColor("#84ECE6"));
        row.setBackgroundResource(R.color.gray);
        row.addView(btn);
        next24table.addView(row,i+1);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                JSONArray curObj;
                try {
                    curObj = resObj.getJSONArray("Next 24 Hours");

                    JSONObject myobj;
                    TableLayout next24table = (TableLayout) findViewById(R.id.next24table);
                    next24table.removeViewAt(25);
                    for (int i = 24; i < curObj.length() && i < 48; i++) {

                        myobj = curObj.getJSONObject(i);
                        JSONObject mainobj = myobj.getJSONObject("maindata");
                        TableRow row = new TableRow(getApplicationContext());
                        TableLayout.LayoutParams tableRowParams =
                                new TableLayout.LayoutParams
                                        (TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        tableRowParams.setMargins(0, 5, 0, 5);
                        row.setPadding(0, 2, 0, 2);
                        row.setLayoutParams(tableRowParams);


                        TextView tv = new TextView(getApplicationContext());
                        tv.setText(mainobj.getString("Time"));
                        tv.setGravity(Gravity.LEFT);
                        tv.setTextSize(20);
                        tv.setTextColor(Color.parseColor("#5D5D5D"));
                                tv.setTypeface(null, Typeface.BOLD);
                        row.addView(tv);

                        ImageView imgview = new ImageView(getApplicationContext());
                        imgview.setImageResource(getResources().getIdentifier(mainobj.getString("Summary"), "mipmap", getPackageName()));
                        TableRow.LayoutParams imagelayout = new TableRow.LayoutParams(250, 150);
                        imagelayout.gravity = Gravity.CENTER;
                        row.addView(imgview, imagelayout);

                        TextView tv2 = new TextView(getApplicationContext());
                        tv2.setGravity(Gravity.RIGHT);
                        tv2.setWidth(ActionBar.LayoutParams.FILL_PARENT);
                        tv2.setTextSize(20);
                        tv2.setTextColor(Color.parseColor("#5D5D5D"));
                        tv2.setText(mainobj.getString("Temp"));
                        row.addView(tv2);
                        Log.v("Adalog:", mainobj.getString("Summary"));
                        if (i % 2 == 0) {
                            row.setBackgroundResource(R.color.gray);
                        } else {
                            row.setBackgroundResource(R.color.white);
                        }
                        next24table.addView(row, i + 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void createlayoutfor7 () throws JSONException {
        JSONArray curObj = resObj.getJSONArray("Next 7 Days");
        TableLayout next7table=(TableLayout)findViewById(R.id.next7table);
        for (int i = 0; i <curObj.length(); i++) {

            JSONObject myobj=curObj.getJSONObject(i);
            TableRow row= new TableRow(this);
            TableLayout.LayoutParams tableRowParams=
                    new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

            tableRowParams.setMargins(0, 15, 0, 15);
            row.setPadding(0,5,0,20);
            row.setLayoutParams(tableRowParams);

            TextView tv = new TextView(getApplicationContext());
            tv.setText(Html.fromHtml("<b>" + myobj.getString("Day") + " , " + myobj.getString("Month Date") + "</b><br/><br/>Min: " + myobj.getString("Min Temp") + " | Max:" + myobj.getString("Max Temp")));
            tv.setGravity(Gravity.LEFT);
            tv.setTextSize(21);
            tv.setTextColor(Color.parseColor("#5D5D5D"));
            row.addView(tv);

            ImageView imgview = new ImageView(getApplicationContext());
            imgview.setImageResource(getResources().getIdentifier(myobj.getString("Icon Image"), "mipmap", getPackageName()));
            TableRow.LayoutParams imagelayout=new TableRow.LayoutParams(150, 150);
            imagelayout.gravity=Gravity.RIGHT;
            row.addView(imgview, imagelayout);
            if(i==0) {row.setBackgroundResource(R.color.next71);}
            else if(i==1){row.setBackgroundResource(R.color.next72);}
            else if(i==2){row.setBackgroundResource(R.color.next73);}
            else if(i==3){row.setBackgroundResource(R.color.next74);}
            else if(i==4){row.setBackgroundResource(R.color.next75);}
            else if(i==5){row.setBackgroundResource(R.color.next76);}
            else if(i==6){row.setBackgroundResource(R.color.next77);}
            else{row.setBackgroundResource(R.color.gray);}
            next7table.addView(row,i);
        }
    }


}
