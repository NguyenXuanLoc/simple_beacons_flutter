import 'dart:async';

import 'package:device_info/device_info.dart';
import 'package:flutter/services.dart';
import 'dart:io';

class BeaconsPlugin {
  static const MethodChannel channel = const MethodChannel('beacons_plugin');
  static const event_channel = EventChannel('beacons_plugin_stream');
  static const event_channel_eddy_stone = EventChannel('scan_eddystone_stream');

  // 0 = no messages, 1 = only errors, 2 = all
  static int _debugLevel = 0;

  /// Set the message level value [value] for debugging purpose. 0 = no messages, 1 = errors, 2 = all
  static void setDebugLevel(int value) {
    _debugLevel = value;
  }

  // Send the message [msg] with the [msgDebugLevel] value. 1 = error, 2 = info
  static void printDebugMessage(String? msg, int msgDebugLevel) {
    if (_debugLevel >= msgDebugLevel) {
      print('beacons_plugin: $msg');
    }
  }

  static Future<void> listenNative() async {
    channel.setMethodCallHandler((call) async {
      print("TAG METHOD RESULT: ${call.arguments}");
/*

      switch (call.method) {
        case "scanEddyStone":
          {
            print("TAG METHOD RESULT: ${call.arguments}");
          }
      }
*/
    });
  }

  static Future<void> scanEddyStone() async {
    final String? result = await channel.invokeMethod('scanEddyStone');
    /**/
    print("TAG scanEddyStonescanEddyStone: $result");
  }

  static Future<void> startMonitoring() async {
    final String? result = await channel.invokeMethod('startMonitoring');
    printDebugMessage(result, 2);
  }

  static Future<void> stopMonitoring() async {
    final String? result = await channel.invokeMethod('stopMonitoring');
    printDebugMessage(result, 2);
  }

  static Future<void> addRegion(String identifier, String uuid) async {
    final String? result = await channel.invokeMethod(
        'addRegion', <String, dynamic>{'identifier': identifier, 'uuid': uuid});
    printDebugMessage(result, 2);
  }

  static Future<String> getInfo() async {
    var deviceInfo = DeviceInfoPlugin();
    try {
      if (Platform.isAndroid) {
        var android = await deviceInfo.androidInfo;
        return android.androidId.toString();
      } else if (Platform.isIOS) {
        var iosInfo = await deviceInfo.iosInfo;
        return iosInfo.identifierForVendor.toString();
      }
    } catch (ex) {
      return '';
    }
    return '';
  }

  static Future<void> sentBroadcast() async {
    var deviceId = await getInfo();
    await channel.invokeMethod("sentBroadcast", {'deviceId': deviceId});
  }

  static Future<void> clearRegions() async {
    final String? result = await channel.invokeMethod('clearRegions');
    printDebugMessage(result, 2);
  }

  static Future<void> runInBackground(bool runInBackground) async {
    final String? result = await channel.invokeMethod(
      'runInBackground',
      <String, dynamic>{'background': runInBackground},
    );
    printDebugMessage(result, 2);
  }

  static Future<void> clearDisclosureDialogShowFlag(bool clearFlag) async {
    final String? result = await channel.invokeMethod(
      'clearDisclosureDialogShowFlag',
      <String, dynamic>{'clearFlag': clearFlag},
    );
    printDebugMessage(result, 2);
  }

  static Future<void> addBeaconLayoutForAndroid(String layout) async {
    final String? result = await channel.invokeMethod(
        'addBeaconLayoutForAndroid', <String, dynamic>{'layout': layout});
    printDebugMessage(result, 2);
  }

  static Future<void> setForegroundScanPeriodForAndroid(
      {int foregroundScanPeriod = 1100,
        int foregroundBetweenScanPeriod = 0}) async {
    final String? result = await channel
        .invokeMethod('setForegroundScanPeriodForAndroid', <String, dynamic>{
      'foregroundScanPeriod': foregroundScanPeriod,
      'foregroundBetweenScanPeriod': foregroundBetweenScanPeriod
    });
    printDebugMessage(result, 2);
  }

  static Future<void> setBackgroundScanPeriodForAndroid(
      {int backgroundScanPeriod = 1100,
        int backgroundBetweenScanPeriod = 0}) async {
    final String? result = await channel
        .invokeMethod('setBackgroundScanPeriodForAndroid', <String, dynamic>{
      'backgroundScanPeriod': backgroundScanPeriod,
      'backgroundBetweenScanPeriod': backgroundBetweenScanPeriod
    });
    printDebugMessage(result, 2);
  }

  static Future<void> setDisclosureDialogMessage(
      {String? title, String? message}) async {
    final String? result = await channel.invokeMethod(
        'setDisclosureDialogMessage',
        <String, dynamic>{'title': title, 'message': message});
    printDebugMessage(result, 2);
  }

  static Future<void> addRegionForIOS(String uuid, int major, int minor,
      String name) async {
    final String? result = await channel.invokeMethod(
      'addRegionForIOS',
      <String, dynamic>{
        'uuid': uuid,
        'major': major,
        'minor': minor,
        'name': name
      },
    );
    printDebugMessage(result, 2);
  }

  static listenToScanEddyStone(StreamController controller) {
    event_channel_eddy_stone.receiveBroadcastStream().listen((event) {
      controller.add(event);
    }, onError: (error) {
      print('TAG Received error: ${error.message}');
    });
  }

  static listenToBeacons(StreamController controller) async {
    event_channel.receiveBroadcastStream().listen((dynamic event) {
      printDebugMessage('Received: $event', 2);
      controller.add(event);
    }, onError: (dynamic error) {
      printDebugMessage('Received error: ${error.message}', 1);
    });
  }
}
