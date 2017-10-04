package com.app.arle.theblog.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.arle.theblog.ActivityResults.ActivityResults;
import com.app.arle.theblog.Async.DetailsTask;
import com.app.arle.theblog.Async.LoginTask;
import com.app.arle.theblog.Async.LogoutTask;
import com.app.arle.theblog.Interface.DetailsInterface;
import com.app.arle.theblog.Interface.LoginInterface;
import com.app.arle.theblog.Interface.LogoutInterface;
import com.app.arle.theblog.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity implements LoginInterface, DetailsInterface, LogoutInterface {

    //Results to MainActivity
    final static int mainLogged = 301;
    final static int mainNotLogged = 302;

    //Two layouts in this class
    //When there is no user and when there is a current user already logged in
    LinearLayout loginlayout, accountlayout;

    //Loginlayout
    Button loginbutton, registerbutton, backbutton;
    EditText email, password;
    String mail, pwd;
    LoginTask loginTask;
    LoginInterface loginterface;

    //Accountlayout
    TextView usernameaccount, emailaccount;
    String usernameaccountstring, emailaccountstring;
    int idaccount;
    Button okaccount, logoutaccount;
    DetailsInterface detailsInterface;

    //Logout procedure
    LogoutTask logoutTask;
    LogoutInterface logoutInterface;

    Intent intent;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String currentlogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().hide();
        pref = getSharedPreferences("userdata", MODE_PRIVATE);
        loginterface = this;
        detailsInterface = this;
        logoutInterface = this;

        //I get the references to the previous layouts
        loginlayout = (LinearLayout) findViewById(R.id.loginlayout);
        accountlayout = (LinearLayout) findViewById(R.id.accountlayout);

        //Widgets of login layout
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        loginbutton = (Button) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Email should not be empty!", Toast.LENGTH_SHORT).show();
                } else if (password.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Email should not be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    loginTask = new LoginTask(email.getText().toString(), password.getText().toString(), loginterface);
                    loginTask.execute();
                }
            }
        });

        registerbutton = (Button) findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AccountActivity.this, RegisterActivity.class);
                startActivityForResult(intent, ActivityResults.accountRequest);
            }
        });
        backbutton = (Button) findViewById(R.id.annullabuttonaccount);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getString("currentuser", "").length() == 0) {
                    setResult(ActivityResults.accountNotLogged);
                } else {
                    setResult(ActivityResults.accountLogged);
                }
                finish();
            }
        });

        //Widgets of accountlayout
        usernameaccount = (TextView) findViewById(R.id.usernameaccount);
        emailaccount = (TextView) findViewById(R.id.emailaccount);
        okaccount = (Button) findViewById(R.id.okaccount);
        okaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlogged.length() == 0) {
                    setResult(ActivityResults.accountLogged);
                }
                else {
                    setResult(ActivityResults.accountNotLogged);
                }
                finish();
            }
        });
        logoutaccount = (Button) findViewById(R.id.logoutaccount);
        logoutaccount.setOnClickListener(new View.OnClickListener() {

            int userid;
            @Override
            public void onClick(View v) {
                userid = pref.getInt("id", -1);
                if(userid == -1) {
                    Toast.makeText(getApplicationContext(), "Could not logout! You should have to login first!", Toast.LENGTH_SHORT).show();
                } else {
                    logoutTask = new LogoutTask(userid, logoutInterface);
                    logoutTask.execute();
                }

            }
        });

        currentlogged = pref.getString("currentuser", "");

        //I check if there is a current user
        if (currentlogged.length() == 0) {
            loginlayout.setVisibility(View.VISIBLE);
            accountlayout.setGravity(View.GONE);
        }//not logged
        //Otherwise i redirect to account data state
        else {
            accountlayout.setVisibility(View.VISIBLE);
            loginlayout.setVisibility(View.GONE);
            emailaccount.setText(pref.getString("email", "undefined"));
            usernameaccount.setText(pref.getString("username", "undefined"));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ActivityResults.accountRequest) {
            if(resultCode == ActivityResults.accountLogged) {
                accountlayout.setVisibility(View.VISIBLE);

                usernameaccountstring = pref.getString("username", "user");
                usernameaccount.setText(usernameaccountstring);
                emailaccountstring = pref.getString("currentuser", "user email");
                emailaccount.setText(emailaccountstring);

                loginlayout.setVisibility(View.GONE);
            } else if(resultCode == ActivityResults.accountNotLogged) {
                accountlayout.setVisibility(View.GONE);
                loginlayout.setVisibility(View.VISIBLE);
            }
        }
    }

    //Da migliorare
    @Override
    public void onBackPressed() {
        if(pref.getString("currentuser", "").length() == 0) {
            setResult(ActivityResults.accountNotLogged);
        } else {
            setResult(ActivityResults.accountLogged);
        }
        finish();
    }

    @Override
    public void loginLog(int result) {

    }

    //Il risultato del logintask
    TextView t;

    @Override
    public void logindata(String data) {
        if (data.equalsIgnoreCase("Errore")) {
            Toast.makeText(getApplicationContext(), "Fail to login!", Toast.LENGTH_SHORT).show();
        } else {
            t = (TextView) findViewById(R.id.data);
            try {
                JSONObject json = new JSONObject(data);
                JSONObject success = new JSONObject(json.getString("success"));
                String token = success.getString("token");
                loginlayout.setVisibility(View.GONE);
                accountlayout.setVisibility(View.VISIBLE);

                new DetailsTask(token, detailsInterface).execute();
            } catch (JSONException e) {
                t.setText(e.toString());
            }

        }
    }

    @Override
    public void getDetails(String details) {

        if (details.equalsIgnoreCase("Errore")) {

        } else {
            try {
                JSONObject jsondetails = new JSONObject(details);
                JSONObject userdata = new JSONObject(jsondetails.getString("success"));
                usernameaccountstring = userdata.getString("name");
                emailaccountstring = userdata.getString("email");
                idaccount =  userdata.getInt("id");
                usernameaccount.setText(usernameaccountstring);
                emailaccount.setText(emailaccountstring);
                editor = pref.edit();
                editor.putString("currentuser", emailaccountstring);
                editor.putString("username", usernameaccountstring);
                editor.putInt("id", idaccount);
                editor.commit();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Could not retrive the current user!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void toLogout(int result) {
        if(result < 0) {
            Toast.makeText(getApplicationContext(), "Could not logout, you should login first!", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("currentuser");
            editor.remove("id");
            editor.commit();
            setResult(ActivityResults.accountNotLogged);
            finish();
        }
    }
}



