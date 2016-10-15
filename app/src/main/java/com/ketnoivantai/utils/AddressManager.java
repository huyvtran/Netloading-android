package com.ketnoivantai.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnhVu on 9/15/16.
 */
public class AddressManager {
    private static final String TAG = "AddressManager";
    private static AddressManager instance = null;
    private List<String> mProvinceCodeList = new ArrayList<String>();
    private List<String> mDistrictNameList = new ArrayList<String>();
    private List<String> mDistrictCodeList = new ArrayList<String>();
    private List<String> mProvinceCodeOfDistrictPosistionList = new ArrayList<String>();
    private List<String> mProvinceNameList = new ArrayList<String>();
    private int[] mProvincePosition = new int[105];
    private int[] mDistrictPosition = new int[1005];

    private Context mContext;

    protected AddressManager(Context context) {
        this.mContext = context;

        ReadFileFromAssetsAndAddToList(mProvinceNameList, "ten_tinh.txt");
        ReadFileFromAssetsAndAddToList(mDistrictNameList, "ten_huyen.txt");
        ReadFileFromAssetsAndAddToList(mProvinceCodeList, "ma_tinh.txt");
        ReadFileFromAssetsAndAddToList(mDistrictCodeList, "ma_huyen.txt");
        ReadFileFromAssetsAndAddToList(mProvinceCodeOfDistrictPosistionList, "ma_tinh_cua_huyen.txt");

        for (int i = 0; i < mProvinceCodeList.size() - 1; i++) {
            int value = Integer.parseInt(mProvinceCodeList.get(i));
            Utils.log(TAG, "" + value);
            mProvincePosition[value] = i;
        }

        for (int i = 0; i < mDistrictCodeList.size(); i++) {
            int value = Integer.parseInt(mDistrictCodeList.get(i));
            mDistrictPosition[value] = i;
        }

    }

    public static AddressManager getInstance(Context context) {
        if (instance == null){
            instance = new AddressManager(context);
        }
        return instance;
    }

    private void ReadFileFromAssetsAndAddToList(List list, String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open(fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            int dem = 0;
            while ((mLine = reader.readLine()) != null) {
                //process line
                list.add(mLine);
                dem++;
            }

        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                    e.printStackTrace();
                }
            }
        }
    }

    public String getProvinceFromCode(int code) {
        Utils.log(TAG, "district code: " + code);

        int districtPosition = mDistrictPosition[code];

        int provinceCode = Integer.parseInt(mProvinceCodeOfDistrictPosistionList.get(districtPosition));

        int provincePosition = mProvincePosition[provinceCode];

        Utils.log(TAG, "province code" + provinceCode);
        Utils.log(TAG, "province pos" + provincePosition);

        return mProvinceNameList.get(provincePosition);
    }

    public String getDistrictFromCode(int code) {
        int districtPosition = mDistrictPosition[code];
        return mDistrictNameList.get(districtPosition);
    }

    public List<String> getProvinceNameList() {
        return mProvinceNameList;
    }

    public int getProvincePositionFromCode(int code) {
        Utils.log(TAG, "district code: " + code);

        int districtPosition = mDistrictPosition[code];

        int provinceCode = Integer.parseInt(mProvinceCodeOfDistrictPosistionList.get(districtPosition));

        int provincePosition = mProvincePosition[provinceCode];

        Utils.log(TAG, provincePosition + " --> province position");

        return provincePosition;
    }

}
