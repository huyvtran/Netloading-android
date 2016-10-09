package com.ketnoivantai.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ketnoivantai.R;
import com.ketnoivantai.common.GenericActivity;
import com.ketnoivantai.model.pojo.NewTripPOJO;
import com.ketnoivantai.model.webservice.ServiceGenerator;
import com.ketnoivantai.presenter.NewTripsListPresenter;
import com.ketnoivantai.utils.AddressManager;
import com.ketnoivantai.utils.Utils;
import com.ketnoivantai.view.adapter.NewTripsListAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


/**
 * Created by AnhVu on 9/14/16.
 */
public class NewTripsListActivity extends GenericActivity<NewTripsListPresenter.View, NewTripsListPresenter>
        implements NewTripsListPresenter.View, SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, BaseSliderView.OnSliderClickListener {

    private static final String TAG = "NewTripActivity";

    private ProgressDialog mProgressDialog;
    private ArrayList<NewTripPOJO> mNewTripsPOJOs;

    private ArrayList<NewTripPOJO> mFilterTripPOJOs;

    private NewTripsListAdapter mNewTripsListAdapter;

    private List<String> mStartProvinceName;
    private List<String> mArriveProvinceName;

    private ArrayAdapter mStartProvinceAdapter;
    private ArrayAdapter mArriveProvinceAdapter;

    private int mStartProvincePosition = -1;
    private int mArriveProvincePosition = -1;

    private boolean isFinding = false;

    @Bind(R.id.start_province_spinner)
    Spinner mStartProvinceSpinner;

    @Bind(R.id.arrive_province_spinner)
    Spinner mArriveProvinceSpinner;

    @Bind(R.id.title_tv)
    TextView mTitleTextView;


    public static Intent makeIntent(Context context) {
        return new Intent(context, NewTripsListActivity.class);
    }


    @Bind(R.id.swipe_to_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.new_trips_list)
    ListView mNewTripsListView;

    @Bind(R.id.slider)
    SliderLayout mSliderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_trips_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        getSupportActionBar().setTitle("Danh sách chuyến xe");

        super.onCreate(savedInstanceState, NewTripsListPresenter.class, this);

        mProgressDialog = new ProgressDialog(this);
        if (getOps().isProcessing()) {
            showProgressDialog();
        }


        showProgressDialog();
        getOps().getNewTrips();

        //swipe to refresh
        mSwipeRefreshLayout.setOnRefreshListener(this);

        //--
        mNewTripsListView.setOnScrollListener(this);

        //--
        setUpSlider();


        // Set up province name list for spinner
        setUpSpinner();


        Utils.log(TAG, "is login1: " + ServiceGenerator.isLoggedIn());
    }

    private void setUpSpinner() {
        Utils.log(TAG, "set up spinner");
        mStartProvinceName = new ArrayList<>(AddressManager.getInstance(this).getProvinceNameList());;
        mArriveProvinceName = new ArrayList<>(AddressManager.getInstance(this).getProvinceNameList());

        setUpStartSpinner();
        setUpArriveSpinner();
    }

    private void setUpArriveSpinner() {
        mArriveProvinceAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, mArriveProvinceName) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(R.id.vText)).setText("Tỉnh đến");
                    ((TextView) v.findViewById(R.id.vText)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                ((TextView) v.findViewById(R.id.vText)).setTextSize(14);
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }
        };

        mArriveProvinceAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mArriveProvinceSpinner.setAdapter(mArriveProvinceAdapter);
        mArriveProvinceSpinner.setSelection(mArriveProvinceAdapter.getCount());

        mArriveProvinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == mArriveProvinceAdapter.getCount()) return;
                mArriveProvincePosition = position;
                getOps().filterList(getApplicationContext(), mStartProvincePosition, mArriveProvincePosition, mNewTripsPOJOs);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpStartSpinner() {
        mStartProvinceAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, mStartProvinceName) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(R.id.vText)).setText("Tỉnh đi");
                    ((TextView) v.findViewById(R.id.vText)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                ((TextView) v.findViewById(R.id.vText)).setTextSize(14);
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }
        };

        mStartProvinceAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mStartProvinceSpinner.setAdapter(mStartProvinceAdapter);
        mStartProvinceSpinner.setSelection(mStartProvinceAdapter.getCount());

        mStartProvinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == mStartProvinceAdapter.getCount()) return;
                mStartProvincePosition = position;
                getOps().filterList(getApplicationContext(), mStartProvincePosition, mArriveProvincePosition, mNewTripsPOJOs);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setUpSlider() {
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Nhà xe 1", "https://raw.githubusercontent.com/vubinhne/netloading_images/master/slide1.png");
        url_maps.put("Nhà xe 2", "https://raw.githubusercontent.com/vubinhne/netloading_images/master/slide2.png");
        url_maps.put("Nhà xe 3", "https://raw.githubusercontent.com/vubinhne/netloading_images/master/slide3.png");
        url_maps.put("Nhà xe 4", "https://raw.githubusercontent.com/vubinhne/netloading_images/master/slide4.png");
        url_maps.put("Nhà xe 5", "https://raw.githubusercontent.com/vubinhne/netloading_images/master/slide5.png");

//        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
//        file_maps.put("Nhà xe 1", R.drawable.slide1);
//        file_maps.put("Nhà xe 2", R.drawable.slide2);
//        file_maps.put("Nhà xe 3", R.drawable.slide3);
//        file_maps.put("Nhà xe 4", R.drawable.slide4);
//        file_maps.put("Nhà xe 5", R.drawable.slide5);

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            Picasso picasso = Picasso.with(this);
            textSliderView.setPicasso(picasso);

            picasso.load(url_maps.get(name)).skipMemoryCache();

            textSliderView
//                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mSliderLayout.addSlider(textSliderView);
        }
    }

    @OnItemClick(R.id.new_trips_list)
    void OnNewTripsItemClick(int position) {
        NewTripPOJO tripPOJO = mFilterTripPOJOs.get(position);


        Intent intent = NewTripsDetailActivity.makeIntent(getApplicationContext(), tripPOJO);
        startActivity(intent);

    }

    private void showProgressDialog() {
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("Đang xử lí");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Vui lòng đợi trong giây lát");
        mProgressDialog.show();
    }

    @Override
    public void onRefresh() {
        Utils.log(TAG, "On refresh");
        getOps().getNewTrips();
//        resetSpinner();
    }

    private void resetSpinner() {
        mArriveProvinceSpinner.setSelection(mArriveProvinceAdapter.getCount());
        mStartProvinceSpinner.setSelection(mStartProvinceAdapter.getCount());
        mArriveProvincePosition = -1;
        mStartProvincePosition = -1;
    }

    @Override
    public void onError(int status) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (status == STATUS_NETWORK_ERROR) {
            Utils.toast(this, "Lỗi đường truyền, vui lòng thử lại");
        } else {
            Utils.toast(this, "Đã có lỗi xảy ra, vui lòng thử lại");
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onGetTripsSuccess(ArrayList<NewTripPOJO> newTripPOJOs) {
        this.mNewTripsPOJOs = newTripPOJOs;
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (newTripPOJOs.size() > 0) {
            showList(newTripPOJOs);
        }

        getOps().filterList(getApplicationContext(), mStartProvincePosition, mArriveProvincePosition, mNewTripsPOJOs);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFilterSuccess(ArrayList<NewTripPOJO> filterList) {
        this.mFilterTripPOJOs = filterList;
        mNewTripsListAdapter.setNewTripPOJOs(filterList);
        mNewTripsListAdapter.notifyDataSetChanged();
    }

    private void showList(ArrayList<NewTripPOJO> newTripPOJOs) {
        this.mNewTripsPOJOs = newTripPOJOs;

        Utils.log(TAG, newTripPOJOs.size() + "");

        mNewTripsListAdapter = new NewTripsListAdapter(this, newTripPOJOs);

        Utils.log(TAG, mNewTripsListAdapter.getCount() + " --> newTripsListAdapterCount");

        mNewTripsListView.setAdapter(mNewTripsListAdapter);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean enable = true;
        Utils.log(TAG, mNewTripsListView.getChildCount() + " --> new Trips ListView Child Count.");

        if (mNewTripsListView != null && mNewTripsListView.getChildCount() > 0) {
            boolean firstItemVisible = mNewTripsListView.getFirstVisiblePosition() == 0;

            boolean topOfFirstItemVisible = mNewTripsListView.getChildAt(0).getTop() == 0;

            enable = firstItemVisible && topOfFirstItemVisible;
        }

        Utils.log(TAG, "can refresh");

        mSwipeRefreshLayout.setEnabled(enable);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            backToHome();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.find_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_find) {
            if (!isFinding) {
                mStartProvinceSpinner.setVisibility(View.VISIBLE);
                mArriveProvinceSpinner.setVisibility(View.VISIBLE);
                mTitleTextView.setVisibility(View.INVISIBLE);
            } else {
                mStartProvinceSpinner.setVisibility(View.INVISIBLE);
                mArriveProvinceSpinner.setVisibility(View.INVISIBLE);
                mTitleTextView.setVisibility(View.VISIBLE);

                showProgressDialog();
                getOps().getNewTrips();
                resetSpinner();
            }
            isFinding = !isFinding;

        } else if (id == android.R.id.home) {
            backToHome();
        }

        return super.onOptionsItemSelected(item);
    }

    private void backToHome() {
        int hasToken = 0;
        if (ServiceGenerator.isLoggedIn()) hasToken = 1;

        Intent intent = PickLocationActivity.makeIntent(getApplicationContext(), hasToken);
        startActivity(intent);
    }
}
