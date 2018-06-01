package com.gzhy.aichat.utils;

import android.content.Context;

public class MResources {
    /**
    * 通过反射获取资源
    * @param context Context对象
    * @param className 资源类型名称，例如"id, layout"
    * @param resName 布局id名
    * @return 资源id
    */
    public static int getIdByName(Context context, String className, String resName) {
        String packageName = context.getPackageName();
        int id = 0;
        try {
            Class r = Class.forName(packageName + ".R");
            Class[] classes = r.getClasses();
            Class resClass = null;
            for (Class cls : classes) {
                if (cls.getName().split("\\$")[1].equals(className)) {
                    resClass = cls;
                    break;
                }
            }
            if (resClass != null) {
                id = resClass.getField(resName).getInt(resClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
