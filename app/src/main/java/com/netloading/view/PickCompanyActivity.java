package com.netloading.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.netloading.R;
import com.netloading.common.GenericActivity;
import com.netloading.model.pojo.CompanyTripPOJO;
import com.netloading.presenter.PickCompanyPresenter;
import com.netloading.utils.Utils;
import com.netloading.view.adapter.CompanyListAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;

/**
 * Created by Dandoh on 2/24/16.
 */
public class PickCompanyActivity extends GenericActivity<PickCompanyPresenter.View, PickCompanyPresenter>
        implements PickCompanyPresenter.View, SwipeRefreshLayout.OnRefreshListener {

    private static final String COMPANY_POJO_EXTRA = "company pojo extra";
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

    private ArrayList<CompanyTripPOJO> companyPOJOs;


    public static Intent makeIntent(Context context, ArrayList<CompanyTripPOJO> companyPOJOList, int id) {
        Intent intent = new Intent(context, PickCompanyActivity.class)
                .putParcelableArrayListExtra(COMPANY_POJO_EXTRA, companyPOJOList)
                .putExtra(REQUEST_ID_EXTRA, id);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.pick_company_activity);
        ButterKnife.bind(this);

        super.onCreate(savedInstanceState, PickCompanyPresenter.class, this);

        mProgressDialog = new ProgressDialog(this);
        if (getOps().isProcessing()) {
            showProgressDialog();
        }

        // Get input
        // company list
        ArrayList<CompanyTripPOJO> companyPOJOs = getIntent().getParcelableArrayListExtra(COMPANY_POJO_EXTRA);
        if (companyPOJOs.size() > 0) {
            showList(companyPOJOs);
        } else {
            mNotFoundLayout.setVisibility(View.VISIBLE);
            mCompanyListView.setVisibility(View.INVISIBLE);
        }

        // request id
        requestId = getIntent().getIntExtra(REQUEST_ID_EXTRA, 0);


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


    private void showList(ArrayList<CompanyTripPOJO> companyPOJOs) {
        this.companyPOJOs = companyPOJOs;

        mNotFoundLayout.setVisibility(View.INVISIBLE);
        mCompanyListView.setVisibility(View.VISIBLE);
        mCompanyListAdapter = new CompanyListAdapter(this, companyPOJOs);
        mCompanyListView.setAdapter(mCompanyListAdapter);

    }

    @OnItemClick(R.id.pick_company_list)
    void onCompanyItemClick(int position) {
        int company_id = companyPOJOs.get(position).getCompany_id();

        Intent intent = ReviewCompanyActivity.makeIntent(getApplicationContext(), company_id);
        startActivity(intent);
    }

    @OnClick(R.id.delete_request_btn)
    void deleteRequest() {
        showProgressDialog();
        getOps().deleteRequest(requestId);
    }

    @Override
    public void onDeleteSuccess() {
        mProgressDialog.dismiss();
        Utils.toast(this, "Xoá yêu cầu vận tải thành công");

        Intent intent = PickLocationActivity.makeIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onError(int status) {
        mProgressDialog.dismiss();
        if (status == STATUS_NETWORK_ERROR) {
            Utils.toast(this, "Lỗi đường truyền, vui lòng thử lại");
        } else {
            Utils.toast(this, "Đã có lỗi xảy ra, vui lòng thử lại");
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRetrySuccess(ArrayList<CompanyTripPOJO> companyPOJOs) {

        if (companyPOJOs.size() > 0) {
            showList(companyPOJOs);
        } else {
            mNotFoundLayout.setVisibility(View.VISIBLE);
            mCompanyListView.setVisibility(View.INVISIBLE);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        Utils.log(TAG, "On refresh");

        getOps().retry(requestId);

    }
}
