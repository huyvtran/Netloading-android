package com.ketnoivantai.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.ketnoivantai.R;
import com.ketnoivantai.common.GenericActivity;
import com.ketnoivantai.model.pojo.CompanyPOJO;
import com.ketnoivantai.model.pojo.VehiclePOJO;
import com.ketnoivantai.presenter.ReviewCompanyPresenter;
import com.ketnoivantai.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;

/**
 * Created by AnhVu on 2/25/16.
 */
public class ReviewCompanyActivity extends GenericActivity<ReviewCompanyPresenter.View, ReviewCompanyPresenter>
    implements ReviewCompanyPresenter.View {

    private static final String EXTRA_COMPANY_ID = "extra_company_id";
    private static final String EXTRA_REQUEST_ID = "extra request id";
    private static final String EXTRA_TRIP_ID = "extra trip id";
    private static final String EXTRA_VEHICLE = "extra vehicle";
    private int mCompanyId, mRequestId, mTripId;
    private VehiclePOJO mVehicle;
    private ProgressDialog mProgressDialog;

    @Bind(R.id.company_address_tv)
    TextView mAddressTextView;

    @Bind(R.id.vehicle_type_tv)
    TextView mVehicleTypeTextView;

    @Bind(R.id.vehicle_capacity_tv)
    TextView mVehicleCapacityView;

    @Bind(R.id.vehicle_info_tv)
    TextView mVehicleInfoTextView;

    @Bind(R.id.company_name_tv)
    TextView mCompanyNameTextView;

    @Bind(R.id.accept_trip_btn)
    TextView mAcceptTripButton;


    public static Intent makeIntent(Context context, int company_id, int request_id, int trip_id, VehiclePOJO vehicle) {
        return new Intent(context, ReviewCompanyActivity.class).putExtra(EXTRA_COMPANY_ID, company_id)
                .putExtra(EXTRA_REQUEST_ID, request_id).putExtra(EXTRA_TRIP_ID, trip_id)
                .putExtra(EXTRA_VEHICLE, vehicle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.review_company_activity);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Thông tin nhà xe");

        super.onCreate(savedInstanceState, ReviewCompanyPresenter.class, this);

        mCompanyId = getIntent().getIntExtra(EXTRA_COMPANY_ID, -1);
        mRequestId = getIntent().getIntExtra(EXTRA_REQUEST_ID, -1);
        mTripId = getIntent().getIntExtra(EXTRA_TRIP_ID, -1);
        mVehicle = getIntent().getParcelableExtra(EXTRA_VEHICLE);

        mProgressDialog = new ProgressDialog(this);

        if (getRetainedFragmentManager().firstTimeIn()) {
            showProgressDialog();
            getOps().getCompanyInfo(mCompanyId);
        }

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

    @Override
    public void onError(int status) {
        mProgressDialog.dismiss();
        Utils.toast(getApplicationContext(), "Đã có lỗi xảy ra");

    }

    @OnClick(R.id.accept_trip_btn)
    public void onAcceptTripClick() {
        getOps().acceptTrip(mRequestId, mTripId);
    }

    @Override
    public void updateCompanyInfo(CompanyPOJO companyInfo) {

        mAddressTextView.setText(companyInfo.getAddress());
        mVehicleTypeTextView.setText(mVehicle.getVehicle_type());
        mVehicleCapacityView.setText(mVehicle.getCapacity_number() + "");
        mVehicleInfoTextView.setText(mVehicle.getInformation());
        mCompanyNameTextView.setText(companyInfo.getName());

        mProgressDialog.dismiss();
    }

    @Override
    public void handleAcceptTripDone() {
        Utils.toast(getApplicationContext(), "Yêu cầu của bạn đã được gửi cho " + mCompanyNameTextView.getText().toString()
        + ". Vui lòng đợi phản hồi từ công ty.");

        finish();
        Intent intent = RequestListActivity.makeIntent(getApplicationContext());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
