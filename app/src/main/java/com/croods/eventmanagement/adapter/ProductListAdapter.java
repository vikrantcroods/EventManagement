package com.croods.eventmanagement.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.croods.eventmanagement.R;
import com.croods.eventmanagement.model.ProductListResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductListAdapter extends BaseAdapter {
    private List<ProductListResponse> productList;
    private ArrayList<ProductListResponse> arraylist;
    private Context ctx;
    private LayoutInflater inflater = null;

    public ProductListAdapter(List<ProductListResponse> productList, Context ctx) {
        this.productList = productList;
        this.ctx = ctx;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arraylist = new ArrayList<>();
        arraylist.addAll(productList);
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
            view = inflater.inflate(R.layout.product_list_row, parent, false);

            holder.lbl_product_name = (TextView) view.findViewById(R.id.lbl_product_name);
            holder.lbl_product_row = (TextView) view.findViewById(R.id.lbl_product_row);
            holder.lbl_product_displayName = (TextView) view.findViewById(R.id.lbl_product_displayName);
            holder.lbl_product_quantity = (TextView) view.findViewById(R.id.lbl_product_remain_quantity);
            holder.lbl_product_total_quantity = (TextView) view.findViewById(R.id.lbl_product_total_quantity);
            holder.lbl_product_out_quantity = (TextView) view.findViewById(R.id.lbl_product_out_quantity);
            holder.img_product = (ImageView) view.findViewById(R.id.img_product);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        String qty = String.valueOf(productList.get(position).getQuantity());
        String imageUrl = productList.get(position).getProductImage();
        int isPart = productList.get(position).getIsPart();
        int outQty = productList.get(position).getOutquantity();
        int total = outQty + productList.get(position).getQuantity();

        holder.lbl_product_name.setText(productList.get(position).getName());
        holder.lbl_product_displayName.setText(productList.get(position).getDisplayName());

        if (qty != null)
        {
            String tqty,oqty,rqty;

            tqty = "Total Qty : "+Html.fromHtml("<font color = #3E88FA>" +total + "</font>");
            oqty = "Out Qty : "+  Html.fromHtml("<font color = #3E88FA>" +outQty + "</font>");
            rqty = "Ramain Qty : "+Html.fromHtml("<font color = #3E88FA>" +productList.get(position).getQuantity() + "</font>");
            holder.lbl_product_quantity.setText(qty);
            holder.lbl_product_total_quantity.setText(String.valueOf(total));
            holder.lbl_product_out_quantity.setText(String.valueOf(outQty));
        }
        else
            holder.lbl_product_quantity.setText(" Qty 0");

        if (imageUrl !=null)
            Glide.with(ctx).load(Uri.parse(imageUrl)).into(holder.img_product);
        else
            holder.img_product.setImageDrawable(ctx.getResources().getDrawable(R.drawable.noimage));

        if (isPart == 0)
        {
            holder.lbl_product_row.setBackground(ctx.getResources().getDrawable(R.drawable.no_raw_back));
            holder.lbl_product_row.setText("No");
        }
        else
        {
            holder.lbl_product_row.setBackground(ctx.getResources().getDrawable(R.drawable.raw_back));
            holder.lbl_product_row.setText("Yes");
        }

        return view;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        productList.clear();
        if (charText.length() == 0) {
            productList.addAll(arraylist);
        }
        else
        {
            for (ProductListResponse wp : arraylist)
            {
                if (wp.getDisplayName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    productList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


    private class ViewHolder {
        TextView lbl_product_name,lbl_product_row,lbl_product_displayName,lbl_product_quantity,lbl_product_total_quantity,lbl_product_out_quantity;
        ImageView img_product;
    }
}
