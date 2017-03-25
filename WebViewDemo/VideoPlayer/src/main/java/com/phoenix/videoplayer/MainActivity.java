package com.phoenix.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import static android.R.attr.visible;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private String webUrl = "http://192.168.0.101:8080/webview/video.html";

    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private FrameLayout fullScreenContainer;
    /** 视频全屏参数 */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.wv);

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            initWebView();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }


    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.onPause(); // 暂停网页中正在播放的视频
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.onResume();//恢复播放
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        webView.stopLoading();
//        //重新加载
//        webView.reload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.destroy();
        webView = null;
    }

    /** 展示网页界面 **/
    public void initWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); // 关键点//设置此属性，可任意比例缩放
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容

        //点击下载按钮的监听
        webView.setDownloadListener(new MyWebViewDownLoadListener());

        //弹出系统浏览器的解决方法
        WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        };
        webView.setWebViewClient(wvc);

        webView.setWebChromeClient(new WebChromeClient() {
            private View mVideoProgressView;
            private Bitmap mVideoDefaltPoster;

            //播放视频时，在第一帧呈现之前，需要花一定的时间来进行数据缓冲。ChromeClient可以使用这个函数来提供一个在数据缓冲时显示的视图。 例如,ChromeClient可以在缓冲时显示一个转轮动画
            @Override
            public View getVideoLoadingProgressView() {
                if (mVideoProgressView == null) {
                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
                }
                return mVideoProgressView;
            }

            //设置海报图片
            @Override
            public Bitmap getDefaultVideoPoster() {
                if (mVideoDefaltPoster == null) {
                    mVideoDefaltPoster = BitmapFactory.decodeResource(getResources(),
                            R.drawable.jxwy);
                }
                return mVideoDefaltPoster;
            }

            //将网页中的标题设置为Title
            @Override
            public void onReceivedTitle(WebView view, String title) {
                MainActivity.this.setTitle(title);
            }

            // 进入全屏的时候
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                showCustomView(view, callback);
            }
            // 退出全屏的时候
            @Override
            public void onHideCustomView() {
                hideCustomView();
            }

        });

        // 加载Web地址
        webView.loadUrl(webUrl);
    }

    /** 视频播放全屏 **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        //如果一个视图已经存在，那么立刻终止并新建一个
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullScreenContainer = new FullScreenContainer(MainActivity.this);
        fullScreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullScreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        // 横屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 设置全屏
        setWindowFullScreen(true);
        customViewCallback = callback;
        webView.setVisibility(View.INVISIBLE);
    }

    /** 退出全屏 */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }

        // 竖屏显示，即用户当前的首选方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 退出全屏
        setWindowFullScreen(false);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullScreenContainer);
        fullScreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        webView.setVisibility(View.VISIBLE);
    }

    private void setWindowFullScreen(boolean isFullScreen) {
        int flag = isFullScreen ? WindowManager.LayoutParams.FLAG_FULLSCREEN : 0;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /** 全屏容器界面 */
    class FullScreenContainer extends FrameLayout {
        public FullScreenContainer(Context context) {
            super(context);
            setBackgroundColor(context.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Log.e("TAG", "url="+url);
            Log.e("TAG", "userAgent="+userAgent);
            Log.e("TAG", "contentDisposition="+contentDisposition);
            Log.e("TAG", "mimetype="+mimetype);
            Log.e("TAG", "contentLength="+contentLength);
            //这里并未下载，只是简单调用系统相关应用程序
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
                if (customView != null) {
                    hideCustomView();
                } else if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}
