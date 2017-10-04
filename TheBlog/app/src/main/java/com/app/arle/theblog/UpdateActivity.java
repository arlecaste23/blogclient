package com.app.arle.theblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.arle.theblog.ActivityResults.ActivityResults;
import com.app.arle.theblog.Async.GetArticle;
import com.app.arle.theblog.Async.GetArticles;
import com.app.arle.theblog.Interface.GetInterface;
import com.app.arle.theblog.Url.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class UpdateActivity extends AppCompatActivity implements GetInterface{
    GetArticles getArticles;
    LinearLayout updatelayout;
    Random random = new Random();
    Button backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        getSupportActionBar().hide();

        backbutton = (Button) findViewById(R.id.annullabutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ActivityResults.accountLogged);
                finish();
            }
        });

        updatelayout = (LinearLayout) findViewById(R.id.updatelayout);
        getArticles = new GetArticles(MainActivity.url, this);
        getArticles.execute();


    }
    int articleId = -1;
    Intent intent;
    private String url = Url.ToArticles;

    @Override
    public void geAllArticles(String all) {
        Button v;
        String title;
        JSONObject jsontmp;
        try {
            JSONArray ar = new JSONArray(all);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llp.setMargins(3, 3, 3, 3); // llp.setMargins(left, top, right, bottom);
            for(int i = 0; i<ar.length(); i++) {
                jsontmp = ar.getJSONObject(i);
                title = (String) jsontmp.get("title");
                articleId = (int) jsontmp.get("id");
                v = new Button(this);
                v.setLayoutParams(llp);
                v.setGravity(Gravity.CENTER);
                v.setBackgroundColor(Color.argb(30, 0, 0, random.nextInt(200) + 50));
                v.setText("\n"+title+"\n");
                v.setId(articleId);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(UpdateActivity.this, UpdatePanel.class);
                        if(articleId != -1) {
                            intent.putExtra("id", v.getId());
                            startActivityForResult(intent, ActivityResults.mainRequesst);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Error on id", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                updatelayout.addView(v);
            }
        } catch (JSONException e) {
            v = new Button(this);
            v.setText(e.toString());
            updatelayout.addView(v);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ActivityResults.mainRequesst) {
            if(resultCode == ActivityResults.activityOk) {
                updatelayout.removeAllViews();
                getArticles = new GetArticles(MainActivity.url ,this);
                getArticles.execute();
            }
        }
    }
    @Override
    public void onBackPressed() {
        setResult(ActivityResults.accountLogged);
        finish();
    }
}
