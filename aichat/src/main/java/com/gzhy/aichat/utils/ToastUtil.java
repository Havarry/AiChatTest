package com.gzhy.aichat.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * Created by hy on 2018-05-26
 */

public class ToastUtil {
    private static Toast toast;

    public ToastUtil() {
    }

    public static void showToast(Context context, String text) {
        if(!TextUtils.isEmpty(text)) {
            context = context.getApplicationContext();
            if(toast == null) {
                toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            } else {
                toast.setText(text);
            }

            toast.show();
        }
    }
    public static void showLongToast(Context context, String text) {
        if(!TextUtils.isEmpty(text)) {
            context = context.getApplicationContext();
            if(toast == null) {
                toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            } else {
                toast.setText(text);
            }

            toast.show();
        }
    }

    public static void showCustomToast(Context var0, String var1, int var2) {
        if(TextUtils.isEmpty(var1)) {
            var1 = CommonUtil.getResString(var0, "sobot_server_request_wrong");
        }

        CustomToast.makeText(var0, var1, 0, var2).show();
    }

    public static void showCopyPopWindows(final Context var0, View var1, final String var2, int var3, int var4) {
        View var5 = LayoutInflater.from(var0).inflate(ResourceUtils.getIdByName(var0, "layout", "sobot_pop_chat_room_long_press"), (ViewGroup)null);
        final PopupWindow var6 = new PopupWindow(var5, -2, -2, true);
        var6.setBackgroundDrawable(new ColorDrawable(0));
        var5.measure(150, 150);
        int var7 = var5.getMeasuredWidth();
        int var8 = var5.getMeasuredHeight() + 20;
        int[] var9 = new int[2];
        var1.getLocationOnScreen(var9);
        var6.showAtLocation(var1, 0, var9[0] + var1.getWidth() / 2 - var7 / 2 + var3, var9[1] - var8 + var4);
        var6.update();
        var5.findViewById(CommonUtil.getResId(var0, "sobot_tv_copy_txt")).setOnClickListener(new View.OnClickListener() {
            public void onClick(View var1) {
                if(Build.VERSION.SDK_INT >= 11) {
                    LogUtils.i("API是大于11");
                    ClipboardManager var2x = (ClipboardManager)var0.getApplicationContext().getSystemService("clipboard");
                    var2x.setText(var2);
                    var2x.getText();
                } else {
                    LogUtils.i("API是小于11");
                    android.text.ClipboardManager var3 = (android.text.ClipboardManager)var0.getApplicationContext().getSystemService("clipboard");
                    var3.setText(var2);
                    var3.getText();
                }

                ToastUtil.showCustomToast(var0, CommonUtil.getResString(var0, "sobot_ctrl_v_success"), CommonUtil.getResDrawableId(var0, "sobot_iv_login_right"));
                var6.dismiss();
            }
        });
    }

}
