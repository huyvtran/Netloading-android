package com.netloading.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.netloading.R;
import com.netloading.common.GenericActivity;
import com.netloading.model.pojo.RequestPOJO;
import com.netloading.presenter.OrderInformationPresenter;
import com.netloading.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by AnhVu on 2/26/16.
 */
public class OrderInformationActivity extends GenericActivity<OrderInformationPresenter.View, OrderInformationPresenter>
    implements OrderInformationPresenter.View {


    private static final String REQUEST_INFO_EXTRA = "request info";
    private static final String ORDER_POSITION_EXTRA = "order position";
    private static final String COMPANY_NAME_EXTRA = "company name";
    private static final String ORDER_PRICE_EXTRA = "order price";
    private static final String ORDER_STATUS_EXTRA = "order status";

    public static Intent makeIntent(Context context, RequestPOJO requestPOJO, int position, String companyName,
                                    int orderPrice, int status) {
        return new Intent(context, OrderInformationActivity.class)
                .putExtra(REQUEST_INFO_EXTRA, requestPOJO).putExtra(ORDER_POSITION_EXTRA, position)
                .putExtra(COMPANY_NAME_EXTRA, companyName)
                .putExtra(ORDER_PRICE_EXTRA, orderPrice)
                .putExtra(ORDER_STATUS_EXTRA, status);
    }

    @Bind(R.id.order_title)
    TextView mOrderTitleTextView;

    @Bind(R.id.company_name_tv)
    TextView mCompanyNameTextView;

    @Bind(R.id.start_address_tv)
    TextView mStartAddressTextView;

    @Bind(R.id.arrive_address_tv)
    TextView mArriveAddressTextView;

    @Bind(R.id.order_status_tv)
    TextView mOrderStatusTextView;

    @Bind(R.id.good_name_tv)
    TextView mGoodNameTextView;

    @Bind(R.id.good_weight_tv)
    TextView mGoodWeightTextView;

    @Bind(R.id.order_price_tv)
    TextView mOrderPriceTextView;

    private RequestPOJO mRequestInfo;
    private int mOrderPosition;
    private String mCompanyName;
    private int mOrderPrice;
    private int mOrderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_information_activity);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Thông tin đơn hàng");

        super.onCreate(savedInstanceState, OrderInformationPresenter.class, this);

        mRequestInfo = getIntent().getParcelableExtra(REQUEST_INFO_EXTRA);
        mOrderPosition = getIntent().getIntExtra(ORDER_POSITION_EXTRA, 0);
        mCompanyName = getIntent().getStringExtra(COMPANY_NAME_EXTRA);
        mOrderPrice = getIntent().getIntExtra(ORDER_PRICE_EXTRA, -1);
        mOrderStatus = getIntent().getIntExtra(ORDER_STATUS_EXTRA, -1);

        Utils.log(TAG, mRequestInfo.toString());
        Utils.log(TAG, mOrderPosition + " order position");
        Utils.log(TAG, mCompanyName + " company name");
        Utils.log(TAG, mOrderPrice + " order price");
        Utils.log(TAG, mOrderStatus + " order status");

        updateOrderInformation();
    }

    private void updateOrderInformation() {
        mOrderTitleTextView.setText("Đơn hàng " + (mOrderPosition + 1));

        mCompanyNameTextView.setText(mCompanyName);
        mStartAddressTextView.setText(mRequestInfo.getStart_district_name() + ", " + mRequestInfo.getStart_province_name());
        mArriveAddressTextView.setText(mRequestInfo.getArrive_district_name() + ", " + mRequestInfo.getArrive_province_name());

        if (mOrderStatus == 1)
            mOrderStatusTextView.setText("Thành công");
        else mOrderStatusTextView.setText("Đã hủy đơn hàng");

        mGoodNameTextView.setText(mRequestInfo.getGoods_name());
        String dimension = "kg";
        if (mRequestInfo.getVehicle_type().equals("xeBon")) dimension = "m³";
        mGoodWeightTextView.setText(
                mRequestInfo.getGoods_weight_number() + " " + dimension);

        mOrderPriceTextView.setText(mOrderPrice + "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            backToOrderListActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            backToOrderListActivity();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backToOrderListActivity() {
        Intent intent = OrderListActivity.makeIntent(getApplicationContext());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
