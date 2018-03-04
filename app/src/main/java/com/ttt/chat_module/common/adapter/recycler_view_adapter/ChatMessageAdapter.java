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
import com.ttt.chat_module.models.User;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatMessageAdapter extends EndlessLoadingRecyclerViewAdapter {
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 2;

    private User owner;
    private User friend;

    public ChatMessageAdapter(Context context, User owner, User friend) {
        super(context, false);
        this.owner = owner;
        this.friend = friend;
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

            case VIEW_TYPE_FRIEND_MESSAGE:{
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

            case VIEW_TYPE_FRIEND_MESSAGE:{
                bindFriendMessageViewHolder((FriendMessageViewHolder) viewHolder, position);
            }
            break;

            default:{
                bindNormalViewHolder((NormalViewHolder) viewHolder, position);
            }
            break;
        }
    }

    private void bindFriendMessageViewHolder(FriendMessageViewHolder holder, int position) {
        Message message = getItem(position, Message.class);

        GlideApp.with(getContext())
                .load(friend.getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(holder.imgAvatar);

        holder.txtMessage.setText(message.getMessage());
        holder.txtTime.setText(DateTimeUtils.getDateTimeString(message.getCreatedDate()));

        if(message.isExpanded()) {
            holder.expandView(false);
        } else {
            holder.collapseView(false);
        }
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        Message message = getItem(position, Message.class);

        OwnedMessageViewHolder ownedMessageViewHolder = (OwnedMessageViewHolder) holder;

        ownedMessageViewHolder.txtMessage.setText(message.getMessage());
        ownedMessageViewHolder.txtTime.setText(DateTimeUtils.getDateTimeString(message.getCreatedDate()));

        Set<String> seenBy = message.getSeenBy();
        if(seenBy != null && seenBy.contains(friend.getEmail())) {
            ownedMessageViewHolder.txtSeen.setText(getContext().getString(R.string.seen_by) + friend.getFirstName());
            if(position == getItemCount() - 1) {
                ownedMessageViewHolder.txtSeen.setVisibility(View.VISIBLE);
            }
        } else {
            ownedMessageViewHolder.txtSeen.setText("");
            ownedMessageViewHolder.txtSeen.setVisibility(View.GONE);
        }

        if(message.isExpanded()) {
            ownedMessageViewHolder.expandView(false);
        } else {
            ownedMessageViewHolder.collapseView(false);
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
            if(txtSeen.getText().length() != 0) {
                Message message = getItem(getAdapterPosition(), Message.class);
                if(message.isExpanded()) {
                    collapseView(true);
                    message.setExpanded(false);
                } else {
                    expandView(true);
                    message.setExpanded(true);
                }
            }
        }

        void expandView(boolean animate) {
            if(animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtTime.setVisibility(View.VISIBLE);
            txtSeen.setVisibility(View.VISIBLE);
        }

        void collapseView(boolean animate) {
            if(animate) {
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
            if(message.isExpanded()) {
                collapseView(true);
                message.setExpanded(false);
            } else {
                expandView(true);
                message.setExpanded(true);
            }
        }

        void expandView(boolean animate) {
            if(animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtTime.setVisibility(View.VISIBLE);
        }

        void collapseView(boolean animate) {
            if(animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtTime.setVisibility(View.GONE);
        }
    }
}
