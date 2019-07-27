package com.croods.eventmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.model.EventListResponse;
import com.croods.eventmanagement.model.ProductListResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventListAdapter extends BaseAdapter {
    private List<EventListResponse> eventList;
    private ArrayList<EventListResponse> arraylist;

    private Context ctx;
    private LayoutInflater inflater = null;

    public EventListAdapter(List<EventListResponse> eventList, Context ctx) {
        this.eventList = eventList;
        this.ctx = ctx;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arraylist = new ArrayList<>();

        arraylist.addAll(eventList);

    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
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
            view = inflater.inflate(R.layout.event_list_row, parent, false);

            holder.lbl_ejobcode = (TextView) view.findViewById(R.id.lbl_ejobcode);
            holder.lbl_ecname = (TextView) view.findViewById(R.id.lbl_ecname);
            holder.lbl_eworkdate = (TextView) view.findViewById(R.id.lbl_eworkdate);
            holder.lbl_edate = (TextView) view.findViewById(R.id.lbl_edate);
            holder.lbl_outquantity = (TextView) view.findViewById(R.id.lbl_outquantity);
            holder.lbl_inquantity = (TextView) view.findViewById(R.id.lbl_inquantity);
            holder.card_laptop = (CardView) view.findViewById(R.id.card_laptop);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.lbl_ejobcode.setText(eventList.get(position).getJobCode());
        holder.lbl_ecname.setText(eventList.get(position).getCompanyName());
        holder.lbl_edate.setText(eventList.get(position).getStartDate());
        holder.lbl_eworkdate.setText(eventList.get(position).getWorkStart());
        holder.lbl_inquantity.setText(" "+eventList.get(position).getInquantity()+" Qty");
        holder.lbl_outquantity.setText(" "+eventList.get(position).getOutquantity()+" Qty");

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        eventList.clear();
        if (charText.length() == 0) {
            eventList.addAll(arraylist);
        }
        else
        {
            for (EventListResponse wp : arraylist)
            {
                if (wp.getJobCode().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    eventList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView lbl_ecname, lbl_edate, lbl_eworkdate, lbl_ejobcode,lbl_inquantity,lbl_outquantity;
        CardView card_laptop;
    }
}
