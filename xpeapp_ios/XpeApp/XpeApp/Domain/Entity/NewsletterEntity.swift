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
    let date: Date
    let summary: [String]
    let previewPath: String
    
    init(id: String?, pdfUrl: String, date: Date, summary: [String], previewPath: String) {
        self.id = id
        self.pdfUrl = pdfUrl
        self.date = date
        self.summary = summary
        self.previewPath = previewPath
    }
    
    static func from(model: NewsletterModel) -> NewsletterEntity {
        return NewsletterEntity(
            id: model.id,
            pdfUrl: model.pdfUrl,
            date: model.date,
            summary: model.summary.split(separator: ",").map { String($0) },
            previewPath: model.previewPath ?? ""
        )
    }
    
    static func == (lhs: NewsletterEntity, rhs: NewsletterEntity) -> Bool {
        return lhs.id == rhs.id
    }
}
