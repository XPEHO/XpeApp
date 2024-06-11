//
//  Newsletter.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 06/06/2024.
//

import Foundation

struct Newsletter: Codable {
    var id: String?
    let date: Date
    let pdfUrl: String
    let publicationDate: Date
    let summary: String
    
    init(id: String? = nil, date: Date, pdfUrl: String, publicationDate: Date, summary: String) {
        self.id = id
        self.date = date
        self.pdfUrl = pdfUrl
        self.publicationDate = publicationDate
        self.summary = summary
    }
}

import FirebaseFirestore

extension Newsletter {
    static func fetchAll() async -> [Newsletter]? {
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
