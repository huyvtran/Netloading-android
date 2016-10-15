package com.ketnoivantai.companies.commons;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Dandoh on 4/5/16.
 */
public abstract class GenericCategoryListAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        int count = getNumOfCategory();
        for (int i = 0; i < getNumOfCategory(); i++) {
            count += getCategorySize(i);
        }

        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int categoryNo;
        int itemCount;

        // if the method is get header view of category
        categoryNo = 0;
        itemCount = 0;
        for (int i = 0; i < getNumOfCategory(); i++) {
            if (position == itemCount) {
                return getCategoryHeaderView(categoryNo, convertView, parent);
            } else {
                itemCount += getCategorySize(categoryNo) + 1;
                categoryNo++;
            }
        }

        // if the method is get item view
        int positionInCategory;
        categoryNo = 0;
        itemCount = 0;
        for (int i = 0; i < getNumOfCategory(); i++) {
            if (itemCount < position &&
                    itemCount + getCategorySize(categoryNo) >= position) {
                // in the current category
                positionInCategory = position - itemCount - 1;
                return getItemView(categoryNo, positionInCategory, convertView, parent);
            } else {
                itemCount += getCategorySize(categoryNo) + 1;
                categoryNo++;
            }
        }

        return null;
    }

    protected abstract View getItemView(int categoryNo, int positionInCategory, View convertView, ViewGroup parent);
    protected abstract View getCategoryHeaderView(int categoryNo, View convertView, ViewGroup parent);
    public abstract int getNumOfCategory();
    public abstract int getCategorySize(int categoryNo);


}
