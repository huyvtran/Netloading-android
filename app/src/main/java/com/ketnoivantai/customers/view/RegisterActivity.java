package com.ketnoivantai.customers.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import com.ketnoivantai.R;
import com.ketnoivantai.customers.common.GenericActivity;
import com.ketnoivantai.customers.presenter.RegisterPresenter;
import com.ketnoivantai.utils.Utils;
import com.ketnoivantai.utils.Validator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AnhVu on 2/12/16.
 */
public class RegisterActivity extends GenericActivity<RegisterPresenter.View, RegisterPresenter>
        implements RegisterPresenter.View {

    @Bind(R.id.username)
    EditText mUsernameEditText;
    @Bind(R.id.password)
    EditText mPasswordEditText;
    @Bind(R.id.re_password)
    EditText mRepasswordEditText;
    @Bind(R.id.email)
    EditText mEmailEditText;
    @Bind(R.id.phone_number)
    EditText mPhoneEditText;
    @Bind(R.id.name)
    EditText mNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customers_register_activity);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Đăng ký tài khoản mới");

        super.onCreate(savedInstanceState, RegisterPresenter.class, this);
    }

    @OnClick(R.id.policy)
    public void onPolicyClick() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.netloading.vn/policy.html"));
        startActivity(browserIntent);
    }


    @OnClick(R.id.register_btn)
    public void register() {
        if (TextUtils.isEmpty(mUsernameEditText.getText())
                || TextUtils.isEmpty(mPasswordEditText.getText())
                || TextUtils.isEmpty(mRepasswordEditText.getText())
                || TextUtils.isEmpty(mPhoneEditText.getText())
                || TextUtils.isEmpty(mEmailEditText.getText())
                || TextUtils.isEmpty(mNameEditText.getText())) {
            Utils.toast(this, "Xin quý khách vui lòng nhập đủ hết các thông tin");
            return;
        }

        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        String repassword = mRepasswordEditText.getText().toString();
        String phone = mPhoneEditText.getText().toString();
        String email = mEmailEditText.getText().toString();
        String name = mNameEditText.getText().toString();


        // Validate
        if (!Validator.isUsernameValid(username)) {
            Utils.toast(this, "Tên tài khoản không hợp lệ.");
            return;
        }

        if (!Validator.isEmailValid(email)) {
            Utils.toast(this, "Địa chỉ email không hợp lệ.");
            return;
        }

        if (!Validator.isPhoneValid(phone)) {
            Utils.toast(this, "Số điện thoại không hơp lệ");
            return;
        }

        if (!Validator.isPasswordValid(password)) {
            Utils.toast(this, "Mật khẩu không hợp lệ");
            return;
        }

        if (!password.equals(repassword)) {
            Utils.toast(this, "Mật khẩu nhập lại không đúng.");
            return;
        }


        // TODO - default social id and address
        getOps().register(username, password, phone, email, "Default", "Default", name);
    }

    @Override
    public void registerSucceed() {
        Utils.toast(this, "Đăng kí thành công");
        finish();
    }

    @Override
    public void registerError(int errorCode) {
//        Utils.toast(this, "Đăng kí không thành công " + errorCode);

        if (errorCode == STATUS_NETWORK_ERROR) {
            Utils.toast(this, "Lỗi đường truyền, đăng kí không thành công ");
        } else if (errorCode == STATUS_DUPLICATE_EMAIL) {
            Utils.toast(this, "Đăng kí không thành công, email cuả bạn đã tồn tại");
        } else if (errorCode == STATUS_DUPLICATE_PHONE) {
            Utils.toast(this, "Đăng kí không thành công, điện thoại của bạn đã tồn tại");
        } else if (errorCode == STATUS_DUPLICATE_USERNAME) {
            Utils.toast(this, "Đăng kí không thành công, tên tài khoản của bạn đã tồn tại");
        }
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
