//
//  NotificationManager.swift
//  XpeApp
//
//  Created by Théo Lebègue on 17/12/2024.
//

import Foundation


class LocalNotificationsManager {
    
    static let instance = LocalNotificationsManager()
    
    private init(){}

    
    func scheduleNotification(title: String, message: String) {
        // Create notification content
        let content = UNMutableNotificationContent()
        content.title = title
        content.body = message
        content.sound = .default

        // Create a trigger for the notification (every day at 9 AM)
        var dateComponents = DateComponents()
        dateComponents.hour = 9
        dateComponents.minute = 0
        let trigger = UNCalendarNotificationTrigger(dateMatching: dateComponents, repeats: true)

        // Create a request with a unique identifier
        let request = UNNotificationRequest(identifier: "newsletterCheckNotification", content: content, trigger: trigger)

        // Add the request to the notification center
        UNUserNotificationCenter.current().add(request) { error in
            if let error = error {
                debugPrint("Error scheduling notification: \(error.localizedDescription)")
            }
        }
    }

}
