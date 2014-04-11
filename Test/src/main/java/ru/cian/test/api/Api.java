package ru.cian.test.api;

import ru.cian.test.api.requests.Authorization;

public class Api extends ServerApi {
    public String authorization(String login, String password) throws ApiException, ServerApiException {
        Authorization authorization = new Authorization(login, password);
        return load(authorization);
    }
}