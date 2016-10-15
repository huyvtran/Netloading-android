package com.ketnoivantai.customers.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.ketnoivantai.R;
import com.ketnoivantai.customers.common.GenericActivity;
import com.ketnoivantai.customers.model.pojo.ProfilePOJO;
import com.ketnoivantai.customers.presenter.ProfilePresenter;
import com.ketnoivantai.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AnhVu on 3/12/16.
 */
public class ProfileActivity extends GenericActivity<ProfilePresenter.View, ProfilePresenter>
        implements ProfilePresenter.View {

    private ProgressDialog mProgressDialog;

    @Bind(R.id.user_name)
    TextView mUsernameTextView;

    @Bind(R.id.name)
    TextView mNameTextView;

    @Bind(R.id.phone)
    TextView mPhoneTextView;

    @Bind(R.id.email)
    TextView mEmailTextView;

    @Bind(R.id.change_password_btn)
    Button mChangePasswordButton;

    public static Intent makeIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.customers_activity_profile);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Thông tin cá nhân");


        super.onCreate(savedInstanceState, ProfilePresenter.class, this);

        mProgressDialog = new ProgressDialog(this);
        if (getOps().isProcessing()) {
            showProgressDialog();
        }

        showProgressDialog();
        getOps().updateProfileInfo();
    }

    private void showProgressDialog() {
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("Đang xử lí");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Vui lòng đợi trong giây lát");
        mProgressDialog.show();
    }

    @OnClick(R.id.change_password_btn)
    void onChangePasswordClick() {
        Intent intent = ChangePasswordActivity.makeIntent(getApplicationContext());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onError(int status) {
        mProgressDialog.dismiss();
        Utils.toast(getApplicationContext(), "Đã có lỗi xảy ra");
    }

    @Override
    public void onUpdateProfileSuccess(ProfilePOJO profile) {
        mProgressDialog.dismiss();

        mUsernameTextView.setText(profile.getUsername());
        mNameTextView.setText(profile.getName());
        mEmailTextView.setText(profile.getEmail());
        mPhoneTextView.setText(profile.getPhone());

    }
}
