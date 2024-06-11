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

@main
struct XpeAppApp: App {
    @UIApplicationDelegateAdaptor(XpeAppAppDelegate.self) var delegate
    @StateObject private var dataController = DataController()
    public static var firestore: Firestore = Firestore.firestore()

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environment(\.managedObjectContext, dataController.container.viewContext)
        }
    }
}

// Note(Loucas): This app delegate is used as part
// of the default Firebase SDK setup code
class XpeAppAppDelegate: NSObject, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {
        FirebaseApp.configure()
        Auth.auth().signInAnonymously{ res, err in
            if let err = err {
                print("Error connecting to Firebase anonymously: \(err.localizedDescription)")
                // Todo(Loucas): Handle failure more gracefully
                // maybe an alert "Cannot connect to Firebase"
            }
        }
        return true
    }
}
