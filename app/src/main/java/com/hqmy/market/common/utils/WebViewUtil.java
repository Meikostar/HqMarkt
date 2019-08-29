package com.hqmy.market.common.utils;

import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * webView的工具类
 * Created by THINK on 2018/4/2.
 */

public class WebViewUtil {

    public static final String htmlHeader = "<head><style>img{max-width:100%;height:auto;}</style></head>";

    /**
     * 设置webView属性
     */
    public static void setWebView(WebView webView, Context context) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);// 用于设置webview放大
        settings.setAllowFileAccess(true);//允许访问文件
        settings.setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    /**
     * webView加载Html代码块
     */
    public static void loadHtml(WebView webView, String text) {
        webView.loadDataWithBaseURL(null, htmlHeader + text, "text/html", "utf-8", null);
    }

    /**
     * 销毁webView
     */
    public static void destroyWebView(WebView webView) {
        try {
            if (webView != null) {
                ViewParent parent = webView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(webView);
                }
                webView.stopLoading();
                // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
                webView.getSettings().setJavaScriptEnabled(false);
                webView.clearHistory();
                webView.clearView();
                webView.removeAllViews();
                webView.destroy();
            }
        } catch (Exception ex) {
        }
    }
}
