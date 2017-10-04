package com.app.arle.theblog.Async;

import android.os.AsyncTask;

import com.app.arle.theblog.Interface.RegisterInterface;
import com.app.arle.theblog.Url.Url;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by arle on 01/10/17.
 */

public class RegisterTask extends AsyncTask<Void, Void, String> {

    final static String url = Url.ToRegister;
    String mail, pass, username, confirmpass;
    HttpURLConnection http;
    int response;
    String result;
    URL uurl;
    BufferedReader reader;
    StringBuffer stringbuffer;
    String line = "";
    RegisterInterface registerInterface;
    public RegisterTask(String mail, String pass, String username, String confirmpass, RegisterInterface registerInterface) {
        this.mail = mail;
        this.pass = pass;
        this.username = username;
        this.confirmpass = confirmpass;
        this.registerInterface = registerInterface;
    }
    @Override
    protected String doInBackground(Void... params) {

        try {
            if(!this.confirmpass.equals(pass)) {
                return "Password and confirm password does not match!";
            }
            this.uurl = new URL(url);
            http = (HttpURLConnection) this.uurl.openConnection();
            result = sendPost();
        } catch(Exception e) {
            result = e.toString();
        }
        return result;
    }
    @Override
    protected void onPostExecute(String result)
    {
        registerInterface.toRegister(result);
    }

    private String sendPost() throws Exception{
        http.setRequestMethod("POST");
        http.setRequestProperty("User-Agent", Url.USER_AGENT);
        http.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();
        json.put("name", this.username);
        json.put("email", this.mail);
        json.put("password", this.pass);
        json.put("c_password", this.confirmpass);
        http.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(http.getOutputStream());
        wr.writeBytes(json.toString());
        wr.flush();
        wr.close();

        response = http.getResponseCode();

        if(response != 200) {
            return "Failed to register";
        } else {
            reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            stringbuffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                stringbuffer.append(line);
            }
            return stringbuffer.toString();
        }
    }
}
