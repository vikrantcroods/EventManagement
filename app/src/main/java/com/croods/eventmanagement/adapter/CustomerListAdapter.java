package com.croods.eventmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.model.CustomerListResponse;
import com.croods.eventmanagement.model.EventListResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomerListAdapter extends BaseAdapter {
    private List<CustomerListResponse> customerList;
    private List<CustomerListResponse> arraylist;
    private Context ctx;
    private LayoutInflater inflater = null;

    public CustomerListAdapter(List<CustomerListResponse> customerList, Context ctx) {
        this.customerList = customerList;
        this.ctx = ctx;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arraylist = new ArrayList<>();
        arraylist.addAll(customerList);
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

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        customerList.clear();
        if (charText.length() == 0) {
            customerList.addAll(arraylist);
        }
        else
        {
            for (CustomerListResponse wp : arraylist)
            {
                if (wp.getCompanyName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    customerList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView lbl_ccity,lbl_cname,lbl_cmob;
    }
}
