package com.netloading.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.netloading.R;
import com.netloading.common.GenericActivity;
import com.netloading.model.pojo.OrderPOJO;
import com.netloading.model.pojo.RequestPOJO;
import com.netloading.presenter.OrderListPresenter;
import com.netloading.view.adapter.OrderListAdapter;
import com.netloading.view.adapter.RequestListAdapter;

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

        super.onCreate(savedInstanceState, OrderListPresenter.class, this);


        getOps().updateOrderInformation();

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

    }

    @Override
    public void updateOrderList(ArrayList<OrderPOJO> orderPOJOs) {
        showList(orderPOJOs);
    }
}
