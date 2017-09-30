package com.app.arle.lastblog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by arle on 29/09/17.
 */

public class Article {
    private String title, body;
    private int id;
    private Date createdat, updatedat;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public Article(int id, String title, String body, String createdat, String updatedat) {
        this.id = id;
        this.title = title;
        this.body = body;

        try {
            this.createdat = (Date) formatter.parse(createdat);
            this.updatedat = (Date) formatter.parse(updatedat);
        } catch (ParseException e) {
            this.createdat = null;
            this.updatedat = null;
        }


    }

    public String getTitle() {
        return this.title;
    }
    public String getBody() {
        return this.body;
    }
    public Date getCreatedat() {
        return this.createdat;
    }
    public Date getUpdatedat() {
        return this.updatedat;
    }
    public int getId() {
        return this.id;
    }
    @Override
    public String toString() {
        return this.id + "\n" + this.title + "\n" + this.body + "\n" + this.createdat.toString() + "\n" + this.updatedat.toString();
    }
}
