//
//  FirebaseAPI.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 11/09/2024.
//

import Foundation
import FirebaseFirestore

// Protocol to be able to mock the data source
protocol FirebaseAPIProtocol {
    func fetchAllFeatures() async -> [FeatureModel]?
    func fetchAllNewsletters() async -> [NewsletterModel]?
}

class FirebaseAPI: FirebaseAPIProtocol {
    static let instance = FirebaseAPI()
    
    private init() {
        // This initializer is intentionally left empty to make private
        // to prevent use without shared instance
    }
    
    private let db = Firestore.firestore()
    
    func fetchAllFeatures() async -> [FeatureModel]? {
        let collectionName = "featureFlipping"
        
        let querySnapshot: QuerySnapshot
        do {
            querySnapshot = try await XpeAppApp.firestore.collection(collectionName).getDocuments()
        } catch {
            debugPrint("Error fetching features: \(error.localizedDescription)")
            return nil
        }
        
        do {
            return try querySnapshot.documents.map { doc in
                let d = try doc.data(as: FeatureModel.self)
                return FeatureModel(
                    id: doc.documentID,
                    description: d.description,
                    name: d.name,
                    prodEnabled: d.prodEnabled,
                    uatEnabled: d.uatEnabled
                )
            }
        } catch {
            debugPrint("Error parsing features: \(error.localizedDescription)")
            return nil
        }
    }
    
    func fetchAllNewsletters() async -> [NewsletterModel]? {
        let collectionName = "newsletters"
        
        let querySnapshot: QuerySnapshot
        do {
            querySnapshot = try await XpeAppApp.firestore.collection(collectionName).getDocuments()
        } catch {
            debugPrint("Error fetching newsletters: \(error.localizedDescription)")
            return nil
        }
        
        do {
            return try querySnapshot.documents.map { doc in
                let d = try doc.data(as: NewsletterModel.self)
                return NewsletterModel(
                    id: doc.documentID,
                    date: d.date,
                    pdfUrl: d.pdfUrl,
                    publicationDate: d.publicationDate,
                    summary: d.summary
                )
            }
        } catch {
            debugPrint("Error parsing newsletters: \(error.localizedDescription)")
            return nil
        }
    }
}
