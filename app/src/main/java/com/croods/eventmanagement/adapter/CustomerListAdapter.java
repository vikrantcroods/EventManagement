package com.croods.eventmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.model.CustomerListResponse;

import java.util.List;

public class CustomerListAdapter extends BaseAdapter {
    private List<CustomerListResponse> customerList;
    private Context ctx;
    private LayoutInflater inflater = null;

    public CustomerListAdapter(List<CustomerListResponse> customerList, Context ctx) {
        this.customerList = customerList;
        this.ctx = ctx;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return customerList.size();
    }

    @Override
    public Object getItem(int position) {
        return customerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.customer_list_row, parent, false);

            holder.lbl_ccity = (TextView) view.findViewById(R.id.lbl_ccity);
            holder.lbl_cname = (TextView) view.findViewById(R.id.lbl_cname);
            holder.lbl_cmob = (TextView) view.findViewById(R.id.lbl_cmob);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.lbl_cname.setText(customerList.get(position).getCompanyName());
        holder.lbl_cmob.setText("  "+customerList.get(position).getCompanyMobileno());
        holder.lbl_ccity.setText("  "+customerList.get(position).getCityName());

        return view;
    }

    private class ViewHolder {
        TextView lbl_ccity,lbl_cname,lbl_cmob;
    }
}
