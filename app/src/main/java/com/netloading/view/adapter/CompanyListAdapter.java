package com.netloading.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.netloading.R;
import com.netloading.model.pojo.CompanyPOJO;
import com.netloading.model.pojo.RequestPOJO;

import java.util.List;

/**
 * Created by Dandoh on 2/24/16.
 */
public class CompanyListAdapter extends BaseAdapter {


    private Context context;
    private List<CompanyPOJO> companyPOJOs;

    public CompanyListAdapter(Context context, List<CompanyPOJO> companyPOJOs) {
        this.context = context;
        this.companyPOJOs = companyPOJOs;
    }

    @Override
    public int getCount() {
        return companyPOJOs.size();

    }

    @Override
    public Object getItem(int position) {
        return companyPOJOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.company_item, parent, false);
        }

//        TextView companyName = companyPOJOs.get(position).getName();
        TextView nameTextView = (TextView) convertView.findViewById(R.id.company_name_tv);
        TextView priceTextView = (TextView) convertView.findViewById(R.id.money_number_tv);

        nameTextView.setText(companyPOJOs.get(position).getName());
        priceTextView.setText(String.valueOf(companyPOJOs.get(position).getPrice()));


        return convertView;
    }
}
