//
//  NewsletterService.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation
import FirebaseFirestore

class NewsletterService {
    private let db = Firestore.firestore()
    
    func fetchNewsletters() async -> [Newsletter]? {
        let collectionName = "newsletters"
        
        let querySnapshot: QuerySnapshot
        do {
            querySnapshot = try await XpeAppApp.firestore.collection(collectionName).getDocuments()
        } catch {
            print("Error fetching newsletters: \(error.localizedDescription)")
            return nil
        }
        
        do {
            return try querySnapshot.documents.map { doc in
                let d = try doc.data(as: Newsletter.self)
                return Newsletter(
                    id: doc.documentID ,
                    date: d.date,
                    pdfUrl: d.pdfUrl,
                    publicationDate: d.publicationDate,
                    summary: d.summary)
            }
        } catch {
            print("Error parsing newsletters: \(error.localizedDescription)")
            return nil
        }
    }
}
