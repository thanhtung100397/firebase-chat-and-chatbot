package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.recycler_view_adapter.EndlessLoadingRecyclerViewAdapter;
import com.ttt.chat_module.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ListFriendsAdapter extends EndlessLoadingRecyclerViewAdapter {

    public ListFriendsAdapter(Context context) {
        super(context, false);
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
        View itemView = getInflater().inflate(R.layout.item_friend, parent, false);
        return new ItemFriendViewHolder(itemView);
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        User user = getItem(position, User.class);
        ItemFriendViewHolder itemFriendViewHolder = (ItemFriendViewHolder) holder;

        GlideApp.with(getContext())
                .load(user.getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(itemFriendViewHolder.imgAvatar);
        itemFriendViewHolder.txtName.setText(user.getLastName() + " " + user.getFirstName());
        itemFriendViewHolder.txtEmail.setText(user.getEmail());
        itemFriendViewHolder.imgOnline.setVisibility(user.getIsOnline() ? View.VISIBLE : View.GONE);
    }

    class ItemFriendViewHolder extends NormalViewHolder {
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_email)
        TextView txtEmail;
        @BindView(R.id.img_avatar)
        CircleImageView imgAvatar;
        @BindView(R.id.img_online)
        CircleImageView imgOnline;

        ItemFriendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
