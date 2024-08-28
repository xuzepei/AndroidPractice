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

import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
//import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

import org.rajawali3d.view.ISurface;
import org.rajawali3d.view.SurfaceView;


public class MainActivity extends AppCompatActivity {

    Button actionButton = null;
    WebView webView = null;

    private SceneView sceneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionButton = findViewById(R.id.button);
        sceneView = findViewById(R.id.scene_view);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("####", "Clicked action button.");
                loadModel();
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

    void loadModel() {
        String path = "file:///android_asset/www/models/";
        String filename = "aHR0cHM6Ly9tbWJpei5xcGljLmNuL3N6X21tYml6X3BuZy9rT1ROa2ljNWdWQkhvb2FGb3FzeW9pY2NKRElLTHhsNnBYaWJKNWFyVzRRUllEcnJ3UHV1bVdleWljaWFKVjhJbFJTUWp1aWFqdFFOUjVGRE15ekhFYXJwN243US82NDA_0_hrn_mid_mesh";
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

    public void callJavaScriptMethodWithResult() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sceneView != null) {
            sceneView.destroy();
        }
    }


}