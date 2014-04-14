package ru.cian.test.api.requests;

import android.util.Log;

import org.json.JSONObject;

import ru.cian.test.api.ServerRequest;

public class GetNews extends ServerRequest<String> {
    public static final String URL = "https://api.vk.com/method/newsfeed.get";

    public GetNews(String token, String user) {
        super(URL + "?uid=" + user + "6&access_token=" + token);
    }

    @Override
    protected String response(JSONObject jsonObject) {
        Log.e("DATA", jsonObject.toString());
        return "";
    }
}