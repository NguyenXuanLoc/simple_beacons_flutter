//
//  BeaconScannerManager.swift
//  Runner
//
//  Created by Tà on 18/01/2023.
//  Copyright © 2023 The Chromium Authors. All rights reserved.
//

import Foundation

class BeaconScannerManager {
    private var beaconScanner: BeaconScanner
    private var beaconURLs: [String] = []
    
    public var didFindBeacon: ((_ beacon: BeaconInfo) -> Void)?
    public var didLoseBeacon: ((_ beacon: BeaconInfo) -> Void)?
    public var didUpdateBeacon: ((_ beacon: BeaconInfo) -> Void)?
    public var didObserveURLBeacon: ((_ url: String) -> Void)?

    init() {
        self.beaconScanner = BeaconScanner()
        self.beaconScanner.delegate = self
    }
    
    public func startScanning() {
        self.beaconScanner.startScanning()
    }
    
    public func stopScanning() {
        self.beaconScanner.stopScanning()
    }
}

extension BeaconScannerManager: BeaconScannerDelegate {
    func didFindBeacon(beaconScanner: BeaconScanner, beaconInfo: BeaconInfo) {
        didFindBeacon?(beaconInfo)
        NSLog("FIND: %@", beaconInfo.description)
    }
    
    func didLoseBeacon(beaconScanner: BeaconScanner, beaconInfo: BeaconInfo) {
        didLoseBeacon?(beaconInfo)
        NSLog("LOST: %@", beaconInfo.description)
    }
    
    func didUpdateBeacon(beaconScanner: BeaconScanner, beaconInfo: BeaconInfo) {
        didUpdateBeacon?(beaconInfo)
        NSLog("UPDATE: %@", beaconInfo.description)
    }
    
    func didObserveURLBeacon(beaconScanner: BeaconScanner, URL: NSURL, RSSI: Int) {
        if let urlString = URL.absoluteString {
            if !beaconURLs.contains(urlString) {
                beaconURLs.append(urlString)
                didObserveURLBeacon?(urlString)
            }
        }
        NSLog("URL SEEN: %@, RSSI: %d", URL, RSSI)
    }
}
