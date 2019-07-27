package com.croods.eventmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.model.MaterialSendListResponse;
import com.croods.eventmanagement.model.ProductListResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MaterialSendListAdapter extends BaseAdapter {
    private List<MaterialSendListResponse> materialSendList;
    private ArrayList<MaterialSendListResponse> arraylist;

    private Context ctx;
    private LayoutInflater inflater = null;

    public MaterialSendListAdapter(List<MaterialSendListResponse> materialSendList, Context ctx) {
        this.materialSendList = materialSendList;
        this.ctx = ctx;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        arraylist = new ArrayList<>();
        arraylist.addAll(materialSendList);
    }

    @Override
    public int getCount() {
        return materialSendList.size();
    }

    @Override
    public Object getItem(int position) {
        return materialSendList.get(position);
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
            view = inflater.inflate(R.layout.material_sendlist_row, parent, false);

            holder.lbl_sendmaterial_no = (TextView) view.findViewById(R.id.lbl_sendmaterial_no);
            holder.lbl_sendmaterial_jobcode = (TextView) view.findViewById(R.id.lbl_sendmaterial_jobcode);
            holder.lbl_sendmaterial_date = (TextView) view.findViewById(R.id.lbl_sendmaterial_date);
            holder.lbl_eoutquantity = (TextView) view.findViewById(R.id.lbl_eoutquantity);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        MaterialSendListResponse response = materialSendList.get(position);
        holder.lbl_sendmaterial_no.setText(response.getPrefix()+" "+response.getMaterialOutwardNo());
        holder.lbl_sendmaterial_date.setText(response.getMaterialOutwardDate());
        holder.lbl_sendmaterial_jobcode.setText(response.getJobCode());
        holder.lbl_eoutquantity.setText(" " +response.getOutquantity()+" Qty");
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        materialSendList.clear();
        if (charText.length() == 0) {
            materialSendList.addAll(arraylist);
        }
        else
        {
            for (MaterialSendListResponse wp : arraylist)
            {
                if (wp.getJobCode().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    materialSendList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView lbl_sendmaterial_no,lbl_sendmaterial_jobcode,lbl_sendmaterial_date,lbl_eoutquantity;
    }
}
