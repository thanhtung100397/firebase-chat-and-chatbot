package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.transition.TransitionManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.custom_view.SpaceItemDecoration;
import com.ttt.chat_module.common.recycler_view_adapter.EndlessLoadingRecyclerViewAdapter;
import com.ttt.chat_module.models.ImageItem;
import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.models.message_models.EmojiMessage;
import com.ttt.chat_module.models.message_models.ImageMessage;
import com.ttt.chat_module.models.message_models.LocationMessage;
import com.ttt.chat_module.models.message_models.TextMessage;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.wrapper_model.MessageWrapper;
import com.ttt.chat_module.views.image_details.ImageDetailActivity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatMessageAdapter extends EndlessLoadingRecyclerViewAdapter {
    public static final int VIEW_TYPE_FRIEND_TYPING = -2;

    public static final int VIEW_TYPE_OWNED_TEXT_MESSAGE = 1;
    public static final int VIEW_TYPE_OWNED_IMAGE_MESSAGE = 3;
    public static final int VIEW_TYPE_OWNED_EMOJI_MESSAGE = 5;
    public static final int VIEW_TYPE_OWNED_LOCATION_MESSAGE = 7;

    public static final int VIEW_TYPE_FRIEND_TEXT_MESSAGE = 2;
    public static final int VIEW_TYPE_FRIEND_IMAGE_MESSAGE = 4;
    public static final int VIEW_TYPE_FRIEND_EMOJI_MESSAGE = 6;
    public static final int VIEW_TYPE_FRIEND_LOCATION_MESSAGE = 8;

    private UserInfo ownerInfo;
    private Map<String, UserInfo> mapOppositesInfo;
    private boolean isOppositeTypingMessageShowing;
    private RecyclerView.RecycledViewPool viewPool;

    public ChatMessageAdapter(Context context, UserInfo ownerInfo, Map<String, UserInfo> mapOppositesInfo) {
        super(context, false);
        this.ownerInfo = ownerInfo;
        this.mapOppositesInfo = mapOppositesInfo;
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
                result = new OppositeTypingViewHolder(itemView);
            }
            break;

            case VIEW_TYPE_LOADING: {
                result = initLoadingViewHolder(parent);
            }
            break;

            //------------------------------------------------------------------------------------//

            case VIEW_TYPE_OWNED_TEXT_MESSAGE: {
                View itemView = getInflater().inflate(R.layout.item_owned_text_message, parent, false);
                result = new OwnedTextMessageViewHolder(itemView);
            }
            break;

            case VIEW_TYPE_OWNED_IMAGE_MESSAGE: {
                View itemView = getInflater().inflate(R.layout.item_owned_image_message, parent, false);
                result = new OwnedImageMessageViewHolder(itemView, viewPool, true);
            }
            break;

            case VIEW_TYPE_OWNED_EMOJI_MESSAGE: {
                View itemView = getInflater().inflate(R.layout.item_owned_emoji_message, parent, false);
                result = new OwnedEmojiMessageViewHolder(itemView);
            }
            break;

            case VIEW_TYPE_OWNED_LOCATION_MESSAGE: {
                View itemView = getInflater().inflate(R.layout.item_owned_location_message, parent, false);
                result = new OwnedLocationMessageViewHolder(itemView);
            }
            break;

            //------------------------------------------------------------------------------------//

            case VIEW_TYPE_FRIEND_IMAGE_MESSAGE: {
                View itemView = getInflater().inflate(R.layout.item_friend_image_message, parent, false);
                result = new OppositeImageMessageViewHolder(itemView, viewPool);
            }
            break;

            case VIEW_TYPE_FRIEND_TEXT_MESSAGE: {
                View itemView = getInflater().inflate(R.layout.item_friend_text_message, parent, false);
                result = new OppositeTextMessageViewHolder(itemView);
            }
            break;

            case VIEW_TYPE_FRIEND_EMOJI_MESSAGE: {
                View itemView = getInflater().inflate(R.layout.item_friend_emoji_message, parent, false);
                result = new OppositeEmojiMessageViewHolder(itemView);
            }
            break;

            case VIEW_TYPE_FRIEND_LOCATION_MESSAGE: {
                View itemView = getInflater().inflate(R.layout.item_friend_location_message, parent, false);
                result = new OppositeLocationMessageViewHolder(itemView);
            }
            break;

            default: {
                result = null;
            }
            break;
        }
        return result;
    }

    public void showFriendTypingMessage(String friendID) {
        if (isOppositeTypingMessageShowing) {
            updateModel(0, friendID, false);
        } else {
            addModel(0, friendID, VIEW_TYPE_FRIEND_TYPING, true);
            isOppositeTypingMessageShowing = true;
        }
    }

    public void hideFriendTypingMessage(String friendID) {
        if (isOppositeTypingMessageShowing) {
            String typingFriendID = getItem(0, String.class);
            if (typingFriendID.equals(friendID)) {
                removeModel(0);
                isOppositeTypingMessageShowing = false;
            }
        }
    }

    @Override
    protected RecyclerView.ViewHolder initNormalViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected void solvedOnBindViewHolder(RecyclerView.ViewHolder viewHolder, int viewType, int position) {
        switch (viewType) {
            case VIEW_TYPE_FRIEND_TYPING: {
                bindFriendTypingViewHolder((OppositeTypingViewHolder) viewHolder, position);
            }
            break;

            case VIEW_TYPE_LOADING: {
                bindLoadingViewHolder((LoadingViewHolder) viewHolder, position);
            }
            break;

            //------------------------------------------------------------------------------------//

            case VIEW_TYPE_OWNED_TEXT_MESSAGE: {
                bindTextMessageViewHolder((OwnedTextMessageViewHolder) viewHolder, position);
            }
            break;

            case VIEW_TYPE_OWNED_IMAGE_MESSAGE: {
                bindImageMessageViewHolder((OwnedImageMessageViewHolder) viewHolder, position);
            }
            break;

            case VIEW_TYPE_OWNED_EMOJI_MESSAGE: {
                bindEmojiMessageViewHolder((OwnedEmojiMessageViewHolder) viewHolder, position);
            }
            break;

            case VIEW_TYPE_OWNED_LOCATION_MESSAGE: {
                bindLocationMessageViewHolder((OwnedLocationMessageViewHolder) viewHolder, position);
            }
            break;

            //------------------------------------------------------------------------------------//

            case VIEW_TYPE_FRIEND_TEXT_MESSAGE: {
                bindFriendTextMessageViewHolder((OppositeTextMessageViewHolder) viewHolder, position);
            }
            break;

            case VIEW_TYPE_FRIEND_IMAGE_MESSAGE: {
                bindFriendImageMessageViewHolder((OppositeImageMessageViewHolder) viewHolder, position);
            }
            break;

            case VIEW_TYPE_FRIEND_EMOJI_MESSAGE: {
                bindFriendEmojiMessageViewHolder((OppositeEmojiMessageViewHolder) viewHolder, position);
            }
            break;

            case VIEW_TYPE_FRIEND_LOCATION_MESSAGE: {
                bindFriendLocationMessageViewHolder((OppositeLocationMessageViewHolder) viewHolder, position);
            }
            break;

            default: {
                break;
            }
        }
    }

    private MessageWrapper bindMessageViewHolder(BaseMessageViewHolder holder, int position) {
        MessageWrapper messageWrapper = getItem(position, MessageWrapper.class);

        if (messageWrapper.isSeenExpanded()) {
            holder.expandSeenView(messageWrapper);
        } else {
            holder.collapseSeenView(messageWrapper);
        }

        if (messageWrapper.isDateExpanded()) {
            holder.expandDateView(messageWrapper);
        } else {
            holder.collapseDateView(messageWrapper);
        }

        return messageWrapper;
    }

    private MessageWrapper bindTextMessageViewHolder(OwnedTextMessageViewHolder holder, int position) {
        MessageWrapper messageWrapper = bindMessageViewHolder(holder, position);
        TextMessage textMessage = (TextMessage) messageWrapper.getMessage();

        holder.txtMessage.setText(textMessage.getMessage());

        return messageWrapper;
    }

    private MessageWrapper bindImageMessageViewHolder(OwnedImageMessageViewHolder holder, int position) {
        MessageWrapper messageWrapper = bindMessageViewHolder(holder, position);
        ImageMessage imageMessage = (ImageMessage) messageWrapper.getMessage();

        holder.imageItemAdapter.refreshImageItems(imageMessage.getImages());

        return messageWrapper;
    }

    private MessageWrapper bindEmojiMessageViewHolder(OwnedEmojiMessageViewHolder holder, int position) {
        MessageWrapper messageWrapper = bindMessageViewHolder(holder, position);
        EmojiMessage emojiMessage = (EmojiMessage) messageWrapper.getMessage();

        String path = Constants.ABSOLUTE_EMOJI_ASSETS_FOLDER_PATH + "/" + emojiMessage.getEmojiGroup() + "/" + emojiMessage.getEmojiID();
        GlideApp.with(getContext())
                .load(Uri.parse(path))
                .centerInside()
                .into(holder.imgEmoji);

        return messageWrapper;
    }

    private MessageWrapper bindLocationMessageViewHolder(OwnedLocationMessageViewHolder holder, int position) {
        MessageWrapper messageWrapper = bindMessageViewHolder(holder, position);
        LocationMessage locationMessage = (LocationMessage) messageWrapper.getMessage();

        LatLng latLng = new LatLng(locationMessage.getLat(), locationMessage.getLon());
        holder.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f));
        holder.googleMap.addMarker(new MarkerOptions()
                .title(locationMessage.getAddress())
                .position(latLng))
                .showInfoWindow();

        return messageWrapper;
    }

    private void bindFriendTextMessageViewHolder(OppositeTextMessageViewHolder holder, int position) {
        MessageWrapper messageWrapper = bindTextMessageViewHolder(holder, position);

        GlideApp.with(getContext())
                .load(mapOppositesInfo.get(messageWrapper.getMessage().getOwnerID()).getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(holder.imgAvatar);
    }

    private void bindFriendImageMessageViewHolder(OppositeImageMessageViewHolder holder, int position) {
        MessageWrapper messageWrapper = bindImageMessageViewHolder(holder, position);

        GlideApp.with(getContext())
                .load(mapOppositesInfo.get(messageWrapper.getMessage().getOwnerID()).getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(holder.imgAvatar);
    }

    private void bindFriendEmojiMessageViewHolder(OppositeEmojiMessageViewHolder holder, int position) {
        MessageWrapper messageWrapper = bindEmojiMessageViewHolder(holder, position);

        GlideApp.with(getContext())
                .load(mapOppositesInfo.get(messageWrapper.getMessage().getOwnerID()).getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(holder.imgAvatar);
    }

    private void bindFriendLocationMessageViewHolder(OppositeLocationMessageViewHolder holder, int position) {
        MessageWrapper messageWrapper = bindLocationMessageViewHolder(holder, position);

        GlideApp.with(getContext())
                .load(mapOppositesInfo.get(messageWrapper.getMessage().getOwnerID()).getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(holder.imgAvatar);
    }

    private void bindFriendTypingViewHolder(OppositeTypingViewHolder holder, int position) {
        String userTypingID = getItem(position, String.class);
        UserInfo userInfo = mapOppositesInfo.get(userTypingID);

        GlideApp.with(getContext())
                .load(userInfo.getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(holder.imgAvatar);
    }

    private String generateSeenState(Map<String, Boolean> seenBy) {
        if (seenBy != null && !seenBy.isEmpty()) {
            int size = seenBy.size();
            if (size == 1 && seenBy.containsKey(ownerInfo.getId())) {
                return getContext().getString(R.string.seen);
            }
            StringBuilder result = new StringBuilder(getContext().getString(R.string.seen_by));
            for (Map.Entry<String, UserInfo> entry : mapOppositesInfo.entrySet()) {
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

    }

    private boolean isOwnerMessage(BaseMessage baseMessage) {
        return baseMessage.getOwnerID().equals(ownerInfo.getId());
    }

    public void addTopMessage(BaseMessage baseMessage) {
        if (isOwnerMessage(baseMessage)) {
            switch (baseMessage.getType()) {
                case BaseMessage.TEXT_MESSAGE: {
                    addModel(isOppositeTypingMessageShowing ? 1 : 0, new MessageWrapper(baseMessage), VIEW_TYPE_OWNED_TEXT_MESSAGE, true);
                }
                break;

                case BaseMessage.IMAGE_MESSAGE: {
                    addModel(isOppositeTypingMessageShowing ? 1 : 0, new MessageWrapper(baseMessage), VIEW_TYPE_OWNED_IMAGE_MESSAGE, true);
                }
                break;

                case BaseMessage.EMOJI_MESSAGE: {
                    addModel(isOppositeTypingMessageShowing ? 1 : 0, new MessageWrapper(baseMessage), VIEW_TYPE_OWNED_EMOJI_MESSAGE, true);
                }
                break;

                case BaseMessage.LOCATION_MESSAGE: {
                    addModel(isOppositeTypingMessageShowing ? 1 : 0, new MessageWrapper(baseMessage), VIEW_TYPE_OWNED_LOCATION_MESSAGE, true);
                }
                break;

                default: {
                    break;
                }
            }
        } else {
            switch (baseMessage.getType()) {
                case BaseMessage.TEXT_MESSAGE: {
                    addModel(isOppositeTypingMessageShowing ? 1 : 0, new MessageWrapper(baseMessage), VIEW_TYPE_FRIEND_TEXT_MESSAGE, true);
                }
                break;

                case BaseMessage.IMAGE_MESSAGE: {
                    addModel(isOppositeTypingMessageShowing ? 1 : 0, new MessageWrapper(baseMessage), VIEW_TYPE_FRIEND_IMAGE_MESSAGE, true);
                }
                break;

                case BaseMessage.EMOJI_MESSAGE: {
                    addModel(isOppositeTypingMessageShowing ? 1 : 0, new MessageWrapper(baseMessage), VIEW_TYPE_FRIEND_EMOJI_MESSAGE, true);
                }
                break;

                case BaseMessage.LOCATION_MESSAGE: {
                    addModel(isOppositeTypingMessageShowing ? 1 : 0, new MessageWrapper(baseMessage), VIEW_TYPE_FRIEND_LOCATION_MESSAGE, true);
                }
                break;

                default: {
                    break;
                }
            }
        }
    }

    public void addMessages(List<BaseMessage> baseMessages) {
        for (BaseMessage baseMessage : baseMessages) {
            if (isOwnerMessage(baseMessage)) {
                switch (baseMessage.getType()) {
                    case BaseMessage.TEXT_MESSAGE: {
                        addModel(new MessageWrapper(baseMessage), VIEW_TYPE_OWNED_TEXT_MESSAGE, false);
                    }
                    break;

                    case BaseMessage.IMAGE_MESSAGE: {
                        addModel(new MessageWrapper(baseMessage), VIEW_TYPE_OWNED_IMAGE_MESSAGE, false);
                    }
                    break;

                    case BaseMessage.EMOJI_MESSAGE: {
                        addModel(new MessageWrapper(baseMessage), VIEW_TYPE_OWNED_EMOJI_MESSAGE, false);
                    }
                    break;

                    case BaseMessage.LOCATION_MESSAGE: {
                        addModel(new MessageWrapper(baseMessage), VIEW_TYPE_OWNED_LOCATION_MESSAGE, false);
                    }
                    break;

                    default: {
                        break;
                    }

                }
            } else {
                switch (baseMessage.getType()) {
                    case BaseMessage.TEXT_MESSAGE: {
                        addModel(new MessageWrapper(baseMessage), VIEW_TYPE_FRIEND_TEXT_MESSAGE, false);
                    }
                    break;

                    case BaseMessage.IMAGE_MESSAGE: {
                        addModel(new MessageWrapper(baseMessage), VIEW_TYPE_FRIEND_IMAGE_MESSAGE, false);
                    }
                    break;

                    case BaseMessage.EMOJI_MESSAGE: {
                        addModel(new MessageWrapper(baseMessage), VIEW_TYPE_FRIEND_EMOJI_MESSAGE, false);
                    }
                    break;

                    case BaseMessage.LOCATION_MESSAGE: {
                        addModel(new MessageWrapper(baseMessage), VIEW_TYPE_FRIEND_LOCATION_MESSAGE, false);
                    }
                    break;

                    default: {
                        break;
                    }

                }
            }
        }
    }

    public void updateMessage(BaseMessage baseMessage, int position) {
        MessageWrapper messageWrapper = getItem(isOppositeTypingMessageShowing ? position + 1 : position, MessageWrapper.class);
        messageWrapper.getMessage().update(baseMessage);
        notifyItemChanged(position);
    }

    public void showOwnerImageMessageUploaded(String url, int imagePosition) {
        int position = isOppositeTypingMessageShowing ? 1 : 0;
        RecyclerView recyclerView = getRecyclerView();
        View view = recyclerView.getChildAt(position);
        if (view != null) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof OwnedImageMessageViewHolder) {
                ((OwnedImageMessageViewHolder) viewHolder).imageItemAdapter.updateImageItem(imagePosition, url);
            }
        }
    }

    public void showOwnerImageMessageError() {
        int position = isOppositeTypingMessageShowing ? 1 : 0;
        RecyclerView recyclerView = getRecyclerView();
        View view = recyclerView.getChildAt(position);
        if (view != null) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof OwnedImageMessageViewHolder) {
                ((OwnedImageMessageViewHolder) viewHolder).imageItemAdapter.showError();
            }
        }
    }

    public void showUploadingNextImageMessage() {
        int position = isOppositeTypingMessageShowing ? 1 : 0;
        RecyclerView recyclerView = getRecyclerView();
        View view = recyclerView.getChildAt(position);
        if (view != null) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof OwnedImageMessageViewHolder) {
                ((OwnedImageMessageViewHolder) viewHolder).imageItemAdapter.uploadNextImage();
            }
        }
    }

    abstract class BaseMessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txt_seen)
        TextView txtSeen;
        @BindView(R.id.txt_time)
        TextView txtTime;

        BaseMessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MessageWrapper messageWrapper = getItem(getAdapterPosition(), MessageWrapper.class);

            if (messageWrapper.isExpanded()) {
                collapseSeenView(messageWrapper);
                collapseDateView(messageWrapper);
                messageWrapper.setExpanded(false);
            } else {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
                expandSeenView(messageWrapper);
                expandDateView(messageWrapper);
                messageWrapper.setExpanded(true);
            }
        }

        void expandSeenView(MessageWrapper messageWrapper) {
            BaseMessage baseMessage = messageWrapper.getMessage();
            if (baseMessage.getCreatedDate() == null) {
                return;
            }
            txtSeen.setText(generateSeenState(baseMessage.getSeenBy()));
            txtSeen.setVisibility(View.VISIBLE);
            messageWrapper.setSeenExpanded(true);
        }

        void collapseSeenView(MessageWrapper messageWrapper) {
            txtSeen.setVisibility(View.GONE);
            messageWrapper.setSeenExpanded(false);
        }

        void expandDateView(MessageWrapper messageWrapper) {
            Date createdDate = messageWrapper.getMessage().getCreatedDate();
            if (createdDate == null) {
                return;
            }
            txtTime.setText(DateTimeUtils.getDateTimeString(createdDate));
            txtTime.setVisibility(View.VISIBLE);
            messageWrapper.setDateExpanded(true);
        }

        void collapseDateView(MessageWrapper messageWrapper) {
            txtTime.setVisibility(View.GONE);
            messageWrapper.setDateExpanded(false);
        }
    }


    class OwnedTextMessageViewHolder extends BaseMessageViewHolder {
        @BindView(R.id.txt_message)
        TextView txtMessage;

        OwnedTextMessageViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OwnedImageMessageViewHolder extends BaseMessageViewHolder {
        @BindView(R.id.rc_images)
        RecyclerView rcImages;

        SingleUploadImageItemAdapter imageItemAdapter;

        OwnedImageMessageViewHolder(View itemView, RecyclerView.RecycledViewPool viewPool, boolean isRTL) {
            super(itemView);
            Context context = getContext();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3,
                    GridLayoutManager.VERTICAL, true) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }

                @Override
                protected boolean isLayoutRTL() {
                    return isRTL;
                }
            };
            imageItemAdapter = new SingleUploadImageItemAdapter(context);
            imageItemAdapter.setOnItemClickListener((adapter, position) -> {
                ImageItem imageItem = adapter.getItem(position);
                Intent intent = new Intent(context, ImageDetailActivity.class);
                if (imageItem.getUrl() == null) {
                    intent.putExtra(Constants.KEY_IMAGE_URI, imageItem.getUri().toString());
                } else {
                    intent.putExtra(Constants.KEY_IMAGE_URL, imageItem.getUrl());
                }
                context.startActivity(intent);
            });
            int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.small_padding);
            rcImages.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
            rcImages.setRecycledViewPool(viewPool);
            rcImages.setLayoutManager(gridLayoutManager);
            rcImages.setAdapter(imageItemAdapter);
        }
    }

    class OwnedEmojiMessageViewHolder extends BaseMessageViewHolder {
        @BindView(R.id.img_emoji)
        ImageView imgEmoji;

        OwnedEmojiMessageViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OwnedLocationMessageViewHolder extends BaseMessageViewHolder implements OnMapReadyCallback {
        @BindView(R.id.map_view)
        MapView mapView;
        GoogleMap googleMap;

        OwnedLocationMessageViewHolder(View itemView) {
            super(itemView);
            mapView.onCreate(null);
            mapView.getMapAsync(this);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(getContext());
            this.googleMap = googleMap;
        }
    }

    class OppositeTextMessageViewHolder extends OwnedTextMessageViewHolder {
        @BindView(R.id.img_avatar)
        CircleImageView imgAvatar;

        OppositeTextMessageViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OppositeImageMessageViewHolder extends OwnedImageMessageViewHolder {
        @BindView(R.id.img_avatar)
        CircleImageView imgAvatar;

        OppositeImageMessageViewHolder(View itemView, RecyclerView.RecycledViewPool viewPool) {
            super(itemView, viewPool, false);
        }
    }

    class OppositeEmojiMessageViewHolder extends OwnedEmojiMessageViewHolder {
        @BindView(R.id.img_avatar)
        CircleImageView imgAvatar;

        OppositeEmojiMessageViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OppositeLocationMessageViewHolder extends OwnedLocationMessageViewHolder {
        @BindView(R.id.img_avatar)
        CircleImageView imgAvatar;

        OppositeLocationMessageViewHolder(View itemView) {
            super(itemView);
        }
    }

    class OppositeTypingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_avatar)
        CircleImageView imgAvatar;

        OppositeTypingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
