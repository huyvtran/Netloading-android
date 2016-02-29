package com.ketnoivantai.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ketnoivantai.R;
import com.ketnoivantai.utils.Constants;
import com.ketnoivantai.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by AnhVu on 2/17/16.
 */
public class PickLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "PickLocationActivity";
    private static final String EXTRA_HAS_TOKEN = "Extra has token";
    private List<String> maTinhList = new ArrayList<String>();
    private List<String> allHuyenList = new ArrayList<String>();
    private List<String> maHuyenList = new ArrayList<String>();
    private List<String> maTinhCuaHuyenList = new ArrayList<String>();
    private List<String> kinhDo = new ArrayList<String>();
    private List<String> viDo = new ArrayList<String>();

    private List<String> tinhList = new ArrayList<String>();

    private Spinner tinhDiSpinner;
    private Spinner huyenDiSpinner;

    private Spinner tinhDenSpinner;
    private Spinner huyenDenSpinner;

    private GoogleMap map;
    private boolean huyenDiSelected;
    private boolean huyenDenSelected;
    private int huyenDiSelectedPosition;
    private int huyenDenSelectedPosition;

    private String tenTinhDenSelected;
    private String tenTinhDiSelected;

    List<Integer> diListPosition = new ArrayList<Integer>();
    List<Integer> denListPosition = new ArrayList<Integer>();
    private String vehicleType = "xeTai";

    private NetloadingNavigationHandler mNavigationHandler;


    public static Intent makeIntent(Context context, boolean hasToken) {
        return new Intent(context, PickLocationActivity.class)
                .putExtra(EXTRA_HAS_TOKEN, hasToken)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pick_location_activity);

        ButterKnife.bind(this);

        initializeEssentialView();

        ImageButton continueButton = (ImageButton) findViewById(R.id.pick_continue);
//        continueButton.setBackgroundColor(Color.argb(100, 0, 0, 0));


        ReadFileFromAssetsAndAddToList(tinhList, "ten_tinh.txt");
        ReadFileFromAssetsAndAddToList(allHuyenList, "ten_huyen.txt");
        ReadFileFromAssetsAndAddToList(maTinhList, "ma_tinh.txt");
        ReadFileFromAssetsAndAddToList(maHuyenList, "ma_huyen.txt");
        ReadFileFromAssetsAndAddToList(maTinhCuaHuyenList, "ma_tinh_cua_huyen.txt");

        ReadFileFromAssetsAndAddToList(kinhDo, "kinh_do.txt");
        ReadFileFromAssetsAndAddToList(viDo, "vi_do.txt");

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        huyenDiSelected = false;
        huyenDenSelected = false;

        SetUpSpinner();

    }

    /**
     * Step to add drawer navigation
     * + Add toolbar like this activity toolbar
     * + Wrap drawer_layout outside
     * + Add left_drawer
     * + Copy this code
     */
    private void initializeEssentialView() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        //
        boolean hasToken = getIntent().getBooleanExtra(EXTRA_HAS_TOKEN, true);
        if (!hasToken) {
            findViewById(R.id.navigation_sign_out)
                    .setVisibility(View.INVISIBLE);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chọn địa điểm và loại xe");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        this.mNavigationHandler = new NetloadingNavigationHandler(this);
    }

    public void onNavigationItemClick(View view) {
        mNavigationHandler.onNavigationItemClick(view);
    }

    @Override
    protected void onResume() {
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawers();
        super.onResume();
    }

    /**
     * Navigation part end here
     */

    private void SetUpSpinner() {
        tinhDiSpinner = (Spinner) findViewById(R.id.tinh_di);
        huyenDiSpinner = (Spinner) findViewById(R.id.huyen_di);
        tinhDenSpinner = (Spinner) findViewById(R.id.tinh_den);
        huyenDenSpinner = (Spinner) findViewById(R.id.huyen_den);

        SetUpTinhSpinner(tinhDiSpinner, "di");
        SetUpTinhSpinner(tinhDenSpinner, "den");
    }

    private void SetUpTinhSpinner(Spinner spin, String type) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, tinhList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(R.id.vText)).setText("Chọn Tỉnh");
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
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new TinhSelectedEvent(type));
        spin.setSelection(adapter.getCount());

        updateHuyenSpinner(0, type);
    }

    @OnClick(R.id.pick_continue)
    public void pickContinue() {
        if (!(huyenDenSelected && huyenDiSelected)) return;

        int maHuyenDi = Integer.parseInt(maHuyenList.get(huyenDiSelectedPosition));
        int maHuyenDen = Integer.parseInt(maHuyenList.get(huyenDenSelectedPosition));
        Utils.log("TAG", maHuyenDi + " " + maHuyenDen);


        // Luu lai ten huyen, ten tinh, ma huyen, ma tinh cua diem den va diem di
        // Luu lai loai xe


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putInt(Constants.MA_HUYEN_DI, maHuyenDi).apply();
        sharedPreferences.edit().putInt(Constants.MA_HUYEN_DEN, maHuyenDen).apply();


        Utils.log(TAG,
                tenTinhDiSelected
        );

        sharedPreferences.edit().putString(Constants.TEN_TINH_DI,
                tenTinhDiSelected
        ).apply();

        Utils.log(TAG,
                tenTinhDenSelected
        );

        sharedPreferences.edit().putString(Constants.TEN_TINH_DEN,
                tenTinhDenSelected
        ).apply();

        Utils.log(TAG,
                allHuyenList.get(huyenDiSelectedPosition)
        );

        sharedPreferences.edit().putString(Constants.TEN_HUYEN_DI,
                allHuyenList.get(huyenDiSelectedPosition)
        ).apply();

        Utils.log(TAG,
                allHuyenList.get(huyenDenSelectedPosition)
        );

        sharedPreferences.edit().putString(Constants.TEN_HUYEN_DEN,
                allHuyenList.get(huyenDenSelectedPosition)
        ).apply();

        Utils.log(TAG, vehicleType);

        sharedPreferences.edit().putString(Constants.LOAI_XE,
                vehicleType
        ).apply();


        startActivity(AddInfoRequestActivity.makeIntent(this, AddInfoRequestActivity.STATE_FROM_PICK_LOCATION));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.map = map;
    }


    private class TinhSelectedEvent implements AdapterView.OnItemSelectedListener {

        private String type;

        public TinhSelectedEvent(String type) {
            this.type = type;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (type.equals("di")) {
                tenTinhDiSelected = tinhList.get(position);
            } else {
                tenTinhDenSelected = tinhList.get(position);
            }
            updateHuyenSpinner(position, type);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private class HuyenSelectedEvent implements AdapterView.OnItemSelectedListener {

        private String type;

        public HuyenSelectedEvent(String type) {
            this.type = type;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            if (view != null) {
                int tag = (int) ((TextView) view.findViewById(R.id.vText)).getTag();

                Log.i("TAG", tag + "");

                if (tag != 100) {
                    if (type.equals("di")) {
                        huyenDiSelected = true;

                        huyenDiSelectedPosition = diListPosition.get(position);
                    } else {
                        huyenDenSelected = true;
                        huyenDenSelectedPosition = denListPosition.get(position);
                    }
                } else {
                    if (type.equals("di"))
                        huyenDiSelected = false;
                    else huyenDenSelected = false;
                    findViewById(R.id.pick_continue).setBackgroundResource(R.drawable.next_dis);
                }

                if (huyenDiSelected && huyenDenSelected) {

                    findViewById(R.id.pick_continue).setBackgroundResource(R.drawable.next);

                    double kDoDi = Double.parseDouble(kinhDo.get(huyenDiSelectedPosition));//Double.parseDouble(kinhDoDi.get(huyenDiSelectedPosition));
                    double vDoDi = Double.parseDouble(viDo.get(huyenDiSelectedPosition));//Double.parseDouble(viDoDi.get(huyenDiSelectedPosition));

                    double kDoDen = Double.parseDouble(kinhDo.get(huyenDenSelectedPosition));//Double.parseDouble(kinhDoDen.get(huyenDenSelectedPosition));
                    double vDoDen = Double.parseDouble(viDo.get(huyenDenSelectedPosition));//Double.parseDouble(viDoDen.get(huyenDenSelectedPosition));

                    Log.i("TAG", kDoDi + " " + vDoDi + " " + kDoDen + " " + vDoDen);

                    HandleMap(kDoDi, vDoDi, kDoDen, vDoDen);
                }
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private void HandleMap(double kDoDi, double vDoDi, double kDoDen, double vDoDen) {

        map.clear();
        Marker m1 = map.addMarker(new MarkerOptions()
                .position(new LatLng(kDoDi, vDoDi))
                .title("Start"));

        Marker m2 = map.addMarker(new MarkerOptions()
                .position(new LatLng(kDoDen, vDoDen))
                .title("Finish"));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(m1.getPosition());
        builder.include(m2.getPosition());

        LatLngBounds bounds = builder.build();
        Log.i("Bounds = ", bounds.toString());

        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.moveCamera(cu);
    }

    private void updateHuyenSpinner(int tinhPosition, String type) {
//        Log.i("LOG", "den day roi " + tinhPosition);

        List<String> huyenList = new ArrayList<String>();
//        List<String> kinhDoHuyen = new ArrayList<String>();
//        List<String> viDoHuyen = new ArrayList<String>();

        List<Integer> listPosition = new ArrayList<Integer>();

        for (int i = 0; i < allHuyenList.size(); i++) {
            if (maTinhCuaHuyenList.get(i).equals(maTinhList.get(tinhPosition))) {
//                Log.i("Huyen", i + " " + allHuyenList.get(i));
                huyenList.add(allHuyenList.get(i));
//                kinhDoHuyen.add(kinhDo.get(i));
//                viDoHuyen.add(viDo.get(i));

                listPosition.add(i);

            }
//            else if (huyenList.size() > 0) break;
        }
        huyenList.add("hehe");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, huyenList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(R.id.vText)).setText("Chọn Huyện");
                    ((TextView) v.findViewById(R.id.vText)).setTextSize(15);
                    ((TextView) v.findViewById(R.id.vText)).setTag(100);
                    ((TextView) v.findViewById(R.id.vText)).setHint(getItem(getCount())); //"Hint to be displayed"
                } else {
                    ((TextView) v.findViewById(R.id.vText)).setTag(999);
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        if (type.equals("di")) {
            huyenDiSpinner.setAdapter(adapter);
            huyenDiSpinner.setSelection(adapter.getCount());
            diListPosition = new ArrayList<Integer>(listPosition);
            huyenDiSpinner.setOnItemSelectedListener(new HuyenSelectedEvent("di"));
        } else {
            huyenDenSpinner.setAdapter(adapter);
            huyenDenSpinner.setSelection(adapter.getCount());
            denListPosition = new ArrayList<Integer>(listPosition);
            huyenDenSpinner.setOnItemSelectedListener(new HuyenSelectedEvent("den"));
        }
    }


    @OnCheckedChanged(R.id.radio_xe_tai)
    void onCheckedXeTai(boolean isChecked) {
        if (isChecked) vehicleType = "xeTai";
    }

    @OnCheckedChanged(R.id.radio_xe_bon)
    void onCheckedXeBon(boolean isChecked) {
        if (isChecked) vehicleType = "xeBon";
    }

    @OnCheckedChanged(R.id.radio_xe_dong_lanh)
    void onCheckedXeDongLanh(boolean isChecked) {
        if (isChecked) vehicleType = "xeDongLanh";
    }


    private void ReadFileFromAssetsAndAddToList(List list, String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            int dem = 0;
            while ((mLine = reader.readLine()) != null) {
                //process line
                list.add(mLine);
                dem++;
            }

        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                    e.printStackTrace();
                }
            }
        }
    }
}
