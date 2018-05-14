package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.recycler_view_adapter.EndlessLoadingRecyclerViewAdapter;
import com.ttt.chat_module.models.ChatRoom;
import com.ttt.chat_module.models.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomsAdapter extends EndlessLoadingRecyclerViewAdapter {
    private Map<String, Integer> roomPositionMap;
    private Map<String, UserOnlineStateAndChatRooms> userOnlineStateAndRoomsMap;

    public ChatRoomsAdapter(Context context) {
        super(context, false);
        this.roomPositionMap = new HashMap<>();
        this.userOnlineStateAndRoomsMap = new HashMap<>();
    }

    public void addChatRooms(Map<String, Integer> roomPositionMap, List<ChatRoom> chatRooms) {
        int startInsertedPosition = getItemCount();
        int endInsertedPosition = startInsertedPosition + chatRooms.size();
        for (int i = 0; i < chatRooms.size(); i++) {
            ChatRoom chatRoom = chatRooms.get(i);
            for (Map.Entry<String, UserInfo> userInfoEntry : chatRoom.getFriendsInfo().entrySet()) {
                UserOnlineStateAndChatRooms userOnlineStateAndChatRooms = userOnlineStateAndRoomsMap.get(userInfoEntry.getKey());
                if (userOnlineStateAndChatRooms == null) {
                    return;
                }
                userOnlineStateAndChatRooms.addChatRoom(chatRoom);
            }
            addModel(chatRooms.get(i), VIEW_TYPE_NORMAL, false, false);
        }
        this.roomPositionMap.putAll(roomPositionMap);
        notifyItemRangeInserted(startInsertedPosition, endInsertedPosition);
    }

    public void refreshChatRooms(Map<String, Integer> roomPositionMap, List<ChatRoom> chatRooms) {
        clear();
        this.roomPositionMap.clear();
        addChatRooms(roomPositionMap, chatRooms);
    }

    public void updateOnlineState(String userID, boolean isOnline) {
        UserOnlineStateAndChatRooms userOnlineStateAndChatRooms = userOnlineStateAndRoomsMap.get(userID);
        if (userOnlineStateAndChatRooms == null) {
            userOnlineStateAndChatRooms = new UserOnlineStateAndChatRooms(userID);
            userOnlineStateAndRoomsMap.put(userID, new UserOnlineStateAndChatRooms(userID));
        }
        userOnlineStateAndChatRooms.setOnline(isOnline);
    }

    public void updateLastMessage(String roomID, Map<String, Object> lastMessageMap) {
        Integer position = roomPositionMap.get(roomID);
        if (position == null) {
            return;
        }
        ChatRoom chatRoom = getItem(position, ChatRoom.class);
        chatRoom.setLastMessage(getContext(), lastMessageMap);
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

        public ItemChatRoomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
