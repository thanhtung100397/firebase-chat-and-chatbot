package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import android.content.Context;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.recycler_view_adapter.EndlessLoadingRecyclerViewAdapter;
import com.ttt.chat_module.models.Message;
import com.ttt.chat_module.models.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatMessageAdapter extends EndlessLoadingRecyclerViewAdapter {
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 2;

    private UserInfo ownerInfo;
    private Map<String, UserInfo> mapFriendsInfo;

    public ChatMessageAdapter(Context context, UserInfo ownerInfo, List<UserInfo> friendsInfo) {
        super(context, false);
        this.ownerInfo = ownerInfo;
        this.mapFriendsInfo = new HashMap<>();
        for (UserInfo userInfo : friendsInfo) {
            mapFriendsInfo.put(userInfo.getEmail(), userInfo);
        }
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
    protected RecyclerView.ViewHolder solvedOnCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder result;
        switch (viewType) {
            case VIEW_TYPE_LOADING: {
                result = initLoadingViewHolder(parent);
            }
            break;

            case VIEW_TYPE_FRIEND_MESSAGE: {
                result = initFriendMessageViewHolder(parent);
            }
            break;

            default: {
                result = initNormalViewHolder(parent);
            }
            break;
        }
        return result;
    }

    private RecyclerView.ViewHolder initFriendMessageViewHolder(ViewGroup parent) {
        View itemView = getInflater().inflate(R.layout.item_friend_message, parent, false);
        return new FriendMessageViewHolder(itemView);
    }

    @Override
    protected RecyclerView.ViewHolder initNormalViewHolder(ViewGroup parent) {
        View itemView = getInflater().inflate(R.layout.item_owned_message, parent, false);
        return new OwnedMessageViewHolder(itemView);
    }

    @Override
    protected void solvedOnBindViewHolder(RecyclerView.ViewHolder viewHolder, int viewType, int position) {
        switch (viewType) {
            case VIEW_TYPE_LOADING: {
                bindLoadingViewHolder((LoadingViewHolder) viewHolder, position);
            }
            break;

            case VIEW_TYPE_FRIEND_MESSAGE: {
                bindFriendMessageViewHolder((FriendMessageViewHolder) viewHolder, position);
            }
            break;

            default: {
                bindNormalViewHolder((NormalViewHolder) viewHolder, position);
            }
            break;
        }
    }

    private void bindFriendMessageViewHolder(FriendMessageViewHolder holder, int position) {
        Message message = getItem(position, Message.class);

        UserInfo userInfo = mapFriendsInfo.get(message.getOwnerEmail());

        GlideApp.with(getContext())
                .load(userInfo.getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(holder.imgAvatar);

        holder.txtMessage.setText(message.getMessage());
        holder.txtTime.setText(DateTimeUtils.getDateTimeString(message.getCreatedDate()));

        if (message.isExpanded()) {
            holder.expandView(false);
        } else {
            holder.collapseView(false);
        }
    }

    private String generateSeenState(Set<String> seenBy) {
        if (seenBy != null) {
            StringBuilder result = new StringBuilder(getContext().getString(R.string.seen_by))
                    .append(" ");
            int currentIndex = 0;
            int lastIndex = seenBy.size() - 1;
            for (String friendID : seenBy) {
                UserInfo friendInfo = mapFriendsInfo.get(friendID);
                if(friendInfo != null) {
                    result.append(friendInfo.getFirstName());
                }
                if(currentIndex < lastIndex) {
                    result.append(", ");
                }
                currentIndex++;
            }
            return result.toString();
        }
        return getContext().getString(R.string.sent);
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        Message message = getItem(position, Message.class);

        OwnedMessageViewHolder ownedMessageViewHolder = (OwnedMessageViewHolder) holder;

        ownedMessageViewHolder.txtMessage.setText(message.getMessage());
        ownedMessageViewHolder.txtTime.setText(DateTimeUtils.getDateTimeString(message.getCreatedDate()));

        String seenBy = generateSeenState(message.getSeenBy());
        ownedMessageViewHolder.txtSeen.setText(seenBy);

        if (message.isExpanded()) {
            ownedMessageViewHolder.expandView(false);
        } else {
            ownedMessageViewHolder.collapseView(false);
        }
    }

    public void addMessage(Message message) {
        if (message.getOwnerEmail().equals(ownerInfo.getEmail())) {
            addModel(message, VIEW_TYPE_NORMAL, true);
        } else {
            addModel(message, VIEW_TYPE_FRIEND_MESSAGE, true);
        }
    }

    class OwnedMessageViewHolder extends NormalViewHolder implements View.OnClickListener {
        @BindView(R.id.txt_message)
        TextView txtMessage;
        @BindView(R.id.txt_seen)
        TextView txtSeen;
        @BindView(R.id.txt_time)
        TextView txtTime;

        OwnedMessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (txtSeen.getText().length() != 0) {
                Message message = getItem(getAdapterPosition(), Message.class);
                if (message.isExpanded()) {
                    collapseView(true);
                    message.setExpanded(false);
                } else {
                    expandView(true);
                    message.setExpanded(true);
                }
            }
        }

        void expandView(boolean animate) {
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtTime.setVisibility(View.VISIBLE);
            txtSeen.setVisibility(View.VISIBLE);
        }

        void collapseView(boolean animate) {
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtTime.setVisibility(View.GONE);
            txtSeen.setVisibility(View.GONE);
        }
    }

    class FriendMessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_avatar)
        ImageView imgAvatar;
        @BindView(R.id.txt_message)
        TextView txtMessage;
        @BindView(R.id.txt_time)
        TextView txtTime;

        FriendMessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Message message = getItem(getAdapterPosition(), Message.class);
            if (message.isExpanded()) {
                collapseView(true);
                message.setExpanded(false);
            } else {
                expandView(true);
                message.setExpanded(true);
            }
        }

        void expandView(boolean animate) {
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtTime.setVisibility(View.VISIBLE);
        }

        void collapseView(boolean animate) {
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtTime.setVisibility(View.GONE);
        }
    }
}
