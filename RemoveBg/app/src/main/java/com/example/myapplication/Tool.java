package com.example.myapplication;

import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;


import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogx.util.TextInfo;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {

    public static String source_photo_name = "source_photo.jpg";
    public static String photo_data_json_key = "photo_data_json_key";
    public static String processed_image_name = "processed_image";
    public static String processed_multiple_image_name = "processed_multiple_image_name";
    public static String export_photo_success = "export_photo_success";
    public static String rate_us_shown = "rate_us_shown";
    public static String selected_size_unit_key = "selected_size_unit_key";

    public static String website = "https://xiaomei.appro7.com";
    public static String privacyPolicyUrl = "https://xiaomei.appro7.com/privacypolicy.html";
    public static String termOfUseUrl = "https://www.apple.com/legal/internet-services/itunes/dev/stdeula/";

    public static int shortScreenHeight = 1950;
    public static float smallScreenInInches = 5;

    private boolean isBlocking = false;

    private static Tool instance = null;

    public static synchronized Tool shared() {
        if (instance == null) {
            instance = new Tool();
        }
        return instance;
    }

    public Tool() {
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty() || str.equalsIgnoreCase("null") || str.equalsIgnoreCase("none");
    }


    public static File localImageDirectory() {
        File dir = MainApplication.context.getExternalFilesDir(null);
        String folderName = "images";
        File imageDir = new File(dir, folderName);

        if (!imageDir.exists()) {
            if (imageDir.mkdir()) {
                // 文件夹创建成功
            } else {
                // 文件夹创建失败
            }
        }

        return imageDir;
    }

    public static File getLocalImagePath(String imageUrl) {
        File imageDir = Tool.localImageDirectory();
        String imageFilename = Tool.getMD5(imageUrl);
        File imageFilePath = new File(imageDir, imageFilename);
        return imageFilePath;
    }

    public static boolean saveImage(Bitmap bitmap, String imageUrl, Bitmap.CompressFormat format) {

        File imageFile = Tool.getLocalImagePath(imageUrl);
        Tool.removeFile(imageFile);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(format, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean saveImage(Bitmap bitmap, String imageUrl, int dpi) {

        File imageFile = Tool.getLocalImagePath(imageUrl);
        Tool.removeFile(imageFile);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean saveImageToJPEG(Bitmap bitmap, String imageUrl, int dpi) {

        File imageFile = Tool.getLocalImagePath(imageUrl);
        Tool.removeFile(imageFile);

        try {
            ByteArrayOutputStream imageByteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageByteArray);
            byte[] imageData = imageByteArray.toByteArray();
            Tool.setDPI(imageData, dpi);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            fileOutputStream.write(imageData);
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void setDPI(byte[] imageData, int dpi) {
        imageData[13] = 1;
        imageData[14] = (byte) (dpi >> 8);
        imageData[15] = (byte) (dpi & 0xff);
        imageData[16] = (byte) (dpi >> 8);
        imageData[17] = (byte) (dpi & 0xff);
    }

    public static Bitmap getLocalImage(String imageUrl) {
        File imageFile = Tool.getLocalImagePath(imageUrl);

        try {
            FileInputStream fis = new FileInputStream(imageFile);
            return BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean removeFile(File file) {
        if (file.exists()) {
            if (file.delete()) {
                Log.d("####", "File deleted successfully");
                return true;
            } else {
                Log.d("####", "Failed to delete the file");
            }
        }

        return false;
    }

    public static float getFileSizeInMB(File file) {
        if (file.exists() && file.isFile()) {
            return file.length() / 1024.0f / 1024.0f;
        }
        return 0;
    }

    public static String getMD5(String input) {

        try {
            // 创建 MessageDigest 对象，并指定算法为 MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 将字符串转换为字节数组
            byte[] bytes = input.getBytes();
            // 使用 MessageDigest 更新字节数组
            byte[] digestBytes = digest.digest(bytes);
            // 将字节数组转换为十六进制字符串
            StringBuilder builder = new StringBuilder();
            for (byte b : digestBytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getCurrentRegion() {

        // Get the default locale
        Locale currentLocale = Locale.getDefault();
        // Get the country/region code
        String regionCode = currentLocale.getCountry();
        // Example: Log the region code
        Log.d("####", "Region Code: " + regionCode);

        return regionCode;
    }

    public static String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = MainApplication.context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject getPassportPhotoSizeByRegion(String regionCode) {

        String jsonString = Tool.loadJSONFromAsset("photo_size_all.json");
        if (Tool.isNullOrEmpty(jsonString) || Tool.isNullOrEmpty(regionCode)) {
            return null;
        }

        try {
            // Convert JSON string to JSONObject
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String region_code = jsonObject.getString("region_code");
                if (region_code.equals(regionCode)) {
                    return jsonObject;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONArray getPhotoSizesByRegion(String regionCode) {

        String jsonString = Tool.loadJSONFromAsset("photo_size_all.json");
        if (Tool.isNullOrEmpty(jsonString) || Tool.isNullOrEmpty(regionCode)) {
            return null;
        }

        try {

            // Convert JSON string to JSONObject
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONArray resultArray = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String region_code = jsonObject.getString("region_code");
                if (region_code.equals(regionCode)) {
                    resultArray.put(jsonObject);
                }
            }
            return resultArray;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONArray getAllPhotoSizes() {
        String jsonString = Tool.loadJSONFromAsset("photo_size_all.json");
        if (Tool.isNullOrEmpty(jsonString)) {
            return null;
        }

        try {
            // Convert JSON string to JSONObject
            JSONArray jsonArray = new JSONArray(jsonString);
            if (jsonArray.length() > 0) {
                return jsonArray;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isPad() {
        boolean isTablet = MainApplication.context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
        if (isTablet) {
            return true;
        }

        return false;
    }



    public static DisplayMetrics getScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) MainApplication.context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static double diagonalInInches() {
        //DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) MainApplication.context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        float widthInInches = metrics.widthPixels / metrics.xdpi;
        float heightInInches = metrics.heightPixels / metrics.ydpi;
        double diagonalInInches = Math.sqrt(Math.pow(widthInInches, 2) + Math.pow(heightInInches, 2));

        // 判断对角线尺寸小于4.7英寸
        return diagonalInInches;
    }

    public static Drawable getDrawableImage(Context context, int resId, int colorId) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        if (drawable != null) {
            if (colorId != -1) {
                DrawableCompat.setTint(drawable, colorId);
            }
        }
        return drawable;
    }

    public static String getAppVersion() {
        String version = "";
        try {
            PackageManager packageManager = MainApplication.context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(MainApplication.context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String getPrivacyPolicyUrl() {
//        if (LanguageUtil.isZH()) {
//            if (region.equals(Region.CN)) {
//                return Tool.cn_register_policy_zh;
//            } else if (region.equals(Region.EU)) {
//                return Tool.eu_register_policy_zh;
//            } else if (region.equals(Region.EN)) {
//                return Tool.en_register_policy_zh;
//            } else if (region.equals(Region.IN)) {
//                return Tool.en_register_policy_zh;
//            } else if (region.equals(Region.OTHER)) {
//                return Tool.en_register_policy_zh;
//            }
//        } else {
//            if (region.equals(Region.CN)) {
//                return Tool.cn_register_policy_en;
//            } else if (region.equals(Region.EU)) {
//                return Tool.eu_register_policy_en;
//            } else if (region.equals(Region.EN)) {
//                return Tool.en_register_policy_en;
//            } else if (region.equals(Region.IN)) {
//                return Tool.en_register_policy_en;
//            } else if (region.equals(Region.OTHER)) {
//                return Tool.en_register_policy_en;
//            }
//        }

        return "";
    }

    public static boolean compareVersion(String newVersion) {

        String currentVersion = Tool.getAppVersion();
        if (!Tool.isNullOrEmpty(currentVersion) && !Tool.isNullOrEmpty(newVersion)) {
            Log.d("####", "currentVersion: " + currentVersion);
            Log.d("####", "newVersion: " + newVersion);

            String[] currentVersionArray = currentVersion.split("\\.");
            String[] newVersionArray = newVersion.split("\\.");
            if (currentVersionArray.length == 3 && newVersionArray.length == 3) {

                for (int i = 0; i < 3; i++) {
                    int result = newVersionArray[i].compareTo(currentVersionArray[i]);
                    if (result > 0) {
                        return true;
                    } else if (result == 0) {
                        continue;
                    } else {
                        return false;
                    }
                }
            }

        }

        return false;
    }

    public static long getDirectorySize(File directory) {
        long size = 0;
        if (directory != null && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        size += file.length();
                    } else if (file.isDirectory()) {
                        size += getDirectorySize(file);
                    }
                }
            }
        }
        return size;
    }

    public static String formatSize(long size) {
        String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double sizeInUnit = size;
        while (sizeInUnit >= 1024 && unitIndex < units.length - 1) {
            sizeInUnit /= 1024;
            unitIndex++;
        }
        return String.format("%.0f %s", sizeInUnit, units[unitIndex]);
    }

    public static boolean deleteDirectory(File directory) {
        if (directory != null && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (!file.delete()) {
                            return false;
                        }
                    } else if (file.isDirectory()) {
                        if (!deleteDirectory(file)) {
                            return false;
                        }
                    }
                }
            }
        }
        return directory.delete();
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) MainApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Bitmap rotateImageIfRequired(Bitmap img, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
            return img;
        }

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    public static Bitmap rotateImageIfRequired(Bitmap img, Uri imageUri) {

        ExifInterface ei;
        try {
            InputStream input = MainApplication.context.getContentResolver().openInputStream(imageUri);
            ei = new ExifInterface(input);
        } catch (IOException e) {
            e.printStackTrace();
            return img;
        }

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                return img;
        }
    }

    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

    public static int dpToPx(int dp) {
        float density = MainApplication.context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static int pxToDp(int px) {
        // 获取当前设备的屏幕密度
        float density = MainApplication.context.getResources().getDisplayMetrics().density;
        // 将 px 转换为 dp
        return Math.round(px / density);
    }

    public static String removeWhitespaceAndNewlines(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("\\s+", ""); // Removes all whitespace characters including spaces, tabs, and newlines
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap resizeImage(Bitmap bitmap, int minWidth, int minHeight) {
        int MAX_WIDTH = 2000;
        int MAX_HEIGHT = 2000;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > MAX_WIDTH || height > MAX_HEIGHT) {
            float scaleWidth = ((float) MAX_WIDTH) / width;
            float scaleHeight = ((float) MAX_HEIGHT) / height;
            float scaleFactor = Math.min(scaleWidth, scaleHeight);

            width = Math.round(width * scaleFactor);
            height = Math.round(height * scaleFactor);

            //bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }

        if(minWidth <= minHeight) {
            if(height <= minHeight) {
                if(minHeight != 0 && height < minHeight) {
                    float scaleFactor = height * 1.0f / minHeight;
                    height = minHeight;
                    width = (int)(width / scaleFactor);
                }
            }
        } else  {
            if(width <= minWidth) {
                if(minWidth != 0 && width < minWidth) {
                    float scaleFactor = width * 1.0f / minWidth;
                    width = minWidth;
                    height = (int)(height / scaleFactor);
                }
            }
        }



        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

        return bitmap;
    }

    public static void animateNumber(TextView tv, int startValue, int endValue, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startValue, endValue);
        valueAnimator.setDuration(duration); // 动画持续时间

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int animatedValue = (int) animator.getAnimatedValue();
                if (tv != null)
                    tv.setText(String.valueOf(animatedValue));
            }
        });

        valueAnimator.start(); // 开始动画
    }

    public static int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = MainApplication.context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            statusBarHeight = MainApplication.context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }

    public static boolean isInteger(float number) {
        return number == (int) number;
    }

    // 将 ImageView 中的图片和背景合成为一张图片
    public static Bitmap combineImageViewWithBackground(ImageView imageView, int backgroundColor) {
        // 获取 ImageView 的宽高
        int width = imageView.getWidth();
        int height = imageView.getHeight();

        // 创建一个空白的 Bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // 创建一个 Canvas，绑定到该 Bitmap 上
        Canvas canvas = new Canvas(bitmap);

        // 绘制背景颜色
        canvas.drawColor(backgroundColor);

        // 获取 ImageView 的 Drawable
        Drawable drawable = imageView.getDrawable();

        if (drawable != null) {
            // 设置 Drawable 的边界并绘制到 Canvas 上
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
        }

        return bitmap;
    }

    public static boolean isFloatDecimal(float number) {

        Log.d("####", "" + (number % 1 != 0));
        return number % 1 != 0;
    }

    public static boolean isValidDecimalNumber(String number) {
        if (Tool.isNullOrEmpty(number))
            return false;
        // 正则表达式，匹配正数和带有一个小数点的小数
        String regex = "^[0-9]*\\.?[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public static boolean isValidIntegerNumber(String number) {
        if (Tool.isNullOrEmpty(number))
            return false;
        // 正则表达式，匹配正数和带有一个小数点的小数
        String regex = "^[0-9]*?[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

}
