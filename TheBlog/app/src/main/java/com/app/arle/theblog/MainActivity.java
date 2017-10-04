package com.app.arle.theblog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.arle.theblog.ActivityResults.ActivityResults;
import com.app.arle.theblog.Async.GetArticles;
import com.app.arle.theblog.Interface.GetInterface;
import com.app.arle.theblog.Model.Article;
import com.app.arle.theblog.Url.Url;
import com.app.arle.theblog.account.AccountActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements GetInterface{

    Button accountbutton, updatebutton, createbutton, deletebutton;
    final static String url = Url.ToArticles;
    GetArticles getArticles;
    Intent intent;

    LinearLayout loglayout, crudlayout;
    TextView crudmessage;
    String crudmessagestring;
    Random random = new Random();
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loglayout = (LinearLayout) findViewById(R.id.log);
        loglayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        loglayout.setOrientation(LinearLayout.VERTICAL);
        crudlayout = (LinearLayout) findViewById(R.id.crudlayout);
        crudmessage = (TextView) findViewById(R.id.crudmessage);
        pref = getSharedPreferences("userdata", MODE_PRIVATE);
        if(pref.getString("currentuser", "").length() != 0) {
            crudlayout.setVisibility(View.VISIBLE);
            crudmessagestring = "\nHello " + pref.getString("username", "user") +", what would you want to do?\n";
            crudmessage.setText(crudmessagestring);
        }
        getSupportActionBar().hide();

        accountbutton = (Button) findViewById(R.id.accountbutton);
        accountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivityForResult(intent, ActivityResults.mainRequesst);
            }
        });
        createbutton = (Button) findViewById(R.id.createbutton);
        createbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivityForResult(intent, ActivityResults.mainRequesst);
            }
        });
        updatebutton = (Button) findViewById(R.id.updatebutton);
        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, UpdateActivity.class);
                startActivityForResult(intent, ActivityResults.mainRequesst);
            }
        });
        deletebutton = (Button) findViewById(R.id.deletebutton);
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, DeleteActivity.class);
                startActivityForResult(intent, ActivityResults.mainRequesst);
            }
        });

        getArticles = new GetArticles(url ,this);
        getArticles.execute();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ActivityResults.mainRequesst) {
            if((resultCode == ActivityResults.activityOk) || (resultCode == ActivityResults.accountNotLogged)) {
                crudlayout.setVisibility(View.INVISIBLE);
                loglayout.removeAllViews();
                getArticles = new GetArticles(url ,this);
                getArticles.execute();
            } else
            if(resultCode == ActivityResults.accountLogged) {
                crudlayout.setVisibility(View.VISIBLE);
                crudmessagestring = "\nHello " + pref.getString("username", "user") +", what would you want to do?\n";
                crudmessage.setText(crudmessagestring);
                loglayout.removeAllViews();
                getArticles = new GetArticles(url ,this);
                getArticles.execute();
            }
        }
    }

    @Override
    public void geAllArticles(String all) {
        String tmp = all;
        TextView v;
        String title, body;
        JSONObject jsontmp;
        try {
            JSONArray ar = new JSONArray(all);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llp.setMargins(3, 3, 3, 3); // llp.setMargins(left, top, right, bottom);
            for(int i = 0; i<ar.length(); i++) {
                jsontmp = ar.getJSONObject(i);
                title = (String) jsontmp.get("title");
                body = (String) jsontmp.get("body");
                v = new TextView(this);
                v.setLayoutParams(llp);
                v.setGravity(Gravity.CENTER);
                v.setBackgroundColor(Color.argb(30, random.nextInt(150) + 30, random.nextInt(150) + 30, random.nextInt(150) + 30));
                v.setText("\n"+title+"\n\n"+body+"\n");
                loglayout.addView(v);
            }
        } catch (JSONException e) {
            v = new TextView(this);
            v.setText(e.toString() + "\n-****************--" + tmp + "-********************");
            loglayout.addView(v);
        }
    }
}
