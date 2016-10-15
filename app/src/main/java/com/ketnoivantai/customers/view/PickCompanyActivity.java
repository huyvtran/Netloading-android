package com.ketnoivantai.customers.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ketnoivantai.R;
import com.ketnoivantai.customers.common.GenericActivity;
import com.ketnoivantai.customers.model.pojo.CompanyTripPOJO;
import com.ketnoivantai.customers.model.pojo.RequestPOJO;
import com.ketnoivantai.customers.model.pojo.VehiclePOJO;
import com.ketnoivantai.customers.presenter.PickCompanyPresenter;
import com.ketnoivantai.utils.Utils;
import com.ketnoivantai.customers.view.adapter.CompanyListAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by Dandoh on 2/24/16.
 */
public class PickCompanyActivity extends GenericActivity<PickCompanyPresenter.View, PickCompanyPresenter>
        implements PickCompanyPresenter.View, SwipeRefreshLayout.OnRefreshListener {

    private static final String COMPANY_TRIP_POJO_EXTRA = "company pojo extra";
    private static final String REQUEST_ID_EXTRA = "request id extra";
    private int requestId;

    private CompanyListAdapter mCompanyListAdapter;

    private ProgressDialog mProgressDialog;

    @Bind(R.id.pick_company_list)
    ListView mCompanyListView;

    @Bind(R.id.company_not_found_layout)
    View mNotFoundLayout;

    @Bind(R.id.swipe_to_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.flipper)
    RelativeLayout mHeaderRelativeLayout;

    private ArrayList<CompanyTripPOJO> companyTripPOJOs;


    public static Intent makeIntent(Context context, int id) {
        Intent intent = new Intent(context, PickCompanyActivity.class)
                .putExtra(REQUEST_ID_EXTRA, id);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.customers_pick_company_activity);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Tìm chuyến xe");


        super.onCreate(savedInstanceState, PickCompanyPresenter.class, this);

        mProgressDialog = new ProgressDialog(this);
        if (getOps().isProcessing()) {
            showProgressDialog();
        }

        // request id
        requestId = getIntent().getIntExtra(REQUEST_ID_EXTRA, 0);


        showProgressDialog();
        getOps().retry(requestId);

        //swipe to refresh
        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    private void showProgressDialog() {
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("Đang xử lí");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Vui lòng đợi trong giây lát");
        mProgressDialog.show();
    }


    private void showList(ArrayList<CompanyTripPOJO> companyTripPOJOs) {
        this.companyTripPOJOs = companyTripPOJOs;

        Utils.log(TAG, companyTripPOJOs.size() + "");

        mNotFoundLayout.setVisibility(View.INVISIBLE);
        mCompanyListView.setVisibility(View.VISIBLE);
        mHeaderRelativeLayout.setVisibility(View.VISIBLE);

        mCompanyListAdapter = new CompanyListAdapter(this, companyTripPOJOs);

        Utils.log(TAG, mCompanyListAdapter.getCount() + " ListAdapterCount");

        mCompanyListView.setAdapter(mCompanyListAdapter);

    }

    @OnItemClick(R.id.pick_company_list)
    void onCompanyItemClick(int position) {
        int company_id = companyTripPOJOs.get(position).getCompany_id();
        int trip_id = companyTripPOJOs.get(position).getId();
        VehiclePOJO vehicle = companyTripPOJOs.get(position).getVehicle();
        Utils.log(TAG, "request_id: " + requestId);
        Utils.log(TAG, "trip_id : " + trip_id);

        Intent intent = ReviewCompanyActivity.makeIntent(
                getApplicationContext(), company_id, requestId, trip_id,
                vehicle
            );
        startActivity(intent);
    }

    @OnClick(R.id.delete_request_btn)
    void deleteRequest() {
        showProgressDialog();
        getOps().deleteRequest(requestId);
    }

    @OnClick(R.id.wait_other_btn)
    void onWaitOtherClick() {
        Intent intent = RequestListActivity.makeIntent(getApplicationContext());
        startActivity(intent);
        finish();
    }

    @Override
    public void onDeleteSuccess() {
        mProgressDialog.dismiss();
        Utils.toast(this, "Xoá yêu cầu vận tải thành công");

        Intent intent = PickLocationActivity.makeIntent(this, 1);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onError(int status) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (status == STATUS_NETWORK_ERROR) {
            Utils.toast(this, "Lỗi đường truyền, vui lòng thử lại");
        } else {
            Utils.toast(this, "Đã có lỗi xảy ra, vui lòng thử lại");
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRetrySuccess(ArrayList<CompanyTripPOJO> companyTripPOJOs) {

        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (companyTripPOJOs.size() > 0) {
            showList(companyTripPOJOs);
        } else {
            mNotFoundLayout.setVisibility(View.VISIBLE);
            mCompanyListView.setVisibility(View.INVISIBLE);
            mHeaderRelativeLayout.setVisibility(View.INVISIBLE);
        }
        mSwipeRefreshLayout.setRefreshing(false);


    }

    @Override
    public void onGetRequestDetailSuccess(RequestPOJO requestPOJO) {
        Intent intent = RequestInformationActivity.makeIntent(getApplicationContext(),
                requestPOJO.getId(), RequestInformationActivity.STATE_FROM_PICK_COMPANY);

        startActivity(intent);
    }


    @Override
    public void onRefresh() {
        Utils.log(TAG, "On refresh");
        mNotFoundLayout.setVisibility(View.INVISIBLE);
        getOps().retry(requestId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Utils.backToHome(this);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
//            Utils.toast(this, "setting");

            getOps().getRequestInfo(requestId);

        } else if (id == android.R.id.home) {
            Utils.backToHome(this);
//            Utils.toast(this, "back");
        }

        return super.onOptionsItemSelected(item);
    }
}
