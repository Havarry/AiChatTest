package com.gzhy.aichat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {
    public static boolean isDebug = true;
    public static boolean isCache = true;
    public static boolean allowD = false;
    public static boolean allowE = false;
    public static boolean allowI = false;
    public static boolean allowV = false;
    public static boolean allowW = false;
    public static boolean allowWtf = false;
    public static int maxTime = 3;
    private static String mAppName = "sobot_chat";
    public static String path = null;
    private static File file = null;

    private LogUtils() {
    }

    public static void init(Context var0) {
        PackageManager var1 = var0.getApplicationContext().getPackageManager();

        try {
            ApplicationInfo var2 = var1.getApplicationInfo(var0.getPackageName(), 0);
            mAppName = (String)var1.getApplicationLabel(var2);
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
        }

        if(isCache) {
            setSaveDir(CommonUtil.getSDCardRootPath());
        }

    }

    public static void setSaveDir(String var0) {
        String var1 = var0 + File.separator + mAppName + "_log";
        path = var1 + File.separator + mAppName + "_" + getCurrentTime("yyyyMMdd") + "_log.txt";
        file = new File(var1);
    }

    public static void setCacheTime(int var0) {
        if(var0 >= 0) {
            maxTime = var0;
        }
    }

    private static String generateTag() {
        StackTraceElement var0 = (new Throwable()).getStackTrace()[2];
        String var1 = "%s.%s(L:%d)";
        String var2 = var0.getClassName();
        var2 = var2.substring(var2.lastIndexOf(".") + 1);
        var1 = String.format(var1, new Object[]{var2, var0.getMethodName(), Integer.valueOf(var0.getLineNumber())});
        var1 = TextUtils.isEmpty(mAppName)?var1:"[" + mAppName + "]:" + var1;
        return var1;
    }

    public static void setIsDebug(boolean var0) {
        isDebug = var0;
    }

    public static void setIsCache(boolean var0) {
        isCache = var0;
    }

    @SuppressLint({"SimpleDateFormat"})
    public static void save2Local(String var0, String var1, String var2, Throwable var3) {
        if(!TextUtils.isEmpty(path)) {
            PrintWriter var4 = null;
            Object var5 = null;
            String var6 = "\t\t";
            if(!file.exists()) {
                file.mkdirs();
            }

            try {
                var4 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path, true), "utf-8"));
                var4.println(TextUtils.isEmpty(var2)?"":var2);
                var4.flush();
                if(var3 != null) {
                    var3.printStackTrace(var4);
                }

                var4.flush();
            } catch (Exception var11) {
                var11.printStackTrace();
            } finally {
                if(var4 != null) {
                    var4.close();
                }

            }

            clearLog();
        }

    }

    private static void clearLog() {
        if(maxTime >= 0) {
            try {
                int var0 = Integer.parseInt(getCurrentTime("yyyyMMdd"));
                File[] var1 = file.listFiles();
                if(var1 != null && var1.length > 0) {
                    for(int var2 = 0; var2 < var1.length; ++var2) {
                        if(var1[var2].isFile()) {
                            String var3 = var1[var2].getName().split("_")[1];
                            Integer var4 = Integer.valueOf(var3);
                            if(var0 - var4.intValue() >= maxTime) {
                                var1[var2].delete();
                            }
                        }
                    }
                }
            } catch (Exception var5) {
                ;
            }

        }
    }

    public static synchronized void clearAllLog() {
        try {
            File[] var0 = file.listFiles();
            if(var0 != null && var0.length > 0) {
                for(int var1 = 0; var1 < var0.length; ++var1) {
                    if(var0[var1].isFile()) {
                        var0[var1].delete();
                    }
                }
            }
        } catch (Exception var2) {
            ;
        }

    }

    public static synchronized String getLogContent() {
        File var0 = new File(path);
        if(!var0.exists()) {
            return null;
        } else {
            JSONArray var1 = new JSONArray();
            BufferedReader var2 = null;

            try {
                String var3 = null;
                var2 = new BufferedReader(new FileReader(var0));

                while((var3 = var2.readLine()) != null) {
                    JSONObject var4 = new JSONObject(var3);
                    var1.put(var4);
                }
            } catch (Exception var13) {
                var13.printStackTrace();
            } finally {
                if(var2 != null) {
                    try {
                        var2.close();
                    } catch (IOException var12) {
                        var12.printStackTrace();
                    }
                }

            }

            return var1.toString();
        }
    }

    public static String getLogFilePath() {
        return path;
    }

    public static String getLogFileByDate(String var0) {
        if(TextUtils.isEmpty(var0)) {
            return null;
        } else {
            File var1 = new File(file, mAppName + "_" + var0 + "_log.txt");
            return var1.exists()?var1.getAbsolutePath():null;
        }
    }

    public static void printLogPath() {
        String var0 = generateTag();
        Log.i(var0, file.getPath());
    }

    @SuppressLint({"SimpleDateFormat"})
    private static String getCurrentTime(String var0) {
        Date var1 = new Date();
        SimpleDateFormat var2 = new SimpleDateFormat(var0);
        String var3 = var2.format(var1);
        return var3;
    }

    public static void d(String var0) {
        if(isCache || isDebug) {
            String var1 = generateTag();
            if(isDebug) {
                Log.d(var1, var0);
            }

            if(allowD && isCache) {
                save2Local("D", var1, var0, (Throwable)null);
            }

        }
    }

    public static void d(String var0, Throwable var1) {
        if(isCache || isDebug) {
            String var2 = generateTag();
            if(isDebug) {
                Log.d(var2, var0, var1);
            }

            if(allowD && isCache) {
                save2Local("D", var2, var0, var1);
            }

        }
    }

    public static void e(String var0) {
        if(isCache || isDebug) {
            String var1 = generateTag();
            if(isDebug) {
                Log.e(var1, var0);
            }

            if(allowE && isCache) {
                save2Local("E", var1, var0, (Throwable)null);
            }

        }
    }

    public static void e(String var0, Throwable var1) {
        if(isCache || isDebug) {
            String var2 = generateTag();
            if(isDebug) {
                Log.e(var2, var0, var1);
            }

            if(allowE && isCache) {
                save2Local("E", var2, var0, var1);
            }

        }
    }

    public static void i(String var0) {
        if(isCache || isDebug) {
            String var1 = generateTag();
            if(isDebug) {
                Log.i(var1, var0);
            }

            if(allowI && isCache) {
                save2Local("I", var1, var0, (Throwable)null);
            }

        }
    }

    public static void i(String var0, Throwable var1) {
        if(isCache || isDebug) {
            String var2 = generateTag();
            if(isDebug) {
                Log.i(var2, var0, var1);
            }

            if(allowI && isCache) {
                save2Local("I", var2, var0, var1);
            }

        }
    }

    public static void v(String var0) {
        if(isCache || isDebug) {
            String var1 = generateTag();
            if(isDebug) {
                Log.v(var1, var0);
            }

            if(allowV && isCache) {
                save2Local("V", var1, var0, (Throwable)null);
            }

        }
    }

    public static void v(String var0, Throwable var1) {
        if(isCache || isDebug) {
            String var2 = generateTag();
            if(isDebug) {
                Log.v(var2, var0, var1);
            }

            if(allowV && isCache) {
                save2Local("V", var2, var0, var1);
            }

        }
    }

    public static void w(String var0) {
        if(isCache || isDebug) {
            String var1 = generateTag();
            if(isDebug) {
                Log.w(var1, var0);
            }

            if(allowW && isCache) {
                save2Local("W", var1, var0, (Throwable)null);
            }

        }
    }

    public static void w(String var0, Throwable var1) {
        if(isCache || isDebug) {
            String var2 = generateTag();
            if(isDebug) {
                Log.w(var2, var0, var1);
            }

            if(allowW && isCache) {
                save2Local("W", var2, var0, var1);
            }

        }
    }

    public static void w(Throwable var0) {
        if(isCache || isDebug) {
            String var1 = generateTag();
            if(isDebug) {
                Log.w(var1, var0);
            }

            if(allowW && isCache) {
                save2Local("W", var1, (String)null, var0);
            }

        }
    }

    public static void wtf(String var0) {
        if(isCache || isDebug) {
            String var1 = generateTag();
            if(isDebug) {
                Log.wtf(var1, var0);
            }

            if(allowWtf && isCache) {
                save2Local("WTF", var1, var0, (Throwable)null);
            }

        }
    }

    public static void wtf(String var0, Throwable var1) {
        if(isCache || isDebug) {
            String var2 = generateTag();
            if(isDebug) {
                Log.wtf(var2, var0, var1);
            }

            if(allowWtf && isCache) {
                save2Local("WTF", var2, var0, var1);
            }

        }
    }

    public static void wtf(Throwable var0) {
        if(isCache || isDebug) {
            String var1 = generateTag();
            if(isDebug) {
                Log.wtf(var1, var0);
            }

            if(allowWtf && isCache) {
                save2Local("WTF", var1, (String)null, var0);
            }

        }
    }
}
