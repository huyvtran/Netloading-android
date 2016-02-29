package com.ketnoivantai.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ListView;

import com.ketnoivantai.R;
import com.ketnoivantai.common.GenericActivity;
import com.ketnoivantai.model.pojo.OrderPOJO;
import com.ketnoivantai.presenter.OrderListPresenter;
import com.ketnoivantai.utils.Utils;
import com.ketnoivantai.view.adapter.OrderListAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by AnhVu on 2/26/16.
 */
public class OrderListActivity extends GenericActivity<OrderListPresenter.View, OrderListPresenter>
    implements OrderListPresenter.View {

    private ArrayList<OrderPOJO> orderPOJOs;
    private OrderListAdapter mOrderListAdapter;
    private ProgressDialog mProgressDialog;

    public static Intent makeIntent(Context context) {
        return new Intent(context, OrderListActivity.class);
    }

    @Bind(R.id.all_order_listview)
    ListView mAllOrderListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_list_activity);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Danh sách đơn hàng");

        super.onCreate(savedInstanceState, OrderListPresenter.class, this);

        mProgressDialog = new ProgressDialog(this);

        if (getRetainedFragmentManager().firstTimeIn()) {
            showProgressDialog();

            getOps().updateOrderInformation();
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

    private void showList(ArrayList<OrderPOJO> orders) {

        this.orderPOJOs = orders;

        mOrderListAdapter = new OrderListAdapter(this, orders);

        mAllOrderListView.setAdapter(mOrderListAdapter);
    }

    @OnItemClick(R.id.all_order_listview)
    void onAllOrderItemClick(int position) {
        Intent intent = OrderInformationActivity
                .makeIntent(getApplicationContext(), orderPOJOs.get(position).getRequest(),
                        position, orderPOJOs.get(position).getCompany_name(),
                        orderPOJOs.get(position).getPrice(),
                        orderPOJOs.get(position).getOrder().getStatus());

        startActivity(intent);
    }


    @Override
    public void onError(int statusUnhandledError) {
        mProgressDialog.dismiss();
    }

    @Override
    public void updateOrderList(ArrayList<OrderPOJO> orderPOJOs) {
        showList(orderPOJOs);
        mProgressDialog.dismiss();
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
