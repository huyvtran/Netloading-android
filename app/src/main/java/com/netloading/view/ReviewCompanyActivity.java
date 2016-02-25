package com.netloading.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.netloading.R;
import com.netloading.common.GenericActivity;
import com.netloading.model.pojo.CompanyPOJO;
import com.netloading.presenter.ReviewCompanyPresenter;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.internal.Util;

/**
 * Created by AnhVu on 2/25/16.
 */
public class ReviewCompanyActivity extends GenericActivity<ReviewCompanyPresenter.View, ReviewCompanyPresenter>
    implements ReviewCompanyPresenter.View {

    private static final String EXTRA_COMPANY_ID = "extra_company_id";
    private int mCompanyId;
    private ProgressDialog mProgressDialog;

    @Bind(R.id.company_address_tv)
    TextView mAddressTextView;

    @Bind(R.id.company_phone_tv)
    TextView mPhoneTextView;

    @Bind(R.id.company_fax_tv)
    TextView mFaxTextView;

    @Bind(R.id.company_email_tv)
    TextView mEmailTextView;

    @Bind(R.id.company_website_tv)
    TextView mWebsiteTextView;


    public static Intent makeIntent(Context context, int company_id) {
        return new Intent(context, ReviewCompanyActivity.class).putExtra(EXTRA_COMPANY_ID, company_id);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.review_company_activity);

        ButterKnife.bind(this);

        super.onCreate(savedInstanceState, ReviewCompanyPresenter.class, this);

        mCompanyId = getIntent().getIntExtra(EXTRA_COMPANY_ID, -1);

        mProgressDialog = new ProgressDialog(this);

        if (getRetainedFragmentManager().firstTimeIn()) {
            showProgressDialog();
            getOps().getCompanyInfo(mCompanyId);
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

    @Override
    public void onError(int status) {
    }

    @Override
    public void updateCompanyInfo(CompanyPOJO companyInfo) {

        mAddressTextView.setText(companyInfo.getAddress());
        mPhoneTextView.setText(companyInfo.getPhone());
        mFaxTextView.setText(companyInfo.getFax());
        mEmailTextView.setText(companyInfo.getEmail());
        mWebsiteTextView.setText(companyInfo.getWebsite());

        mProgressDialog.dismiss();


    }
}
