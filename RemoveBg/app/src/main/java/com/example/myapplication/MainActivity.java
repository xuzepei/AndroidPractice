package com.example.myapplication;

import static android.graphics.Color.parseColor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnBottomMenuButtonClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.util.TextInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dev.eren.removebg.RemoveBg;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.flow.FlowCollector;

public class MainActivity extends BaseActivity {
    Button selectBtn;

    ImageView imageView;
    Button removeBgBtn;

    ImageView imageView2;
    Button removeBgBtn2;

    ImageView imageView3;
    Button removeBgBtn3;

    String currentPhotoPath = "";
    Bitmap originalBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissionCallback = new RequestPermissionCallback() {
            @Override
            public void onResult(boolean granted, int requestCode, Context context) {
                switch (requestCode) {
                    case CAMERA_REQUEST: {
                        if (granted) {
                            selectPhotoFromCamera();
                        } else {
                        }
                    }
                    break;
                    case STORAGE_REQUEST: {
                    }
                    break;
                }
            }
        };


        selectBtn = findViewById(R.id.select_btn);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectionBottomMenu();
            }
        });

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

    void showSelectionBottomMenu() {

        DialogX.globalStyle = IOSStyle.style();

        List<CharSequence> list = new ArrayList<>();
        list.add("Camera");
        list.add("Album");
        BottomMenu.show("", "Select photo source", list)
                .setStyle(IOSStyle.style())
                .setCancelButton("Cancel", new OnBottomMenuButtonClickListener<BottomMenu>() {
                    @Override
                    public boolean onClick(BottomMenu dialog, View v) {
                        Log.d("####", "Clicked cancel button.");
                        return false;
                    }
                })
                .setCancelable(false)
                .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                    @Override
                    public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                        Log.d("####", "Clicked menu item: " + index);
                        if (index == 0) {
                            //调用相机需要用户授权
                            boolean result = requestCameraPermission();
                            if (result) {
                                selectPhotoFromCamera();
                            }
                        } else if (index == 1) {
                            //从相册取图片不需要用户授权
                            selectPhotoFromAlbum();
                        }
                        return false;
                    }
                });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        //String fileName = System.currentTimeMillis() + ".jpg";
        String imageFileName = "temp_source_photo";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        Tool.removeFile(imageFile);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    void selectPhotoFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // 处理错误情况
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(intent, REQUEST_PHOTO_FROM_CAMERA);
            }
        } catch (ActivityNotFoundException e) {
            // display error state to the user
            e.printStackTrace();
        }
    }

    void selectPhotoFromAlbum() {

//        if (com.photo.easyidphoto.BuildConfig.DEBUG) {
//            goToPhotoEditorActivity();
//        } else {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PHOTO_FROM_ALBUM);
        //       }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照或相册选择后的处理
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PHOTO_FROM_CAMERA) {
                // 处理照片（photoURI中保存了照片的URI）
                Bitmap sourceBitmap = BitmapFactory.decodeFile(currentPhotoPath);
                if (sourceBitmap != null) {
                    sourceBitmap = Tool.rotateImageIfRequired(sourceBitmap, currentPhotoPath);
                    String tempImageName = "temp_source_photo.jpg";

                    boolean result = Tool.saveImage(sourceBitmap, tempImageName, Bitmap.CompressFormat.JPEG);
                    if (result) {
                        // 处理选择照片结果
                        Uri photoUri = Uri.fromFile(Tool.getLocalImagePath(tempImageName));
                        //startImageCrop(photoUri); //先裁剪
                        handleSourceImage(photoUri);
                    }
                }

            } else if (requestCode == REQUEST_PHOTO_FROM_ALBUM) {
                if (data == null) {
                    return;
                }

                // 处理选择照片结果
                Uri photoUri = data.getData();
                try {
                    Bitmap bitmap = getBitmapFromUri(photoUri);
                    Bitmap sourceBitmap = Tool.rotateImageIfRequired(bitmap, photoUri);
                    String tempImageName = "temp_source_photo.jpg";
                    boolean result = Tool.saveImage(sourceBitmap, tempImageName, Bitmap.CompressFormat.JPEG);
                    if (result) {
                        // 处理选择照片结果
                        photoUri = Uri.fromFile(Tool.getLocalImagePath(tempImageName));
                        //startImageCrop(photoUri); //先裁剪
                        handleSourceImage(photoUri);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // 在这里处理选择的照片
            } else if (requestCode == REQUEST_IMAGE_CROPPER) {

            }
        } else {
        }
    }

    private long getFileSizeFromUri(Uri uri) {
        long fileSize = 0;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (!cursor.isNull(sizeIndex)) {
                    fileSize = cursor.getLong(sizeIndex);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return fileSize;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ContentResolver contentResolver = getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        if (inputStream != null) {
            inputStream.close();
        }
        return bitmap;
    }

    void handleSourceImage(Uri imageUri) {
        Log.d("####", "handleSourceImage");

        Bitmap bitmap = null;
        try {
            long fileSizeInBytes = getFileSizeFromUri(imageUri);
            Log.d("####", "selected image file size:" + fileSizeInBytes);
            if (fileSizeInBytes > 5 * 1024 * 1024) {
                MessageDialog.show("Tip", "The photo is too large.", "ok").setOkTextInfo(new TextInfo().setFontSize(16));
                return;
            }
            bitmap = getBitmapFromUri(imageUri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bitmap == null) {
            return;
        }

        bitmap = Tool.resizeImage(bitmap, 1024, 1024);
        if (bitmap == null) {
            return;
        }

        boolean res = Tool.saveImage(bitmap, Tool.source_photo_name, Bitmap.CompressFormat.JPEG);
        if (res == false) {
            return;
        }

        File imageFile = Tool.getLocalImagePath(Tool.source_photo_name);
        if (!imageFile.exists()) {
            return;
        }

        originalBitmap = bitmap;
        imageView.setImageBitmap(originalBitmap);
        imageView2.setImageBitmap(originalBitmap);
        imageView3.setImageBitmap(originalBitmap);

    }
}