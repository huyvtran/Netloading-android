package com.netloading.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.netloading.R;
import com.netloading.common.ContextView;
import com.netloading.common.GenericActivity;
import com.netloading.model.pojo.RequestPOJO;
import com.netloading.presenter.RequestListPresenter;
import com.netloading.utils.Utils;
import com.netloading.view.adapter.RequestListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by AnhVu on 2/25/16.
 */
public class RequestListActivity extends GenericActivity<RequestListPresenter.View, RequestListPresenter>
    implements RequestListPresenter.View {

    private ProgressDialog mProgressDialog;
    private RequestListAdapter mRequestListAdapter;
    private ArrayList<RequestPOJO> requestPOJOs;

    @Bind(R.id.all_request_listview)
    ListView mAllRequestListView;

    public static Intent makeIntent(Context context) {
        return new Intent(context, RequestListActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.request_list_activity);

        ButterKnife.bind(this);

        super.onCreate(savedInstanceState, RequestListPresenter.class, this);

        mProgressDialog = new ProgressDialog(this);

        if (getRetainedFragmentManager().firstTimeIn()) {
            showProgressDialog();

            getOps().getAllRequest();
        }

        if (getOps().isProcessing()) {
            showProgressDialog();
        }
    }

    private void showList(ArrayList<RequestPOJO> requestPOJOs) {

        this.requestPOJOs = requestPOJOs;

        mRequestListAdapter = new RequestListAdapter(this, requestPOJOs);

        mAllRequestListView.setAdapter(mRequestListAdapter);
    }

    private void showProgressDialog() {
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("Đang xử lí");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Vui lòng đợi trong giây lát");
        mProgressDialog.show();
    }

    @OnItemClick(R.id.all_request_listview)
    void onAllRequestItemClick(int position) {

        Intent intent = RequestInformationActivity.makeIntent(getApplicationContext(), requestPOJOs.get(position), position);
        startActivity(intent);

    }

    @Override
    public void onError(int status) {
        mProgressDialog.dismiss();
    }

    @Override
    public void updateRequestList(ArrayList<RequestPOJO> requestPOJOs) {
        mProgressDialog.dismiss();

        Utils.log(TAG, requestPOJOs.toString());

        showList(requestPOJOs);
    }
}
