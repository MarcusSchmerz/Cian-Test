package ru.cian.test.api;

import org.json.JSONObject;

public abstract class ServerRequest<T> {
    private String url = null;

    public ServerRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    protected abstract T response(JSONObject jsonObject);

    protected T findError(JSONObject jsonObject) throws ApiException {
        //
        //Find api errors
        //
        return response(jsonObject);
    }
}