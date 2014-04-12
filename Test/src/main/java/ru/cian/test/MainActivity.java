package ru.cian.test;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends ActionBarActivity {
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String ID = "4302036";
    private static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    private static final String URL_AUTHORIZATION = "https://oauth.vk.com/authorize";
    private static final String URL_LOGOUT = "https://oauth.vk.com/logout";
    private static final String USER_ID = "USER_ID";

    private Dialog dialog = null;
    private SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        //
        //
        /*ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Api api = new Api();
                try {
                    api.authorization("", "");
                } catch (ApiException e) {
                    e.printStackTrace();
                } catch (ServerApiException e) {
                    e.printStackTrace();
                }
            }
        });*/
        //
        //
        //
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        login();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ExitMenu:
                dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_login);
                dialog.setTitle(R.string.Exit);
                dialog.show();
                final WebView logoutWebView = (WebView) dialog.findViewById(R.id.LoginWebView);
                WebSettings webSettings = logoutWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                logoutWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(ACCESS_TOKEN);
                        editor.remove(USER_ID);
                        editor.commit();
                        dialog.dismiss();
                        login();
                    }
                });
                CookieSyncManager.createInstance(MainActivity.this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                logoutWebView.loadUrl(URL_LOGOUT);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void login() {
        String accessToken = sharedPreferences.getString(ACCESS_TOKEN, null);
        String userId = sharedPreferences.getString(USER_ID, null);
        if (accessToken == null || userId == null) {
            dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_login);
            dialog.setTitle(R.string.Login);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            dialog.show();
            WebView loginWebView = (WebView) dialog.findViewById(R.id.LoginWebView);
            WebSettings webSettings = loginWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            loginWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    if (url.startsWith(REDIRECT_URL)) {
                        if (url.contains("error=")) {
                            finish();
                        } else {
                            String accessToken = extract(url, "access_token=(.*?)&");
                            String userId = extract(url, "user_id=(\\d*)");
                            Log.e("DATA", "ACCESS_TOKEN: " + accessToken);
                            Log.e("DATA", "USER_ID: " + userId);
                            if (accessToken != null && userId != null) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(ACCESS_TOKEN, accessToken);
                                editor.putString(USER_ID, userId);
                                editor.commit();
                            }
                        }
                        dialog.dismiss();
                    }
                }

                private String extract(String url, String key) {
                    Pattern pattern = Pattern.compile(key);
                    Matcher matcher = pattern.matcher(url);
                    if (!matcher.find()) {
                        return null;
                    }
                    return matcher.toMatchResult().group(1);
                }
            });
            loginWebView.loadUrl(URL_AUTHORIZATION + "?client_id=" + ID + "&scope=" + "wall,photos" + "&response_type=token");
        }
    }
}