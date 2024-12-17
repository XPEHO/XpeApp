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
    


    private let startDate = Date(timeIntervalSinceNow: 120) // Schedule the next task in 2 minutes
    private let localNotificationsManager = LocalNotificationsManager.instance
    
    private init() {}
    
    
    func submitBackgroundTask() {
        // check if there is a pending task request or not
        BGTaskScheduler.shared.getPendingTaskRequests { request in
            debugPrint("\(request.count) BGTask pending.")
            guard request.isEmpty else { return }
            // Create a new background task request
            let request = BGProcessingTaskRequest(identifier: self.taskId)
            request.requiresNetworkConnectivity = false
            request.requiresExternalPower = false
            request.earliestBeginDate = startDate
            
            do {
                // Schedule the background task
                try BGTaskScheduler.shared.submit(request)
            } catch {
                debugPrint("Unable to schedule background task: \(error.localizedDescription)")
            }
        }
    }
    
    func handleBackgroundTask(task: BGProcessingTask) {
        
        // let obtainedLastNewsletter = await NewsletterRepositoryImpl.instance.getLastNewsletter()

        // Perform background task work here (e.g., trigger local notifications)
    
        localNotificationsManager.scheduleNotification("title", "message de tests")

        // Mark the task as completed
        task.setTaskCompleted(success: true)
    }
}
