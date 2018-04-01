package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import android.content.Context;
import android.support.transition.TransitionManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.recycler_view_adapter.EndlessLoadingRecyclerViewAdapter;
import com.ttt.chat_module.models.ImageItem;
import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.models.message_models.ImageMessage;
import com.ttt.chat_module.models.message_models.TextMessage;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.wrapper_model.FriendMessageWrapper;
import com.ttt.chat_module.models.wrapper_model.OwnerMessageWrapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatMessageAdapter extends EndlessLoadingRecyclerViewAdapter {
    public static final int VIEW_TYPE_FRIEND_TYPING = -2;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 2;
    public static final int VIEW_TYPE_OWNER_IMAGE_MESSAGE = 3;

    private UserInfo ownerInfo;
    private Map<String, UserInfo> mapFriendsInfo;
    private boolean isFriendTypingMessageShowing;
    private RecyclerView.RecycledViewPool viewPool;

    public ChatMessageAdapter(Context context, UserInfo ownerInfo, Map<String, UserInfo> mapFriendsInfo) {
        super(context, false);
        this.ownerInfo = ownerInfo;
        this.mapFriendsInfo = mapFriendsInfo;
        this.viewPool = new RecyclerView.RecycledViewPool();
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
            case VIEW_TYPE_FRIEND_TYPING: {
                View itemView = getInflater().inflate(R.layout.item_friend_typing, parent, false);
                result = new FriendTypingViewHolder(itemView);
            }
            break;

            case VIEW_TYPE_LOADING: {
                result = initLoadingViewHolder(parent);
            }
            break;

            case VIEW_TYPE_OWNER_IMAGE_MESSAGE: {
                View itemView = getInflater().inflate(R.layout.item_owned_image_message, parent, false);
                result = new OwnerImageMessageViewHolder(itemView, viewPool);
            }
            break;

            case VIEW_TYPE_FRIEND_MESSAGE: {
                View itemView = getInflater().inflate(R.layout.item_friend_typing, parent, false);
                result = new FriendTypingViewHolder(itemView);
            }
            break;

            default: {
                result = initNormalViewHolder(parent);
            }
            break;
        }
        return result;
    }

    public void showFriendTypingMessage(String friendID) {
        if (isFriendTypingMessageShowing) {
            updateModel(0, friendID, false);
        } else {
            addModel(0, friendID, VIEW_TYPE_FRIEND_TYPING, true);
            isFriendTypingMessageShowing = true;
        }
    }

    public void hideFriendTypingMessage(String friendID) {
        if (isFriendTypingMessageShowing) {
            String typingFriendID = getItem(0, String.class);
            if (typingFriendID.equals(friendID)) {
                removeModel(0);
                isFriendTypingMessageShowing = false;
            }
        }
    }

    @Override
    protected RecyclerView.ViewHolder initNormalViewHolder(ViewGroup parent) {
        View itemView = getInflater().inflate(R.layout.item_owned_message, parent, false);
        return new OwnedTextMessageViewHolder(itemView);
    }

    @Override
    protected void solvedOnBindViewHolder(RecyclerView.ViewHolder viewHolder, int viewType, int position) {
        switch (viewType) {
            case VIEW_TYPE_FRIEND_TYPING: {
                bindFriendTypingViewHolder((FriendTypingViewHolder) viewHolder, position);
            }
            break;

            case VIEW_TYPE_LOADING: {
                bindLoadingViewHolder((LoadingViewHolder) viewHolder, position);
            }
            break;

            case VIEW_TYPE_OWNER_IMAGE_MESSAGE: {
                bindOwnerImageMessageViewHolder((OwnerImageMessageViewHolder) viewHolder, position);
            }
            break;

            case VIEW_TYPE_FRIEND_MESSAGE: {
                bindFriendMessageViewHolder((FriendTextMessageViewHolder) viewHolder, position);
            }
            break;

            default: {
                bindNormalViewHolder((NormalViewHolder) viewHolder, position);
            }
            break;
        }
    }

    private void bindOwnerImageMessageViewHolder(OwnerImageMessageViewHolder holder, int position) {
        OwnerMessageWrapper messageWrapper = getItem(position, OwnerMessageWrapper.class);
        ImageMessage imageMessage = (ImageMessage) messageWrapper.getMessage();

        holder.imageItemAdapter.refreshImageItems(imageMessage.getImages());

        if (messageWrapper.isSeenExpanded()) {
            holder.expandSeenView(false, messageWrapper);
        } else {
            holder.collapseSeenView(false, messageWrapper);
        }

        if (messageWrapper.isDateExpanded()) {
            holder.expandDateView(false, messageWrapper);
        } else {
            holder.collapseDateView(false, messageWrapper);
        }
    }

    private void bindFriendTypingViewHolder(FriendTypingViewHolder holder, int position) {
        String userTypingID = getItem(position, String.class);
        UserInfo userInfo = mapFriendsInfo.get(userTypingID);

        GlideApp.with(getContext())
                .load(userInfo.getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(holder.imgAvatar);
    }

    private void bindFriendMessageViewHolder(FriendTextMessageViewHolder holder, int position) {
        FriendMessageWrapper messageWrapper = getItem(position, FriendMessageWrapper.class);
        TextMessage textMessage = (TextMessage) messageWrapper.getMessage();

        GlideApp.with(getContext())
                .load(mapFriendsInfo.get(textMessage.getOwnerID()).getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(holder.imgAvatar);

        holder.txtMessage.setText(textMessage.getMessage());

        if (messageWrapper.isExpanded()) {
            holder.expandView(false, messageWrapper);
        } else {
            holder.collapseView(false, messageWrapper);
        }
    }

    private String generateSeenState(Map<String, Boolean> seenBy) {
        if (seenBy != null && !seenBy.isEmpty()) {
            int size = seenBy.size();
            if (size == 1 && seenBy.containsKey(ownerInfo.getEmail())) {
                return getContext().getString(R.string.seen);
            }
            StringBuilder result = new StringBuilder(getContext().getString(R.string.seen_by));
            for (Map.Entry<String, UserInfo> entry : mapFriendsInfo.entrySet()) {
                if (seenBy.containsKey(entry.getKey())) {
                    result.append(" ").append(entry.getValue().getFirstName());
                }
            }
            return result.toString();
        }
        return getContext().getString(R.string.sent);
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        OwnerMessageWrapper messageWrapper = getItem(position, OwnerMessageWrapper.class);
        TextMessage textMessage = (TextMessage) messageWrapper.getMessage();

        OwnedTextMessageViewHolder ownedTextMessageViewHolder = (OwnedTextMessageViewHolder) holder;

        ownedTextMessageViewHolder.txtMessage.setText(textMessage.getMessage());
        if (textMessage.getCreatedDate() == null) {
            ownedTextMessageViewHolder.txtMessage.getBackground().setLevel(0);
        } else {
            ownedTextMessageViewHolder.txtMessage.getBackground().setLevel(1);
        }

        if (messageWrapper.isSeenExpanded()) {
            ownedTextMessageViewHolder.expandSeenView(false, messageWrapper);
        } else {
            ownedTextMessageViewHolder.collapseSeenView(false, messageWrapper);
        }

        if (messageWrapper.isDateExpanded()) {
            ownedTextMessageViewHolder.expandDateView(false, messageWrapper);
        } else {
            ownedTextMessageViewHolder.collapseDateView(false, messageWrapper);
        }
    }

    private boolean isOwnerMessage(TextMessage textMessage) {
        return textMessage.getOwnerID().equals(ownerInfo.getId());
    }

    public void addTopMessage(TextMessage textMessage) {
        if (isOwnerMessage(textMessage)) {
            addModel(isFriendTypingMessageShowing ? 1 : 0, new OwnerMessageWrapper(textMessage), VIEW_TYPE_NORMAL, true);
        } else {
            addModel(isFriendTypingMessageShowing ? 1 : 0, new FriendMessageWrapper(textMessage), VIEW_TYPE_FRIEND_MESSAGE, true);
        }
    }

    public void addMessages(List<TextMessage> textMessages) {
        for (TextMessage textMessage : textMessages) {
            if (isOwnerMessage(textMessage)) {
                addModel(new OwnerMessageWrapper(textMessage), VIEW_TYPE_NORMAL, false);
            } else {
                addModel(new FriendMessageWrapper(textMessage), VIEW_TYPE_FRIEND_MESSAGE, false);
            }
        }
    }

    public void updateTextMessage(TextMessage textMessage, int position) {
        BaseMessage baseMessage;
        if (isOwnerMessage(textMessage)) {
            OwnerMessageWrapper messageWrapper = getItem(isFriendTypingMessageShowing ? position + 1 : position, OwnerMessageWrapper.class);
            baseMessage = messageWrapper.getMessage();
        } else {
            FriendMessageWrapper messageWrapper = getItem(isFriendTypingMessageShowing ? position + 1 : position, FriendMessageWrapper.class);
            baseMessage = messageWrapper.getMessage();
        }
        if (baseMessage instanceof TextMessage) {
            ((TextMessage) baseMessage).update(textMessage);
            notifyItemChanged(position);
        }
    }

    public void updateOwnerTopImageMessage(String url, int imagePosition) {
        OwnerMessageWrapper messageWrapper = getItem(isFriendTypingMessageShowing ? 1 : 0, OwnerMessageWrapper.class);
        BaseMessage baseMessage = messageWrapper.getMessage();
        if (baseMessage instanceof ImageMessage) {
            ImageItem imageItem = ((ImageMessage) baseMessage).getImages().get(imagePosition + "");
            if (imageItem != null) {
                imageItem.setUrl(url);
            }
        }
    }

    class OwnedTextMessageViewHolder extends NormalViewHolder implements View.OnClickListener {
        @BindView(R.id.txt_message)
        TextView txtMessage;
        @BindView(R.id.txt_seen)
        TextView txtSeen;
        @BindView(R.id.txt_time)
        TextView txtTime;

        OwnedTextMessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            OwnerMessageWrapper messageWrapper = getItem(getAdapterPosition(), OwnerMessageWrapper.class);

            if (messageWrapper.isSeenExpanded()) {
                collapseSeenView(false, messageWrapper);
            } else {
                expandSeenView(true, messageWrapper);
            }

            if (messageWrapper.isDateExpanded()) {
                collapseDateView(false, messageWrapper);
            } else {
                expandDateView(true, messageWrapper);
            }
        }

        void expandSeenView(boolean animate, OwnerMessageWrapper messageWrapper) {
            BaseMessage baseMessage = messageWrapper.getMessage();
            if (baseMessage.getCreatedDate() == null) {
                return;
            }
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtSeen.setText(generateSeenState(baseMessage.getSeenBy()));
            txtSeen.setVisibility(View.VISIBLE);
            messageWrapper.setSeenExpanded(true);
        }

        void collapseSeenView(boolean animate, OwnerMessageWrapper messageWrapper) {
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtSeen.setVisibility(View.GONE);
            messageWrapper.setSeenExpanded(false);
        }

        void expandDateView(boolean animate, OwnerMessageWrapper messageWrapper) {
            Date createdDate = messageWrapper.getMessage().getCreatedDate();
            if (createdDate == null) {
                return;
            }
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtTime.setText(DateTimeUtils.getDateTimeString(createdDate));
            txtTime.setVisibility(View.VISIBLE);
            messageWrapper.setDateExpanded(true);
        }

        void collapseDateView(boolean animate, OwnerMessageWrapper messageWrapper) {
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtTime.setVisibility(View.GONE);
            messageWrapper.setDateExpanded(false);
        }
    }

    class OwnerImageMessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rc_images)
        RecyclerView rcImages;
        @BindView(R.id.txt_seen)
        TextView txtSeen;
        @BindView(R.id.txt_time)
        TextView txtTime;

        UploadImageItemAdapter imageItemAdapter;

        public OwnerImageMessageViewHolder(View itemView, RecyclerView.RecycledViewPool viewPool) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Context context = getContext();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            imageItemAdapter = new UploadImageItemAdapter(context);
            rcImages.setRecycledViewPool(viewPool);
            rcImages.setLayoutManager(gridLayoutManager);
            rcImages.setAdapter(imageItemAdapter);
        }

        void expandSeenView(boolean animate, OwnerMessageWrapper messageWrapper) {
            BaseMessage baseMessage = messageWrapper.getMessage();
            if (baseMessage.getCreatedDate() == null) {
                return;
            }
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtSeen.setText(generateSeenState(baseMessage.getSeenBy()));
            txtSeen.setVisibility(View.VISIBLE);
            messageWrapper.setSeenExpanded(true);
        }

        void collapseSeenView(boolean animate, OwnerMessageWrapper messageWrapper) {
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtSeen.setVisibility(View.GONE);
            messageWrapper.setSeenExpanded(false);
        }

        void expandDateView(boolean animate, OwnerMessageWrapper messageWrapper) {
            Date createdDate = messageWrapper.getMessage().getCreatedDate();
            if (createdDate == null) {
                return;
            }
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtTime.setText(DateTimeUtils.getDateTimeString(createdDate));
            txtTime.setVisibility(View.VISIBLE);
            messageWrapper.setDateExpanded(true);
        }

        void collapseDateView(boolean animate, OwnerMessageWrapper messageWrapper) {
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtTime.setVisibility(View.GONE);
            messageWrapper.setDateExpanded(false);
        }
    }

    class FriendTextMessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_avatar)
        ImageView imgAvatar;
        @BindView(R.id.txt_message)
        TextView txtMessage;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_seen)
        TextView txtSeen;

        FriendTextMessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            FriendMessageWrapper messageWrapper = getItem(getAdapterPosition(), FriendMessageWrapper.class);
            if (messageWrapper.isExpanded()) {
                collapseView(false, messageWrapper);
            } else {
                expandView(true, messageWrapper);
            }
        }

        void expandView(boolean animate, FriendMessageWrapper messageWrapper) {
            BaseMessage baseMessage = messageWrapper.getMessage();
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtTime.setText(DateTimeUtils.getDateTimeString(baseMessage.getCreatedDate()));
            txtTime.setVisibility(View.VISIBLE);
            txtSeen.setText(generateSeenState(baseMessage.getSeenBy()));
            txtSeen.setVisibility(View.VISIBLE);
            messageWrapper.setExpanded(true);
        }

        void collapseView(boolean animate, FriendMessageWrapper messageWrapper) {
            if (animate) {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
            }
            txtTime.setVisibility(View.GONE);
            txtSeen.setVisibility(View.GONE);
            messageWrapper.setExpanded(false);
        }
    }

    class FriendTypingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_avatar)
        ImageView imgAvatar;

        public FriendTypingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
