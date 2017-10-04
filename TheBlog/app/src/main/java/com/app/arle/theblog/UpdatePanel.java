package com.app.arle.theblog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.arle.theblog.ActivityResults.ActivityResults;
import com.app.arle.theblog.Async.GetArticle;
import com.app.arle.theblog.Async.GetArticles;
import com.app.arle.theblog.Async.UpdateArticle;
import com.app.arle.theblog.Interface.GetInterface;
import com.app.arle.theblog.Interface.UpdateInterface;
import com.app.arle.theblog.Url.Url;

import org.json.JSONObject;

public class UpdatePanel extends AppCompatActivity implements GetInterface, UpdateInterface{

    //Task to retrieve the articles
    GetArticle getArticle;

    //Task to get the selected article
    UpdateArticle updateArticle;

    //To retrieve the selected article from the server
    int articleId;

    //Layout widgets
    EditText titleedit, bodyedit;
    String titleString, bodyString;
    Button updateButton, resetbutton, backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting the Layout
        setContentView(R.layout.activity_update_panel);
        titleedit = (EditText) findViewById(R.id.titletext);
        bodyedit = (EditText) findViewById(R.id.bodytext);
        getSupportActionBar().hide();

        //Getting the selected id from the previous activity
        Bundle b = getIntent().getExtras();
        String url = Url.ToArticles;
        if(b != null) {
            articleId = b.getInt("id");
            url += articleId;
            getArticle = new GetArticle(url, this);
        } else {
            Toast.makeText(getApplicationContext(), "No article found!", Toast.LENGTH_SHORT).show();
        }
        getArticle.execute();

        updateArticle = new UpdateArticle(this, getApplicationContext(), url);
        updateButton = (Button) findViewById(R.id.updatebutton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateArticle.execute();
                setResult(ActivityResults.activityOk);
                finish();
            }
        });
        resetbutton = (Button) findViewById(R.id.resetbutton);
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleedit.setText(titleString);
                bodyedit.setText(bodyString);
            }
        });
        backbutton = (Button) findViewById(R.id.annullabutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ActivityResults.activityOk);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        setResult(ActivityResults.activityOk);
        finish();
    }

    //Buffer variable
    JSONObject jsontmp;
    @Override
    public void geAllArticles(String all) {

        try {
            jsontmp = new JSONObject(all);
            titleString = jsontmp.getString("title");
            bodyString = jsontmp.getString("body");

            titleedit.setText(titleString);
            bodyedit.setText(bodyString);
        } catch(Exception e) {
            titleedit.setText("Error");
            bodyedit.setText("Error");
        }

    }

    String newTitle, newBody;
    byte b64[];
    String realb64;
    @Override
    public String getData() {
        try {
            JSONObject json = new JSONObject();

            newTitle = titleedit.getText().toString();
            b64 = newTitle.getBytes("UTF-8");
            realb64 = Base64.encodeToString(b64, Base64.DEFAULT);
            json.put("title", realb64);

            newBody = bodyedit.getText().toString();
            b64 = newBody.getBytes("UTF-8");
            realb64 = Base64.encodeToString(b64, Base64.DEFAULT);
            json.put("body", realb64);
            return json.toString();
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            return "Errore " + e.toString();
        }
    }//updatearticles
}
