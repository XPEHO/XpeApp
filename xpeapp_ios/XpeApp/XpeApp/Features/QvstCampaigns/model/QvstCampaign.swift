//
//  QvstCampaign.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation

struct QvstCampaign: Codable, Identifiable {
    let id: String
    let name: String
    let theme: QvstTheme
    let status: String
    let startDate: Date
    let endDate: Date
    let participationRate: String
    
    enum CodingKeys: String, CodingKey {
        case id
        case name
        case theme
        case status
        case startDate = "start_date"
        case endDate = "end_date"
        case participationRate = "participation_rate"
    }
    
    init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.id = try container.decode(String.self, forKey: .id)
        self.name = try container.decode(String.self, forKey: .name)
        self.theme = try container.decode(QvstTheme.self, forKey: .theme)
        self.status = try container.decode(String.self, forKey: .status)
        self.participationRate = try container.decode(String.self, forKey: .participationRate)
        
        let startDateString = try container.decode(String.self, forKey: .startDate)
        let endDateString = try container.decode(String.self, forKey: .endDate)
        
        guard let startDate = QvstCampaign.dateDatabaseFormatter.date(from: startDateString),
              let endDate = QvstCampaign.dateDatabaseFormatter.date(from: endDateString) else {
            throw DecodingError.dataCorruptedError(forKey: .startDate, in: container, debugDescription: "Date string does not match format expected by formatter.")
        }
        
        self.startDate = startDate
        self.endDate = endDate
    }
    
    // Find the progress associated to the campaign in the progress base
    func findAssociatedProgress(in progressBase: [QvstProgress]) -> QvstProgress? {
        for progress in progressBase {
            if progress.campaignId == self.id {
                return progress
            }
        }
        return nil
    }
    
    // Know if the campaign has ended
    func isOver() -> Bool {
        return getDaysUntilEnd() <= 0
    }
    
    // Get the number of days until the end of the campaign
    func getDaysUntilEnd() -> Int {
        if let res = countDaysBetween(Date(), and: endDate) {
            return res
        } else {
            return 0
        }
    }
    
    // Formatter for decode
    static let dateDatabaseFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        formatter.timeZone = TimeZone(secondsFromGMT: 0)
        return formatter
    }()
}
