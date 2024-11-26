//
//  FirebaseAPI.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 11/09/2024.
//

import Foundation
import FirebaseFirestore
import FirebaseStorage

// Protocol to be able to mock the data source
protocol FirebaseAPIProtocol {
    func fetchAllFeatures() async -> [FeatureModel]?
    func fetchAllNewsletters() async -> [NewsletterModel]?
    func getNewsletterPreviewUrl(previewPath: String, completion: @escaping (String?) -> Void)
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
                    summary: d.summary,
                    previewPath: d.previewPath
                )
            }
        } catch {
            debugPrint("Error parsing newsletters: \(error.localizedDescription)")
            return nil
        }
    }
    
    func getNewsletterPreviewUrl(previewPath: String, completion: @escaping (String?) -> Void) {
        guard !previewPath.isEmpty else {
            debugPrint("No preview for newsletter")
            completion(nil)
            return
        }
        
        let storageRef = Storage.storage().reference(withPath: previewPath)
        
        storageRef.downloadURL { url, error in
            if let error = error {
                debugPrint("Error fetching preview URL: \(error.localizedDescription)")
                completion(nil)
            } else if let url = url {
                let previewURL = url.absoluteString
                completion(previewURL)
            }
        }
    }
}
