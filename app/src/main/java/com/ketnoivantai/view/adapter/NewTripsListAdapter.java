package com.ketnoivantai.view.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ketnoivantai.R;
import com.ketnoivantai.model.pojo.NewTripPOJO;
import com.ketnoivantai.utils.AddressManager;
import com.ketnoivantai.utils.Utils;

import java.util.List;

import okhttp3.internal.Util;

/**
 * Created by AnhVu on 9/15/16.
 */
public class NewTripsListAdapter extends BaseAdapter{

    private static final String TAG = "NewTripsListAdapter";
    private Context context;
    private List<NewTripPOJO> newTripPOJOs;

    public NewTripsListAdapter(Context context, List<NewTripPOJO> newTripPOJOs) {
        this.context = context;
        this.newTripPOJOs = newTripPOJOs;
    }


    @Override
    public int getCount() {
        return newTripPOJOs.size();
    }

    @Override
    public Object getItem(int position) {
        return newTripPOJOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.new_trips_item, parent, false);
        }

        TextView routeTextView = (TextView) convertView.findViewById(R.id.route_tv);
        TextView startDateTextView = (TextView) convertView.findViewById(R.id.start_date_tv);
        TextView vehicleTypeTextView = (TextView) convertView.findViewById(R.id.vehicle_type_tv);
        TextView capacityTextView = (TextView) convertView.findViewById(R.id.vehicle_capacity_tv);
        TextView pricePerUnitTextView = (TextView) convertView.findViewById(R.id.price_per_unit_tv);
        ImageView mVehicleImageView = (ImageView) convertView.findViewById(R.id.vehicle_icon_iv);

        NewTripPOJO item = newTripPOJOs.get(position);

        fillRouteTextView(routeTextView, item);
        fillStartDateTextView(startDateTextView, item);
        fillVehicleTypeTextView(vehicleTypeTextView, item);
        fillCapacityTextView(capacityTextView, item);
        fillPricePerUnitTextView(pricePerUnitTextView, item);

        // TODO: 9/15/16 - Đặt ảnh logo tương ứng với kiểu xe

        if (item.getVehicle_type().equals("xeTai")) {
            mVehicleImageView.setImageResource(R.drawable.icon_xetai_blue);
        } else
        if (item.getVehicle_type().equals("xeBon")) {
            mVehicleImageView.setImageResource(R.drawable.icon_xebon_blue);
        } else
        if (item.getVehicle_type().equals("xeDongLanh")) {
            mVehicleImageView.setImageResource(R.drawable.icon_xedonglanh_blue);
        }

        return convertView;
    }


    private void fillPricePerUnitTextView(TextView pricePerUnitTextView, NewTripPOJO item) {
        String dimension = Utils.ConvertToDimension(item.getVehicle_type()).second;
        int pricePerUnit = item.getPrice_per_unit();

        pricePerUnitTextView.setText(Html.fromHtml("Giá trên đơn vị: " + Utils.formatNumber(pricePerUnit) + " VNĐ/" + dimension));
    }

    private void fillVehicleTypeTextView(TextView vehicleTypeTextView, NewTripPOJO item) {
        vehicleTypeTextView.setText(Html.fromHtml("Loại xe: " + Utils.nameFromVehicleTypeString(item.getVehicle_type())));
    }

    private void fillCapacityTextView(TextView capacityTextView, NewTripPOJO item) {

        Pair<String, String> pair = Utils.ConvertToDimension(item.getVehicle_type());

        capacityTextView.setText(Html.fromHtml("Tải trọng: " + Utils.formatNumber(item.getCapacity_number()) + " " + pair.second));

    }

    private void fillStartDateTextView(TextView startDateTextView, NewTripPOJO item) {
        startDateTextView.setText(Html.fromHtml("Ngày đi: " + item.getStart_date()));
    }

    private void fillRouteTextView(TextView routeTextView, NewTripPOJO item) {

        int startAddress = Integer.parseInt(item.getStart_address());
        int arriveAddress = Integer.parseInt(item.getArrive_address());

        Utils.log(TAG, startAddress + " " + arriveAddress);

        String startProvinceName = AddressManager.getInstance(context).getProvinceFromCode(startAddress);
        String arriveProvinceName = AddressManager.getInstance(context).getProvinceFromCode(arriveAddress);

        routeTextView.setText(Html.fromHtml("<b>Lộ trình: </b>" + startProvinceName + " --> " + arriveProvinceName));
    }
}
