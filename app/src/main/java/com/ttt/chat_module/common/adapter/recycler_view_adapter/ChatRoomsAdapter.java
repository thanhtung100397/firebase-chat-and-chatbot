package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.recycler_view_adapter.EndlessLoadingRecyclerViewAdapter;
import com.ttt.chat_module.common.utils.Utils;
import com.ttt.chat_module.models.ChatRoom;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomsAdapter extends EndlessLoadingRecyclerViewAdapter {
    private Map<String, Integer> roomPositionMap;
    private Map<String, UserInfoChatRooms> userInfoChatRoomsMap;

    public ChatRoomsAdapter(Context context) {
        super(context, false);
        this.roomPositionMap = new HashMap<>();
        this.userInfoChatRoomsMap = new HashMap<>();
    }

    public void addChatRooms(Map<String, UserInfoChatRooms> userInfoChatRoomsMap, List<ChatRoom> chatRooms) {
        int startInsertedPosition = getItemCount();
        int endInsertedPosition = startInsertedPosition + chatRooms.size();
        for (int i = 0; i < chatRooms.size(); i++) {
            ChatRoom chatRoom = chatRooms.get(i);
            roomPositionMap.put(chatRoom.getRoomID(), startInsertedPosition + i);
            addModel(chatRoom, VIEW_TYPE_NORMAL, false, false);
        }
        this.userInfoChatRoomsMap.putAll(userInfoChatRoomsMap);
        notifyItemRangeInserted(startInsertedPosition, endInsertedPosition);
    }

    public void refreshChatRooms(Map<String, UserInfoChatRooms> userInfoChatRoomsMap, List<ChatRoom> chatRooms) {
        clear();
        this.roomPositionMap.clear();
        this.userInfoChatRoomsMap.clear();
        addChatRooms(userInfoChatRoomsMap, chatRooms);
    }

    public void updateUserInfo(UserInfo userInfo) {
        UserInfoChatRooms userInfoChatRooms = userInfoChatRoomsMap.get(userInfo.getId());
        if (userInfoChatRooms != null) {
            boolean needRefreshView = needRefreshView(userInfo, userInfoChatRooms.getUserInfo());
            userInfoChatRooms.getUserInfo().update(userInfo);
            if(needRefreshView) {
                for (String chatRoomID : userInfoChatRooms.getRoomIDs()) {
                    Integer roomPosition = roomPositionMap.get(chatRoomID);
                    if (roomPosition != null) {
                        notifyItemChanged(roomPosition);
                    }
                }
            }
        }
    }

    private boolean needRefreshView(UserInfo newUserInfo, UserInfo oldUserInfo) {
        return (newUserInfo.getIsOnline() != oldUserInfo.getIsOnline()) ||
                !newUserInfo.getFirstName().equals(oldUserInfo.getFirstName()) ||
                !newUserInfo.getLastName().equals(oldUserInfo.getLastName()) ||
                !newUserInfo.getAvatarUrl().equals(oldUserInfo.getAvatarUrl());
    }

    public void updateChatRoom(ChatRoomInfo chatRoomInfo) {
        Integer position = roomPositionMap.get(chatRoomInfo.getId());
        if (position == null) {
            return;
        }
        ChatRoom chatRoom = getItem(position, ChatRoom.class);
        chatRoom.update(getContext(), chatRoomInfo, chatRoomInfo.getLastMessage());
        notifyItemChanged(position);
    }

    @Override
    protected RecyclerView.ViewHolder initLoadingViewHolder(ViewGroup parent) {
        View itemView = getInflater().inflate(R.layout.item_loading, parent, false);
        return new LoadingViewHolder(itemView);
    }

    @Override
    protected void bindLoadingViewHolder(LoadingViewHolder loadingViewHolder, int position) {

    }

    @Override
    protected RecyclerView.ViewHolder initNormalViewHolder(ViewGroup parent) {
        View itemView = getInflater().inflate(R.layout.item_chat_room, parent, false);
        return new ItemChatRoomViewHolder(itemView);
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        ChatRoom chatRoom = getItem(position, ChatRoom.class);

        UserInfo userInfo = chatRoom.getFriendsInfo().entrySet().iterator().next().getValue();

        ItemChatRoomViewHolder chatRoomViewHolder = (ItemChatRoomViewHolder) holder;
        GlideApp.with(getContext())
                .load(userInfo.getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(chatRoomViewHolder.imgAvatar);
        chatRoomViewHolder.imgOnlineState.setVisibility(chatRoom.isOnline() ? View.VISIBLE : View.GONE);
        chatRoomViewHolder.txtName.setText(userInfo.getLastName() + " " + userInfo.getFirstName());
        chatRoomViewHolder.txtMessage.setText(chatRoom.getLastMessage());

        if (!chatRoom.isOwnedMessage() && !chatRoom.isSeen()) {
            setHtmlText(chatRoomViewHolder.txtMessage, Utils.toBoth(chatRoom.getLastMessage()));
        } else {
            chatRoomViewHolder.txtMessage.setText(chatRoom.getLastMessage());
        }
    }

    private void setHtmlText(TextView textView, String htmlText) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(htmlText));
        } else {
            textView.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT));
        }
    }

    class ItemChatRoomViewHolder extends NormalViewHolder {
        @BindView(R.id.img_avatar)
        CircleImageView imgAvatar;
        @BindView(R.id.img_online)
        ImageView imgOnlineState;
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_message)
        TextView txtMessage;

        ItemChatRoomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
