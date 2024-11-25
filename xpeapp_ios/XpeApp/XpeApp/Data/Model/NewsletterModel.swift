//
//  NewsletterModel.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation

struct NewsletterModel: Codable {
    var id: String?
    let date: Date
    let pdfUrl: String
    let publicationDate: Date
    let summary: String
    let previewPath: String?
    
    init(id: String? = nil, date: Date, pdfUrl: String, publicationDate: Date, summary: String, previewPath: String? = nil) {
        self.id = id
        self.date = date
        self.pdfUrl = pdfUrl
        self.publicationDate = publicationDate
        self.summary = summary
        self.previewPath = previewPath
    }
    
    func toEntity() -> NewsletterEntity {
        return NewsletterEntity(
            id: self.id,
            pdfUrl: self.pdfUrl,
            date: self.date,
            summary: self.summary.split(separator: ",").map { String($0) },
            previewPath: self.previewPath ?? ""
        )
    }
}
