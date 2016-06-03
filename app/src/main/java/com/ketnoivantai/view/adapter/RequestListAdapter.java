package com.ketnoivantai.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ketnoivantai.R;
import com.ketnoivantai.model.pojo.RequestPOJO;
import com.ketnoivantai.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Dandoh on 2/25/16.
 */
public class RequestListAdapter extends BaseAdapter {

    private final Context context;
    private final List<RequestPOJO> requestPOJOs;

    public RequestListAdapter(Context context, List<RequestPOJO> requestPOJOs) {

        this.context = context;
        this.requestPOJOs = requestPOJOs;
    }

    @Override
    public int getCount() {
        return requestPOJOs.size();
    }

    @Override
    public Object getItem(int position) {
        return requestPOJOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.request_information_item, parent, false);
        }

        TextView requestStatus = (TextView) convertView.findViewById(R.id.request_status);
        TextView requestDate = (TextView) convertView.findViewById(R.id.request_date);

        int status = requestPOJOs.get(position).getStatus();

        String statusString = "Đang tìm nhà xe";
        if (status == 2) {
            statusString = "Đang chờ chấp nhận";
//            convertView.setBackgroundColor(Color.parseColor("#e7eccd"));
        } else if (status == 3) statusString = "Đã khớp lệnh";

        requestStatus.setText(statusString);



        String dtStart = requestPOJOs.get(position).getPickup_date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(dtStart);
            String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
            String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
            String day = (String) android.text.format.DateFormat.format("dd", date); //20


            String dateString = day + "/" + intMonth + "/" + year;
            requestDate.setText(dateString);

            Utils.log("DATE", dateString);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            requestDate.setText(dtStart);
            e.printStackTrace();
        }




        ((TextView)convertView.findViewById(R.id.request_name)).setText("Yêu cầu #" + requestPOJOs.get(position).getId());

        return convertView;
    }
}
