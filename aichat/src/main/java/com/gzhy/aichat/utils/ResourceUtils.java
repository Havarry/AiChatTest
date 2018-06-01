package com.gzhy.aichat.utils;

import android.content.Context;

public class ResourceUtils {
    public ResourceUtils() {
    }

    public static int[] getResourseIdByName(String var0, String var1, String var2) {
        Class var3 = null;
        int[] var4 = new int[1];

        try {
            var3 = Class.forName(var0 + ".R");
            Class[] var5 = var3.getClasses();
            Class var6 = null;

            for(int var7 = 0; var7 < var5.length; ++var7) {
                if(var5[var7].getName().split("\\$")[1].equals(var1)) {
                    var6 = var5[var7];
                    break;
                }
            }

            if(var6 != null) {
                var4 = (int[])((int[])var6.getField(var2).get(var6));
            }
        } catch (ClassNotFoundException var8) {
            var8.printStackTrace();
        } catch (IllegalArgumentException var9) {
            var9.printStackTrace();
        } catch (SecurityException var10) {
            var10.printStackTrace();
        } catch (IllegalAccessException var11) {
            var11.printStackTrace();
        } catch (NoSuchFieldException var12) {
            var12.printStackTrace();
        }

        return var4;
    }

    public static int getIdByName(Context var0, String var1, String var2) {
        var0 = var0.getApplicationContext();
        String var3 = var0.getPackageName();
        int var4 = var0.getResources().getIdentifier(var2, var1, var3);
        return var4;
    }

}
