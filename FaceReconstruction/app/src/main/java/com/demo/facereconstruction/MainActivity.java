package com.demo.facereconstruction;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

    MyRenderer mRenderer = null;
    WebView webView = null;

    private SceneView sceneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SurfaceView surface = new SurfaceView(this);
//        surface.setFrameRate(60.0);
//        surface.setRenderMode(ISurface.RENDERMODE_WHEN_DIRTY);
//        setContentView(surface);
//
//        mRenderer = new MyRenderer(this);
//        surface.setSurfaceRenderer(mRenderer);

        sceneView = findViewById(R.id.scene_view);
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
        //loadModel();
    }

    void loadScene() {
        webView.loadUrl("file:///android_asset/www/index.html");
        //loadModel();
    }

//    void loadModel() {
//        String action = "javascript:loadModel('file:///android_asset/www/index.html/models/','PEACE_LILLY_5K')";
//        webView.loadUrl(action);
//    }

    void loadModel() {
        Uri modelUri = Uri.parse("file:///android_asset/20190618124505345_0_hrn_mid_mesh.obj");

//        RenderableSource renderableSource = RenderableSource.builder()
//                .setSource(this, modelUri, RenderableSource.SourceType)
//                .setScale(0.5f)  // Adjust the scale of the model if necessary
//                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
//                .build();

        ModelRenderable.builder()
                .setSource(this, modelUri)
                .setRegistryId("model")
                .build()
                .thenAccept(modelRenderable -> {
                    // Create a node for the model
                    com.google.ar.sceneform.Node modelNode = new com.google.ar.sceneform.Node();
                    modelNode.setRenderable(modelRenderable);

                    // Optionally set the position of the model
                    modelNode.setLocalPosition(new Vector3(0f, 0f, -1f)); // Adjust the position as needed

                    // Add the node to the scene
                    Scene scene = sceneView.getScene();
                    scene.addChild(modelNode);
                })
                .exceptionally(throwable -> {
                    // Handle any errors during model loading
                    throwable.printStackTrace();
                    return null;
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sceneView != null) {
            sceneView.destroy();
        }
    }


}