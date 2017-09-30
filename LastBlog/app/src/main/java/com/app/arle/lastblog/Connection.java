package com.app.arle.lastblog;

import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
//import com.google.gson.*;
/**
 * Created by arle on 29/09/17.
 */

public class Connection extends AsyncTask <Void, Void, String> {
    private URL url;
    private Context context;
    HttpURLConnection http;
    private final String USER_AGENT = "Mozilla/5.0";
    int response;
    BufferedReader reader;
    String line = "";
    StringBuffer stringbuffer;
    TextView result;
    private boolean check = false;
    public Connection(Context context, String url, TextView result) throws Exception{
        this.context = context;
        this.url = new URL(url);
        http = (HttpURLConnection) this.url.openConnection();
        this.result = result;
    }
    @Override
    protected String doInBackground(Void... params){
        String result;
        try {
            result = sendGet();
        } catch (Exception e) {
            result = e.toString();
        }
        return result;

    }
    @Override
    protected void onPostExecute(String result)
    {
         //   JsonObject json = new JsonObject();
       // Gson gson = new GsonBuilder().serializeNulls().create();
     //   String res = gson.toJson(result);
       // res.replaceAll("\"", res);
        //Article art = new Article(1, "Ciao", "Un saluto a tutti", "2017-09-28 13:24:50", "2017-09-28 13:24:50");
       /* JSONObject json = null;
        try {
             json= new JSONObject(result);
        } catch (JSONException e) {
            result = e.toString();
        }

        try {
            this.result.append(json.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
       //recupero l'array in json
        this.result.setGravity(Gravity.CENTER);
        try {
            JSONArray jar = null;
            String title, body, realtitle;
            byte data[];
            byte[] b64title;
            jar = new JSONArray(result);
            for (int i = 0; i < jar.length(); i++) {
                JSONObject articlejson = jar.getJSONObject(i);
                title = articlejson.getString("title");
                data = title.getBytes("UTF-8");
                b64title = Base64.decode(data, Base64.DEFAULT);
                realtitle = new String(b64title, "UTF-8");
                this.result.append(realtitle + "\n\n");

                title = articlejson.getString("body");
                data = title.getBytes("UTF-8");
                b64title = Base64.decode(data, Base64.DEFAULT);
                realtitle = new String(b64title, "UTF-8");
                this.result.append(realtitle + "\n\n\n");
            }
        } catch(Exception e) {
            this.result.append(e.toString());
        }

    }
    public boolean getCheck() {
        return check;
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
