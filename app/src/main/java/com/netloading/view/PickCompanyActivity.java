package com.netloading.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.netloading.R;
import com.netloading.common.GenericActivity;
import com.netloading.model.pojo.CompanyPOJO;
import com.netloading.model.pojo.RequestPOJO;
import com.netloading.presenter.PickCompanyPresenter;
import com.netloading.utils.Utils;
import com.netloading.view.adapter.CompanyListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dandoh on 2/24/16.
 */
public class PickCompanyActivity extends GenericActivity<PickCompanyPresenter.View, PickCompanyPresenter>
        implements PickCompanyPresenter.View {

    private static final String COMPANY_POJO_EXTRA = "company pojo extra";

    private CompanyListAdapter mCompanyListAdapter;

    @Bind(R.id.pick_company_list)
    ListView mCompanyListView;

    @Bind(R.id.company_not_found_layout)
    View mNotFoundLayout;


    public static Intent makeIntent(Context context, ArrayList<CompanyPOJO> companyPOJOList) {
        Intent intent = new Intent(context, PickCompanyActivity.class)
                .putParcelableArrayListExtra(COMPANY_POJO_EXTRA, companyPOJOList);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.pick_company_activity);
        ButterKnife.bind(this);

        super.onCreate(savedInstanceState, PickCompanyPresenter.class, this);

        ArrayList<CompanyPOJO> companyPOJOs = getIntent().getParcelableArrayListExtra(COMPANY_POJO_EXTRA);
        if (companyPOJOs.size() > 0) {
            showList(companyPOJOs);
        } else {
            mNotFoundLayout.setVisibility(View.VISIBLE);
            mCompanyListView.setVisibility(View.INVISIBLE);
        }

    }


    private void showList(ArrayList<CompanyPOJO> companyPOJOs) {
        mNotFoundLayout.setVisibility(View.INVISIBLE);
        mCompanyListView.setVisibility(View.VISIBLE);
        mCompanyListAdapter = new CompanyListAdapter(this, companyPOJOs);
        mCompanyListView.setAdapter(mCompanyListAdapter);

    }



//    @OnClick(R.id.delete_request_btn)
//    private void deleteRequest() {
//        getOps().
//    }




}
