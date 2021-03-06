package com.ketnoivantai.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.ketnoivantai.R;
import com.ketnoivantai.common.GenericActivity;
import com.ketnoivantai.model.webservice.ServiceGenerator;
import com.ketnoivantai.presenter.ReviewRequestPresenter;
import com.ketnoivantai.utils.Constants;
import com.ketnoivantai.utils.Utils;

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

    @Bind(R.id.goods_description)
    TextView mGoodsDescriptionTextView;


    private ProgressDialog mProgressDialog;

    private String startProvinceName;
    private String startDistrictName;
    private String arriveProvinceName;
    private String arriveDistrictName;
    private String pickUpDate;
    private String goodsName;
    private int goodsWeightNumber;
    private String goodsWeightDimension;
    private String goodsDescription;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Xem lại yêu cầu");

        super.onCreate(savedInstanceState, ReviewRequestPresenter.class, this);

        loadRequestInformation();
        mProgressDialog = new ProgressDialog(this);

        if (getOps().isProcessing()) {
            showProgressDialog();
        }
    }

    private void showProgressDialog() {
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("Đang xử lí");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Vui lòng đợi trong giây lát");
        mProgressDialog.show();
    }

    private void loadRequestInformation() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        startProvinceName = sharedPreferences.getString(Constants.START_PROVINCE_NAME, "");
        startDistrictName = sharedPreferences.getString(Constants.START_DISTRICT_NAME, "");
        arriveProvinceName = sharedPreferences.getString(Constants.ARRIVE_PROVINCE_NAME, "");
        arriveDistrictName = sharedPreferences.getString(Constants.ARRIVE_DISTRICT_NAME, "");
        pickUpDate = sharedPreferences.getString(Constants.GOODS_PICKUP_DATE, "");
        goodsName = sharedPreferences.getString(Constants.GOODS_NAME, "");
        goodsWeightNumber = sharedPreferences.getInt(Constants.GOODS_WEIGHT_NUMBER, 0);
        goodsWeightDimension = sharedPreferences.getString(Constants.GOODS_WEIGHT_DIMENSION, "");
        goodsDescription = sharedPreferences.getString(Constants.GOODS_DESCRIPTION, "");
        startDistrictCode = sharedPreferences.getInt(Constants.START_DISTRICT_CODE, 0);
        arriveDistrictCode = sharedPreferences.getInt(Constants.ARRIVE_DISTRICT_CODE, 0);
        vehicleType = sharedPreferences.getString(Constants.VEHICLE_TYPE, "");


        mStartAddressTextView.setText(startDistrictName + ", " + startProvinceName);
        mArriveAddressTextView.setText(arriveDistrictName + ", " + arriveProvinceName);
        mPickupDateTextView.setText(pickUpDate);
        mGoodsNameTextView.setText(goodsName);

//        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
//        otherSymbols.setDecimalSeparator(',');
//        otherSymbols.setGroupingSeparator('.');
//        DecimalFormat df = new DecimalFormat("#,###.##", otherSymbols);

//        String goodsWeightNumberString = df.format(goodsWeightNumber);

        mGoodsWeightTextView.setText(Utils.formatNumber(goodsWeightNumber) + " " + goodsWeightDimension);
        mGoodsDescriptionTextView.setText(goodsDescription);

    }


    @OnClick(R.id.send_request)
    void sendRequest() {
        Log.d(TAG, "press send_request button");
        new AlertDialog.Builder(this)
                .setTitle("Gửi yêu cầu vận tải")
                .setMessage("Bạn có chắc chắn muốn gửi yêu cầu này?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.log(TAG, "press dong y button");
                        Log.v(TAG, "den day roi");

                        String token = ServiceGenerator.getAccessToken();

                        Utils.log(TAG, token + "");

                        showProgressDialog();

                        // TODO -- check whether is logged in or not, if not, start login activity clear task | new task
                        if (ServiceGenerator.isLoggedIn()) {

                            getOps().sendRequest(pickUpDate, goodsWeightDimension, goodsWeightNumber,
                                    startDistrictCode, arriveDistrictCode, vehicleType,
                                    goodsDescription, goodsName,
                                    startProvinceName, arriveProvinceName,
                                    startDistrictName, arriveDistrictName);
                        } else {

                            Intent intent = LoginActivity.makeIntent(getApplicationContext(), LoginActivity.LOGIN_FIRST_TIME_CREATE_REQUEST)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_menu_send)
                .show();

    }

    @Override
    public void onError(int status) {
        mProgressDialog.dismiss();

        if (status == STATUS_ERROR_NETWORK) {
            Utils.toast(this, "Lỗi đường truyền, vui lòng thử lại");
        }
    }

    @Override
    public void onRequestResult(int requestId) {
        mProgressDialog.dismiss();
        String token = ServiceGenerator.getAccessToken();
        Utils.log(TAG, token);
        Intent intent = PickCompanyActivity.makeIntent(this, requestId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.modify_request)
    void onModifyClick() {
        Utils.log(TAG, "press mod button");
        backToAddInfoActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            backToAddInfoActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backToAddInfoActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backToAddInfoActivity() {
        finish();
        Intent intent = AddInfoRequestActivity.makeIntent(this, AddInfoRequestActivity.STATE_FROM_REVIEW)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


}
