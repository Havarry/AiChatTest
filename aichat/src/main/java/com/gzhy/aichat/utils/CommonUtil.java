package com.gzhy.aichat.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint({"SimpleDateFormat"})
public class CommonUtil {
    public CommonUtil() {
    }

    public static boolean isNetWorkConnected(Context context) {
        if(context != null) {
            @SuppressLint("WrongConstant") ConnectivityManager var1 = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo var2 = var1.getActiveNetworkInfo();
            if(var2 != null) {
                return var2.isAvailable();
            }
        }

        return false;
    }

    public static String GetNetworkType(Context context) {
        String var1 = "无网络";
        @SuppressLint("WrongConstant") ConnectivityManager var2 = (ConnectivityManager)context.getSystemService("connectivity");
        NetworkInfo var3 = var2.getActiveNetworkInfo();
        if(var3 != null && var3.isConnected()) {
            if(var3.getType() == 1) {
                var1 = "WIFI";
            } else if(var3.getType() == 0) {
                String var4 = var3.getSubtypeName();
                int var5 = var3.getSubtype();
                switch(var5) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        var1 = "2G";
                        break;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                        var1 = "3G";
                        break;
                    case 13:
                        var1 = "4G";
                        break;
                    default:
                        if(!var4.equalsIgnoreCase("TD-SCDMA") && !var4.equalsIgnoreCase("WCDMA") && !var4.equalsIgnoreCase("CDMA2000")) {
                            var1 = var4;
                        } else {
                            var1 = "3G";
                        }
                }
            }
        }

        return var1;
    }

    public static long getFileSize(String var0) {
        File var1 = new File(var0);
        long var2 = 0L;
        FileInputStream var4 = null;

        try {
            if(var1.exists()) {
                var4 = new FileInputStream(var1);
                var2 = (long)var4.available();
            } else {
                var1.createNewFile();
                System.out.println("文件不存在");
            }

            long var5 = var2;
            return var5;
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            try {
                if(var4 != null) {
                    var4.close();
                }
            } catch (IOException var15) {
                var15.printStackTrace();
            }

        }

        return 0L;
    }

    public static String formetFileSize(long var0) {
        DecimalFormat var2 = new DecimalFormat("#.00");
        String var3 = "";
        if(var0 < 1024L) {
            var3 = var2.format((double)var0) + "B";
        } else if(var0 < 1048576L) {
            var3 = var2.format((double)var0 / 1024.0D) + "K";
        } else if(var0 < 1073741824L) {
            var3 = var2.format((double)var0 / 1048576.0D) + "M";
        } else {
            var3 = var2.format((double)var0 / 1.073741824E9D) + "G";
        }

        return var3;
    }

    public static boolean isExitsSdcard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getSDPath() {
        File var0 = null;
        boolean var1 = Environment.getExternalStorageState().equals("mounted");
        if(var1) {
            var0 = Environment.getExternalStorageDirectory();
            return var0.toString();
        } else {
            return "/sdcard/Record/";
        }
    }

    public static String getSDCardRootPath() {
        String var0 = Environment.getExternalStorageDirectory().toString();
        LogUtils.i("手机外部存储地址：" + var0);
        return var0;
    }

    public static String getPrivatePath(Context var0) {
        return var0.getFilesDir().getPath();
    }

    public static int getVersion(Context var0) {
        PackageManager var1 = var0.getPackageManager();

        try {
            PackageInfo var2 = var1.getPackageInfo(var0.getPackageName(), 0);
            int var3 = var2.versionCode;
            return var3;
        } catch (PackageManager.NameNotFoundException var4) {
            return 0;
        }
    }

    public static String getPartnerId(Context var0) {
        return getDeviceId(var0);
    }

    public static String getDeviceId(Context var0) {
        StringBuilder var1 = new StringBuilder();
        String var2 = SharedPreferencesUtil.getStringData(var0, "deviceId", "");
        if(!TextUtils.isEmpty(var2)) {
            var1.append(var2);
            LogUtils.i("deviceId:" + var1.toString());
            return var1.toString();
        } else {
            String var3 = UUID.randomUUID().toString() + System.currentTimeMillis();
            var1.append(var3);
            SharedPreferencesUtil.saveStringData(var0, "deviceId", var3);
            LogUtils.i("deviceId:" + var1.toString());
            return var1.toString();
        }
    }

    public static String getPlatformUserId(Context var0) {
        StringBuilder var1 = new StringBuilder();
        String var2 = SharedPreferencesUtil.getStringData(var0, "sobot_platform_userid", "");
        if(!TextUtils.isEmpty(var2)) {
            var1.append(var2);
            LogUtils.i("platformUserId:" + var1.toString());
            return var1.toString();
        } else {
            String var3 = SharedPreferencesUtil.getStringData(var0, "sobot_platform_unioncode", "");
            String var4 = var3 + UUID.randomUUID().toString() + System.currentTimeMillis();
            var1.append(var4);
            SharedPreferencesUtil.saveStringData(var0, "sobot_platform_userid", var4);
            LogUtils.i("platformUserId:" + var1.toString());
            return var1.toString();
        }
    }

    public static String getPackageName(Context var0) {
        PackageManager var1 = var0.getPackageManager();

        try {
            PackageInfo var2 = var1.getPackageInfo(var0.getPackageName(), 0);
            String var3 = var2.packageName;
            return var3;
        } catch (PackageManager.NameNotFoundException var4) {
            return null;
        }
    }

    public static String getVersionName(Context var0) {
        PackageManager var1 = var0.getPackageManager();

        try {
            PackageInfo var2 = var1.getPackageInfo(var0.getPackageName(), 0);
            String var3 = var2.versionName;
            return var3;
        } catch (PackageManager.NameNotFoundException var4) {
            return "";
        }
    }

    public static synchronized String getAppName(Context var0) {
        try {
            PackageManager var1 = var0.getPackageManager();
            PackageInfo var2 = var1.getPackageInfo(var0.getPackageName(), 0);
            int var3 = var2.applicationInfo.labelRes;
            return var0.getResources().getString(var3);
        } catch (Exception var4) {
            var4.printStackTrace();
            return "";
        }
    }

    public static String encode(String var0) {
        if(var0 != null) {
            Matcher var1 = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(var0);

            while(var1.find()) {
                String var2 = var1.group();

                try {
                    var0 = var0.replaceAll(var2, URLEncoder.encode(var2, "UTF-8"));
                } catch (UnsupportedEncodingException var4) {
                    var4.printStackTrace();
                }
            }
        }

        return var0;
    }

    public static String encodeStr(String var0) {
        if(!TextUtils.isEmpty(var0)) {
            try {
                return URLEncoder.encode(var0, "UTF-8");
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

        return var0;
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat var0 = new SimpleDateFormat("HH:mm");
        return var0.format(new Date());
    }

    public static String getSobotCloudChatAppKey(Context var0) {
        return getPackageName(var0) + ".SobotCloudChatAppKey";
    }

    public static Bitmap rotaingImageView(int var0, Bitmap var1) {
        Matrix var2 = new Matrix();
        var2.postRotate((float)var0);
        Bitmap var3 = Bitmap.createBitmap(var1, 0, 0, var1.getWidth(), var1.getHeight(), var2, true);
        return var3;
    }

    public static int getBitmapDegree(String var0) {
        short var1 = 0;

        try {
            ExifInterface var2 = new ExifInterface(var0);
            int var3 = var2.getAttributeInt("Orientation", 1);
            switch(var3) {
                case 3:
                    var1 = 180;
                    break;
                case 6:
                    var1 = 90;
                    break;
                case 8:
                    var1 = 270;
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return var1;
    }

    public static boolean isMobileNO(String var0) {
        Pattern var1 = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher var2 = var1.matcher(var0);
        return var2.matches();
    }

    public static boolean isEmail(String var0) {
        String var1 = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern var2 = Pattern.compile(var1);
        Matcher var3 = var2.matcher(var0);
        return var3.matches();
    }

    public static boolean isBackground(Context var0) {
        @SuppressLint("WrongConstant") ActivityManager var1 = (ActivityManager)var0.getSystemService("activity");
        List var2 = var1.getRunningAppProcesses();
        if(var2 != null && var2.size() != 0) {
            Iterator var3 = var2.iterator();

            ActivityManager.RunningAppProcessInfo var4;
            do {
                if(!var3.hasNext()) {
                    return false;
                }

                var4 = (ActivityManager.RunningAppProcessInfo)var3.next();
            } while(var4.importance != 100);

            return var4.processName.equals(var0.getPackageName());
        } else {
            return false;
        }
    }

    public static String getRunningActivityName(Context var0) {
        @SuppressLint("WrongConstant") ActivityManager var1 = (ActivityManager)var0.getSystemService("activity");
        List var2 = var1.getRunningTasks(1);
        String var3 = "";
        if(var2 != null && var2.size() > 0) {
            var3 = ((ActivityManager.RunningTaskInfo)var2.get(0)).topActivity.getClassName();
        }

        return var3;
    }

    public static void sendLocalBroadcast(Context var0, Intent var1) {
        String var2 = getPackageName(var0);
        if(!TextUtils.isEmpty(var2)) {
            var1.setPackage(var2);
        }

        var0.sendBroadcast(var1);
    }

    public static boolean isScreenLock(Context var0) {
        @SuppressLint("WrongConstant") KeyguardManager var1 = (KeyguardManager)var0.getSystemService("keyguard");
        return var1.inKeyguardRestrictedInputMode();
    }

    public static int getTargetSdkVersion(Context var0) {
        int var1 = 0;

        try {
            PackageInfo var2 = var0.getPackageManager().getPackageInfo(var0.getPackageName(), 0);
            var1 = var2.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
        }

        return var1;
    }

    public static String getApplicationName(Context var0) {
        PackageManager var1 = null;
        ApplicationInfo var2 = null;

        try {
            var1 = var0.getPackageManager();
            var2 = var1.getApplicationInfo(var0.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException var4) {
            var2 = null;
        }

        String var3 = "";
        if(var2 != null) {
            var3 = (String)var1.getApplicationLabel(var2);
        }

        return var3;
    }

    public static String getResString(Context var0, String var1) {
        return var0.getResources().getString(getResStringId(var0, var1));
    }

    public static String getResString(Context var0, int var1) {
        return var0.getResources().getString(var1);
    }

    public static int getResStringId(Context var0, String var1) {
        return ResourceUtils.getIdByName(var0, "string", var1);
    }

    public static int getResId(Context var0, String var1) {
        return ResourceUtils.getIdByName(var0, "id", var1);
    }

    public static int getResDrawableId(Context var0, String var1) {
        return ResourceUtils.getIdByName(var0, "drawable", var1);
    }

    public static int getDimenId(Context var0, String var1) {
        return ResourceUtils.getIdByName(var0, "dimen", var1);
    }

    public static float getDimensPix(Context var0, String var1) {
        return var0.getResources().getDimension(getDimenId(var0, var1));
    }

    public static int isZh(Context var0) {
        Locale var1 = var0.getResources().getConfiguration().locale;
        String var2 = var1.getLanguage();
        return var2.endsWith("en")?1:0;
    }
}
