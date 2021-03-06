package com.ketnoivantai.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ListView;

import com.ketnoivantai.R;
import com.ketnoivantai.common.GenericActivity;
import com.ketnoivantai.model.pojo.RequestPOJO;
import com.ketnoivantai.presenter.RequestListPresenter;
import com.ketnoivantai.utils.Utils;
import com.ketnoivantai.view.adapter.RequestListAdapter;

import java.util.ArrayList;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Danh sách yêu cầu");

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

    @Override
    protected void onResume() {
        showProgressDialog();
        getOps().getAllRequest();
        super.onResume();
    }

    private void showList(ArrayList<RequestPOJO> requestPOJOs) {

        for (RequestPOJO r:
             requestPOJOs) {
            Log.i(TAG, r.toString());
        }


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

        Intent intent = RequestInformationActivity.makeIntent(getApplicationContext(), requestPOJOs.get(position).getId(),
                RequestInformationActivity.STATE_FROM_REQ_LIST);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Utils.backToHome(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Utils.backToHome(this);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
