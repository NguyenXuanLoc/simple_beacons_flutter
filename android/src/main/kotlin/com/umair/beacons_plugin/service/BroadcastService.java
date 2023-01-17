package com.umair.beacons_plugin.service;

import android.app.Activity;
import android.content.Context;

import com.umair.beacons_plugin.beaconsimulator.App;
import com.umair.beacons_plugin.beaconsimulator.bluetooth.BeaconSimulatorService;
import com.umair.beacons_plugin.beaconsimulator.bluetooth.model.BeaconModel;
import com.umair.beacons_plugin.beaconsimulator.bluetooth.model.BeaconType;
import com.umair.beacons_plugin.beaconsimulator.bluetooth.model.EddystoneURL;

public class BroadcastService {

    public static void sentBroadcast(Context context, String deviceId, Activity currentActivity) {
        EddystoneURL eddUrl = new EddystoneURL();
        eddUrl.setUrl("https://" + deviceId + ".cl");
        BeaconModel mBeaconModel = new BeaconModel(BeaconType.eddystoneURL);
        mBeaconModel.setEddystoneURL(eddUrl);
        App.getInstance().getBeaconStore().saveBeacon(mBeaconModel);
        if (BeaconSimulatorService.isBluetoothOn(context) && BeaconSimulatorService.isBroadcastAvailable(context)) {
            BeaconSimulatorService.startBroadcast(context, mBeaconModel.getId(), true);
        }
    }
}
