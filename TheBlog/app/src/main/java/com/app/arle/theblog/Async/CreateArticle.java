package com.app.arle.theblog.Async;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import com.app.arle.theblog.Interface.CreateInterface;
import com.app.arle.theblog.Interface.DeleteInterface;
import com.app.arle.theblog.Interface.GetInterface;
import com.app.arle.theblog.Model.Article;
import com.app.arle.theblog.Url.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arle on 01/10/17.
 */

public class CreateArticle extends AsyncTask<Void, Void, String> {
    private String USER_AGENT = Url.USER_AGENT;
    private String urll = Url.ToArticles;
    URL url;
    HttpURLConnection http;
    int response = -1;
    CreateInterface createInterface;
    String result;
    String tosend = "";
    Context context;
    public CreateArticle(CreateInterface listener, Context context) {
        this.createInterface = listener;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            tosend = this.createInterface.getData().toString();
            if(tosend.contains("Errore")) {
                return "Errore";
            }
            this.url = new URL(urll);
            http = (HttpURLConnection) this.url.openConnection();

            result = sendPost() + "";
        } catch(Exception e) {
            result = e.toString();
        }
        return result;
    }


    @Override
    protected void onPostExecute(String result)
    {
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        createInterface.createArticle();
    }
    private int sendPost() throws Exception{
        http.setRequestMethod("POST");
        http.setRequestProperty("User-Agent", USER_AGENT);
        http.setRequestProperty("Content-Type", "application/json");
        http.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(http.getOutputStream());
        wr.writeBytes(tosend);
        wr.flush();
        wr.close();

        response = http.getResponseCode();
        return response;
    }
}
