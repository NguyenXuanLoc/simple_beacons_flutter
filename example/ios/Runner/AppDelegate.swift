import UIKit
import Flutter
import CoreLocation
import CoreBluetooth

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {

    let locationManager = CLLocationManager()
    var flutterChannelManager: FlutterChannelManager?

    override func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {

        configBase()

        GeneratedPluginRegistrant.register(with: self)
                
        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    }
    
    private func configBase() {
        locationManager.requestAlwaysAuthorization()
        
        if #available(iOS 10.0, *) {
          UNUserNotificationCenter.current().delegate = self
        }
        
        configChannel()
    }
    
    private func configChannel() {
        flutterChannelManager = FlutterChannelManager()
        
        configBeaConScanner()
    }
    
    private func configBeaConScanner() {
        flutterChannelManager?.configBeaconScanner()
    }
    
}
