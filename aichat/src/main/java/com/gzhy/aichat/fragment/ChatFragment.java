package com.gzhy.aichat.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzhy.aichat.adapter.ChatMsgAdapter;
import com.gzhy.aichat.listener.NoDoubleClickListener;
import com.gzhy.aichat.model.ChatMessage;
import com.gzhy.aichat.utils.HyAiConstant;
import com.gzhy.aichat.utils.LogUtils;
import com.gzhy.aichat.widget.chatLinearLayout.KPSwitchConflictUtil;
import com.gzhy.aichat.widget.chatLinearLayout.KPSwitchPanelLinearLayout;
import com.gzhy.aichat.widget.chatLinearLayout.KeyboardUtil;
import com.gzhy.aichat.widget.DropdownListView;
import com.gzhy.aichat.widget.emoji.Emojicon;
import com.gzhy.aichat.widget.emoji.InputHelper;
import com.gzhy.aichat.widget.view.BaseChattingPanelView;
import com.gzhy.aichat.widget.view.ChattingPanelEmoticonView;
import com.gzhy.aichat.widget.view.ChattingPanelUploadView;
import com.gzhy.aichat.widget.view.CustomeChattingPanel;
import com.gzhy.aichat.widget.view.CustomeViewFactory;

import java.util.ArrayList;

/**
 * Created by hy on 2018-05-26
 */

public class ChatFragment extends ChatBaseFragment implements View.OnClickListener
        , DropdownListView.OnRefreshListenerHeader, ChatMsgAdapter.MsgCallBack
        ,ChattingPanelEmoticonView.SobotEmoticonClickListener,ChattingPanelUploadView.SobotPlusClickListener{

    private ArrayList<ChatMessage> msgList;
    private ChatMsgAdapter chatMsgAdapter;

    //键盘相关
    public int currentPanelId = 0;//切换聊天面板时 当前点击的按钮id 为了能切换到对应的view上
    private int mBottomViewtype = 0;//记录键盘的状态

    //---------
    //键盘监听
    private ViewTreeObserver.OnGlobalLayoutListener mKPSwitchListener;

    //---------------UI控件 START---------------

    //  ai_chat_fragment 布局
    public RelativeLayout rl_net_status_remide;
    public TextView tv_net_not_connect;

    //  chat_layout_titlebar 布局
    public RelativeLayout rl_layout_titlebar;
    public TextView tv_chat_title;
    public LinearLayout ll_container_conn_status;
    public ProgressBar pb_conn_loading;
    public TextView tv_title_conn_status;
    public TextView tv_left_back, tv_right_back;

    //  ai_chat_main 布局
    public RelativeLayout rl_chat_main;
    public LinearLayout chat_layout_bottom;
    public DropdownListView lv_message;  /* 带下拉的ListView */
    public TextView tv_notReadInfo;

    //  chat_layout_bottom 布局
    public TextView send_voice_robot_hint;
    public LinearLayout ll_bottom;
    public ImageButton btn_set_mode_rengong;  // 转人工button
    public RelativeLayout rl_edittext_layout;
    public EditText et_sendmessage;
    public LinearLayout ll_btn_emoticon_view;
    public ImageButton im_btn_emoticon_view;
    public Button btn_send; // 发送消息按钮
    public Button btn_upload_view;  //  加号按钮
    private KPSwitchPanelLinearLayout sobot_panel_root;

    //---------------UI控件 END---------------

    public static ChatFragment newInstance(Bundle info) {
        Bundle arguments = new Bundle();
        arguments.putBundle("informationBundle", info);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i("onCreate");
        if (getArguments() != null) {
            Bundle informationBundle = getArguments().getBundle("informationBundle");
            if (informationBundle != null) {
//                info = (Information) informationBundle.getSerializable("info");
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(getResLayoutId("ai_chat_fragment"), container, false);
        initView(root);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView(View rootView) {
        if (rootView == null) {
            return;
        }

        //  ai_chat_fragment 布局
        rl_net_status_remide = rootView.findViewById(getResId("rl_net_status_remide"));
        tv_net_not_connect = rootView.findViewById(getResId("tv_net_not_connect"));

        //  chat_layout_titlebar 布局
        rl_layout_titlebar = rootView.findViewById(getResId("rl_layout_titlebar"));
        tv_chat_title = rootView.findViewById(getResId("tv_chat_title"));
        ll_container_conn_status = rootView.findViewById(getResId("ll_container_conn_status"));
        pb_conn_loading = rootView.findViewById(getResId("pb_conn_loading"));
        tv_title_conn_status = rootView.findViewById(getResId("tv_title_conn_status"));
        tv_left_back = rootView.findViewById(getResId("tv_left_back"));
        tv_right_back = rootView.findViewById(getResId("tv_right_back"));
//        rl_layout_titlebar.setVisibility(View.GONE);

        //  ai_chat_main 布局
        rl_chat_main = rootView.findViewById(getResId("rl_chat_main"));
        chat_layout_bottom = rootView.findViewById(getResId("chat_layout_bottom"));
        lv_message = rootView.findViewById(getResId("lv_message"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            lv_message.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        tv_notReadInfo = rootView.findViewById(getResId("tv_notReadInfo"));

        //  chat_layout_bottom 布局
        send_voice_robot_hint = rootView.findViewById(getResId("send_voice_robot_hint"));
        ll_bottom = rootView.findViewById(getResId("ll_bottom"));
        btn_set_mode_rengong = rootView.findViewById(getResId("btn_set_mode_rengong"));
        rl_edittext_layout = rootView.findViewById(getResId("rl_edittext_layout"));
        et_sendmessage = rootView.findViewById(getResId("et_sendmessage"));
        ll_btn_emoticon_view = rootView.findViewById(getResId("ll_btn_emoticon_view"));
        im_btn_emoticon_view = rootView.findViewById(getResId("im_btn_emoticon_view"));
        btn_send = rootView.findViewById(getResId("btn_send"));
        btn_upload_view = rootView.findViewById(getResId("btn_upload_view"));
        sobot_panel_root = rootView.findViewById(getResId("sobot_panel_root"));
    }

    /* 处理消息 */
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {

        @SuppressWarnings("unchecked")
        public void handleMessage(final android.os.Message msg) {
            if (!isActive()) {
                return;
            }
            switch (msg.what) {
                case HyAiConstant.hander_my_senderMessage:/* 我的文本消息 */
                    updateUiMessage(chatMsgAdapter, msg);
                    lv_message.setSelection(chatMsgAdapter.getCount());
                    break;
                default:
                    break;
            }
        }
    };

    protected void initData() {
        initMessage();
        setToolBar();
        initListener();
        setupListView();
        gotoLastItem();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    // 获取标题内容
    public String getActivityTitle() {
        return tv_chat_title.getText().toString();
    }

    //    设置ToolBar
    private void setToolBar() {
        if (getView() == null) {
            return;
        }
        View rootView = getView();
        View toolBar = rootView.findViewById(getResId("rl_layout_titlebar"));
        View tv_left_back = rootView.findViewById(getResId("tv_left_back"));
        if (toolBar != null) {
            if (tv_left_back != null) {
                //找到 Toolbar 的返回按钮,并且设置点击事件,点击关闭这个 Activity
                //设置导航栏返回按钮
                showLeftMenu(tv_left_back, getResDrawableId("sobot_btn_back_selector"), getResString("sobot_back"));
                tv_left_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLeftMenuClick(v);
                    }
                });
            }
        }
    }

    //     初始化聊天页面
    private void setupListView() {
        chatMsgAdapter = new ChatMsgAdapter(getActivity(), msgList, this);
        lv_message.setAdapter(chatMsgAdapter);
        lv_message.setPullRefreshEnable(true);// 设置下拉刷新列表
        lv_message.setOnRefreshListenerHead(this);
    }

    //    监听聊天的面板
    private void initListener() {

        mKPSwitchListener = KeyboardUtil.attach(getActivity(), sobot_panel_root,
                new KeyboardUtil.OnKeyboardShowingListener() {
                    @Override
                    public void onKeyboardShowing(boolean isShowing) {
                        resetEmoticonBtn();
                        if (isShowing) {
                            lv_message.setSelection(chatMsgAdapter.getCount());
                        }
                    }
                });
        btn_send.setOnClickListener(this);  //  发送按钮
        btn_upload_view.setOnClickListener(this);   //  加号按钮
        //      转人工
        btn_set_mode_rengong.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
              doClickTransferBtn();
            }
        });

        et_sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                resetBtnUploadAndSend();
            }
        });

        et_sendmessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean isFocused) {
                if (isFocused) {
                    int length = et_sendmessage.getText().toString().trim().length();
                    if (length != 0) {
                        btn_send.setVisibility(View.VISIBLE);
                        btn_upload_view.setVisibility(View.GONE);
                    }
                    //根据是否有焦点切换实际的背景
                    rl_edittext_layout.setBackgroundResource(getResDrawableId("sobot_chatting_bottom_bg_focus"));
                } else {
                    rl_edittext_layout.setBackgroundResource(getResDrawableId("sobot_chatting_bottom_bg_blur"));
                }
            }
        });

        et_sendmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                resetBtnUploadAndSend();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        lv_message.setDropdownListScrollListener(new DropdownListView.DropdownListScrollListener() {
            @Override
            public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2, int arg3) {
                if(tv_notReadInfo.getVisibility() == View.VISIBLE && msgList.size() > 0){
                    if (msgList.get(firstVisiableItem) != null){
                        tv_notReadInfo.setVisibility(View.GONE);
                    }
                }
            }
        });

        lv_message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    hidePanelAndKeyboard(sobot_panel_root);
                }
                return false;
            }
        });
    }

//    转人工
    @Override
    public void doClickTransferBtn() {

    }

    @Override
    public void hidePanelAndKeyboard() {
        hidePanelAndKeyboard(sobot_panel_root);
    }

    // 设置标题内容
    public void setTitle(CharSequence title) {
        tv_chat_title.setText(title);
    }

    /**
     * 导航栏左边点击事件
     *
     * @param view
     */
    protected void onLeftMenuClick(View view) {
        onBackPress();
    }

    private void onBackPress() {
        finish();
    }

    @Override
    public void onClick(View view) {

        if (view == btn_send) {// 发送消息按钮
            final String message_result = et_sendmessage.getText().toString().trim();
            if (!TextUtils.isEmpty(message_result)) {
                //转人工接口没跑完的时候  屏蔽发送，防止统计出现混乱
                resetEmoticonBtn();
                try {
                    et_sendmessage.setText("");
                    sendMsg(message_result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (view == btn_upload_view) {// 加号按钮
            switchPanelAndKeyboard(sobot_panel_root, btn_upload_view, et_sendmessage);
            doEmoticonBtn2Blur();
            gotoLastItem();
        }

        if (view == im_btn_emoticon_view) {// 表情按钮
            switchPanelAndKeyboard(sobot_panel_root, im_btn_emoticon_view, et_sendmessage);
            switchEmoticonBtn();
            gotoLastItem();
        }
    }

    private void sendMsg(String content) {
        sendTextMessageToHandler(content, handler, "1");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        sendTextMessageToHandler("请问有什么可以帮到您", handler, "0");
    }

    @Override
    public void onRefresh() {
        String msgId = System.currentTimeMillis() + "";

    }

    /**
     * 根据输入框里的内容切换显示  发送按钮还是加号（更多方法）
     */
    private void resetBtnUploadAndSend() {
        if (et_sendmessage.getText().toString().length() > 0) {
            btn_upload_view.setVisibility(View.GONE);
            btn_send.setVisibility(View.VISIBLE);
        } else {
            btn_send.setVisibility(View.GONE);
            btn_upload_view.setVisibility(View.VISIBLE);
            btn_upload_view.setEnabled(true);
            btn_upload_view.setClickable(true);
            if (Build.VERSION.SDK_INT >= 11) {
                btn_upload_view.setAlpha(1f);
            }
        }
    }

    private void gotoLastItem() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                lv_message.setSelection(chatMsgAdapter.getCount());
            }
        });
    }

    /**
     * 重置表情按钮的焦点键盘
     */
    public void resetEmoticonBtn() {
        String panelViewTag = getPanelViewTag(sobot_panel_root);
        String instanceTag = CustomeViewFactory.getInstanceTag(mAppContext, im_btn_emoticon_view.getId());
        if (sobot_panel_root.getVisibility() == View.VISIBLE && instanceTag.equals(panelViewTag)) {
            doEmoticonBtn2Focus();
        } else {
            doEmoticonBtn2Blur();
        }
    }

    /**
     * 获取当前显示的聊天面板的tag
     *
     * @param panelLayout
     */
    private String getPanelViewTag(final View panelLayout) {
        String str = "";
        if (panelLayout instanceof KPSwitchPanelLinearLayout) {
            KPSwitchPanelLinearLayout tmpView = (KPSwitchPanelLinearLayout) panelLayout;
            View childView = tmpView.getChildAt(0);
            if (childView != null && childView instanceof CustomeChattingPanel) {
                CustomeChattingPanel customeChattingPanel = (CustomeChattingPanel) childView;
                str = customeChattingPanel.getPanelViewTag();
            }
        }
        return str;
    }

    //切换键盘和面板的方法
    public void switchPanelAndKeyboard(final View panelLayout,final View switchPanelKeyboardBtn, final View focusView) {
        if(currentPanelId == 0 || currentPanelId == switchPanelKeyboardBtn.getId()){
            //没选中的时候或者  点击是自身的时候正常切换面板和键盘
            boolean switchToPanel = panelLayout.getVisibility() != View.VISIBLE;
            LogUtils.e("panelLayout是否显示：" + switchToPanel);
            if (!switchToPanel) {
                KPSwitchConflictUtil.showKeyboard(panelLayout, focusView);
            } else {
                KPSwitchConflictUtil.showPanel(panelLayout);
                setPanelView(panelLayout, switchPanelKeyboardBtn.getId());
            }
        } else {
            //之前选过  但是现在点击的不是自己的时候  显示自己的面板
            KPSwitchConflictUtil.showPanel(panelLayout);
            setPanelView(panelLayout, switchPanelKeyboardBtn.getId());
        }
        currentPanelId = switchPanelKeyboardBtn.getId();
    }

    /**
     * 设置聊天面板的view
     * @param panelLayout
     * @param btnId
     */
    private void setPanelView(final View panelLayout,int btnId){
        if(panelLayout instanceof KPSwitchPanelLinearLayout){
            KPSwitchPanelLinearLayout tmpView = (KPSwitchPanelLinearLayout) panelLayout;
            View childView = tmpView.getChildAt(0);
            if(childView != null && childView instanceof CustomeChattingPanel){
                CustomeChattingPanel customeChattingPanel = (CustomeChattingPanel) childView;
                Bundle bundle = new Bundle();
                bundle.putInt("current_client_model",current_client_model);
                customeChattingPanel.setupView(btnId,bundle, ChatFragment.this);
            }
        }
    }

    /**
     * 隐藏键盘和面板
     * @param layout
     */
    public void hidePanelAndKeyboard(KPSwitchPanelLinearLayout layout){
//        et_sendmessage.dismissPop();
        KPSwitchConflictUtil.hidePanelAndKeyboard(layout);
        doEmoticonBtn2Blur();
        currentPanelId = 0;
    }

    /**
     * 使表情按钮失去焦点
     */
    public void doEmoticonBtn2Blur() {
        im_btn_emoticon_view.setSelected(false);
    }

    /**
     * 使表情按钮获取焦点
     */
    public void doEmoticonBtn2Focus() {
        im_btn_emoticon_view.setSelected(true);
    }

    /**
     * 切换表情按钮焦点
     */
    public void switchEmoticonBtn(){
        boolean flag = im_btn_emoticon_view.isSelected();
        if(flag){
            doEmoticonBtn2Blur();
        }else{
            doEmoticonBtn2Focus();
        }
    }

    private void initMessage() {
        msgList = new ArrayList<>();
        ChatMessage message1 = new ChatMessage("宋慧乔是我女神", "0");
        msgList.add(message1);
        ChatMessage message2 = new ChatMessage("宋钟基是我男神", "1");
        msgList.add(message2);
        ChatMessage message3 = new ChatMessage("呵呵", "0");
        msgList.add(message3);
        ChatMessage message4 = new ChatMessage("呵呵", "1");
        msgList.add(message4);
        ChatMessage message5 = new ChatMessage("我错了", "0");
        msgList.add(message5);
        ChatMessage message6 = new ChatMessage("66666", "0");
        msgList.add(message6);
        ChatMessage message7 = new ChatMessage("77777", "1");
        msgList.add(message7);
        ChatMessage message8 = new ChatMessage("dddd", "0");
        msgList.add(message8);
        ChatMessage message9 = new ChatMessage("fggggggg", "1");
        msgList.add(message9);
    }

    @Override
    public void backspace() {
        InputHelper.backspace(et_sendmessage);
    }

    @Override
    public void inputEmoticon(Emojicon item) {
        InputHelper.input2OSC(et_sendmessage,item);
    }

    @Override
    public void btnPicture() {
        InputHelper.backspace(et_sendmessage);
    }

    @Override
    public void btnCameraPicture() {
        hidePanelAndKeyboard(sobot_panel_root);
        selectPicFromCamera(); // 拍照 上传
        lv_message.setSelection(chatMsgAdapter.getCount());
    }

    @Override
    public void btnSatisfaction() {
        lv_message.setSelection(chatMsgAdapter.getCount());
        //满意度逻辑 点击时首先判断是否评价过 评价过 弹您已完成提示 未评价 判断是否达到可评价标准
        hidePanelAndKeyboard(sobot_panel_root);
//        submitEvaluation(true,5);
    }

    @Override
    public void startToPostMsgActivty(boolean flag) {

    }
}
