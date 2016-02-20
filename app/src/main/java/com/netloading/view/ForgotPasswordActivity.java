package com.netloading.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.netloading.R;
import com.netloading.common.GenericActivity;
import com.netloading.presenter.ForgotpassPresenter;
import com.netloading.utils.Utils;
import com.netloading.utils.Validator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dandoh on 2/16/16.
 */
public class ForgotPasswordActivity extends GenericActivity<ForgotpassPresenter.View, ForgotpassPresenter>
        implements ForgotpassPresenter.View {


    @Bind(R.id.forgot_password_email)
    EditText mEmailEditText;

    @Bind(R.id.forgot_password_submit)
    Button mSubmit;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);

        ButterKnife.bind(this);

        super.onCreate(savedInstanceState, ForgotpassPresenter.class, this);

        mProgressDialog = new ProgressDialog(this);
        if (getOps().isProcessing()) {
            showDialog();
        }
    }

    private void showDialog() {
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("Đang xử lí");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Vui lòng đợi trong giây lát");
        mProgressDialog.show();
    }


    @OnClick(R.id.forgot_password_submit)
    public void submitEmail() {
        String email = mEmailEditText.getText().toString();

        if (Validator.isEmailValid(email)) {

            showDialog();
            getOps().submitEmail(email);

        } else {

            Utils.toast(this, "Email vừa nhập không hợp lệ");

        }
    }

    @Override
    public void onSucceed() {
        mProgressDialog.dismiss();
        Utils.toast(this, "Mật khẩu đã được gửi đến email của bạn, vui lòng kiểm tra hòm thư");
        finish();
    }

    @Override
    public void onError(int status) {
        mProgressDialog.dismiss();
        if (status == STATUS_EMAIL_ERROR) {
            Utils.toast(this, "Email bạn vừa nhập không tồn tại");
        } else {
            Utils.toast(this, "Có lỗi xảy ra, vui lòng thử lại");

        }
    }
}
