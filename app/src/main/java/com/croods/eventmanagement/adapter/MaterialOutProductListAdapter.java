package com.croods.eventmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.model.ProductListResponse;

import java.util.List;

public class MaterialOutProductListAdapter extends BaseAdapter {
    private List<ProductListResponse> productList;
    private Context ctx;
    private LayoutInflater inflater = null;

    public MaterialOutProductListAdapter(List<ProductListResponse> productList, Context ctx) {
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
            view = inflater.inflate(R.layout.materialout_event_product_row, parent, false);

            holder.lbl_product_name = (TextView) view.findViewById(R.id.lbl_product_name);
            holder.lbl_product_totalqty = (TextView) view.findViewById(R.id.lbl_product_totalqty);
            holder.lbl_product_totalrqty = (TextView) view.findViewById(R.id.lbl_product_totalrqty);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        String qty = String.valueOf(productList.get(position).getQuantity());
        String imageUrl = productList.get(position).getProductImage();
        int isPart = productList.get(position).getIsPart();

        holder.lbl_product_name.setText(productList.get(position).getName());

        holder.lbl_product_totalqty.setText(""+productList.get(position).getQuantity());
        holder.lbl_product_totalrqty.setText(""+productList.get(position).getRquantity());

        return view;
    }

    private class ViewHolder {
        TextView lbl_product_totalrqty, lbl_product_totalqty, lbl_product_name;

    }
}
