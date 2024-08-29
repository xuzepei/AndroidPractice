package com.demo.facereconstruction;


import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Tool {

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

    public static boolean isActivityRunning(Context context, Class<?> activityClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();
            for (ActivityManager.AppTask task : tasks) {
                try {
                    ActivityManager.RecentTaskInfo taskInfo = task.getTaskInfo();
                    if (taskInfo.isRunning) {
                        if (taskInfo.topActivity.getClassName().equals(activityClass.getName())) {
                            Log.d("####", "taskInfo.topActivity.getClassName():" + taskInfo.topActivity.getClassName());
                            Log.d("####", "activityClass.getName():" + activityClass.getName());
                            return true;
                        }
                    }
                } catch (Exception e) {
                    // 无法获取任务信息，可能是因为Activity已经结束
                }
            }
        }

        return false;
    }

    public static boolean isAnyActivityRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();
            for (ActivityManager.AppTask task : tasks) {
                try {
                    ActivityManager.RecentTaskInfo taskInfo = task.getTaskInfo();
                    if (taskInfo != null && taskInfo.isRunning) {
                        // 如果能够成功获取任务信息，则说明至少有一个Activity在运行
                        return true;
                    }

                } catch (Exception e) {
                    // 无法获取任务信息，可能是因为Activity已经结束
                }
            }
        }

        return false;
    }

    public static boolean saveModel(InputStream inputStream, String modelId) {

        if (inputStream == null || Tool.isNullOrEmpty(modelId)) {
            return false;
        }

        File dir = Tool.localModelDirectory();
        String filename = modelId + ".zip";
        File desFile = new File(dir, filename);

        Log.d("####", "Model zip path: " + desFile.getPath());

        removeFile(desFile);

        try (OutputStream outputStream = new FileOutputStream(desFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static File getLocalModelZip(String modelId) {

        if (Tool.isNullOrEmpty(modelId))
            return null;

        File dir = Tool.localModelDirectory();
        String filename = modelId + ".zip";
        File file = new File(dir, filename);
        if (file.exists()) {
            return file;
        }

        return null;
    }

    public static File localModelDirectory() {
        File dir = MainApplication.context.getExternalFilesDir(null);
        String folderName = "models";
        File modelDir = new File(dir, folderName);

        if (!modelDir.exists()) {
            if (modelDir.mkdir()) {
                // 文件夹创建成功
            } else {
                // 文件夹创建失败
            }
        }

        return modelDir;
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

}
