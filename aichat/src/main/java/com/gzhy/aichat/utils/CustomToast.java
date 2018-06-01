package com.gzhy.aichat.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hy on 2018-05-30
 */
public class CustomToast {
    public CustomToast() {
    }

    public static Toast makeText(Context var0, int var1, int var2) {
        Toast var3 = new Toast(var0.getApplicationContext());
        View var4 = View.inflate(var0, ResourceUtils.getIdByName(var0, "layout", "sobot_custom_toast_layout"), (ViewGroup)null);
        TextView var5 = (TextView)var4.findViewById(ResourceUtils.getIdByName(var0, "id", "sobot_tv_content"));
        var5.setText(var1);
        ImageView var6 = (ImageView)var4.findViewById(ResourceUtils.getIdByName(var0, "id", "sobot_iv_content"));
        var6.setImageResource(ResourceUtils.getIdByName(var0, "drawable", "sobot_pop_warning_attention"));
        var3.setView(var4);
        var3.setGravity(17, 0, 0);
        var3.setDuration(var2);
        return var3;
    }

    public static Toast makeText(Context var0, CharSequence var1, int var2) {
        Toast var3 = new Toast(var0.getApplicationContext());
        View var4 = View.inflate(var0, ResourceUtils.getIdByName(var0, "layout", "sobot_custom_toast_layout"), (ViewGroup)null);
        TextView var5 = (TextView)var4.findViewById(ResourceUtils.getIdByName(var0, "id", "sobot_tv_content"));
        var5.setText(var1);
        ImageView var6 = (ImageView)var4.findViewById(ResourceUtils.getIdByName(var0, "id", "sobot_iv_content"));
        var6.setImageResource(ResourceUtils.getIdByName(var0, "drawable", "sobot_pop_warning_attention"));
        var3.setView(var4);
        var3.setGravity(17, 0, 0);
        var3.setDuration(var2);
        return var3;
    }

    public static Toast makeText(Context var0, CharSequence var1, int var2, int var3) {
        Toast var4 = new Toast(var0.getApplicationContext());
        View var5 = View.inflate(var0, ResourceUtils.getIdByName(var0, "layout", "sobot_custom_toast_layout"), (ViewGroup)null);
        TextView var6 = (TextView)var5.findViewById(ResourceUtils.getIdByName(var0, "id", "sobot_tv_content"));
        var6.setText(var1);
        ImageView var7 = (ImageView)var5.findViewById(ResourceUtils.getIdByName(var0, "id", "sobot_iv_content"));
        var7.setImageResource(var3);
        var4.setView(var5);
        var4.setGravity(17, 0, 0);
        var4.setDuration(var2);
        return var4;
    }
}
