package ru.cian.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String ID = "4302036";
    private static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    private static final String URL = "https://oauth.vk.com/authorize";
    private static final String USER_ID = "USER_ID";

    private Dialog dialog = null;
    private SharedPreferences sharedPreferences = null;

    @SuppressLint("SetJavaScriptEnabled")
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
        String accessToken = sharedPreferences.getString(ACCESS_TOKEN, null);
        String userId = sharedPreferences.getString(USER_ID, null);
        if (accessToken == null || userId == null) {
            dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_login);
            dialog.setTitle(R.string.Login);
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
                            Toast toast = Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            Log.e("DATA", "URL: " + url);
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
            loginWebView.loadUrl(URL + "?client_id=" + ID + "&scope=" + "wall" + "&redirect_uri=" + REDIRECT_URL + "&response_type=token");
        }
    }
}