package com.netloading.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.netloading.R;
import com.netloading.model.pojo.AcceptTripPOJO;
import com.netloading.model.pojo.OrderPOJO;
import com.netloading.model.pojo.RequestPOJO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by AnhVu on 2/26/16.
 */
public class OrderListAdapter extends BaseAdapter {

    private final Context context;
    private List<OrderPOJO> orders;

    public OrderListAdapter(Context context, List<OrderPOJO> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.order_information_item, parent, false);
        }

        TextView mOrderNameTextureView = (TextView) convertView.findViewById(R.id.order_name);
        TextView mCompanyNameTextView = (TextView) convertView.findViewById(R.id.company_name_tv);
        TextView mPickupDateTextView = (TextView) convertView.findViewById(R.id.pickup_date_tv);


        mOrderNameTextureView.setText("Đơn hàng " + (position + 1));

        //TODO - set the company_name instead of good_name
        mCompanyNameTextView.setText(orders.get(position).getCompany_name());

        handlePickupDate(orders.get(position).getRequest(), mPickupDateTextView);

        return convertView;
    }

    private void handlePickupDate(RequestPOJO requestInfo, TextView tv) {
        String dtStart = requestInfo.getPickup_date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        try {
            Date date = format.parse(dtStart);
            String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
            String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
            String day = (String) android.text.format.DateFormat.format("dd", date); //20
            String dateString = day + "/" + intMonth + "/" + year;

            tv.setText(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
