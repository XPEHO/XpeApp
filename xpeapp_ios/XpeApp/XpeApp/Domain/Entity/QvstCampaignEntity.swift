//
//  QvstCampaignEntity.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/09/2024.
//

import Foundation

@Observable class QvstCampaignEntity: Identifiable {
    let id: String
    let name: String
    let themeName: String
    let status: String
    let outdated: Bool
    let completed: Bool
    let remainingDays: Int
    let endDate: Date
    
    init(id: String, name: String, themeName: String, status: String, outdated: Bool, completed: Bool, remainingDays: Int, endDate: Date) {
        self.id = id
        self.name = name
        self.themeName = themeName
        self.status = status
        self.outdated = outdated
        self.completed = completed
        self.remainingDays = remainingDays
        self.endDate = endDate
    }
}
