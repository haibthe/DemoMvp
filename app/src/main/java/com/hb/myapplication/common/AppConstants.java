package com.hb.myapplication.common;

import android.Manifest;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by buihai on 7/29/17.
 */

public interface AppConstants {

    String[] PERMISSIONS_IN_APP = {
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };


    int TRANSPORT_MOTOR = 1;
    int TRANSPORT_CAR = 2;
    int TRANSPORT_TRUCK = 3;
    int TRANSPORT_WALK = 0;
    int TRANSPORT_PRIORITY = 5;
    int TRANSPORT_BUS = 6;

    int REGION_DISTRICT = 0;
    int REGION_WARD = 1;

    //

    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

}
