package ru.cian.test.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Data {
    private Api api = null;
    private ExecutorService executorService = null;

    public Data() {
        api = new Api();
        executorService = Executors.newFixedThreadPool(2);
    }
}