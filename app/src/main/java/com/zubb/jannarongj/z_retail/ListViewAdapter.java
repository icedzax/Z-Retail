package com.zubb.jannarongj.z_retail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<VbelnName> vbelnNameList = null;
    private ArrayList<VbelnName> arraylist;


    public ListViewAdapter(Context context, List<VbelnName> animalNamesList) {
        mContext = context;
        this.vbelnNameList = animalNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<VbelnName>();
        this.arraylist.addAll(animalNamesList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return vbelnNameList.size();
    }

    @Override
    public VbelnName getItem(int position) {
        return vbelnNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();

            view = inflater.inflate(R.layout.listview_vbeln, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.vbeln);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(vbelnNameList.get(position).getAnimalName());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        vbelnNameList.clear();
        if (charText.length() == 0) {
            vbelnNameList.addAll(arraylist);
        } else {
            for (VbelnName wp : arraylist) {
                if (wp.getAnimalName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    vbelnNameList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}