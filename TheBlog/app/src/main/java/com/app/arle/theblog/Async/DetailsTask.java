package com.app.arle.theblog.Async;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.app.arle.theblog.Interface.DetailsInterface;
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

public class DetailsTask extends AsyncTask<Void, Void, String> {
    private String USER_AGENT = Url.USER_AGENT;
    private String url = Url.ToDetails;
    HttpURLConnection http;
    int response;
    String result, token;
    URL uurl;
    BufferedReader reader;
    StringBuffer stringbuffer;
    String line = "";
    DetailsInterface detailsInterface;

    public DetailsTask(String token, DetailsInterface detailsInterface) {
        this.token = token;
        this.detailsInterface = detailsInterface;
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
        detailsInterface.getDetails(result);
    }


    private String sendPost() throws Exception{
        http.setRequestMethod("POST");
        http.setRequestProperty("User-Agent", USER_AGENT);
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Authorization", "Bearer " + this.token);

        response = http.getResponseCode();

       if(response == 200) {
            reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            stringbuffer = new StringBuffer();
            while((line = reader.readLine()) != null) {
                stringbuffer.append(line);
            }
            return stringbuffer.toString();
        } else {
            return "Errore";
        }

    }
}
