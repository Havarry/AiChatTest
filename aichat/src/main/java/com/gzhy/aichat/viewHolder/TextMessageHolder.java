package com.gzhy.aichat.viewHolder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gzhy.aichat.model.ChatMessage;
import com.gzhy.aichat.utils.LogUtils;
import com.gzhy.aichat.utils.ResourceUtils;
import com.gzhy.aichat.utils.ToastUtil;

/**
 * 文本消息
 * Created by jinxl on 2017/3/17.
 */
public class TextMessageHolder extends MessageHolderBase {
    TextView msg; // 聊天的消息内容
    public TextMessageHolder(Context context, View convertView){
        super(context,convertView);
        msg = (TextView) convertView.findViewById(ResourceUtils.getIdByName(context, "id", "sobot_msg"));
    }

    @Override
    public void bindData(final Context context, ChatMessage message) {
        String content = !TextUtils.isEmpty(message.getMsg())?message.getMsg():"消息为空";
        msg.setVisibility(View.VISIBLE);
        msg.setText(content);
    }
}
