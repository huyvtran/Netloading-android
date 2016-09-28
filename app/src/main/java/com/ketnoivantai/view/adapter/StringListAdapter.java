package com.ketnoivantai.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ketnoivantai.R;

import java.util.List;

import static android.R.id.list;

/**
 * Created by AnhVu on 9/28/16.
 */

public class StringListAdapter extends BaseAdapter {

    private List<String> mList;
    private Context context;

    public StringListAdapter(Context c, List<String> list) {
        this.mList = list;
        this.context = c;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.spinner_item, parent, false);
        }

        String item = mList.get(position);
        TextView tv = (TextView) convertView.findViewById(R.id.vText);
        tv.setText(item);
        return convertView;
    }
}
