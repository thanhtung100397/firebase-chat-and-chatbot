package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.recycler_view_adapter.RecyclerViewAdapter;
import com.ttt.chat_module.models.EmojiItem;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmojiAdapter extends RecyclerViewAdapter {

    public EmojiAdapter(String type, Context context) {
        super(context, false);
        loadEmojiItemFromAssets(type, context);
    }

    private void loadEmojiItemFromAssets(String type, Context context) {
        AssetManager assetManager = context.getAssets();
        try {
            String[] emojiPaths = assetManager.list(Constants.EMOJI_ROOT_ASSETS_FOLDER_PATH + "/" + type);
            for (String emojiPath : emojiPaths) {
                addModel(new EmojiItem(type, emojiPath), false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected RecyclerView.ViewHolder initNormalViewHolder(ViewGroup parent) {
        View itemView = getInflater().inflate(R.layout.item_image, parent, false);
        return new EmojiViewHolder(itemView);
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        EmojiViewHolder emojiViewHolder = (EmojiViewHolder) holder;

        EmojiItem emojiItem = getItem(position, EmojiItem.class);

        GlideApp.with(getContext())
                .load(Uri.parse(emojiItem.getPath()))
                .centerInside()
                .into(emojiViewHolder.imageView);
    }

    class EmojiViewHolder extends NormalViewHolder {
        @BindView(R.id.img_view)
        ImageView imageView;

        EmojiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
