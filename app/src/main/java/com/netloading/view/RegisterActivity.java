package com.netloading.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.netloading.R;
import com.netloading.common.GenericActivity;
import com.netloading.presenter.RegisterPresenter;
import com.netloading.utils.Utils;
import com.netloading.utils.Validator;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        ButterKnife.bind(this);

        super.onCreate(savedInstanceState, RegisterPresenter.class, this);
    }


    @OnClick(R.id.register_btn)
    public void register() {
        if (TextUtils.isEmpty(mUsernameEditText.getText())
                || TextUtils.isEmpty(mPasswordEditText.getText())
                || TextUtils.isEmpty(mRepasswordEditText.getText())
                || TextUtils.isEmpty(mPhoneEditText.getText())
                || TextUtils.isEmpty(mEmailEditText.getText())) {
            Utils.toast(this, "Xin quý khách vui lòng nhập đủ hết các thông tin");
            return;
        }

        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        String repassword = mRepasswordEditText.getText().toString();
        String phone = mPhoneEditText.getText().toString();
        String email = mEmailEditText.getText().toString();


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
        getOps().register(username, password, phone, email, "Default", "Default");
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
}
