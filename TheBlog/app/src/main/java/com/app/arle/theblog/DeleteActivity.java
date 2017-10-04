package com.app.arle.theblog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
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
import com.app.arle.theblog.Async.DeleteArticle;
import com.app.arle.theblog.Async.GetArticle;
import com.app.arle.theblog.Async.GetArticles;
import com.app.arle.theblog.Interface.DeleteInterface;
import com.app.arle.theblog.Interface.GetInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class DeleteActivity extends AppCompatActivity implements GetInterface, DeleteInterface{
    GetArticles getArticles;
    LinearLayout deletelayout;
    DeleteInterface deleteInterface;
    Button annullabutton;
    Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        getSupportActionBar().hide();
        annullabutton = (Button) findViewById(R.id.annullabutton);
        annullabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ActivityResults.accountLogged);
                finish();
            }
        });
        deletelayout = (LinearLayout) findViewById(R.id.deletelayout);
        getArticles = new GetArticles(MainActivity.url, this);
        getArticles.execute();
        deleteInterface = this;
    }

    @Override
    public void onBackPressed() {
        setResult(ActivityResults.accountLogged);
        finish();
    }

    int articleId = -1;
    Intent intent;
    public String url = "http://192.168.0.4:8989/api/articles/";
    AlertDialog.Builder dialog;

    @Override
    public void geAllArticles(String all) {
        Button v0;
        dialog = new AlertDialog.Builder(DeleteActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Dialog on Android");
        dialog.setMessage("Are you sure you want to delete this item?" );
        dialog.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });

        final AlertDialog alert = dialog.create();



        String title, body;
        JSONObject jsontmp;


        try {
            JSONArray ar = new JSONArray(all);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llp.setMargins(3, 3, 3, 3); // llp.setMargins(left, top, right, bottom);
            for(int i = 0; i<ar.length(); i++) {
                jsontmp = ar.getJSONObject(i);
                title = (String) jsontmp.get("title");
                articleId = (int) jsontmp.get("id");
                v0 = new Button(this);
                v0.setLayoutParams(llp);
                v0.setGravity(Gravity.CENTER);
                v0.setBackgroundColor(Color.argb(30, random.nextInt(200) +50, 0, 0));
                v0.setText("\n"+title+"\n");
                v0.setId(articleId);
                v0.setOnClickListener(new View.OnClickListener() {
                    Button vt;
                    @Override
                    public void onClick(View v) {;
                        vt = (Button) v;
                        dialog.setTitle("Deleting: '" + ((Button) v).getText().toString().trim() + "'");
                        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //Action for "Delete".
                                Toast.makeText(getApplicationContext(), "Deleting...", Toast.LENGTH_SHORT).show();
                                new DeleteArticle(deleteInterface, vt.getId()).execute();
                            }
                        });
                        dialog.create().show();
                    }
                });
                deletelayout.addView(v0);
            }
        } catch (JSONException e) {
            v0 = new Button(this);
            v0.setText(e.toString());
            deletelayout.addView(v0);
        }
    }


    @Override
    public void deleteArticle(int result) {
        if(result == 204) {
            setResult(ActivityResults.accountLogged);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Error: Cannot delete the article!", Toast.LENGTH_SHORT).show();

        }
    }
}
