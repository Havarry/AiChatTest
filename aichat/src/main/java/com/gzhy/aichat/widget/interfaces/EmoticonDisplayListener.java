package com.gzhy.aichat.widget.interfaces;

import android.view.ViewGroup;

import com.gzhy.aichat.widget.adpater.EmoticonsAdapter;


public interface EmoticonDisplayListener<T> {

    void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, T t, boolean isDelBtn);
}