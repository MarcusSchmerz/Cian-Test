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

    private SharedPreferences sharedPreferences = null;
    private String accessToken = null;
    private String userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        accessToken = sharedPreferences.getString(ACCESS_TOKEN, null);
        userId = sharedPreferences.getString(USER_ID, null);
        if (accessToken == null || userId == null) {
            login();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exitMenu:
                logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void login() {
        final Dialog loginDialog = new Dialog(MainActivity.this);
        loginDialog.setContentView(R.layout.dialog_login);
        loginDialog.setTitle(R.string.login);
        loginDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        loginDialog.show();
        WebView loginWebView = (WebView) loginDialog.findViewById(R.id.loginWebView);
        WebSettings webSettings = loginWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        loginWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.startsWith(REDIRECT_URL)) {
                    if (url.contains("access_token=") && url.contains("user_id=")) {
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
                        loginDialog.dismiss();
                    } else {
                        finish();
                    }
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
        loginWebView.loadUrl(URL_AUTHORIZATION + "?client_id=" + ID + "&scope=" + "friends,wall,photos" + "&response_type=token");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void logout() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_login);
        WebView logoutWebView = (WebView) dialog.findViewById(R.id.loginWebView);
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
                login();
            }
        });
        CookieSyncManager.createInstance(MainActivity.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        logoutWebView.loadUrl(URL_LOGOUT);
    }
}