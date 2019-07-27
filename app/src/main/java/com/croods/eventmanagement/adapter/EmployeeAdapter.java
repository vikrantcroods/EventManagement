package com.croods.eventmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.croods.eventmanagement.R;
import com.croods.eventmanagement.model.SpinnerResponse;

import java.util.ArrayList;

public class EmployeeAdapter extends ArrayAdapter<SpinnerResponse> {
    private Context mContext;
    private ArrayList<SpinnerResponse> listState;
    private EmployeeAdapter myAdapter;
    private boolean isFromView = false;

    public EmployeeAdapter(Context context, int resource, ArrayList<SpinnerResponse> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<SpinnerResponse>) objects;
        this.myAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getCompanyName());

       // holder.mCheckBox.setChecked(listState.get(position).isSelected());

        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                listState.get(position).setSelected(isChecked);

        });

        holder.mCheckBox.setTag(position);

        return convertView;
    }

    SpinnerResponse getProduct(int position) {
        return ((SpinnerResponse) getItem(position));
    }

    public ArrayList<SpinnerResponse> getBox() {
        ArrayList<SpinnerResponse> box = new ArrayList<>();
        for (SpinnerResponse p : listState) {
            if (p.isSelected())
                box.add(p);
        }
        return box;
    }

    private CompoundButton.OnCheckedChangeListener myCheckChangList = (buttonView, isChecked) ->
            isChecked = getProduct((Integer) buttonView.getTag()).isSelected();

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}
