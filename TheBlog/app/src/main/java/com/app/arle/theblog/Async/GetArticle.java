package com.app.arle.theblog.Async;

import android.os.AsyncTask;
import android.util.Base64;
import android.widget.TextView;

import com.app.arle.theblog.Interface.GetInterface;
import com.app.arle.theblog.Model.Article;

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

public class GetArticle extends AsyncTask<Void, Void, String> {
    private final String USER_AGENT = "Mozilla/5.0";
    URL url;
    HttpURLConnection http;
    int response = -1;
    BufferedReader reader = null;
    StringBuffer stringbuffer = null;
    String line = "", result, uurl;
    List<Article> articles = new ArrayList<>();
    GetInterface getInterface;
    public GetArticle(String url, GetInterface getInterface) {
        this.uurl = url;
        this.getInterface = getInterface;

    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            this.url = new URL(uurl);
            http = (HttpURLConnection) this.url.openConnection();
            result = sendGet();
        } catch(Exception e) {
            result = e.toString();
        }
        return result;
    }


    //Buffers
    String title, body, realtitle, createdat, updatedat;
    int id;
    byte data[];
    byte[] b64title;

    @Override
    protected void onPostExecute(String result)
    {
        try {
            Article tmparticle;
            JSONObject selectedArticle = new JSONObject(result);

                id = selectedArticle.getInt("id");
                title = selectedArticle.getString("title");
                data = title.getBytes("UTF-8");
                b64title = Base64.decode(data, Base64.DEFAULT);
                realtitle = new String(b64title, "UTF-8");

                title = selectedArticle.getString("body");
                data = title.getBytes("UTF-8");
                b64title = Base64.decode(data, Base64.DEFAULT);
                body = new String(b64title, "UTF-8");

                createdat = (String) selectedArticle.get("created_at");
                updatedat = (String) selectedArticle.get("updated_at");

                tmparticle = new Article(id, realtitle, body, createdat, updatedat);

                JSONObject gr = new JSONObject();
                gr.put("id", tmparticle.getId());
                gr.put("title", tmparticle.getTitle());
                gr.put("body", tmparticle.getBody());
                gr.put("created_at", tmparticle.getCreatedat());
                gr.put("updated_at", tmparticle.getUpdatedat());
            getInterface.geAllArticles(gr.toString());
            } catch(Exception e) {
            getInterface.geAllArticles(e.toString());
        }
    }
    private String sendGet() throws Exception{
        http.setRequestMethod("GET");
        http.setRequestProperty("User-Agent", USER_AGENT);
        response = http.getResponseCode();
        reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        stringbuffer = new StringBuffer();
        while((line = reader.readLine()) != null) {
            stringbuffer.append(line);
        }
        return stringbuffer.toString();
    }
}
