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
        FirebaseApp.configure()

        // Set UNUserNotificationCenter delegate
        UNUserNotificationCenter.current().delegate = self
        
        // Request notification permissions
        registerForPushNotifications()
        
        application.registerForRemoteNotifications()
        
        Messaging.messaging().delegate = self

        return true
    }

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }

    func applicationWillTerminate(_ application: UIApplication) {
        do {
            try Auth.auth().signOut()
            debugPrint("Successfully signed out from Firebase")
        } catch let signOutError as NSError {
            debugPrint("Error disconnecting from Firebase: \(signOutError)")
        }
    }
}

func registerForPushNotifications() {
    let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
    UNUserNotificationCenter.current().requestAuthorization(options: authOptions) { granted, error in
        print("Permission granted: \(granted)")
        guard granted else { return }
        getNotificationSettings()
    }
}

func getNotificationSettings() {
    UNUserNotificationCenter.current().getNotificationSettings { settings in
        print("Notification settings: \(settings)")
        guard settings.authorizationStatus == .authorized else { return }
        DispatchQueue.main.async {
            UIApplication.shared.registerForRemoteNotifications()
        }
    }
}

extension XpeAppAppDelegate: UNUserNotificationCenterDelegate {
    // Receive displayed notifications for iOS 10 devices.
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification) async
    -> UNNotificationPresentationOptions {
        let userInfo = notification.request.content.userInfo
        // Print full message.
        print(userInfo)
        // Change this to your preferred presentation option
        return [.banner, .sound]
    }

    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse) async {
        let userInfo = response.notification.request.content.userInfo
        // Print full message.
        print(userInfo)
    }
}

extension XpeAppAppDelegate: MessagingDelegate {
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("Firebase registration token: \(String(describing: fcmToken))")
        let dataDict: [String: String] = ["token": fcmToken ?? ""]
        NotificationCenter.default.post(
            name: Notification.Name("FCMToken"),
            object: nil,
            userInfo: dataDict
        )
        // TODO: If necessary send token to application server.
    }
}
