//
//  Newsletter.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
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
