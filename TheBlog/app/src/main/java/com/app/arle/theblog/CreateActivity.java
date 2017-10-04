package com.app.arle.theblog;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.arle.theblog.ActivityResults.ActivityResults;
import com.app.arle.theblog.Async.CreateArticle;
import com.app.arle.theblog.Interface.CreateInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class CreateActivity extends AppCompatActivity implements CreateInterface{

    EditText title, body;
    String titletext, bodytext;
    Button createbutton, resetbutton, backbutton;
    CreateArticle createarticle;
    Random random;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        getSupportActionBar().hide();
        random = new Random();
        title = (EditText) findViewById(R.id.titletext);
        title.setBackgroundColor(Color.argb(30, 0, random.nextInt(200) + 50, 0));
        body = (EditText) findViewById(R.id.bodytext);
        body.setBackgroundColor(Color.argb(30, 0, random.nextInt(200) + 50, 0));
        createarticle = new CreateArticle(this, getApplicationContext());
        createbutton = (Button) findViewById(R.id.createbutton);
        createbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Title should not be empty!", Toast.LENGTH_SHORT).show();
                } else if(body.length() ==0 ) {
                    Toast.makeText(getApplicationContext(), "Body should not be empty!", Toast.LENGTH_SHORT).show();

                } else {
                    createarticle.execute();

                }
            }
        });
        resetbutton = (Button) findViewById(R.id.resetbuttoncreate);
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText("");
                body.setText("");
            }
        });
        backbutton = (Button) findViewById(R.id.annullabuttoncreate);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ActivityResults.accountLogged);
                finish();
            }
        });
    }

    @Override
    public void createArticle() {
        setResult(ActivityResults.accountLogged);
        finish();
    }

    //Buffers
    byte b64[];
    String realb64;
    JSONObject json;

    @Override
    public String getData() {
        try {
            json = new JSONObject();
            titletext = title.getText().toString();
            b64 = titletext.getBytes("UTF-8");
            realb64 = Base64.encodeToString(b64, Base64.DEFAULT);
            json.put("title", realb64);

            bodytext = body.getText().toString();
            b64 = bodytext.getBytes("UTF-8");
            realb64 = Base64.encodeToString(b64, Base64.DEFAULT);
            json.put("body", realb64);

            return json.toString();

        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            return "Errore " + e.toString();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(ActivityResults.accountLogged);
        finish();
    }
}
