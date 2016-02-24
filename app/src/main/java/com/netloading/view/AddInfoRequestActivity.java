package com.netloading.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.netloading.R;
import com.netloading.common.LifecycleLoggingActivity;
import com.netloading.utils.Constants;
import com.netloading.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dandoh on 2/20/16.
 */
public class AddInfoRequestActivity extends LifecycleLoggingActivity {


    private String mDimension;

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddInfoRequestActivity.class);
    }

    @Bind(R.id.pickup_date_textview)
    TextView mDateTextView;

    @Bind(R.id.goods_name)
    EditText mGoodsNameEditText;

    @Bind(R.id.expected_price)
    EditText mExpectedPriceEditText;

    @Bind(R.id.good_weight_number)
    EditText mGoodWeightNumberEditText;

    @Bind(R.id.dimension_spinner)
    Spinner mDimensionSpinner;

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

        setupSpinner();

    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dimension_item, Arrays.asList(
                new String[]{"kg", "m³", "hehe"})) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(R.id.vText)).setText("Đơn vị");
                    ((TextView) v.findViewById(R.id.vText)).setHint(getItem(getCount())); //"Hint to be displayed"
                }


                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mDimensionSpinner.setAdapter(adapter);
        mDimensionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mDimension = "kg";
                } else {
                    mDimension = "m3";
                }

                Utils.log(TAG, mDimension);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mDimensionSpinner.setSelection(adapter.getCount());

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
        if (TextUtils.isEmpty(mGoodsNameEditText.getText())
                || TextUtils.isEmpty(mGoodWeightNumberEditText.getText())
                || TextUtils.isEmpty(mDateTextView.getText())) {
            Utils.toast(this, "Vui lòng điền đầy đủ các thông về yêu cầu chở hàng");
            return;
        }


        if (myCalendar.before(Calendar.getInstance())) {
            Utils.toast(this, "Ngày đóng hàng không hợp lệ");
            return;
        }

        String goodName = mGoodsNameEditText.getText().toString();
        int goodWeightNumber = Integer.parseInt(mGoodWeightNumberEditText.getText().toString());
        String date = mDateTextView.getText().toString();
        String expectedPrice = mExpectedPriceEditText.getText().toString();


        /// save into preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(Constants.GOODS_NAME, goodName).apply();
        sharedPreferences.edit().putInt(Constants.GOODS_WEIGHT_NUMBER, goodWeightNumber).apply();
        sharedPreferences.edit().putString(Constants.GOODS_PICKUP_DATE, date).apply();
        sharedPreferences.edit().putString(Constants.GOODS_EXPTECTED_PRICE, expectedPrice).apply();
        sharedPreferences.edit().putString(Constants.GOODS_WEIGHT_DIMENSION, mDimension).apply();

        startActivity(ReviewRequestActivity.makeIntent(this));



    }
}
