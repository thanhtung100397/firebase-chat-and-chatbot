package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.recycler_view_adapter.RecyclerViewAdapter;
import com.ttt.chat_module.models.google_map.Address;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TranThanhTung on 04/02/2018.
 */

public class AddressAdapter extends RecyclerViewAdapter {

    public AddressAdapter(Context context) {
        super(context, false);
    }

    @Override
    protected RecyclerView.ViewHolder initNormalViewHolder(ViewGroup parent) {
        View item = getInflater().inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(item);
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        AddressViewHolder addressViewHolder = (AddressViewHolder) holder;
        Address address = getItem(position, Address.class);
        addressViewHolder.textName.setText(address.getFormattedAddress());
    }

    public class AddressViewHolder extends RecyclerViewAdapter.NormalViewHolder {
        @BindView(R.id.txt_name)
        TextView textName;

        AddressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


