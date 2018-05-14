package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.recycler_view_adapter.RecyclerViewAdapter;
import com.ttt.chat_module.models.BotInfo;
import com.ttt.chat_module.models.ContactInfo;
import com.ttt.chat_module.models.chat_bot_message.ContactQueryMessage;
import com.ttt.chat_module.models.chat_bot_message.SimpleMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatBotMessageAdapter extends RecyclerViewAdapter {
    public static final int VIEW_TYPE_BOT_TEXT_MESSAGE = 1;
    public static final int VIEW_TYPE_BOT_TEXT_AND_ITEMS_MESSAGE = 2;

    private BotInfo botInfo;

    public ChatBotMessageAdapter(Context context, BotInfo botInfo) {
        super(context, false);
        this.botInfo = botInfo;
    }

    @Override
    protected RecyclerView.ViewHolder solvedOnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_BOT_TEXT_MESSAGE: {
                View itemView = getInflater().inflate(R.layout.item_bot_text_message, parent, false);
                return new BotTextMessageViewHolder(itemView);
            }

            case VIEW_TYPE_BOT_TEXT_AND_ITEMS_MESSAGE: {
                View itemView = getInflater().inflate(R.layout.item_bot_text_message_and_items, parent, false);
                return new BotTextMessageAndItemsViewHolder(itemView);
            }

            default: {
                return initNormalViewHolder(parent);
            }
        }
    }

    @Override
    protected RecyclerView.ViewHolder initNormalViewHolder(ViewGroup parent) {
        View itemView = getInflater().inflate(R.layout.item_owned_text_message_simple, parent, false);
        return new OwnedTextMessageViewHolder(itemView);
    }

    @Override
    protected void solvedOnBindViewHolder(RecyclerView.ViewHolder viewHolder, int viewType, int position) {
        switch (viewType) {
            case VIEW_TYPE_BOT_TEXT_MESSAGE: {
                bindBotTextMessageViewHolder((BotTextMessageViewHolder) viewHolder, position);
            }
            break;

            case VIEW_TYPE_BOT_TEXT_AND_ITEMS_MESSAGE: {
                bindBotTextAndItemsMessageViewHolder((BotTextMessageAndItemsViewHolder) viewHolder, position);
            }
            break;

            default: {
                bindNormalViewHolder((NormalViewHolder) viewHolder, position);
                break;
            }
        }
    }

    private SimpleMessage bindBotTextMessageViewHolder(BotTextMessageViewHolder holder, int position) {
        SimpleMessage simpleMessage = getItem(position, SimpleMessage.class);
        setHtmlText(holder.txtMessage, simpleMessage.getMessage());

        GlideApp.with(getContext())
                .load(botInfo.getAvatarUrl())
                .placeholder(R.drawable.api_ai_logo)
                .into(holder.imgAvatar);

        return simpleMessage;
    }

    private void bindBotTextAndItemsMessageViewHolder(BotTextMessageAndItemsViewHolder holder, int position) {
        ContactQueryMessage contactQueryMessage = (ContactQueryMessage) bindBotTextMessageViewHolder(holder, position);
        holder.showContacts(contactQueryMessage);
    }

    private void setHtmlText(TextView textView, String htmlText) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(htmlText));
        } else {
            textView.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT));
        }
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        SimpleMessage simpleMessage = getItem(position, SimpleMessage.class);

        OwnedTextMessageViewHolder ownedTextMessageViewHolder = (OwnedTextMessageViewHolder) holder;
        ownedTextMessageViewHolder.txtMessage.setText(simpleMessage.getMessage());
    }

    class OwnedTextMessageViewHolder extends NormalViewHolder {
        @BindView(R.id.txt_message)
        TextView txtMessage;

        OwnedTextMessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class BotTextMessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_avatar)
        CircleImageView imgAvatar;
        @BindView(R.id.txt_message)
        TextView txtMessage;

        BotTextMessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class BotTextMessageAndItemsViewHolder extends BotTextMessageViewHolder implements OnItemClickListener {
        @BindView(R.id.rc_items)
        RecyclerView rcItems;
        ContactInfoAdapter contactInfoAdapter;
        String action;

        BotTextMessageAndItemsViewHolder(View itemView) {
            super(itemView);
            Context context = getContext();
            contactInfoAdapter = new ContactInfoAdapter(context);
            contactInfoAdapter.addOnItemClickListener(this);
            rcItems.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            rcItems.setAdapter(contactInfoAdapter);
        }

        void showContacts(ContactQueryMessage contactQueryMessage) {
            this.action = contactQueryMessage.getAction();
            this.contactInfoAdapter.refresh(contactQueryMessage.getContactsInfo());
            if (contactInfoAdapter.getItemCount() == 0) {
                rcItems.setVisibility(View.GONE);
            } else {
                rcItems.setVisibility(View.VISIBLE);
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onItemClick(RecyclerView.Adapter adapter, RecyclerView.ViewHolder viewHolder,
                                int viewType, int position) {
            Context context = getContext();
            ContactInfo contactInfo = contactInfoAdapter.getItem(position, ContactInfo.class);
            String message;
            switch (action) {
                case Constants.CHAT_BOT_ACTION_CALL: {
                    Uri number = Uri.parse("tel:" + contactInfo.getContactNumber());
                    Intent callIntent = new Intent(Intent.ACTION_CALL, number);
                    context.startActivity(callIntent);
                    message = context.getString(R.string.make_call) + " " + contactInfo.getContactName() + " (" + contactInfo.getContactNumber() + ")";
                }
                break;

                case Constants.CHAT_BOT_ACTION_DIAL: {
                    Uri number = Uri.parse("tel:" + contactInfo.getContactNumber());
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    context.startActivity(callIntent);
                    message = context.getString(R.string.make_dial) + " " + contactInfo.getContactName() + " (" + contactInfo.getContactNumber() + ")";
                }
                break;

                default: {
                    message = "no action";
                    break;
                }
            }
            removeModel(getAdapterPosition());
            addModel(0, new SimpleMessage(message), VIEW_TYPE_BOT_TEXT_MESSAGE, true);
        }
    }
}
