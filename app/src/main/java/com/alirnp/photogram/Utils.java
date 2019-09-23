package com.alirnp.photogram;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Utils {

    private static final String TAG = "UtilsLog";


    static void logMe(Class cls, String message) {
        Log.i(TAG, "LogMe: " + cls.toString() + " | " + message);
    }


    static void enableWifi(Context context, boolean enable) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }

    static void enableMobileData(Context context, boolean enable) {

        try {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.FROYO) {
                //android 2.2 versiyonu için
                Method dataConnSwitchmethod;
                Class<?> telephonyManagerClass;
                Object ITelephonyStub;
                Class<?> ITelephonyClass;

                TelephonyManager telephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);

                telephonyManagerClass = Class.forName(telephonyManager
                        .getClass().getName());
                Method getITelephonyMethod = telephonyManagerClass
                        .getDeclaredMethod("getITelephony");
                getITelephonyMethod.setAccessible(true);
                ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
                ITelephonyClass = Class.forName(ITelephonyStub.getClass()
                        .getName());

                if (enable) {
                    dataConnSwitchmethod = ITelephonyClass
                            .getDeclaredMethod("enableDataConnectivity");
                } else {
                    dataConnSwitchmethod = ITelephonyClass
                            .getDeclaredMethod("disableDataConnectivity");
                }
                dataConnSwitchmethod.setAccessible(true);
                dataConnSwitchmethod.invoke(ITelephonyStub);

            } else {
                // android 2.2 üstü versiyonlar için
                final ConnectivityManager conman = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                final Class<?> conmanClass = Class.forName(conman.getClass()
                        .getName());
                final Field iConnectivityManagerField = conmanClass
                        .getDeclaredField("mService");
                iConnectivityManagerField.setAccessible(true);
                final Object iConnectivityManager = iConnectivityManagerField
                        .get(conman);
                final Class<?> iConnectivityManagerClass = Class
                        .forName(iConnectivityManager.getClass().getName());
                final Method setMobileDataEnabledMethod = iConnectivityManagerClass
                        .getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                setMobileDataEnabledMethod.setAccessible(true);
                setMobileDataEnabledMethod.invoke(iConnectivityManager, enable);
            }


        } catch (Exception e) {
            Log.e("Mobile Data", "error turning on/off data");

        }

    }
}
