package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.models.ImageItem;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadImageItemAdapter extends RecyclerView.Adapter<UploadImageItemAdapter.ImageItemViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private Map<String, ImageItem> mapImageItems;
    private int errorImageItemPosition = -1;

    public UploadImageItemAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mapImageItems = new HashMap<>(1);
    }

    public void refreshImageItems(Map<String, ImageItem> mapImageItems) {
        this.mapImageItems.clear();
        this.mapImageItems.putAll(mapImageItems);
        notifyDataSetChanged();
    }

    public void updateImageItem(int position, String url) {
        if(url != null) {
            ImageItem imageItem = mapImageItems.get(position+"");
            if(imageItem != null) {
                imageItem.setUrl(url);
                notifyItemChanged(position);
            }
        }
    }

    public void showErrorImageItem(int position) {
        this.errorImageItemPosition = position;
        notifyItemRangeChanged(position, getItemCount() - position);
    }

    @Override
    public ImageItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_image_uploading, parent, false);
        return new ImageItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageItemViewHolder holder, int position) {
        ImageItem imageItem = mapImageItems.get(position+"");
        if(imageItem.getUrl() == null) {
            GlideApp.with(context)
                    .load(imageItem.getUri())
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.imageView);
            if(errorImageItemPosition == -1) {
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.imgError.setVisibility(View.GONE);
                holder.imageView.clearColorFilter();
            } else {
                holder.progressBar.setVisibility(View.GONE);
                holder.imgError.setVisibility(View.VISIBLE);
            }
        } else {
            if(imageItem.getUri() != null) {
                GlideApp.with(context)
                        .load(imageItem.getUri())
                        .placeholder(R.drawable.image_placeholder)
                        .into(holder.imageView);
            } else {
                GlideApp.with(context)
                        .load(imageItem.getUrl())
                        .placeholder(R.drawable.image_placeholder)
                        .into(holder.imageView);
            }
            holder.progressBar.setVisibility(View.GONE);
            holder.imageView.setColorFilter(context.getResources().getColor(R.color.transparency_black));
        }
    }

    @Override
    public int getItemCount() {
        return mapImageItems.size();
    }

    class ImageItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_view)
        ImageView imageView;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.img_error)
        ImageView imgError;

        ImageItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
