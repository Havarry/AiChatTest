package com.gzhy.aichat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.gzhy.aichat.fragment.ChatFragment;

/**
 * Created by hy on 2018-05-26
 */

public class ChatActivity extends BaseActivity {

    Bundle informationBundle;
    ChatFragment chatFragment;

    @Override
    protected int getContentViewResId() {
        return getResLayoutId("ai_chat_activity");
    }

    protected void initBundleData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            informationBundle = getIntent().getBundleExtra("informationBundle");
        } else {
            informationBundle = savedInstanceState.getBundle("informationBundle");
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        //销毁前缓存数据
        outState.putBundle("informationBundle", informationBundle);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        chatFragment = (ChatFragment) getSupportFragmentManager()
                .findFragmentById(getResId("fl_contentFrame"));
        if (chatFragment == null) {
            chatFragment = ChatFragment.newInstance(informationBundle);

            addFragmentToActivity(getSupportFragmentManager(),
                    chatFragment, getResId("fl_contentFrame"));
        }
    }

    public static void addFragmentToActivity(FragmentManager fragmentManager,
                                             Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    @Override
    protected void initData() {

    }

}
