package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.recycler_view_adapter.EndlessLoadingRecyclerViewAdapter;
import com.ttt.chat_module.models.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ListFriendsAdapter extends EndlessLoadingRecyclerViewAdapter {
    private Map<String, Integer> userPositionMap;

    public ListFriendsAdapter(Context context) {
        super(context, false);
        this.userPositionMap = new HashMap<>();
    }

    public void addFriends(Map<String, Integer> userPositionMap, List<UserInfo> usersInfo) {
        this.userPositionMap.putAll(userPositionMap);
        addModels(usersInfo, false);
    }

    public void refreshFriends(Map<String, Integer> userPositionMap, List<UserInfo> usersInfo) {
        this.userPositionMap = userPositionMap;
        refresh(usersInfo);
    }

    public void updateUserInfo(UserInfo userInfo) {
        Integer position = userPositionMap.get(userInfo.getId());
        if (position == null) {
            return;
        }
        UserInfo userInfoFound = getItem(position, UserInfo.class);
        boolean needRefreshView = needRefreshView(userInfo, userInfoFound);
        userInfoFound.update(userInfo);
        if(needRefreshView) {
            notifyItemChanged(position);
        }
    }

    private boolean needRefreshView(UserInfo newUserInfo, UserInfo oldUserInfo) {
        return (newUserInfo.getIsOnline() != oldUserInfo.getIsOnline()) ||
                !newUserInfo.getFirstName().equals(oldUserInfo.getFirstName()) ||
                !newUserInfo.getLastName().equals(oldUserInfo.getLastName()) ||
                !newUserInfo.getAvatarUrl().equals(oldUserInfo.getAvatarUrl());
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
        UserInfo userInfo = getItem(position, UserInfo.class);
        ItemFriendViewHolder itemFriendViewHolder = (ItemFriendViewHolder) holder;

        GlideApp.with(getContext())
                .load(userInfo.getAvatarUrl())
                .placeholder(R.drawable.avatar_placeholder)
                .into(itemFriendViewHolder.imgAvatar);
        itemFriendViewHolder.txtName.setText(userInfo.getLastName() + " " + userInfo.getFirstName());
        itemFriendViewHolder.txtEmail.setText(userInfo.getEmail());
        itemFriendViewHolder.imgOnline.setVisibility(userInfo.getIsOnline() ? View.VISIBLE : View.GONE);
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
