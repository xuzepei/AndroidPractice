package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.mediapipe.framework.image.BitmapImageBuilder;
import com.google.mediapipe.framework.image.ByteBufferExtractor;
import com.google.mediapipe.framework.image.MPImage;
import com.google.mediapipe.tasks.core.BaseOptions;
import com.google.mediapipe.tasks.core.Delegate;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.imagesegmenter.ImageSegmenter;
import com.google.mediapipe.tasks.vision.imagesegmenter.ImageSegmenterResult;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class ImageProcessor {

    private static ImageProcessor instance = null;

    ResultCallback resultCallback = null;
    Bitmap originalBitmap = null;

    public static synchronized ImageProcessor shared() {
        if (instance == null) {
            instance = new ImageProcessor();
        }
        return instance;
    }

    public void removeBackground(Bitmap originalBitmap, ResultCallback callback) {

        if(originalBitmap == null)
            return;

        this.originalBitmap = originalBitmap;
        this.resultCallback = callback;

        processWithMediaPipe(0);
    }

    void processWithMediaPipe(int maskType) {

        BaseOptions.Builder baseOptionsBuilder = BaseOptions.builder();
        baseOptionsBuilder.setModelAssetPath("selfie_multiclass1.tflite");
        baseOptionsBuilder.setDelegate(Delegate.CPU);

        ImageSegmenter imageSegmenter = null;
        try {
            ImageSegmenter.ImageSegmenterOptions options = ImageSegmenter.ImageSegmenterOptions.builder()
                    .setBaseOptions(
                            baseOptionsBuilder.build()
                    )
                    .setRunningMode(RunningMode.IMAGE)
                    .setOutputCategoryMask(true)
                    .setOutputConfidenceMasks(true)
                    .build();

            imageSegmenter = ImageSegmenter.createFromOptions(MainApplication.context, options);
        } catch (Exception e) {
            e.printStackTrace();

            //DialogX.globalStyle = MaterialStyle.style();
            //PopTip.show(R.drawable.error, e.toString()).setTintIcon(false).setMessageTextInfo(new TextInfo().setFontSize(16)).setBackgroundColor(Colors.white).setRadius(100).showLong();
        }

        if(0 == maskType) {
            useCategoryMask(imageSegmenter);
        } else if(1 == maskType) {
            useConfidenceMasks(imageSegmenter);
        }
    }

    void useConfidenceMasks(ImageSegmenter imageSegmenter) {

        if(null == imageSegmenter)
            return;

        // 获取原始图片的 Bitmap
        Bitmap inputBitmap = originalBitmap;

        // Create an MPImage from the input Bitmap
        MPImage mpImage = new BitmapImageBuilder(inputBitmap).build();
        // Perform image segmentation
        ImageSegmenterResult segmenterResult = imageSegmenter.segment(mpImage);

        // Retrieve the confidence masks (one FloatBuffer per class)
        MPImage confidenceMaskGet = segmenterResult.confidenceMasks().get().get(0);

        FloatBuffer byteBuffer = ByteBufferExtractor.extract(confidenceMaskGet).asFloatBuffer();

        int[] pixels = new int[byteBuffer.capacity()];
        int[] originalPixels = new int[inputBitmap.getWidth() * inputBitmap.getHeight()];
        inputBitmap.getPixels(originalPixels, 0, inputBitmap.getWidth(), 0, 0, inputBitmap.getWidth(), inputBitmap.getHeight());

        for (int i = 0; i < pixels.length; i++) {
            float confidence = byteBuffer.get();

            //Log.d("####", "confidence: " + confidence);
            if(confidence <= 0.5) {
                //Log.d("####", "pixel: " + originalPixels[i]);
                int argbValue = originalPixels[i];
                pixels[i] = argbValue;

            } else {
                int color = Color.argb((int) (0), 255, 0, 0);
                pixels[i] = color;
//                int argbValue = originalPixels[i];
//                pixels[i] = argbToColor(argbValue, 1 - confidence);
            }

        }

        Bitmap image = Bitmap.createBitmap(
                pixels,                   // The pixel array
                confidenceMaskGet.getWidth(),  // The width of the bitmap
                confidenceMaskGet.getHeight(), // The height of the bitmap
                Bitmap.Config.ARGB_8888    // The bitmap configuration
        );

        if(resultCallback != null) {
            resultCallback.OnResult(true, image);
        }
    }

    public static int argbToColor(int argb, float confident) {
        int alpha = (argb >> 24) & 0xFF;
        int red = (argb >> 16) & 0xFF;
        int green = (argb >> 8) & 0xFF;
        int blue = argb & 0xFF;

        int newAlpha = (int) (alpha * confident);

        // Create and return the color using the extracted components
        return Color.argb(newAlpha, red, green, blue);
    }

    void useCategoryMask(ImageSegmenter imageSegmenter) {

        if(null == imageSegmenter)
            return;

        // 获取原始图片的 Bitmap
        Bitmap inputBitmap = originalBitmap;

        // Create an MPImage from the input Bitmap
        MPImage mpImage = new BitmapImageBuilder(inputBitmap).build();
        // Perform image segmentation
        ImageSegmenterResult segmenterResult = imageSegmenter.segment(mpImage);

        MPImage categoryMaskGet = segmenterResult.categoryMask().get();
        ByteBuffer byteBuffer = ByteBufferExtractor.extract(categoryMaskGet);

        int[] pixels = new int[byteBuffer.capacity()];
        int[] originalPixels = new int[inputBitmap.getWidth() * inputBitmap.getHeight()];
        inputBitmap.getPixels(originalPixels, 0, inputBitmap.getWidth(), 0, 0, inputBitmap.getWidth(), inputBitmap.getHeight());

        for (int i = 0; i < pixels.length; i++) {
            int index = Byte.toUnsignedInt(byteBuffer.get(i)) % 20;
            if (index != 0) {
                pixels[i] = originalPixels[i];
            }
        }

        Bitmap image = Bitmap.createBitmap(
                pixels,                   // The pixel array
                categoryMaskGet.getWidth(),  // The width of the bitmap
                categoryMaskGet.getHeight(), // The height of the bitmap
                Bitmap.Config.ARGB_8888    // The bitmap configuration
        );

        if(resultCallback != null) {
            resultCallback.OnResult(true, image);
        }
    }

    /*
    void processWithMLKit() {
        // 获取原始图片的 Bitmap
        Bitmap originalBitmapCopy = originalBitmap;

        // 创建 Selfie Segmenter
        SelfieSegmenterOptions options =
                new SelfieSegmenterOptions.Builder()
                        .setDetectorMode(SelfieSegmenterOptions.STREAM_MODE)
                        .build();

        Segmenter segmenter = Segmentation.getClient(options);

        // 将 Bitmap 转换为 InputImage
        InputImage image = InputImage.fromBitmap(originalBitmapCopy, 0);


        // 处理图像并获取结果
        segmenter.process(image)
                .addOnSuccessListener(segmentationMask -> {
                    Bitmap outputBitmap = removeBackground(originalBitmapCopy, segmentationMask);

                    //Bitmap outputBitmap = smoothBackgroundRemoval(originalBitmapCopy, segmentationMask);
                    photoIV.setImageBitmap(outputBitmap);
                })
                .addOnFailureListener(e -> {
                    // 处理失败情况
                    e.printStackTrace();
                });
    }

    // 移除背景方法
    private Bitmap removeBackground(Bitmap originalBitmap, SegmentationMask mask) {
        Bitmap resultBitmap = Bitmap.createBitmap(
                originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        //paint.setFilterBitmap(true);

        ByteBuffer maskBuffer = mask.getBuffer();

        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        // 遍历每个像素，根据 mask 确定是否保留
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                float foregroundConfidence = maskBuffer.getFloat();

                if (foregroundConfidence > 0.5) {
                    paint.setColor(originalBitmap.getPixel(x, y));
                } else {
                    paint.setColor(Color.WHITE);
                }
                canvas.drawPoint(x, y, paint);
            }
        }

        return resultBitmap;
    }
    */
}
