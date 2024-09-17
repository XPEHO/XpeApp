//
//  NewsletterEntity.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/09/2024.
//

import Foundation

@Observable class NewsletterEntity: Equatable {
    let id: String?
    let pdfUrl: String
    let publicationDate: Date
    let summary: [String]
    
    init(id: String?, pdfUrl: String, publicationDate: Date, summary: [String]) {
        self.id = id
        self.pdfUrl = pdfUrl
        self.publicationDate = publicationDate
        self.summary = summary
    }
    
    static func from(model: NewsletterModel) -> NewsletterEntity {
        return NewsletterEntity(
            id: model.id,
            pdfUrl: model.pdfUrl,
            publicationDate: model.publicationDate,
            summary: model.summary.split(separator: ",").map { String($0) }
        )
    }
    
    static func == (lhs: NewsletterEntity, rhs: NewsletterEntity) -> Bool {
        return lhs.id == rhs.id
    }
}
