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

public class SingleUploadImageItemAdapter extends RecyclerView.Adapter<SingleUploadImageItemAdapter.ImageItemViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private Map<String, ImageItem> mapImageItems;
    private int currentImageItemPosition = -1;
    private boolean isError = false;
    private OnItemClickListener onItemClickListener;

    public SingleUploadImageItemAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mapImageItems = new HashMap<>(1);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ImageItem getItem(int position) {
        return mapImageItems.get(position+"");
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

    public void showError() {
        this.isError = true;
        notifyItemChanged(currentImageItemPosition);
    }

    public void uploadNextImage() {
        int currentPosition = currentImageItemPosition;
        currentImageItemPosition++;
        notifyItemRangeChanged(currentPosition, 2);
    }

    @Override
    public ImageItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_image_uploading, parent, false);
        ImageItemViewHolder imageItemViewHolder = new ImageItemViewHolder(itemView);
        return new ImageItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageItemViewHolder holder, int position) {
        ImageItem imageItem = mapImageItems.get(position+"");
        if(imageItem.getUrl() == null) {//image not uploaded
            GlideApp.with(context)
                    .load(imageItem.getUri())
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.imageView);

            if(currentImageItemPosition == position) {
                if(isError) {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.imgError.setVisibility(View.VISIBLE);
                } else {
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.imgError.setVisibility(View.GONE);
                }
            }
            holder.imageView.setColorFilter(context.getResources().getColor(R.color.transparency_black));
        } else {//image uploaded
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
            holder.imgError.setVisibility(View.GONE);
            holder.imageView.clearColorFilter();
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
            itemView.setOnClickListener(view -> {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(SingleUploadImageItemAdapter.this, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(SingleUploadImageItemAdapter adapter, int position);
    }
}
