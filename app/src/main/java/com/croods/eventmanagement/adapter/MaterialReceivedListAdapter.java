package com.croods.eventmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.model.MaterialReceivedListResponse;

import java.util.List;

public class MaterialReceivedListAdapter extends BaseAdapter {
    private List<MaterialReceivedListResponse> materialReceivedList;
    private Context ctx;
    private LayoutInflater inflater = null;

    public MaterialReceivedListAdapter(List<MaterialReceivedListResponse> materialReceivedList, Context ctx) {
        this.materialReceivedList = materialReceivedList;
        this.ctx = ctx;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return materialReceivedList.size();
    }

    @Override
    public Object getItem(int position) {
        return materialReceivedList.get(position);
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
            view = inflater.inflate(R.layout.material_receivedlist_row, parent, false);

            holder.lbl_recmaterial_no = (TextView) view.findViewById(R.id.lbl_recmaterial_no);
            holder.lbl_recmaterial_jobcode = (TextView) view.findViewById(R.id.lbl_recmaterial_jobcode);
            holder.lbl_recmaterial_date = (TextView) view.findViewById(R.id.lbl_recmaterial_date);
            holder.lbl_recquantity = (TextView) view.findViewById(R.id.lbl_recquantity);
            holder.lbl_store = (TextView) view.findViewById(R.id.lbl_store);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        MaterialReceivedListResponse response = materialReceivedList.get(position);
        holder.lbl_recmaterial_no.setText(response.getPrefix()+" "+response.getMaterialInwardNo());
        holder.lbl_recmaterial_date.setText(response.getMaterialInwardDate());
        holder.lbl_recmaterial_jobcode.setText(response.getJobCode());
        holder.lbl_store.setText(response.getStoreVo().getStoreName());
        holder.lbl_recquantity.setText(" " +response.getInquantity()+" Qty");
        return view;
    }

    private class ViewHolder {
        TextView lbl_recmaterial_no,lbl_recmaterial_jobcode,lbl_recmaterial_date,lbl_recquantity,lbl_store;
    }
}
