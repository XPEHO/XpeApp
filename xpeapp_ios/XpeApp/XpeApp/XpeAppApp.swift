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
import BackgroundTasks
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
    let taskId = "xpeapp.notifications_check"
    
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {
        // Initialize Firebase
        FirebaseApp.configure()
        // Request notification permissions
        registerForPushNotifications(application: application)
        
        // Register background task
        BGTaskScheduler.shared.register(forTaskWithIdentifier: self.taskId, using: nil) { task in
            guard let task = task as? BGProcessingTask else { return }
            self.handleBackgroundTask(task: task)
        }
        // Submit background task
        submitBackgroundTask()

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
    private func registerForPushNotifications(application: UIApplication) {
        UNUserNotificationCenter.current().delegate = self
        
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(
            options: authOptions
        ) { (granted, error) in
            guard granted else {
                debugPrint("Permission denied for local notifications")
                return
            }
            debugPrint("Permission granted for local notifications")
        }
    }
    
    private func submitBackgroundTask() {
        // check if there is a pending task request or not
        BGTaskScheduler.shared.getPendingTaskRequests { request in
            debugPrint("\(request.count) BGTask pending.")
            guard request.isEmpty else { return }
            // Create a new background task request
            let request = BGProcessingTaskRequest(identifier: self.taskId)
            request.requiresNetworkConnectivity = false
            request.requiresExternalPower = false
            request.earliestBeginDate = Date(timeIntervalSinceNow: 120) // Schedule the next task in 2 minutes
            
            do {
                // Schedule the background task
                try BGTaskScheduler.shared.submit(request)
            } catch {
                debugPrint("Unable to schedule background task: \(error.localizedDescription)")
            }
        }
    }
    
    func handleBackgroundTask(task: BGProcessingTask) {
        // Perform background task work here (e.g., trigger local notifications)
        scheduleNotification()

        // Mark the task as completed
        task.setTaskCompleted(success: true)
    }
    
    func scheduleNotification() {
        // Create notification content
        let content = UNMutableNotificationContent()
        content.title = "Local Notification"
        content.body = "This is a local notification example."
        content.sound = .default

        // Create a trigger for the notification (every 2 minutes)
        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 120, repeats: true)

        // Create a request with a unique identifier
        let request = UNNotificationRequest(identifier: "localNotification", content: content, trigger: trigger)

        // Add the request to the notification center
        UNUserNotificationCenter.current().add(request) { error in
          if let error = error {
              debugPrint("Error scheduling notification: \(error.localizedDescription)")
          }
        }
    }
}

