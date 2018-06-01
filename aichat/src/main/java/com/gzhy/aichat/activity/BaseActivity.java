package com.gzhy.aichat.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.gzhy.aichat.application.MyApplication;
import com.gzhy.aichat.utils.CommonUtil;
import com.gzhy.aichat.utils.HyAiConstant;
import com.gzhy.aichat.utils.ResourceUtils;
import com.gzhy.aichat.utils.ToastUtil;
import com.gzhy.aichat.widget.statusbar.StatusBarCompat;

public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());
        //        设置状态栏颜色
        int sobot_status_bar_color = getResColor("sobot_status_bar_color");
        if (sobot_status_bar_color != 0) {
            try {
                StatusBarCompat.setStatusBarColor(this, sobot_status_bar_color);
            } catch (Exception e) {
                //请传入正确的颜色值
            }
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        MyApplication.getInstance().addActivity(this);
        try {
            initBundleData(savedInstanceState);
            initView();
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        MyApplication.getInstance().deleteActivity(this);
        super.onDestroy();
    }

    //返回布局id
    protected abstract int getContentViewResId();

    protected void initBundleData(Bundle savedInstanceState) {
    }

    protected abstract void initView();

    protected abstract void initData();

    public int getResId(String name) {
        return ResourceUtils.getIdByName(BaseActivity.this, "id", name);
    }

    public int getResDrawableId(String name) {
        return ResourceUtils.getIdByName(BaseActivity.this, "drawable", name);
    }

    public int getResLayoutId(String name) {
        return ResourceUtils.getIdByName(BaseActivity.this, "layout", name);
    }

    public int getResColorId(String name) {
        return ResourceUtils.getIdByName(BaseActivity.this, "color", name);
    }

    public int getResStringId(String name) {
        return ResourceUtils.getIdByName(BaseActivity.this, "string", name);
    }

    public String getResString(String name) {
        return getResources().getString(getResStringId(name));
    }

    public int getResColor(String name) {
        int resColorId = getResColorId(name);
        if (resColorId != 0) {
            return getResources().getColor(resColorId);
        }
        return 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case HyAiConstant.AI_PERMISSIONS_REQUEST_CODE:
                try {
                    for (int i = 0; i < grantResults.length; i++) {
                        //判断权限的结果，如果有被拒绝，就return
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            if (permissions[i] != null && permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                ToastUtil.showToast(getApplicationContext(), getResString("sobot_no_write_external_storage_permission"));
                            } else if (permissions[i] != null && permissions[i].equals(Manifest.permission.RECORD_AUDIO)) {
                                ToastUtil.showToast(getApplicationContext(), getResString("sobot_no_record_audio_permission"));
                            } else if (permissions[i] != null && permissions[i].equals(Manifest.permission.CAMERA)) {
                                ToastUtil.showToast(getApplicationContext(), getResString("sobot_no_camera_permission"));
                            }
                        }
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 检查存储权限
     *
     * @return true, 已经获取权限;false,没有权限,尝试获取
     */
    protected boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23 && CommonUtil.getTargetSdkVersion(getApplicationContext()) >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        HyAiConstant.AI_PERMISSIONS_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    /**
     * 检查录音权限
     *
     * @return true, 已经获取权限;false,没有权限,尝试获取
     */
    protected boolean checkStorageAndAudioPermission() {
        if (Build.VERSION.SDK_INT >= 23 && CommonUtil.getTargetSdkVersion(getApplicationContext()) >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        HyAiConstant.AI_PERMISSIONS_REQUEST_CODE);
                return false;
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        HyAiConstant.AI_PERMISSIONS_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    /**
     * 检查相机权限
     *
     * @return true, 已经获取权限;false,没有权限,尝试获取
     */
    protected boolean checkStorageAndCameraPermission() {
        if (Build.VERSION.SDK_INT >= 23 && CommonUtil.getTargetSdkVersion(getApplicationContext()) >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        HyAiConstant.AI_PERMISSIONS_REQUEST_CODE);
                return false;
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        HyAiConstant.AI_PERMISSIONS_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }
}
