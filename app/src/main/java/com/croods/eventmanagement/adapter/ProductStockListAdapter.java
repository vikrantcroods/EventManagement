package com.croods.eventmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.model.ProductStockDetailList;

import java.util.List;

public class ProductStockListAdapter extends BaseAdapter {
    private List<ProductStockDetailList> productList;
    private Context ctx;
    private LayoutInflater inflater = null;

    public ProductStockListAdapter(List<ProductStockDetailList> productList, Context ctx) {
        this.productList = productList;
        this.ctx = ctx;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
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
            view = inflater.inflate(R.layout.product_detail_list_row, parent, false);

            holder.lbl_event_qty = (TextView) view.findViewById(R.id.lbl_event_qty);
            holder.lbl_event_name = (TextView) view.findViewById(R.id.lbl_event_name);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.lbl_event_qty.setText(productList.get(position).getQty());
        holder.lbl_event_name.setText(productList.get(position).getName());

        return view;
    }

    private class ViewHolder {
        TextView lbl_event_name,lbl_event_qty;
    }
}
