package com.app.arle.theblog.Url;

/**
 * Created by arle on 04/10/17.
 */

public final class Url {

    public static String USER_AGENT = "Mozilla/5.0";
    public static String Home = "http://212.237.25.227:8989";

    public static String Api = Home + "/api/";
    public static String ToLogin = Api + "login";
    public static String ToRegister = Api + "register";
    public static String ToLogout = Api + "logout/";
    public static String ToDetails = Api + "details";

    public static String ToArticles = Api + "articles/";
    public static String ToUpdate = ToArticles + "update/";
    public static String ToDelete = ToArticles + "delete/";

}
