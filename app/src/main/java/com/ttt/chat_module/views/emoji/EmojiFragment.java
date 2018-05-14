package com.ttt.chat_module.views.emoji;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ttt.chat_module.R;
import com.ttt.chat_module.bus_event.EmojiSelectedEvent;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.adapter.recycler_view_adapter.EmojiAdapter;
import com.ttt.chat_module.common.custom_view.SpaceItemDecoration;
import com.ttt.chat_module.common.recycler_view_adapter.RecyclerViewAdapter;
import com.ttt.chat_module.models.EmojiItem;
import com.ttt.chat_module.presenters.BasePresenter;
import com.ttt.chat_module.views.base.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmojiFragment extends BaseFragment implements RecyclerViewAdapter.OnItemClickListener {
    @BindView(R.id.rc_emoji)
    RecyclerView rcEmoji;
    private EmojiAdapter emojiAdapter;

    @Override
    protected int getLayoutResources() {
        return R.layout.fragment_emoji;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        String type = bundle.getString(Constants.KEY_EMOJI_TYPE);
        if (type == null) {
            return;
        }
        Context context = getActivity();
        emojiAdapter = new EmojiAdapter(type, context);
        emojiAdapter.addOnItemClickListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);

        int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.small_padding);
        rcEmoji.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        rcEmoji.setLayoutManager(gridLayoutManager);
        rcEmoji.setAdapter(emojiAdapter);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onItemClick(RecyclerView.Adapter adapter, RecyclerView.ViewHolder viewHolder, int viewType, int position) {
        EmojiItem emojiItem = emojiAdapter.getItem(position, EmojiItem.class);
        EventBus.getDefault().post(new EmojiSelectedEvent(emojiItem.getType(), emojiItem.getId()));
    }
}
