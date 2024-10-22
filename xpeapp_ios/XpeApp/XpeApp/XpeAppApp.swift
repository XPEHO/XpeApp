//
//  XpeAppApp.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import SwiftUI
import SwiftData
import FirebaseCore
import FirebaseAuth
import FirebaseFirestore
import FirebaseMessaging
import xpeho_ui

@main
struct XpeAppApp: App {
    @UIApplicationDelegateAdaptor(XpeAppAppDelegate.self) var delegate
    public static var firestore: Firestore = Firestore.firestore()
    
    init() {
        
        // Load XpehoUI fonts
        Fonts.registerFonts()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

// Note(Loucas): This app delegate is used as part
// of the default Firebase SDK setup code
// Note(Loucas): Firebase method swizzling has been disabled. If it becomes necessary in the future,
// we can enable it through setting the FirebaseAppDelegateProxyEnabled boolean to 'NO' in Info.plist
class XpeAppAppDelegate: NSObject, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {
        // Initialize Firebase
        FirebaseApp.configure()
        // Set Firebase Messaging delegate
        Messaging.messaging().delegate = self
        // Request notification permissions
        registerForPushNotifications(application: application)
        return true
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Disconnect from Firebase
        do {
            try Auth.auth().signOut()
            debugPrint("Successfully signed out from Firebase")
        } catch let signOutError as NSError {
            debugPrint("Error disconnecting from Firebase: \(signOutError)")
        }
    }
}

extension XpeAppAppDelegate: UNUserNotificationCenterDelegate {
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print(error)
    }
    
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        //self.sendDeviceTokenToServer(data: deviceToken)
        Messaging.messaging().setAPNSToken(deviceToken, type: .unknown)
    }
    
    private func registerForPushNotifications(application: UIApplication) {
        UNUserNotificationCenter.current().delegate = self
        
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(
            options: authOptions
        ) { (granted, error) in
            guard granted else {return}
            DispatchQueue.main.async{
                application.registerForRemoteNotifications()
            }
        }
    }
}

extension XpeAppAppDelegate: MessagingDelegate {
    
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        //print("Firebase registration token: \(String(describing: fcmToken))")
    }
}

