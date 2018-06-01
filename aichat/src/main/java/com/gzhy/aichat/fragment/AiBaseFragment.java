package com.gzhy.aichat.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.gzhy.aichat.utils.CommonUtil;
import com.gzhy.aichat.utils.HyAiConstant;
import com.gzhy.aichat.utils.ResourceUtils;
import com.gzhy.aichat.utils.ToastUtil;

/**
 * Created by hy on 2018-05-26
 */

public abstract class AiBaseFragment extends Fragment{

    public AiBaseFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public int getResId(String name) {
        return ResourceUtils.getIdByName(getContext(), "id", name);
    }

    public int getResDrawableId(String name) {
        return ResourceUtils.getIdByName(getContext(), "drawable", name);
    }

    public int getResLayoutId(String name) {
        return ResourceUtils.getIdByName(getContext(), "layout", name);
    }

    public int getResStringId(String name) {
        return ResourceUtils.getIdByName(getContext(), "string", name);
    }

    public String getResString(String name) {
        return getResources().getString(getResStringId(name));
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
                                ToastUtil.showToast(getContext().getApplicationContext(), getResString("sobot_no_write_external_storage_permission"));
                            } else if (permissions[i] != null && permissions[i].equals(Manifest.permission.RECORD_AUDIO)) {
                                ToastUtil.showToast(getContext().getApplicationContext(), getResString("sobot_no_record_audio_permission"));
                            } else if (permissions[i] != null && permissions[i].equals(Manifest.permission.CAMERA)) {
                                ToastUtil.showToast(getContext().getApplicationContext(), getResString("sobot_no_camera_permission"));
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
     * @param resourceId
     * @param textId
     */
    protected void showRightMenu(View tmpMenu, int resourceId, String textId) {
        if (tmpMenu == null || !(tmpMenu instanceof TextView)) {
            return;
        }
        TextView rightMenu = (TextView) tmpMenu;
        if (!TextUtils.isEmpty(textId)) {
            rightMenu.setText(textId);
        } else {
            rightMenu.setText("");
        }

        if (resourceId != 0) {
            Drawable img = getResources().getDrawable(resourceId);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            rightMenu.setCompoundDrawables(null, null, img, null);
        } else {
            rightMenu.setCompoundDrawables(null, null, null, null);
        }

        rightMenu.setVisibility(View.VISIBLE);
    }

    /**
     * @param resourceId
     * @param textId
     */
    protected void showLeftMenu(View tmpMenu,int resourceId, String textId) {
        if (tmpMenu == null || !(tmpMenu instanceof TextView)) {
            return;
        }
        TextView leftMenu = (TextView) tmpMenu;
        if (!TextUtils.isEmpty(textId)) {
            leftMenu.setText(textId);
        } else {
            leftMenu.setText("");
        }

        if (resourceId != 0) {
            Drawable img = getResources().getDrawable(resourceId);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            leftMenu.setCompoundDrawables(img, null, null, null);
        } else {
            leftMenu.setCompoundDrawables(null, null, null, null);
        }

    }

    /**
     * 检查存储权限
     *
     * @return true, 已经获取权限;false,没有权限,尝试获取
     */
    protected boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23 && CommonUtil.getTargetSdkVersion(getContext().getApplicationContext()) >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
        if (Build.VERSION.SDK_INT >= 23 && CommonUtil.getTargetSdkVersion(getContext().getApplicationContext()) >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},
                        HyAiConstant.AI_PERMISSIONS_REQUEST_CODE);
                return false;
            }
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},
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
        if (Build.VERSION.SDK_INT >= 23 && CommonUtil.getTargetSdkVersion(getContext().getApplicationContext()) >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                        HyAiConstant.AI_PERMISSIONS_REQUEST_CODE);
                return false;
            }
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                        HyAiConstant.AI_PERMISSIONS_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

}
