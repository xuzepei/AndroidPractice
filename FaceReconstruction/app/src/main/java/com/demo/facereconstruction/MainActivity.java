package com.demo.facereconstruction;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewAssetLoader;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Button downloadButton = null;
    Button actionButton = null;
    WebView webView = null;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadButton = findViewById(R.id.download_btn);
        actionButton = findViewById(R.id.action_btn);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("####", "Clicked download button.");
                downloadModel();
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("####", "Clicked action button.");
                String filename = "20190618124505345_0_hrn_mid_mesh";
                //loadModel(filename);
            }
        });

        //获得控件
        webView = findViewById(R.id.webview);
        if(webView != null) {

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAllowContentAccess(true);
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    Log.d("####", "onPageStarted");
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    Log.d("####", "onPageFinished");
                    //loadModel();
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    Log.d("####", "onReceivedError: " + error.toString());
                }

                @Override
                public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                    String url = request.getUrl().toString();

                    // Intercept and handle the request here if necessary
                    return super.shouldInterceptRequest(view, request);
                }
            });
        }

        loadScene();
    }

    void loadScene() {
        webView.loadUrl("file:///android_asset/www/index.html");
    }

    void loadModel(String folderName, String filename) {
        //String path = "file:///android_asset/www/models/";
        //String path = "file:///storage/emulated/0/Android/data/com.demo.facereconstruction/files/models/";
        String path = "file://" + Tool.localModelDirectory() + "/" + folderName + "/";

        //String action = "javascript:loadModel(" + path + "," +filename+")";
        String action = "javascript:loadModel(" + "\"" + path + "\",\"" +filename+"\""+")";
        Log.d("####", action);
        webView.evaluateJavascript(action, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                // Handle the result here
                Log.d("#### WebView", "Result from JS: " + value);
            }
        });
    }

    void downloadModel() {
        String urlString = "https://xuzepei.github.io/vue/test.zip";

        HttpRequest.shared().downloadModel(urlString, "test", new HttpCallbackWithToken() {
            @Override
            public void onFailure(String error, String token) {
                Log.d("####", "downloadFile failed: " + error);
            }

            @Override
            public void onSuccess(Object data, JSONObject token) {
                Log.d("####", "downloadFile succeeded");
            }

            public void onSuccess(String token) {
                Log.d("####", "downloadFile succeeded, token: " + token);
                handleSucceed(token);
            }
        });

    }

    void handleSucceed(String token) {
        runOnUiThread(new Runnable() {
            public void run() {
                if(!Tool.isNullOrEmpty(token)) {
                    String modelFilename = unzipModelFile(token);
                    if(!Tool.isNullOrEmpty(modelFilename)) {
                        loadModel(token, modelFilename);
                    }
                }
            }
        });
    }

    public String unzipModelFile(String modelId) {
        try {
            File destinationDirectory = new File(Tool.localModelDirectory(), modelId);
            Tool.deleteDirectory(destinationDirectory);

            List<String> filePaths = ZipUtils.unzip(Tool.getLocalModelZip(modelId), destinationDirectory);
            // 解压完成
            for (String path : filePaths) {
                Log.d("####", "Unzipped Filepath: " + path);
                if(path.toLowerCase().endsWith(".obj")) {
                    String folderName = "/";
                    int index = path.lastIndexOf(folderName);
                    if(index > 0) {
                        String fileName = path.substring(index + folderName.length(), path.length() - 4);
                        Log.d("####", "Model filename: " + fileName);
                        return fileName;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }


}