package com.ttt.chat_module.common.recycler_view_adapter;

import android.support.v7.util.DiffUtil;
import android.util.Log;

import java.util.List;

/**
 * Created by TranThanhTung on 26/11/2017.
 */

public class DiffUtilCallBack extends DiffUtil.Callback {
    private List<RecyclerViewAdapter.ModelWrapper> oldItems;
    private List<RecyclerViewAdapter.ModelWrapper> newItems;

    public DiffUtilCallBack(List<RecyclerViewAdapter.ModelWrapper> oldItems,
                            List<RecyclerViewAdapter.ModelWrapper> newItems) {
        this.oldItems = oldItems;
        this.newItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).id == newItems.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
    }
}
