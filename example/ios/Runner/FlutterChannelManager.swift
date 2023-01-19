//
//  FlutterChannelManager.swift
//  Runner
//
//  Created by Tà on 18/01/2023.
//  Copyright © 2023 The Chromium Authors. All rights reserved.
//

import Foundation
import Flutter

class FlutterChannelManager: NSObject {
    
    private let scanEddystoneStreamEvent = "scan_eddystone_stream"
    
    private var beaconEventChannel: FlutterEventChannel?
    var eventSink: FlutterEventSink?

    override init() {
        super.init()
        guard let flutterVC = UIApplication.shared.windows.first?.rootViewController as? FlutterViewController else { return }
        beaconEventChannel = FlutterEventChannel(name: scanEddystoneStreamEvent, binaryMessenger: flutterVC.binaryMessenger)
        beaconEventChannel?.setStreamHandler(self)
    }
}

extension FlutterChannelManager: FlutterStreamHandler {
    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        self.eventSink = events
        return nil
    }
    
    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        self.eventSink = nil
        return nil
    }
}

// MARK: Beacon Scanner
extension FlutterChannelManager {
    public func configBeaconScanner() {
        let beaconScanner = BeaconScannerManager()
        beaconScanner.didObserveURLBeacon = { [weak self] url in
            guard let self = self else { return }
            self.sendBeaconURL(url: url)
        }
        beaconScanner.startScanning()
    }
    
    public func sendBeaconURL(url: String?) {
        eventSink?(url)
    }
}
