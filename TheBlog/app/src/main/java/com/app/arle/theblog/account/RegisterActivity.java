package com.app.arle.theblog.account;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.arle.theblog.ActivityResults.ActivityResults;
import com.app.arle.theblog.Async.RegisterTask;
import com.app.arle.theblog.Interface.RegisterInterface;
import com.app.arle.theblog.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements RegisterInterface{

    Button registerbutton, backbutton;
    EditText mail, pass, cpass, name;
    String username, password, confirmpassword, email;
    RegisterTask registerTask;
    RegisterInterface registerInterface;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        registerInterface = this;
        mail = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        cpass = (EditText) findViewById(R.id.confirmpassword);

        backbutton = (Button) findViewById(R.id.annullabutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ActivityResults.accountNotLogged);
                finish();
            }
        });
        registerbutton = (Button) findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = name.getText().toString();
                password = pass.getText().toString();
                confirmpassword = cpass.getText().toString();
                email = mail.getText().toString();

                if (username.equals("")) {
                    Toast.makeText(getApplicationContext(), "Username should not be empty!", Toast.LENGTH_SHORT).show();
                } else if (email.equals("")) {
                    Toast.makeText(getApplicationContext(), "Email should not be empty!", Toast.LENGTH_SHORT).show();

                } else if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Password should not be empty!", Toast.LENGTH_SHORT).show();

                } else if (confirmpassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Confirm password should not be empty!", Toast.LENGTH_SHORT).show();
                } else
                if(password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password should be at least 6 characters!", Toast.LENGTH_SHORT).show();

                } else
                if (!password.equals(confirmpassword)) {
                    Toast.makeText(getApplicationContext(), "Password and confirm password should not be the same!", Toast.LENGTH_SHORT).show();
                } else {
                    registerTask = new RegisterTask(email, password, username, confirmpassword, registerInterface);
                    registerTask.execute();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        setResult(ActivityResults.accountNotLogged);
        finish();
    }

    JSONObject json, jsondata;
    String token, usernamedata;
    int currentid;
    @Override
    public void toRegister(String data) {
        //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
        try {
            json = new JSONObject(data);
            jsondata = new JSONObject(json.getString("success"));

            //to get the interested data from the result
            token = jsondata.getString("token");
            currentid = jsondata.getInt("id");
            usernamedata = jsondata.getString("name");

            //To save the interested data on sharedPreferences
            pref = getSharedPreferences("userdata", MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("currentuser", email);
            editor.putString("username", usernamedata);
            editor.putInt("id", currentid);
            editor.commit();

            setResult(ActivityResults.accountLogged);
            finish();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Fail to retrieve the token!", Toast.LENGTH_SHORT).show();
        }


    }
}
