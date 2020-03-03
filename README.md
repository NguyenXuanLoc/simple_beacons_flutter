# beacons_plugin

[![pub package](https://img.shields.io/pub/v/beacons_plugin)](https://pub.dev/packages/beacons_plugin)


This plugin is developed to scan nearby iBeacons on both Android iOS. This library makes it easier to scan & range nearby BLE beacons and read their proximity values.

## Install
In your pubspec.yaml

```yaml
dependencies:
  beacons_plugin: ^1.0.1
```

```dart
import 'dart:io' show Platform;
import 'package:beacons_plugin/beacons_plugin.dart';
```

## Ranging Beacons

```dart
    if (Platform.isAndroid) {
      await BeaconsPlugin.addRegion("Beacon1");
    } else if (Platform.isIOS) {
      await BeaconsPlugin.addRegionForIOS(
          "01022022-f88f-0000-00ae-9605fd9bb620", 1, 1, "BeaconName");
    }
```

## Listen To Beacon Scan Results

```dart
    static const channel = EventChannel('beacons_plugin_stream');
    
    static listenToBeacons() async {
        channel.receiveBroadcastStream().listen((dynamic event) {
          print('Received: $event');
        }, onError: (dynamic error) {
          print('Received error: ${error.message}');
        });
    }
```

## Native Libraries

* For iOS: [CoreLocation](https://developer.apple.com/documentation/corelocation/)
* For Android: [Android-Beacon-Library](https://github.com/AltBeacon/android-beacon-library) 

# Author

Flutter Beacons plugin is developed by Umair Adil. You can email me at <m.umair.adil@gmail.com> for any queries.