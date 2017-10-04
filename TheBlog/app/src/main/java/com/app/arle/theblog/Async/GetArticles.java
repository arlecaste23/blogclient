package com.app.arle.theblog.Async;

import android.os.AsyncTask;
import android.util.Base64;
import android.widget.TextView;

import com.app.arle.theblog.Interface.GetInterface;
import com.app.arle.theblog.Model.Article;
import com.app.arle.theblog.Url.Url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arle on 01/10/17.
 */

public class GetArticles extends AsyncTask<Void, Void, String> {

    URL url;
    HttpURLConnection http;
    int response = -1;
    BufferedReader reader = null;
    StringBuffer stringbuffer = null;
    String line = "", result, uurl;
    List<Article> articles = new ArrayList<>();
    GetInterface getInterface;

    public GetArticles(String url, GetInterface getInterface) {
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


    @Override
    protected void onPostExecute(String result)
    {
        try {
            Article tmparticle;
            JSONArray jar = null;
            String title, body, realtitle, createdat, updatedat;
            int id;
            byte data[];
            byte[] b64title;

            //Prendo dal server tutto gi articoli tramite un jsonarray
            jar = new JSONArray(result);
            //Itero su ciascun elemento dell'array
            for (int i = 0; i < jar.length(); i++) {

                //Recupero l'articolo tramite un oggetto json
                JSONObject articlejson = jar.getJSONObject(i);

                id = articlejson.getInt("id");
                title = articlejson.getString("title");
                data = title.getBytes("UTF-8");
                b64title = Base64.decode(data, Base64.DEFAULT);
                realtitle = new String(b64title, "UTF-8");

                title = articlejson.getString("body");
                data = title.getBytes("UTF-8");
                b64title = Base64.decode(data, Base64.DEFAULT);
                body = new String(b64title, "UTF-8");

                createdat = (String) articlejson.get("created_at");
                updatedat = (String) articlejson.get("updated_at");

                tmparticle = new Article(id, realtitle, body, createdat, updatedat);

                articles.add(tmparticle);
            }
            JSONArray jr = new JSONArray();
            for(int i = 0; i<articles.size(); i++) {
                JSONObject gr = new JSONObject();

                gr.put("id", articles.get(i).getId());
                gr.put("title", articles.get(i).getTitle());
                gr.put("body", articles.get(i).getBody());
                gr.put("created_at", articles.get(i).getCreatedat());
                gr.put("updated_at", articles.get(i).getUpdatedat());

                jr.put(gr);
            }
            getInterface.geAllArticles(jr.toString());
        } catch(Exception e) {
            getInterface.geAllArticles(e.toString() + "\n" + result);
        }
    }
    private String sendGet() throws Exception{
        http.setRequestMethod("GET");
        http.setRequestProperty("User-Agent", Url.USER_AGENT);
        response = http.getResponseCode();
        reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        stringbuffer = new StringBuffer();
        while((line = reader.readLine()) != null) {
            stringbuffer.append(line);
        }
        return stringbuffer.toString();
    }
}
