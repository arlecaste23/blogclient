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
import com.app.arle.theblog.Interface.UpdateInterface;
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

public class UpdateArticle extends AsyncTask<Void, Void, String> {

    URL url;
    HttpURLConnection http;
    int response = -1;
    String uurl;
    UpdateInterface updateInterface;
    String result;
    String tosend = "";
    Context context;
    public UpdateArticle(UpdateInterface listener, Context context, String url) {
        this.updateInterface = listener;
        this.context = context;
        this.uurl = url;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            tosend = this.updateInterface.getData().toString();
            if(tosend.contains("Errore")) {
                return "Errore";
            }
            this.url = new URL(uurl);
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

    }
    private int sendPost() throws Exception{
        http.setRequestMethod("PUT");
        http.setRequestProperty("User-Agent", Url.USER_AGENT);
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
