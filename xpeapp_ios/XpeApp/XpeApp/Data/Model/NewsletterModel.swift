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
    
    init(id: String? = nil, date: Date, pdfUrl: String, publicationDate: Date, summary: String) {
        self.id = id
        self.date = date
        self.pdfUrl = pdfUrl
        self.publicationDate = publicationDate
        self.summary = summary
    }
    
    func toEntity() -> NewsletterEntity {
        return NewsletterEntity(
            id: self.id,
            pdfUrl: self.pdfUrl,
            publicationDate: self.publicationDate,
            summary: self.summary.split(separator: ",").map { String($0) }
        )
    }
}
