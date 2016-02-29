package com.ketnoivantai.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.ketnoivantai.R;
import com.ketnoivantai.common.LifecycleLoggingActivity;
import com.ketnoivantai.utils.Constants;
import com.ketnoivantai.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dandoh on 2/20/16.
 */
public class AddInfoRequestActivity extends LifecycleLoggingActivity implements View.OnFocusChangeListener {


    private static final String EXTRA_ADD_INFO_STATE = "add info state";
    public static final int STATE_FROM_REVIEW = 99;
    public static final int STATE_FROM_PICK_LOCATION = 123;
    private String mDimension;

    public static Intent makeIntent(Context context, int state) {
        return new Intent(context, AddInfoRequestActivity.class)
                .putExtra(EXTRA_ADD_INFO_STATE, state);
    }

    @Bind(R.id.pickup_date_textview)
    TextView mDateTextView;

    @Bind(R.id.goods_name)
    EditText mGoodsNameEditText;

    @Bind(R.id.expected_price)
    EditText mExpectedPriceEditText;

    @Bind(R.id.good_weight_number)
    EditText mGoodWeightNumberEditText;

    @Bind(R.id.dimension_edit_text)
    EditText mDimensionEditText;

    private Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_request_activity);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Nhập thông tin yêu cầu");

        setDimension();

        mGoodsNameEditText.setOnFocusChangeListener(this);
        mDateTextView.setOnFocusChangeListener(this);
        mGoodWeightNumberEditText.setOnFocusChangeListener(this);
        mExpectedPriceEditText.setOnFocusChangeListener(this);

        initializeDefaultValue();

    }



    private void setDimension() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String vehicleType = sharedPreferences.getString(Constants.LOAI_XE, "");

        Utils.log(TAG, vehicleType);

        if (vehicleType.equals("xeTai") || vehicleType.equals("xeDongLanh")) {
            mDimension = "kg";
            mDimensionEditText.setText("kg");
        } else if (vehicleType.equals("xeBon")) {
            mDimension = "m3";
            mDimensionEditText.setText("m³");
        }
    }


    @OnClick(R.id.pickup_date_textview)
    void pickDate() {
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDateTextView.setText(sdf.format(myCalendar.getTime()));
    }


    @OnClick(R.id.btn_continue)
    void continueToReviewRequest() {
        // TODO - uncomment this
        if (TextUtils.isEmpty(mGoodsNameEditText.getText())
                || TextUtils.isEmpty(mGoodWeightNumberEditText.getText())
                || TextUtils.isEmpty(mDateTextView.getText())) {
            Utils.toast(this, "Vui lòng điền đầy đủ các thông về yêu cầu chở hàng");
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        if (myCalendar.before(cal)) {
            Utils.toast(this, "Ngày đóng hàng không hợp lệ");
            return;
        }

        String goodName = mGoodsNameEditText.getText().toString();//"Bánh kẹo";
        int goodWeightNumber = Integer.parseInt(mGoodWeightNumberEditText.getText().toString());//500;
        String date = mDateTextView.getText().toString();//"2016-02-28";
        String expectedPrice = mExpectedPriceEditText.getText().toString();//"1000000";


        /// save into preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(Constants.GOODS_NAME, goodName).apply();
        sharedPreferences.edit().putInt(Constants.GOODS_WEIGHT_NUMBER, goodWeightNumber).apply();
        sharedPreferences.edit().putString(Constants.GOODS_PICKUP_DATE, date).apply();
        sharedPreferences.edit().putString(Constants.GOODS_EXPTECTED_PRICE, expectedPrice).apply();
        sharedPreferences.edit().putString(Constants.GOODS_WEIGHT_DIMENSION, mDimension).apply();

        startActivity(ReviewRequestActivity.makeIntent(this));
    }

    private void initializeDefaultValue() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String goodName = sharedPreferences.getString(Constants.GOODS_NAME, "");
        int goodWeightNumber = sharedPreferences.getInt(Constants.GOODS_WEIGHT_NUMBER, 0);
        String date = sharedPreferences.getString(Constants.GOODS_PICKUP_DATE, "");
        String expectedPrice = sharedPreferences.getString(Constants.GOODS_EXPTECTED_PRICE, "");

        mGoodsNameEditText.setText(goodName);
        mGoodWeightNumberEditText.setText(String.valueOf(goodWeightNumber));
        mDateTextView.setText(date);
        mExpectedPriceEditText.setText(expectedPrice + "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            handleBack();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handleBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            hideKeyboard(v);
        }
    }

    private void handleBack() {
        if (getIntent().getIntExtra(EXTRA_ADD_INFO_STATE, -1) == STATE_FROM_PICK_LOCATION) {
            finish();
        } else {
            Utils.backToHome(getApplicationContext());
        }
    }

}
