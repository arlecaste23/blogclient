package com.app.arle.theblog.Async;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.TextView;

import com.app.arle.theblog.Interface.DeleteInterface;
import com.app.arle.theblog.Interface.GetInterface;
import com.app.arle.theblog.Model.Article;
import com.app.arle.theblog.Url.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arle on 01/10/17.
 */

public class DeleteArticle extends AsyncTask<Void, Void, Integer> {
    private final String USER_AGENT = Url.USER_AGENT;
    String urll = Url.ToArticles;
    URL url;
    HttpURLConnection http;
    int response = -1;
    String uurl;
    DeleteInterface deleteInterface;
    int result;
    public DeleteArticle(DeleteInterface listener, int id) {
       this.uurl = urll + id + "";
        this.deleteInterface = listener;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            this.url = new URL(uurl);
            http = (HttpURLConnection) this.url.openConnection();
            result = sendDelete();
        } catch(Exception e) {
            result = -1;
        }
        return result;
    }


    @Override
    protected void onPostExecute(Integer result)
    {
        deleteInterface.deleteArticle(result);
    }
    private int sendDelete() throws Exception{
        http.setRequestMethod("DELETE");
        http.setRequestProperty("User-Agent", USER_AGENT);
        http.connect();

        response = http.getResponseCode();
        return response;
    }
}
