package com.gzhy.aichat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzhy.aichat.model.ChatMessage;
import com.gzhy.aichat.utils.ResourceUtils;
import com.gzhy.aichat.utils.SharedPreferencesUtil;
import com.gzhy.aichat.viewHolder.MessageHolderBase;
import com.gzhy.aichat.viewHolder.TextMessageHolder;

import java.util.List;

/**
 * Created by hy on 2018-05-29
 */

public class ChatMsgAdapter extends ChatBaseAdapter<ChatMessage> {

    private static final String[] layoutRes = {
            "sobot_chat_msg_item_txt_l",//文本消息左边的布局文件
            "sobot_chat_msg_item_txt_r",//文本消息右边的布局文件
            "sobot_chat_msg_item_imgt_l",//图片消息左边的布局文件
            "sobot_chat_msg_item_imgt_r",//图片消息右边的布局文件
    };

    /**
     * 非法消息类型
     */
    private static final int MSG_TYPE_ILLEGAL = 0;
    /**
     * 收到的文本消息
     */
    public static final int MSG_TYPE_TXT_L = 0;
    /**
     * 发送的文本消息
     */
    public static final int MSG_TYPE_TXT_R = 1;
    /**
     * 收到图片消息
     */
    public static final int MSG_TYPE_IMG_L = 4;
    /**
     * 发送图片消息
     */
    public static final int MSG_TYPE_IMG_R = 5;

    private String senderface;
    private String sendername;

    private MsgCallBack mMsgCallBack;

    public ChatMsgAdapter(Context context, List<ChatMessage> list, MsgCallBack msgCallBack) {
        super(context, list);
        mMsgCallBack = msgCallBack;
        senderface = SharedPreferencesUtil.getStringData(context, "sobot_current_sender_face", "");
        sendername = SharedPreferencesUtil.getStringData(context, "sobot_current_sender_name", "");

    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ChatMessage message = list.get(position);
        if (message != null) {
            int itemType = getItemViewType(position);
            convertView = initView(convertView, itemType, position, message);
            MessageHolderBase holder = (MessageHolderBase) convertView.getTag();
            holder.setMsgCallBack(mMsgCallBack);
            holder.initNameAndFace(itemType, context, message, senderface, sendername);
            holder.bindData(context,message);
        }
        return convertView;
    }

    private View initView(View convertView, int itemType, int position, final ChatMessage message) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(ResourceUtils.getIdByName(context, "layout", layoutRes[itemType]), null);
            MessageHolderBase holder;
            switch (itemType) {
                case MSG_TYPE_TXT_L:
                case MSG_TYPE_TXT_R: {
                    holder = new TextMessageHolder(context, convertView);
                    if (itemType == MSG_TYPE_TXT_L) {
                        holder.setRight(false);
                    } else {
                        holder.setRight(true);
                    }
                    break;
                }
                default: {
                    holder = new TextMessageHolder(context, convertView);
                    break;
                }
            }
            convertView.setTag(holder);
        }
        return convertView;
    }

    public void addData(ChatMessage message) {
        list.add(message);
    }

    @Override
    public int getViewTypeCount() {
        if (layoutRes.length > 0) {
            return layoutRes.length;
        }
        return super.getViewTypeCount();
    }

    @Override
    public ChatMessage getItem(int position) {
        if (position < 0 || position >= list.size()) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        try {
            ChatMessage message = getItem(position);
            if (message == null) {
                return MSG_TYPE_ILLEGAL;
            }
            return Integer.parseInt(message.getSenderType());

        } catch (Exception e) {
            e.printStackTrace();
            return MSG_TYPE_ILLEGAL;
        }
    }

    public interface MsgCallBack{
        void doClickTransferBtn();
        void hidePanelAndKeyboard();
    }
}
