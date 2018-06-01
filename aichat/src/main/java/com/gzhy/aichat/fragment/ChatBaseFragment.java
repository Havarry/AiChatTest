package com.gzhy.aichat.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.gzhy.aichat.adapter.ChatMsgAdapter;
import com.gzhy.aichat.enums.CustomerState;
import com.gzhy.aichat.model.ChatMessage;
import com.gzhy.aichat.utils.ChatUtils;
import com.gzhy.aichat.utils.CommonUtil;
import com.gzhy.aichat.utils.HyAiConstant;
import com.gzhy.aichat.utils.LogUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hy on 2018-05-26
 */

public abstract class ChatBaseFragment extends AiBaseFragment {

    protected Context mAppContext;
    protected File cameraFile;
    //消息发送状态
    protected static final int SEND_VOICE = 0;
    protected static final int UPDATE_VOICE = 1;
    protected static final int CANCEL_VOICE = 2;
    protected static final int SEND_TEXT = 0;
    protected static final int UPDATE_TEXT = 1;
    protected static final int UPDATE_TEXT_VOICE = 2;

    //当前客户端模式
    protected int current_client_model = HyAiConstant.client_model_robot;
    //客服在线状态
    protected CustomerState customerState = CustomerState.Offline;
//    protected AiInitModeBase initModel;/*初始化成功服务器返回的实体对象*/
    //
    protected String currentUserName;
    private String adminFace = "";

    protected boolean isAboveZero = false;
    protected int remindRobotMessageTimes = 0;//机器人的提醒次数

    //
    //防止询前表单接口重复执行
    private boolean isQueryFroming = false;

    //定时器
    protected boolean customTimeTask = false;
    protected boolean userInfoTimeTask = false;
    protected boolean is_startCustomTimerTask = false;
    protected int noReplyTimeUserInfo = 0; // 用户已经无应答的时间

    private Timer timerUserInfo;
    private TimerTask taskUserInfo;
    /**
     * 客服的定时任务
     */
    protected Timer timerCustom;
    protected TimerTask taskCustom;
    protected int noReplyTimeCustoms = 0;// 客服无应答的时间


    //正在输入监听
    private Timer inputtingListener = null;//用于监听正在输入的计时器
    private boolean isSendInput = false;//防止同时发送正在输入
    private String lastInputStr = "";
    private TimerTask inputTimerTask = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAppContext = getContext().getApplicationContext();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected void finish() {
        if (isActive() && getActivity() != null) {
            getActivity().finish();
        }
    }

    /**
     * fragment是否有效
     * @return
     */
    protected boolean isActive(){return isAdded();}

    // ##################### 更新界面的ui ###############################
    /**
     * handler 消息实体message 更新ui界面
     *
     * @param messageAdapter
     * @param msg
     */
    protected void updateUiMessage(ChatMsgAdapter messageAdapter, Message msg) {
        ChatMessage myMessage = (ChatMessage) msg.obj;
        updateUiMessage(messageAdapter, myMessage);
    }

    /**
     * 通过消息实体 ChatMessage 进行封装
     * @param messageAdapter
     * @param chatMessage
     */
    protected void updateUiMessage(ChatMsgAdapter messageAdapter, ChatMessage chatMessage) {
        messageAdapter.addData(chatMessage);
        messageAdapter.notifyDataSetChanged();
    }

    /**
     * 文本通知
     *
     * @param msgContent
     * @param handler
     */
    protected void sendTextMessageToHandler(String msgContent, Handler handler, String type) {
        ChatMessage myMessage = new ChatMessage();
        myMessage.setMsg(msgContent);
        myMessage.setSenderType(type);
        Message handMyMessage = handler.obtainMessage();
        handMyMessage.what = HyAiConstant.hander_my_senderMessage;
        handMyMessage.obj = myMessage;
        handler.sendMessage(handMyMessage);
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

    /**
     * 通过照相上传图片
     */
    public void selectPicFromCamera() {
        if (!CommonUtil.isExitsSdcard()) {
            Toast.makeText(getContext().getApplicationContext(), getResString("sobot_sdcard_does_not_exist"),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkStorageAndCameraPermission()) {
            return;
        }
        cameraFile = ChatUtils.openCamera(getActivity(),ChatBaseFragment.this);
    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        if (!checkStoragePermission()) {
            return;
        }
        ChatUtils.openSelectPic(getActivity(),ChatBaseFragment.this);
    }
}
