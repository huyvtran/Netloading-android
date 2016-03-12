package com.ketnoivantai.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.ketnoivantai.R;
import com.ketnoivantai.common.GenericActivity;
import com.ketnoivantai.model.pojo.ChangePasswordPOJO;
import com.ketnoivantai.presenter.ChangePasswordPresenter;
import com.ketnoivantai.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;

/**
 * Created by AnhVu on 3/12/16.
 */
public class ChangePasswordActivity extends GenericActivity<ChangePasswordPresenter.View, ChangePasswordPresenter>
    implements ChangePasswordPresenter.View {

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChangePasswordActivity.class);
    }

    @Bind(R.id.current_password)
    EditText mCurrentPasswordEditText;

    @Bind(R.id.new_password)
    EditText mNewPasswordEditText;

    @Bind(R.id.re_new_password)
    EditText mRenewPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_password);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Thông tin cá nhân");

        super.onCreate(savedInstanceState, ChangePasswordPresenter.class, this);
    }

    @OnClick(R.id.change_pw_submit)
    void onChangePasswordSubmit() {

        String currentPassword = mCurrentPasswordEditText.getText().toString();
        String newPassword = mNewPasswordEditText.getText().toString();
        String renewPassword = mRenewPasswordEditText.getText().toString();

        if (!newPassword.equals(renewPassword)) {
            Utils.toast(this, "Mật khẩu nhập lại không đúng.");
            return;
        }

        if (newPassword.equals(currentPassword)) {
            Utils.toast(this, "Mật khẩu mới trùng mật khẩu cũ");
            return;
        }

        getOps().changeCustomerPassword(new ChangePasswordPOJO(currentPassword, newPassword));

    }

    @Override
    public void onError(int status) {
        Utils.toast(this, "Đã có lỗi xảy ra, kiểm tra lại đường truyền mạng.");
    }

    @Override
    public void onWrongPasswordError() {
        Utils.toast(this, "Mật khẩu hiện tại không đúng.");
    }

    @Override
    public void onUnhandledError() {
        Utils.toast(this, "Đã có lỗi xảy ra.");
    }

    @Override
    public void onChangPasswordSuccess() {
        Utils.toast(this, "Đổi mật khẩu thành công.");
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
