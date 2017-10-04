package com.app.arle.theblog.Async;

import android.os.AsyncTask;

import com.app.arle.theblog.Interface.LoginInterface;
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

public class LoginTask extends AsyncTask<Void, Void, String> {

    private String url = Url.ToLogin;
    String mail, pass;
    HttpURLConnection http;
    int response;
    String result;
    URL uurl;
    BufferedReader reader;
    StringBuffer stringbuffer;
    String line = "";
    LoginInterface loginInterface;

    public LoginTask(String mail, String pass, LoginInterface loginInterface) {
        this.mail = mail;
        this.pass = pass;
        this.loginInterface = loginInterface;
    }
    @Override
    protected String doInBackground(Void... params) {
        try {
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
        loginInterface.loginLog(response);
        loginInterface.logindata(result);
    }

    private String sendPost() throws Exception{
        http.setRequestMethod("POST");
        http.setRequestProperty("User-Agent", Url.USER_AGENT);
        http.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();
        json.put("email", this.mail);
        json.put("password", this.pass);

        http.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(http.getOutputStream());
        wr.writeBytes(json.toString());
        wr.flush();
        wr.close();

        response = http.getResponseCode();
        if(response == 200) {
            reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            stringbuffer = new StringBuffer();
            while((line = reader.readLine()) != null) {
                stringbuffer.append(line);
            }
            return stringbuffer.toString();
        } else {
            return "Error";
        }


    }
}
