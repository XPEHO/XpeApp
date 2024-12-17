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
    private let startDate = Date(timeIntervalSinceNow: 30) // Schedule the next task in 2 minutes
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
        debugPrint("Background task Received")
        
        localNotificationsManager.scheduleNotification(
            title: "Test de background task",
            message: "La background task fonctionne !",
            secondsFromNow: 1
        )
        // Mark the task as completed
        task.setTaskCompleted(success: true)

        // Reschedule the task
        self.submitBackgroundTask()
        
        /*
        Task {
            // Send newsletter notif if needed
            await checkNewNewsletter()

            // Mark the task as completed
            task.setTaskCompleted(success: true)

            // Reschedule the task
            self.submitBackgroundTask()
        }
         */
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
        }
    }

}
