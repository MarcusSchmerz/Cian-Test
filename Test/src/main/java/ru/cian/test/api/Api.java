package ru.cian.test.api;

import ru.cian.test.api.requests.GetNews;

public class Api extends ServerApi {
    public String getNews(String login, String password) throws ApiException, ServerApiException {
        GetNews getNews = new GetNews(login, password);
        return load(getNews);
    }
}