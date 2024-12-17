//
//  BackgroundTaskManager.swift
//  XpeApp
//
//  Created by Théo Lebègue on 17/12/2024.
//

import Foundation
import BackgroundTasks
import SwiftUI

class BackgroundTaskManager {
    
    static let instance = BackgroundTaskManager()
    
    let taskId = "xpeapp.notifications_check" // Define task id
    private let startDate = Date(timeIntervalSinceNow: 120) // Schedule the next task in 2 minutes
    private let localNotificationsManager = LocalNotificationsManager.instance
    
    private init() {}
    
    func registerBackgroundTask() {
        BGTaskScheduler.shared.register(forTaskWithIdentifier: self.taskId, using: nil) { task in
            guard let task = task as? BGProcessingTask else { return }
            self.handleBackgroundTask(task: task)
        }
        self.submitBackgroundTask()
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
            request.earliestBeginDate = self.startDate
            
            do {
                // Schedule the background task
                try BGTaskScheduler.shared.submit(request)
            } catch {
                debugPrint("Unable to schedule background task: \(error.localizedDescription)")
            }
        }
    }
    
    private func handleBackgroundTask(task: BGProcessingTask) {
        
        // let obtainedLastNewsletter = await NewsletterRepositoryImpl.instance.getLastNewsletter()
        
        // Send test notification
        localNotificationsManager.scheduleNotification(
            title: "title",
            message: "message de tests"
        )

        // Mark the task as completed
        task.setTaskCompleted(success: true)

        // Reschedule the task
        self.submitBackgroundTask()
    }

    private checkNewNewsletter() {
        // TODO: Call to the API to check if there is a new newsletter
        // let obtainedLastNewsletter = await NewsletterRepositoryImpl.instance.getLastNewsletter()
        
        // TODO: Check if there is a newsletter saved in local storage
        // We only save the last newsletter pdfURL to identify the newsletter

        // TODO: If there is not newsletter in the local storage, save the fetched one

        // TODO: If there is a newsletter in the local storage, compare the last newsletter fetched with the local one
        // If they are different, save the new one and send a notification

        // Send test notification
        localNotificationsManager.scheduleNotification(
            title: "Nouvelle newsletter !",
            message: "Restez informé avec notre nouvelle newsletter !"
        )
    }
}
