package ru.cian.test.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public abstract class ServerApi {
    public <T> T load(ServerRequest<T> serverRequest) throws ApiException, ServerApiException {
        try {
            HttpPost httpPost = new HttpPost(serverRequest.getUrl());
            StringEntity stringEntity = new StringEntity("");
            httpPost.setEntity(stringEntity);
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String string = EntityUtils.toString(httpEntity);
            JSONObject jsonObject = new JSONObject(string);
            return serverRequest.findError(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerApiException(e);
        }
    }
}