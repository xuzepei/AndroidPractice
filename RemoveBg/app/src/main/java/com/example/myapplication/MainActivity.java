package com.example.myapplication;

import static android.graphics.Color.parseColor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import dev.eren.removebg.RemoveBg;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.flow.FlowCollector;

public class MainActivity extends AppCompatActivity {

    Bitmap originalBitmap;

    ImageView imageView;
    Button removeBgBtn;

    ImageView imageView2;
    Button removeBgBtn2;

    ImageView imageView3;
    Button removeBgBtn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        imageView.setBackgroundColor(parseColor("#ff0000"));

        removeBgBtn = findViewById(R.id.button);
        removeBgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedRemoveBgBtn();
            }
        });

        imageView2 = findViewById(R.id.imageView2);
        imageView2.setBackgroundColor(parseColor("#ff0000"));

        removeBgBtn2 = findViewById(R.id.button2);
        removeBgBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedRemoveBgBtn2();
            }
        });

        imageView3 = findViewById(R.id.imageView3);
        imageView3.setBackgroundColor(parseColor("#ff0000"));

        removeBgBtn3 = findViewById(R.id.button3);
        removeBgBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedRemoveBgBtn3();
            }
        });

        loadImageFromAssets();


    }

    private void loadImageFromAssets() {
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        try {
            // 打开 assets 目录中的图像文件
            inputStream = assetManager.open("photo2.jpg");
            // 将 InputStream 转换为 Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            // 设置到 ImageView 中
            originalBitmap = bitmap;

            imageView.setImageBitmap(originalBitmap);
            imageView2.setImageBitmap(originalBitmap);
            imageView3.setImageBitmap(originalBitmap);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Continuation<Unit> continuation = new Continuation<Unit>() {
        @Override
        public CoroutineContext getContext() {
            return EmptyCoroutineContext.INSTANCE;
        }

        @Override
        public void resumeWith(Object result) {
            // Handle the result of the coroutine here (success or exception)
            if (result instanceof Result) {
                Result<?> resultValue = (Result<?>) result;
//                if (resultValue.isFailure()) {
//                    Throwable exception = resultValue.exceptionOrNull();
//                    System.out.println("Coroutine failed: " + exception);
//                } else {
//                    System.out.println("Coroutine completed successfully");
//                }
            }
        }
    };

    FlowCollector<Bitmap> flowCollector = new FlowCollector<Bitmap>() {
        @Override
        public Object emit(Bitmap value, Continuation<? super Unit> continuation) {
            // Handle the emitted Bitmap here
            System.out.println("Received a Bitmap: " + value);
            imageView.setImageBitmap(value);
            return Unit.INSTANCE;
        }
    };

    FlowCollector<Bitmap> flowCollector2 = new FlowCollector<Bitmap>() {
        @Override
        public Object emit(Bitmap value, Continuation<? super Unit> continuation) {
            // Handle the emitted Bitmap here
            Log.d("####", "Received a Bitmap: " + value);

//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });

            ImageProcessor.shared().removeBackground(value, new ResultCallback() {
                @Override
                public void OnResult(boolean b, Object data) {
                    if (data != null) {
                        imageView3.setImageBitmap((Bitmap) data);
                    }
                }
            });

            return Unit.INSTANCE;
        }
    };

    void clickedRemoveBgBtn() {

        RemoveBg removeBg = new RemoveBg(this);
        removeBg.clearBackground(originalBitmap).collect(flowCollector, continuation);
    }

    void clickedRemoveBgBtn2() {
        ImageProcessor.shared().removeBackground(originalBitmap, new ResultCallback() {
            @Override
            public void OnResult(boolean b, Object data) {
                if (data != null) {
                    imageView2.setImageBitmap((Bitmap) data);
                }
            }
        });
    }

    void clickedRemoveBgBtn3() {
        RemoveBg removeBg = new RemoveBg(this);
        removeBg.clearBackground(originalBitmap).collect(flowCollector2, continuation);
    }
}