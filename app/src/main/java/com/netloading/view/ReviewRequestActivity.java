package com.netloading.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.netloading.R;
import com.netloading.common.GenericActivity;
import com.netloading.presenter.ReviewRequestPresenter;
import com.netloading.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dandoh on 2/20/16.
 */
public class ReviewRequestActivity extends GenericActivity<ReviewRequestPresenter.View, ReviewRequestPresenter>
        implements ReviewRequestPresenter.View {

    @Bind(R.id.start_address)
    TextView mStartAddressTextView;

    @Bind(R.id.arrive_address)
    TextView mArriveAddressTextView;

    @Bind(R.id.pickup_date)
    TextView mPickupDateTextView;

    @Bind(R.id.goods_name)
    TextView mGoodsNameTextView;

    @Bind(R.id.goods_weight)
    TextView mGoodsWeightTextView;

    @Bind(R.id.expected_price)
    TextView mExpectedPriceTextView;

    private String startProvinceName;
    private String startDistrictName;
    private String arriveProvinceName;
    private String arriveDistrictName;
    private String pickUpDate;
    private String goodsName;
    private String goodsWeightNumber;
    private String goodsWeightDimension;
    private String expectedPrice;
    private String vehicleType;
    private int startDistrictCode;
    private int arriveDistrictCode;

    public static Intent makeIntent(Context context) {
        return new Intent(context, ReviewRequestActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.review_request_activity);
        ButterKnife.bind(this);


        super.onCreate(savedInstanceState, ReviewRequestPresenter.class, this);

        loadRequestInformation();
    }

    private void loadRequestInformation() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        startProvinceName = sharedPreferences.getString(Constants.TEN_TINH_DI, "");
        startDistrictName = sharedPreferences.getString(Constants.TEN_HUYEN_DI, "");
        arriveProvinceName = sharedPreferences.getString(Constants.TEN_TINH_DEN, "");
        arriveDistrictName = sharedPreferences.getString(Constants.TEN_HUYEN_DEN, "");
        pickUpDate = sharedPreferences.getString(Constants.GOODS_PICKUP_DATE, "");
        goodsName = sharedPreferences.getString(Constants.GOODS_NAME, "");
        goodsWeightNumber = sharedPreferences.getString(Constants.GOODS_WEIGHT_NUMBER, "");
        goodsWeightDimension = sharedPreferences.getString(Constants.GOODS_WEIGHT_DIMENSION, "");
        expectedPrice = sharedPreferences.getString(Constants.GOODS_EXPTECTED_PRICE, "");
        startDistrictCode = sharedPreferences.getInt(Constants.MA_HUYEN_DI, 0);
        arriveDistrictCode = sharedPreferences.getInt(Constants.MA_HUYEN_DEN, 0);
        vehicleType = sharedPreferences.getString(Constants.LOAI_XE, "");


        mStartAddressTextView.setText(startDistrictName + ", " + startProvinceName);
        mArriveAddressTextView.setText(arriveDistrictName + ", " + arriveProvinceName);
        mPickupDateTextView.setText(pickUpDate);
        mGoodsNameTextView.setText(goodsName);
        mGoodsWeightTextView.setText(goodsWeightNumber + " " + goodsWeightDimension);
        mExpectedPriceTextView.setText(expectedPrice);

    }


    @OnClick(R.id.send_request)
    void sendRequest() {
        getOps().sendRequest(pickUpDate, goodsWeightDimension, goodsWeightNumber,
                startDistrictCode, arriveDistrictCode, vehicleType, expectedPrice, goodsName);
    }
}
