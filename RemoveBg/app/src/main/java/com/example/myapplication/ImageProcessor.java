package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

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

    static FloatBuffer preprocessBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        FloatBuffer buffer = FloatBuffer.allocate(3 * height * width);

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        // Normalize to [-1, 1]
        for (int c = 0; c < 3; c++) { // channel-first
            for (int i = 0; i < pixels.length; i++) {
                int color = pixels[i];
                int value = 0;
                switch (c) {
                    case 0: value = (color >> 16) & 0xFF; break; // R
                    case 1: value = (color >> 8) & 0xFF; break;  // G
                    case 2: value = color & 0xFF; break;         // B
                }
                float normalized = (value / 255.0f - 0.5f) / 0.5f; // => [-1,1]
                buffer.put(c * width * height + i, normalized);
            }
        }

        return buffer;
    }

    static Bitmap createAlphaBitmap(float[][] matte, int width, int height) {
        int h = matte.length;
        int w = matte[0].length;

        Bitmap alpha = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        for (int y = 0; y < height; y++) {
            int srcY = Math.min((int) ((float) y * h / height), h - 1);
            for (int x = 0; x < width; x++) {
                int srcX = Math.min((int) ((float) x * w / width), w - 1);
                int a = Math.min(255, Math.max(0, (int) (matte[srcY][srcX] * 255)));
                alpha.setPixel(x, y, Color.argb(a, 0, 0, 0));
            }
        }
        return alpha;
    }

    static Bitmap combineRGBWithAlpha(Bitmap rgb, Bitmap alpha) {
        Bitmap result = Bitmap.createBitmap(rgb.getWidth(), rgb.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        canvas.drawBitmap(rgb, 0, 0, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(alpha, 0, 0, paint);

        return result;
    }
}
