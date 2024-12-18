//
//  NotificationManager.swift
//  XpeApp
//
//  Created by Théo Lebègue on 17/12/2024.
//

import Foundation
import SwiftUI

class LocalNotificationsManager {
    
    static let instance = LocalNotificationsManager()
    
    private init(){}

    func scheduleNotification(title: String, message: String, secondsFromNow: TimeInterval) {
        // Create notification content
        let content = UNMutableNotificationContent()
        content.title = title
        content.body = message
        content.sound = .default

        // Create a trigger for the notification
        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: secondsFromNow, repeats: false)

        // Create a request with a unique identifier
        let request = UNNotificationRequest(identifier: "checkNotification", content: content, trigger: trigger)

        // Add the request to the notification center
        UNUserNotificationCenter.current().add(request) { error in
            if let error = error {
                debugPrint("Error scheduling notification: \(error.localizedDescription)")
            }
        }
    }

}
