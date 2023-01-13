/****************************************************************************************
 * Copyright (c) 2016, 2017, 2019 Vincent Hiribarren                                    *
 *                                                                                      *
 * This program is free software; you can redistribute it and/or modify it under        *
 * the terms of the GNU General Public License as published by the Free Software        *
 * Foundation; either version 3 of the License, or (at your option) any later           *
 * version.                                                                             *
 *                                                                                      *
 * Linking Beacon Simulator statically or dynamically with other modules is making      *
 * a combined work based on Beacon Simulator. Thus, the terms and conditions of         *
 * the GNU General Public License cover the whole combination.                          *
 *                                                                                      *
 * As a special exception, the copyright holders of Beacon Simulator give you           *
 * permission to combine Beacon Simulator program with free software programs           *
 * or libraries that are released under the GNU LGPL and with independent               *
 * modules that communicate with Beacon Simulator solely through the                    *
 * com.umair.beacons_plugin.beaconsimulator.bluetooth.AdvertiseDataGenerator and the                    *
 * com.umair.beacons_plugin.beaconsimulator.bluetooth.AdvertiseDataParser interfaces. You may           *
 * copy and distribute such a system following the terms of the GNU GPL for             *
 * Beacon Simulator and the licenses of the other code concerned, provided that         *
 * you include the source code of that other code when and as the GNU GPL               *
 * requires distribution of source code and provided that you do not modify the         *
 * com.umair.beacons_plugin.beaconsimulator.bluetooth.AdvertiseDataGenerator and the                    *
 * com.umair.beacons_plugin.beaconsimulator.bluetooth.AdvertiseDataParser interfaces.                   *
 *                                                                                      *
 * The intent of this license exception and interface is to allow Bluetooth low energy  *
 * closed or proprietary advertise data packet structures and contents to be sensibly   *
 * kept closed, while ensuring the GPL is applied. This is done by using an interface   *
 * which only purpose is to generate android.bluetooth.le.AdvertiseData objects.        *
 *                                                                                      *
 * This exception is an additional permission under section 7 of the GNU General        *
 * Public License, version 3 (“GPLv3”).                                                 *
 *                                                                                      *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY      *
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A      *
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.             *
 *                                                                                      *
 * You should have received a copy of the GNU General Public License along with         *
 * this program.  If not, see <http://www.gnu.org/licenses/>.                           *
 ****************************************************************************************/

package com.umair.beacons_plugin.beaconsimulator.bluetooth.model;


import com.umair.beacons_plugin.R;

public enum BeaconType {

    eddystoneURL (R.drawable.ic_beacon_eddystone_url),
    eddystoneTLM (R.drawable.ic_beacon_eddystone_tlm),
    eddystoneUID (R.drawable.ic_beacon_eddystone_uid),
    eddystoneEID (R.drawable.ic_beacon_eddystone_eid),
    ibeacon (R.drawable.ic_beacon_ibeacon),
    altbeacon (R.drawable.ic_beacon_altbeacon),
    raw (R.drawable.ic_beacon_misc);

    private final int mImageResource;
    BeaconType(int resourceId) {
        mImageResource = resourceId;
    }

    public int getImageResource() {
        return mImageResource;
    }
}
