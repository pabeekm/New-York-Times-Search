package com.example.pbeekman.newyorktimessearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

/**
 * Created by pbeekman on 6/20/16.
 */

@Parcel
public class Article{
    String url;
    String headline;
    String thumbnail;
    String date;

    public String getUrl() {
        return url;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDate() {
        try {
            String nDate = date.substring(0, date.indexOf("T"));
            String year = nDate.substring(0, nDate.indexOf("-"));
            String month = nDate.substring(nDate.indexOf("-") + 1, nDate.lastIndexOf("-"));
            month = new DateFormatSymbols().getMonths()[Integer.parseInt(month) - 1];
            String day = nDate.substring(nDate.lastIndexOf("-") + 1);
            return month + " " + day + ", " + year;
        } catch (StringIndexOutOfBoundsException e) {
            return "";
        } catch (NullPointerException e) {
            return "";
        }
    }

    // empty constructor needed by the Parceler library
    public Article() {}

    public Article(JSONObject j, boolean top) {
        try {
            if (!top) {
                this.url = j.getString("web_url");
                this.headline = j.getJSONObject("headline").getString("main");
                this.date = j.getString("pub_date");
                JSONArray media = j.getJSONArray("multimedia");
                if (media.length() > 0) {
                    JSONObject media0 = media.getJSONObject(0);
                    this.thumbnail = "http://nytimes.com/" + media0.getString("url");
                } else {
                    this.thumbnail = "";
                }
            }
            else {
                this.url = j.getString("url");
                this.headline = j.getString("title");
                this.date = j.getString("published_date");
                JSONArray media = j.getJSONArray("multimedia");
                if (media.length() > 0) {
                    JSONObject media0 = media.getJSONObject(0);
                    this.thumbnail = media0.getString("url");
                } else {
                    this.thumbnail = "";
                }
            }
        } catch (JSONException e) {}
    }

    public static ArrayList<Article> fromJSONArray(JSONArray ja, boolean top) {
        ArrayList<Article> results = new ArrayList<>();
        for (int i = 0; i < ja.length(); i++) {
            try {
                results.add(new Article(ja.getJSONObject(i), top));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
