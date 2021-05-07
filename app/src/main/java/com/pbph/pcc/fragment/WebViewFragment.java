package com.pbph.pcc.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.tools.WaitUI;

import cn.jpush.android.api.JPushInterface;


public class WebViewFragment extends Fragment {
    private View view = null;
    private WebView webview = null;
    private RelativeLayout titleLayout = null;
    private LinearLayout progressBarLayout = null;
    private ImageView back;
    private String main_url, titleContent = "";
    private PccApplication application = null;
    private static final String ARG_PARAM1 = "url";
    private static final String ARG_PARAM2 = "title";

    public WebViewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main_url = getArguments().getString("url");
        titleContent = getArguments().getString("title");
       // Log.e("onCreateView", "url   =  " + main_url);
        view = inflater.inflate(R.layout.fragment_my_browsers, null);
        titleLayout = view.findViewById(R.id.include_title);
        TextView title = view.findViewById(R.id.tv_title);
        if (TextUtils.isEmpty(titleContent)) {
            titleLayout.setVisibility(View.GONE);
        }
        title.setText(titleContent);
        ImageView mRight = view.findViewById(R.id.btn_right);
        ImageView mLeft = view.findViewById(R.id.btn_left);
        mRight.setVisibility(View.VISIBLE);
        mLeft.setVisibility(View.VISIBLE);
        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    getActivity().finish();
                }
            }
        });
        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        webview = view.findViewById(R.id.home_webView);
        progressBarLayout = view.findViewById(R.id.ll_progressBar);
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        webview.getSettings().setSupportZoom(false);
        webview.getSettings().setBuiltInZoomControls(false);
        webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new MyWebChromeClient());
        webview.loadUrl(main_url);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
//        webview.getSettings()
//                .setUserAgentString(
//                        "Mozilla/5.0 (Linux; U; Android 2.2; en-gb; Nexus One Build/FRF50) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
//        webview.getSettings()
//                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
//        webview.addJavascriptInterface(new JsClass(getActivity(), webview), "callClass");
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
       // Log.e("maomao", "densityDpi = " + mDensity);
        if (mDensity == 240) {
            webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        return view;
    }


//    private void backSetVisibility() {
//        if (webview.copyBackForwardList().getCurrentIndex() > 0) {
//            back.setVisibility(View.VISIBLE);
//        } else {
//            back.setVisibility(View.INVISIBLE);
//        }
//    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();
            } else {
                getActivity().finish();
            }
           // Log.e("WebFragmet事件", "OK");
        }
        return true;
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (progress == 100) {
                String registrationID = JPushInterface.getRegistrationID(application);
                if (!TextUtils.isEmpty(registrationID)) {
                    webview.loadUrl("javascript:getJPushId('" + registrationID + "','" + "0" + "')");
                }
                if (!TextUtils.isEmpty(application.getUserid())) {
                    webview.loadUrl("javascript:getuser('" + application.getUserid() + "')");
                }
                WaitUI.Cancel();
            }
        }

        // 处理javascript中的alert
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {
            // 构架一个builder来显示网页中的对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("提示信息");
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击确定按钮之后，继续执行网页中的操作
                            result.confirm();
                        }
                    });

            // 屏蔽keycode等于84之类的按键
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    return true;
                }
            });

            builder.setCancelable(false);
            builder.create();
            builder.show();
            return true;
        }

        // 处理javascript中的confirm
        @Override
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("提示信息");
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });
            // 屏蔽keycode等于84之类的按键
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    return true;
                }
            });

            builder.setCancelable(false);
            builder.create();
            builder.show();
            return true;
        }

        /**
         * 覆盖默认的window.prompt
         */
        @Override
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, final JsPromptResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    view.getContext());
            builder.setTitle("提示信息").setMessage(message);
            final EditText et = new EditText(view.getContext());
            et.setSingleLine();
            et.setText(defaultValue);
            builder.setView(et);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm(et.getText().toString());
                }
            }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });

            // 屏蔽keycode等于84之类的按键
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    return true;
                }
            });

            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {// 网页页面开始加载的时候
            progressBarLayout.setVisibility(View.VISIBLE);
            webview.setVisibility(View.VISIBLE);
//            if (!TextUtils.isEmpty(cookie)) {
//                syncCookie(cookie);
//            }
            // webview.setEnabled(false);// 当加载网页的时候将网页进行隐藏
            super.onPageStarted(view, url, favicon);
        }

        // C:\Users\cool\Documents\pbph
        @Override
        public void onPageFinished(WebView view, String url) {// 网页加载结束的时候
            progressBarLayout.setVisibility(View.GONE);
//            backSetVisibility();
            // view.getLayoutParams().height = view.getContentHeight();
           // Log.e("dialog.Cancel()", "run......." + url);
            webview.setEnabled(true);

        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
           // Log.e("onReceivedError", description + "    " + errorCode + "     " + failingUrl);
//            view.stopLoading();
//            view.clearView();
            // webview.setVisibility(View.GONE);
//            view.loadUrl("file:///android_asset/404.html");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { // 网页加载时的连接的网址
           // Log.e("即将加载的页面:", url);
            view.loadUrl(url);
            return true;

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
           // Log.e("即将加载的页面1111:", view.getUrl());
            return true;
        }
    }

    @Override
    public void onAttach(Context context) {
       // Log.e("MainFragment", "onAttach");
        super.onAttach(context);
        application = (PccApplication) getActivity().getApplication();
//        BroadcastManager.getInstance(getActivity()).addAction(ConstantData.CLOSE_PAGE_ACTION, new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if (main_url.contains("goodsList.aspx")) {
//                    getActivity().finish();
//                }
//            }
//        });
    }

    @Override
    public void onDestroyView() {
       // Log.e("MainFragment", "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
       // Log.e("MainFragment", "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
       // Log.e("MainFragment", "onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
       // Log.e("MainFragment", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
       // Log.e("MainFragment", "onStop");
    }

    @Override
    public void onDetach() {
//        BroadcastManager.getInstance(getActivity()).destroy(ConstantData.CLOSE_PAGE_ACTION);
       // Log.e("MainFragment", "onDetach");
        super.onDetach();
    }

    public static WebViewFragment newInstance(String param1, String param2) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}
