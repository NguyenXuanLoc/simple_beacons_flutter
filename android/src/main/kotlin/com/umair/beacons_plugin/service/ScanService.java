package com.umair.beacons_plugin.service;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.umair.beacons_plugin.R;
import com.umair.beacons_plugin.ble.advertising.ADPayloadParser;
import com.umair.beacons_plugin.ble.advertising.ADStructure;
import com.umair.beacons_plugin.ble.advertising.EddystoneURL;

import java.util.ArrayList;
import java.util.List;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;


public class ScanService {
    private Context context;
    private Activity activity;
    private BluetoothAdapter mBluetoothAdapter;
    private String TAG = this.getClass().getSimpleName();
    private BluetoothLeScanner mBleScanner;
    private ScanBeaconResultCallBack scanCallBack;
    private boolean mIsScanning = false;
    private MethodCall methodCall;
    private MethodChannel.Result result;
    List<String> lResult = new ArrayList<String>();
    private MethodChannel methodChannel;

    public ScanService(Context context, Activity activity, ScanBeaconResultCallBack callBack, MethodChannel methodChannel) {
        this.context = context;
        this.activity = activity;
        this.methodChannel = methodChannel;
        this.scanCallBack = callBack;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBleScanner = mBluetoothAdapter.getBluetoothLeScanner();
    }

    //    List<String> eddyStoneUrls = new ArrayList<>();
    private final ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult scanResult) {
            super.onScanResult(callbackType, scanResult);
            List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanResult.getScanRecord().getBytes());
            for (ADStructure structure : structures) {
                if (structure instanceof EddystoneURL) {
                    EddystoneURL eddUrl = (EddystoneURL) structure;
                    String url = eddUrl.getURL().toString();
                    boolean isExits = false;
                    scanCallBack.eddyStoneUrlCallBack(url);
                    for (int i = 0; i < lResult.size(); i++) {
                        if (lResult.get(i).equals(url)) {
                            isExits = true;
                            break;
                        }
                    }
                    if (!isExits) {
                        Log.e(TAG, "onScanResult: " + url);
//                        methodChannel.invokeMethod("scanEddyStone",url);
//                        result.success(url);
                        lResult.add(url);
                    }
                }
            /*    if (structure instanceof EddystoneTLM) {
                    EddystoneTLM eddTlm = (EddystoneTLM) structure;
                    String tlm = context.getString(
                            R.string.item_model_params_eddystoneTLM,
                            eddTlm.getBatteryVoltage(), eddTlm.getAdvertisementCount());
                    String tlmSub = context.getString(
                            R.string.item_model_params_eddystoneTLM_sub,
                            eddTlm.getBeaconTemperature(), eddTlm.getElapsedTime() / 1000.0);
//                    Log.e(TAG, "onScanResult: EddystoneTLM" + tlm + "\n" + tlmSub);
                }
                if (structure instanceof EddystoneEID) {
                    EddystoneEID eddEid = (EddystoneEID) structure;
                    result[0] = context.getString(
                            R.string.item_model_params_eddystoneTLM_scan,
                            eddEid.getEIDAsString());
                    result[1] = context.getString(
                            R.string.item_model_params_eddystoneTLM_scan_sub_unresolved
                    );
                }
                   if (structure instanceof EddystoneUID) {
                    EddystoneUID eddUid = (EddystoneUID) structure;
                    result[0] = context.getString(
                            R.string.item_model_params_eddystoneUID,
                            ByteTools.bytesToHex(eddUid.getNamespaceId()));
                    result[1] = context.getString(
                            R.string.item_model_params_eddystoneUID_sub,
                            ByteTools.bytesToHex(eddUid.getInstanceId()));

                }*/
            }
        }

        public void onBatchScanResults(List<ScanResult> results) {
        }

        public void onScanFailed(int errorCode) {
        }
    };

    public void stopScan() {
        if (mBleScanner != null && mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
            mBleScanner.stopScan(mScanCallback);
        }
    }

    public void startBeaconScan(MethodCall methodCall, MethodChannel.Result result) {
        stopScan();
        Log.e(TAG, "startBeaconScan:");
        this.methodCall = methodCall;
        this.result = result;
        if (mIsScanning) {
            return;
        }
        boolean ok = checkPermission();
        if (!ok) {
            return;
        }
        Log.e(TAG, "Starting scan of beacons");
        mIsScanning = true;
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        mBleScanner.startScan(null, builder.build(), mScanCallback);
    }

    private boolean checkPermission() {
        boolean needLocation = false;
        boolean needBluetooth = false;
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Log.e(TAG, "checkPermission: NEED PERMISSION");
            }
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);
            return false;
        }

        // Check if BL adapter on
        if (!mBluetoothAdapter.isEnabled()) {
            needBluetooth = true;
        }

        // Check if location enable
        int locationMode = Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
        if (locationMode == Settings.Secure.LOCATION_MODE_OFF) {
            needLocation = true;
        }

        if (needBluetooth || needLocation) {
            Log.e(TAG, "NEED PERMISSION BLUETOOTH OR LOCATION");
            return false;
        } else {
            return true;
        }

    }
}
