package ru.cian.test.api.requests;

import android.util.Log;

import org.json.JSONObject;

import ru.cian.test.api.ServerRequest;

public class GetUser extends ServerRequest<String> {
    public static final String ID = "4302036";
    public static final String KEY = "2MNFuaaEPLDtRxcp4Jst";
    public static final String TYPE = "client_credentials";
    public static final String URL = "https://oauth.vk.com/access_token/";

    public GetUser(String login, String password) {
        super(URL + "?client_id=" + ID + "&client_secret=" + KEY + "&grant_type=" + TYPE);
    }

    @Override
    protected String response(JSONObject jsonObject) {
        Log.e("DATA", jsonObject.toString());
        return "";
    }
}