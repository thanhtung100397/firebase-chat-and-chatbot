package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.recycler_view_adapter.RecyclerViewAdapter;
import com.ttt.chat_module.models.ContactInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactInfoAdapter extends RecyclerViewAdapter {
    public ContactInfoAdapter(Context context) {
        super(context, false);
    }

    @Override
    protected RecyclerView.ViewHolder initNormalViewHolder(ViewGroup parent) {
        View itemView = getInflater().inflate(R.layout.item_contact, parent, false);
        return new ContactInfoViewHolder(itemView);
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        ContactInfo contactInfo = getItem(position, ContactInfo.class);

        ContactInfoViewHolder contactInfoViewHolder = (ContactInfoViewHolder) holder;
        contactInfoViewHolder.txtContactName.setText(contactInfo.getContactName());
        contactInfoViewHolder.txtContactNumber.setText(contactInfo.getContactNumber());
    }

    class ContactInfoViewHolder extends NormalViewHolder {
        @BindView(R.id.txt_contact_name)
        TextView txtContactName;
        @BindView(R.id.txt_contact_number)
        TextView txtContactNumber;

        ContactInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
