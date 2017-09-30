package com.app.arle.lastblog;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener{
    Button accountbutton;
    SettingsFragment settingsfragment;
    ArticlesFragment articlesfragment;
    CreateFragment createfragment;
    LinearLayout container;
    Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = (LinearLayout) findViewById(R.id.container);
      //  container.setBackgroundColor(Color.argb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 10));

        settingsfragment = new SettingsFragment();
        articlesfragment = new ArticlesFragment();
        createfragment = new CreateFragment();

        accountbutton = (Button) findViewById(R.id.accountbutton);
        accountbutton.setBackgroundColor(Color.argb(random.nextInt(255), random.nextInt(255),random.nextInt(255), 150));
        accountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* FragmentTransaction ft = getSupportFragmentManager().beginTransaction().add(R.id.settingslayout, settingsfragment, "settings");
                ft.commit();

                getSupportFragmentManager().beginTransaction().remove(articlesfragment).commit();*/
              //  container.setBackgroundColor(Color.argb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 10));
                /*Space space = new Space(getApplicationContext());
                FrameLayout f = (FrameLayout) findViewById(R.id.articleslayout);
                f.addView(space);*/
                Intent myIntent = new Intent(MainActivity.this, AccountActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });
        FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction().add(R.id.articleslayout, articlesfragment, "articles");
        ft0.commit();
        getSupportActionBar().hide();
    }

    @Override
    public ArticlesFragment getArticlesFragment() {
        return articlesfragment;
    }

    @Override
    public SettingsFragment getSettingsFragment() {
        return settingsfragment;
    }
    @Override
    public CreateFragment getCreateFragment() {
        return createfragment;
    }

    @Override
    public int getArticlesLayout() {
        return R.id.articleslayout;
    }
}
