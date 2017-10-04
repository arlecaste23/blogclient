package com.app.arle.theblog.Async;

import android.os.AsyncTask;

import com.app.arle.theblog.Interface.LoginInterface;
import com.app.arle.theblog.Interface.LogoutInterface;
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

public class LogoutTask extends AsyncTask<Void, Void, Integer> {

    final static String url = Url.ToLogout;
    String urll;
    HttpURLConnection http;
    int response;
    int result;
    URL uurl;

    LogoutInterface logoutInterface;
    public LogoutTask(int id, LogoutInterface logoutInterface) {
        this.logoutInterface = logoutInterface;
        this.urll = url + id;
    }
    @Override
    protected Integer doInBackground(Void... params) {
        try {
            this.uurl = new URL(urll);
            http = (HttpURLConnection) this.uurl.openConnection();
            result = sendPost();
        } catch(Exception e) {
            result = -2;
        }
        return result;
    }
    @Override
    protected void onPostExecute(Integer result)
    {
        logoutInterface.toLogout(result);
    }

    private int sendPost() throws Exception{
        http.setRequestMethod("POST");
        http.setRequestProperty("User-Agent", Url.USER_AGENT);
        http.setRequestProperty("Content-Type", "application/json");

        response = http.getResponseCode();
        if(response == 200) {
            return response;
        } else {
            return -1;
        }
    }
}
