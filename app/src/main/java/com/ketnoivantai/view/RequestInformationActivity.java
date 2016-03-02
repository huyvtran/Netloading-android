package com.ketnoivantai.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ketnoivantai.R;
import com.ketnoivantai.common.GenericActivity;
import com.ketnoivantai.model.pojo.RequestPOJO;
import com.ketnoivantai.presenter.RequestInformationPresenter;
import com.ketnoivantai.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AnhVu on 2/26/16.
 */
public class RequestInformationActivity extends GenericActivity<RequestInformationPresenter.View, RequestInformationPresenter>
    implements RequestInformationPresenter.View{

    private static final String REQUEST_INFO_EXTRA = "request info";
    private static final String REQUEST_ID_EXTRA = "request pos";
    private static final String REQUEST_STATE_EXTRA = "request state";
    public static final int STATE_FROM_PICK_COMPANY = 179;
    public static final int STATE_FROM_GCM = 210;
    public static final int STATE_FROM_REQ_LIST = 321;

    private int mRequestId;
    private int mState;

    @Bind(R.id.start_address_tv)
    TextView mStartAddressTextView;

    @Bind(R.id.arrive_address_tv)
    TextView mArriveTextView;

    @Bind(R.id.pickup_date_tv)
    TextView mPickupDateTextView;

    @Bind(R.id.status_tv)
    TextView mStatusTextView;

    @Bind(R.id.good_name_tv)
    TextView mGoodNameTextView;

    @Bind(R.id.good_weight_tv)
    TextView mGoodWeightTextView;

    @Bind(R.id.expected_price_tv)
    TextView mExpectedPriceTextView;

    @Bind(R.id.request_title)
    TextView mRequestTitleTextView;

    @Bind(R.id.delete_request_btn)
    Button mDeleteRequestButton;

    private ProgressDialog mProgressDialog;


    public static Intent makeIntent(Context context, int id, int state) {
        return new Intent(context, RequestInformationActivity.class)
                .putExtra(REQUEST_ID_EXTRA, id)
                .putExtra(REQUEST_STATE_EXTRA, state);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.request_information_activity);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Thông tin yêu cầu");

        mRequestId = getIntent().getIntExtra(REQUEST_ID_EXTRA, -1);
        mState = getIntent().getIntExtra(REQUEST_STATE_EXTRA, STATE_FROM_REQ_LIST);

        super.onCreate(savedInstanceState, RequestInformationPresenter.class, this);

        mProgressDialog = new ProgressDialog(this);

        if (getRetainedFragmentManager().firstTimeIn()) {
            showProgressDialog();
            getOps().getRequestInfo(mRequestId);
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

    @OnClick(R.id.delete_request_btn)
    void deleteRequest() {
        getOps().deleteRequest(mRequestId);
    }

    @OnClick(R.id.retry_request_btn)
    void retryRequest() {
        Intent intent = PickCompanyActivity.makeIntent(this, mRequestId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void updateRequestInformation(RequestPOJO requestInfo) {
        mProgressDialog.dismiss();
        mRequestTitleTextView.setText("Yêu Cầu " + (requestInfo.getId()));

        mStartAddressTextView.setText(requestInfo.getStart_district_name() + ", " + requestInfo.getStart_province_name());
        mArriveTextView.setText(requestInfo.getArrive_district_name() + ", " + requestInfo.getArrive_province_name());
        handlePickupDate(requestInfo);
        handleStatus(requestInfo);
        mGoodNameTextView.setText(requestInfo.getGoods_name());
        String dimension = "kg";
        if (requestInfo.getVehicle_type().equals("xeBon")) dimension = "m³";
        mGoodWeightTextView.setText(
                Utils.formatNumber(requestInfo.getGoods_weight_number()) + " " + dimension);

        mExpectedPriceTextView.setText(Utils.formatNumber(Integer.parseInt(requestInfo.getExpected_price())) + " VNĐ");
    }

    private void handleStatus(RequestPOJO requestInfo) {
        Utils.log(TAG, requestInfo.getStatus() + " status");

        int status = requestInfo.getStatus();
        String statusString = "Đang tìm nhà xe";
        ((LinearLayout)findViewById(R.id.footer)).setVisibility(View.VISIBLE);
        if (status == 2) statusString = "Đang chờ chấp nhận";
        else if (status == 3) {
            statusString = "Đã khớp lệnh";
            ((LinearLayout)findViewById(R.id.footer)).setVisibility(View.INVISIBLE);
        }

        mStatusTextView.setText(statusString);
    }

    private void handlePickupDate(RequestPOJO requestInfo) {
        String dtStart = requestInfo.getPickup_date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        try {
            Date date = format.parse(dtStart);
            String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
            String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
            String day = (String) android.text.format.DateFormat.format("dd", date); //20
            String dateString = day + "/" + intMonth + "/" + year;

            mPickupDateTextView.setText(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDeleteSuccess() {
        Utils.toast(this, "Xoá yêu cầu vận tải thành công");
        backToRequestListActivity();
//        Intent intent = RequestListActivity.makeIntent(this);
//        startActivity(intent);
    }

    @Override
    public void onError(int status) {
        mProgressDialog.dismiss();
        if (status == STATUS_NETWORK_ERROR) {
            Utils.toast(this, "Lỗi đường truyền, vui lòng thử lại");
        } else {
            Utils.toast(this, "Đã có lỗi xảy ra, vui lòng thử lại");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            backToRequestListActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void backToRequestListActivity() {
        if (mState != STATE_FROM_PICK_COMPANY) {
            Intent intent = RequestListActivity.makeIntent(getApplicationContext());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            backToRequestListActivity();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
