package com.ketnoivantai.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.ketnoivantai.R;
import com.ketnoivantai.common.GenericActivity;
import com.ketnoivantai.model.pojo.NewTripPOJO;
import com.ketnoivantai.presenter.NewTripsDetailPresenter;
import com.ketnoivantai.utils.AddressManager;
import com.ketnoivantai.utils.Constants;
import com.ketnoivantai.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AnhVu on 9/15/16.
 */
public class NewTripsDetailActivity extends GenericActivity<NewTripsDetailPresenter.View, NewTripsDetailPresenter>
        implements NewTripsDetailPresenter.View {


    private static final String EXTRA_NEW_TRIP = "new trip";
    private NewTripPOJO mNewTripPOJO;

    public static Intent makeIntent(Context context, NewTripPOJO newTripPOJO) {
        return new Intent(context, NewTripsDetailActivity.class).putExtra(EXTRA_NEW_TRIP, newTripPOJO);
    }

    @Bind(R.id.company_id_tv)
    TextView mCompanyIdTextView;

    @Bind(R.id.start_address_tv)
    TextView mStartAddressTextView;

    @Bind(R.id.arrive_address_tv)
    TextView mArriveAddressTextView;

    @Bind(R.id.start_date_tv)
    TextView mStartDateTextView;

    @Bind(R.id.arrive_date_tv)
    TextView mArriveDateTextView;

    @Bind(R.id.vehicle_type_tv)
    TextView mVehicleTypeTextView;

    @Bind(R.id.vehicle_capacity_tv)
    TextView mVehicleCapacityTextView;

    @Bind(R.id.plate_number_tv)
    TextView mPlateNumberTextView;

    @Bind(R.id.vehicle_info_tv)
    TextView mVehicleInfoTextView;

    @Bind(R.id.price_per_unit_tv)
    TextView mPricePerUnitTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_trips_detail);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Chi tiết chuyến xe");

        super.onCreate(savedInstanceState, NewTripsDetailPresenter.class, this);

        mNewTripPOJO = getIntent().getParcelableExtra(EXTRA_NEW_TRIP);

        updateTripsDetail(mNewTripPOJO);
    }

    private void updateTripsDetail(NewTripPOJO trip) {

        mCompanyIdTextView.setText(Html.fromHtml("<b>Mã nhà xe: </b>" + trip.getCompany_id()));

        int startAddressCode = Integer.parseInt(trip.getStart_address());
        String startDistrict = AddressManager.getInstance(this).getDistrictFromCode(startAddressCode);
        String startProvince = AddressManager.getInstance(this).getProvinceFromCode(startAddressCode);

        mStartAddressTextView.setText(Html.fromHtml("<b>Điểm đi: </b>" + startDistrict + " - " + startProvince));

        int arriveAddressCode = Integer.parseInt(trip.getArrive_address());
        String arriveDistrict = AddressManager.getInstance(this).getDistrictFromCode(arriveAddressCode);
        String arriveProvince = AddressManager.getInstance(this).getProvinceFromCode(arriveAddressCode);

        mArriveAddressTextView.setText(Html.fromHtml("<b>Điểm đến: </b>" + arriveDistrict + " - " + arriveProvince));
        mStartDateTextView.setText(Html.fromHtml("<b>Ngày đi: </b>" + trip.getStart_date()));
        mArriveDateTextView.setText(Html.fromHtml("<b>Ngày đến: </b>" + trip.getArrive_date()));
        mVehicleTypeTextView.setText(Html.fromHtml("<b>Loại xe: </b>" + Utils.nameFromVehicleTypeString(trip.getVehicle_type())));

        String dimens = Utils.ConvertToDimension(trip.getVehicle_type()).second;
        String capacity = Utils.formatNumber(trip.getCapacity_number());
        mVehicleCapacityTextView.setText(Html.fromHtml("<b>Tải trọng: </b>" + capacity + " " + dimens));
        mPlateNumberTextView.setText(Html.fromHtml("<b>Biển số: </b>" + trip.getPlate_number()));
        mVehicleInfoTextView.setText(Html.fromHtml("<b>Mô tả chi tiết: </b>" + trip.getVehicle_information()));
        mPricePerUnitTextView.setText(Html.fromHtml("<b>Cước thầu: </b>" + Utils.formatNumber(trip.getPrice_per_unit()) + " VNĐ/" + dimens));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.choose_trip_btn)
    void OnChooseTripClick() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        int startAddressCode = Integer.parseInt(mNewTripPOJO.getStart_address());
        int arriveAddressCode = Integer.parseInt(mNewTripPOJO.getArrive_address());

        sharedPreferences.edit().putInt(Constants.START_DISTRICT_CODE, startAddressCode).apply();
        sharedPreferences.edit().putInt(Constants.ARRIVE_DISTRICT_CODE, arriveAddressCode).apply();

        String startDistrictName = AddressManager.getInstance(this).getDistrictFromCode(startAddressCode);
        String startProvinceName = AddressManager.getInstance(this).getProvinceFromCode(startAddressCode);

        String arriveDistrictName = AddressManager.getInstance(this).getDistrictFromCode(arriveAddressCode);
        String arriveProvinceName = AddressManager.getInstance(this).getProvinceFromCode(arriveAddressCode);

        Utils.log(TAG,
                startProvinceName
        );

        sharedPreferences.edit().putString(Constants.START_PROVINCE_NAME,
                startProvinceName
        ).apply();

        Utils.log(TAG,
                arriveProvinceName
        );

        sharedPreferences.edit().putString(Constants.ARRIVE_PROVINCE_NAME,
                arriveProvinceName
        ).apply();

        Utils.log(TAG,
                startDistrictName
        );

        sharedPreferences.edit().putString(Constants.START_DISTRICT_NAME,
                startDistrictName
        ).apply();

        Utils.log(TAG,
                arriveDistrictName
        );

        sharedPreferences.edit().putString(Constants.ARRIVE_DISTRICT_NAME,
                arriveDistrictName
        ).apply();

        String vehicleType = mNewTripPOJO.getVehicle_type();

        Utils.log(TAG, vehicleType);

        sharedPreferences.edit().putString(Constants.VEHICLE_TYPE,
                vehicleType
        ).apply();

        Intent intent = AddInfoRequestActivity.makeIntent(getApplicationContext(),
                AddInfoRequestActivity.STATE_FROM_PICK_LOCATION);

        startActivity(intent);
    }

}
