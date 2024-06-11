//
//  DataController.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 07/06/2024.
//

import Foundation
import CoreData

class DataController: ObservableObject {
    let container = NSPersistentContainer(name: "XpeApp")

    init() {
        container.loadPersistentStores { description, error in
            if let error = error {
                print("Core Data failed to load: \(error.localizedDescription)")
            }
        }
    }
}
