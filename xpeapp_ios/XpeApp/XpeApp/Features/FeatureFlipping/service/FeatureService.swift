//
//  FeatureService.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 03/09/2024.
//

import Foundation
import FirebaseFirestore

class FeatureService {
    private let db = Firestore.firestore()
    
    func fetchFeatures() async -> [String:Feature]? {
        let collectionName = "featureFlipping"
        
        let querySnapshot: QuerySnapshot
        do {
            querySnapshot = try await XpeAppApp.firestore.collection(collectionName).getDocuments()
        } catch {
            print("Error fetching features: \(error.localizedDescription)")
            return nil
        }
        
        do {
            return try querySnapshot.documents.reduce(into: [String: Feature]()) { dict, doc in
                let d = try doc.data(as: Feature.self)
                let feature = Feature(
                    id: doc.documentID,
                    description: d.description,
                    name: d.name,
                    prodEnabled: d.prodEnabled,
                    uatEnabled: d.uatEnabled
                )
                dict[doc.documentID] = feature
            }
        } catch {
            print("Error parsing features: \(error.localizedDescription)")
            return nil
        }
    }
}
