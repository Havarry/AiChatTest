package com.gzhy.aichat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesUtil {
    private static SharedPreferences sharedPreferences;
    private static String CONFIG = "gzhyai_config";

    public SharedPreferencesUtil() {
    }

    public static void saveStringData(Context var0, String var1, String var2) {
        if(sharedPreferences == null) {
            sharedPreferences = var0.getSharedPreferences(CONFIG, MODE_PRIVATE);
        }

        sharedPreferences.edit().putString(var1, var2).commit();
    }

    public static void saveBooleanData(Context var0, String var1, boolean var2) {
        if(sharedPreferences == null) {
            sharedPreferences = var0.getSharedPreferences(CONFIG, MODE_PRIVATE);
        }

        sharedPreferences.edit().putBoolean(var1, var2).commit();
    }

    public static boolean getBooleanData(Context var0, String var1, boolean var2) {
        if(sharedPreferences == null) {
            sharedPreferences = var0.getSharedPreferences(CONFIG, MODE_PRIVATE);
        }

        return sharedPreferences.getBoolean(var1, var2);
    }

    public static String getStringData(Context var0, String var1, String var2) {
        if(sharedPreferences == null) {
            sharedPreferences = var0.getSharedPreferences(CONFIG, MODE_PRIVATE);
        }

        return sharedPreferences.getString(var1, var2);
    }

    public static void saveIntData(Context var0, String var1, int var2) {
        if(sharedPreferences == null) {
            sharedPreferences = var0.getSharedPreferences(CONFIG, MODE_PRIVATE);
        }

        sharedPreferences.edit().putInt(var1, var2).commit();
    }

    public static void saveLongData(Context var0, String var1, long var2) {
        if(sharedPreferences == null) {
            sharedPreferences = var0.getSharedPreferences(CONFIG, MODE_PRIVATE);
        }

        sharedPreferences.edit().putLong(var1, var2).commit();
    }

    public static int getIntData(Context var0, String var1, int var2) {
        if(sharedPreferences == null) {
            sharedPreferences = var0.getSharedPreferences(CONFIG, MODE_PRIVATE);
        }

        return sharedPreferences.getInt(var1, var2);
    }

    public static long getLongData(Context var0, String var1, long var2) {
        if(sharedPreferences == null) {
            sharedPreferences = var0.getSharedPreferences(CONFIG, MODE_PRIVATE);
        }

        return sharedPreferences.getLong(var1, var2);
    }

    public static void removeKey(Context var0, String var1) {
        if(sharedPreferences == null) {
            sharedPreferences = var0.getSharedPreferences(CONFIG, MODE_PRIVATE);
        }

        sharedPreferences.edit().remove(var1).commit();
    }

}
