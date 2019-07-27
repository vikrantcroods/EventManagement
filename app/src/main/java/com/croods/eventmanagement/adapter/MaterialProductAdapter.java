package com.croods.eventmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.local_data_manage.LocalProductModel;

import java.util.List;

public class MaterialProductAdapter extends  RecyclerView.Adapter<MaterialProductAdapter.MyViewHolder>{


    private List<LocalProductModel> productlist;
    private Context ctx;
    //private DbHelper helper;

    public MaterialProductAdapter(Context ctx, List<LocalProductModel> productlist)
    {
        this.ctx = ctx;
        this.productlist = productlist;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView lbl_product_namee,lbl_barcode;
        TextView txt_productremark,txt_productqty;


        public MyViewHolder(View itemView) {
            super(itemView);

            lbl_product_namee = itemView.findViewById(R.id.lbl_product_namee);
            lbl_barcode = itemView.findViewById(R.id.lbl_barcode);
            //txt_productremark = itemView.findViewById(R.id.txt_productremark);
            //txt_productqty = itemView.findViewById(R.id.txt_productqty);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.lbl_product_namee.setText(productlist.get(position).getDisplayName());
        holder.lbl_barcode.setText(productlist.get(position).getBarcode());
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }


    public void removeItem(int position) {
        productlist.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(LocalProductModel item, int position) {
        productlist.add(position, item);
        notifyItemInserted(position);
    }

    public List<LocalProductModel> getData() {
        return productlist;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_product_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

}



