package com.croods.eventmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.model.SpinnerResponse;

import java.util.List;

public class TransportAdapter extends BaseAdapter {

    private Context ctx;
    private List<SpinnerResponse> lstTransport;

    public TransportAdapter(Context ctx, List<SpinnerResponse> lstTransport) {
        this.ctx = ctx;
        this.lstTransport = lstTransport;
    }


    @Override
    public int getCount() {
        return lstTransport.size();
    }

    @Override
    public Object getItem(int i) {
        return lstTransport.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(ctx);
            convertView = layoutInflator.inflate(R.layout.spinner_transport_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(lstTransport.get(position).getCompanyName());

        return convertView;    }

    private class ViewHolder {
        private TextView mTextView;
    }

}
