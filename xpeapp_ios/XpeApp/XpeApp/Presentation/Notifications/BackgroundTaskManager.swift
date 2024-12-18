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
    private let startDate = Date().addingTimeInterval(60) // Schedule the next task in 1 minute
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
        // Manual test : e -l objc -- (void)[[BGTaskScheduler sharedScheduler] _simulateLaunchForTaskWithIdentifier:@"xpeapp.notifications_check"]
        // check if there is a pending task request or not
        BGTaskScheduler.shared.getPendingTaskRequests { request in
            guard request.isEmpty else {
                debugPrint("A BGTask is already scheduled")
                return
            }
            debugPrint("Try scheduling a new BGTask")
            // Create a new background task request
            let request = BGProcessingTaskRequest(identifier: self.taskId)
            request.requiresNetworkConnectivity = true
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
        let count = UserDefaults.standard.integer(forKey: "task_run_count")
        UserDefaults.standard.set(count+1, forKey: "task_run_count")
        
        Task {
            // Send newsletter notif if needed
            await checkNewNewsletter()

            // Mark the task as completed
            task.setTaskCompleted(success: true)
        }
    }

    private func checkNewNewsletter() async {
            // Étape 1: Récupérer la dernière newsletter depuis l'API
        let obtainedLastNewsletter = await NewsletterRepositoryImpl.instance.getLastNewsletter()
        
        // Étape 2: Vérifier si une newsletter est enregistrée localement via le UserDefaults
        let lastSavedPDFURL = UserDefaults.standard.string(forKey: "lastNewsletterPDFURL")
        
        // Étape 3: Comparer la newsletter locale avec la nouvelle
        if let lastSavedPDFURL = lastSavedPDFURL {
            if lastSavedPDFURL == obtainedLastNewsletter?.pdfUrl {
                debugPrint("Aucune nouvelle newsletter détectée.")
                // Étape 4: Envoyer une notification
                localNotificationsManager.scheduleNotification(
                    title: "Info",
                    message: "Aucune nouvelle newsletter !",
                    secondsFromNow: 1
                )
            } else {
                // Étape 4: Nouvelle newsletter détectée -> Mettre à jour le stockage local
                UserDefaults.standard.set(obtainedLastNewsletter?.pdfUrl, forKey: "lastNewsletterPDFURL")
                
                // Étape 5: Envoyer une notification
                localNotificationsManager.scheduleNotification(
                    title: "Nouvelle newsletter !",
                    message: "Restez informé avec notre nouvelle newsletter !",
                    secondsFromNow: 1
                )
                debugPrint("Nouvelle newsletter détectée et notification envoyée.")
            }

            
        } else {
            debugPrint("Sauvegarde d'une newsletter pour référence !")
            // Étape 4: Nouvelle newsletter détectée -> Mettre à jour le stockage local
            UserDefaults.standard.set(obtainedLastNewsletter?.pdfUrl, forKey: "lastNewsletterPDFURL")
            // Étape 5: Envoyer une notification
            localNotificationsManager.scheduleNotification(
                title: "Info",
                message: "Sauvegarde d'une newsletter pour référence !",
                secondsFromNow: 1
            )
        }
    }

}
