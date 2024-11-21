//
//  QvstCampaignModel.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation

struct QvstCampaignModel: Codable, Identifiable {
    let id: String
    let name: String
    let theme: QvstThemeModel
    let status: String
    let startDate: Date
    let endDate: Date
    let action: String
    let participationRate: String
    
    enum CodingKeys: String, CodingKey {
        case id
        case name
        case theme
        case status
        case startDate = "start_date"
        case endDate = "end_date"
        case action
        case participationRate = "participation_rate"
    }
    
    init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.id = try container.decode(String.self, forKey: .id)
        self.name = try container.decode(String.self, forKey: .name)
        self.theme = try container.decode(QvstThemeModel.self, forKey: .theme)
        self.status = try container.decode(String.self, forKey: .status)
        self.participationRate = try container.decode(String.self, forKey: .participationRate)
        self.action = try container.decode(String.self, forKey: .action)
        
        let startDateString = try container.decode(String.self, forKey: .startDate)
        let endDateString = try container.decode(String.self, forKey: .endDate)
        
        guard let startDate = QvstCampaignModel.dateDatabaseFormatter.date(from: startDateString),
              let endDate = QvstCampaignModel.dateDatabaseFormatter.date(from: endDateString) else {
            throw DecodingError.dataCorruptedError(forKey: .startDate, in: container, debugDescription: "Date string does not match format expected by formatter.")
        }
        
        self.startDate = startDate
        self.endDate = endDate
    } 

    init(id: String, 
         name: String,
         theme: QvstThemeModel,
         status: String,
         startDate: Date,
         endDate: Date,
         action: String,
         participationRate: String
    ) {
        self.id = id
        self.name = name
        self.theme = theme
        self.status = status
        self.startDate = startDate
        self.endDate = endDate
        self.action = action
        self.participationRate = participationRate
    }
    
    // Formatter for decode
    static let dateDatabaseFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        formatter.timeZone = TimeZone(secondsFromGMT: 0)
        return formatter
    }()
}
