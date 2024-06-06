//
//  Newsletter.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 06/06/2024.
//

import Foundation
import FirebaseFirestore
import FirebaseAuth

struct Newsletter: Codable {
    @DocumentID var id: String?
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

// Note(Loucas): This is to allow fetching newsletters from firebase
// Todo(Loucas): Ensure that having a Firestore.firestore() call per class
// doesn't recreate connections each time.
extension Newsletter {
    init?(document: DocumentSnapshot) {
        do {
            let data = try document.data(as: Newsletter.self)
            self = data
        } catch {
            print("Error initializing newsletter: \(error.localizedDescription)")
            return nil
        }
    }
    
    static private let db = Firestore.firestore()
    static private let collectionName = "newsletters"
    static func fetchAll() async -> [Newsletter]? {
        do {
            let querySnapshot = try await db.collection(collectionName).getDocuments()
            return querySnapshot.documents.map { doc in Newsletter(document: doc)! }
        } catch {
            print("Error fetching newsletters: \(error.localizedDescription)")
            return nil
        }
    }
}
