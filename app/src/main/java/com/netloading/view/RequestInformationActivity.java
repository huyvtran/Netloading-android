package com.netloading.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netloading.R;
import com.netloading.common.GenericActivity;
import com.netloading.model.pojo.CompanyTripPOJO;
import com.netloading.model.pojo.RequestPOJO;
import com.netloading.presenter.RequestInformationPresenter;
import com.netloading.utils.Utils;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;

/**
 * Created by AnhVu on 2/26/16.
 */
public class RequestInformationActivity extends GenericActivity<RequestInformationPresenter.View, RequestInformationPresenter>
    implements RequestInformationPresenter.View{

    private static final String REQUEST_INFO_EXTRA = "request info";
    private static final String REQUEST_POSITION_EXTRA = "request pos";

    private RequestPOJO requestInfo;

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


    public static Intent makeIntent(Context context, RequestPOJO requestPOJO, int position) {
        return new Intent(context, RequestInformationActivity.class)
                .putExtra(REQUEST_INFO_EXTRA, requestPOJO).putExtra(REQUEST_POSITION_EXTRA, position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.request_information_activity);
        ButterKnife.bind(this);


        super.onCreate(savedInstanceState, RequestInformationPresenter.class, this);

        requestInfo = getIntent().getParcelableExtra(REQUEST_INFO_EXTRA);
        Utils.log(TAG, requestInfo.toString());

        updateRequestInformation(requestInfo);
    }

    @OnClick(R.id.delete_request_btn)
    void deleteRequest() {
        getOps().deleteRequest(requestInfo.getId());
    }

    @OnClick(R.id.retry_request_btn)
    void retryRequest() {
        getOps().retryRequest(requestInfo.getId());
    }


    private void updateRequestInformation(RequestPOJO requestInfo) {
        mRequestTitleTextView.setText("Yêu Cầu " + (requestInfo.getId()));

        mStartAddressTextView.setText(requestInfo.getStart_district_name() + ", " + requestInfo.getStart_province_name());
        mArriveTextView.setText(requestInfo.getArrive_district_name() + ", " + requestInfo.getArrive_province_name());
        handlePickupDate(requestInfo);
        handleStatus(requestInfo);
        mGoodNameTextView.setText(requestInfo.getGoods_name());
        String dimension = "kg";
        if (requestInfo.getVehicle_type().equals("xeBon")) dimension = "m³";
        mGoodWeightTextView.setText(
                requestInfo.getGoods_weight_number() + " " + dimension);

        mExpectedPriceTextView.setText(requestInfo.getExpected_price());
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
        finish();
//        Intent intent = RequestListActivity.makeIntent(this);
//        startActivity(intent);
    }

    @Override
    public void onError(int status) {
        if (status == STATUS_NETWORK_ERROR) {
            Utils.toast(this, "Lỗi đường truyền, vui lòng thử lại");
        } else {
            Utils.toast(this, "Đã có lỗi xảy ra, vui lòng thử lại");
        }
    }

    @Override
    public void onRetrySuccess(ArrayList<CompanyTripPOJO> companyTripPOJOs) {
        Intent intent = PickCompanyActivity.makeIntent(this, companyTripPOJOs, requestInfo.getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
