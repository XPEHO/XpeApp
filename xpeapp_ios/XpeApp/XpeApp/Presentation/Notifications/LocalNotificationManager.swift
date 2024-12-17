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

    func scheduleNotification(title: String, message: String) {
        // Create notification content
        let content = UNMutableNotificationContent()
        content.title = title
        content.body = message
        content.sound = .default

         // Create date components for the current date and time
        let now = Date()
        let calendar = Calendar.current
        let dateComponents = calendar.dateComponents([.year, .month, .day, .hour, .minute, .second], from: now)

        // Trigger the notification immediately
        let trigger = UNCalendarNotificationTrigger(dateMatching: dateComponents, repeats: false)

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
