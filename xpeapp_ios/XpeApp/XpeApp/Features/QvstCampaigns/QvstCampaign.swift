//
//  QvstCampaign.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 07/06/2024.
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
    
    // Util function to count days between two dates
    private func countDaysBetween(_ from: Date, and to: Date) -> Int? {
        let calendar = Calendar.current
        let components = calendar.dateComponents([.day], from: from, to: to)
        return components.day
    }
}

extension QvstCampaign{
    // Note(Loucas): This init is needed to decode Dates
    static let dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        formatter.timeZone = TimeZone(secondsFromGMT: 0)
        return formatter
    }()
    
    init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.id = try container.decode(String.self, forKey: .id)
        self.name = try container.decode(String.self, forKey: .name)
        self.theme = try container.decode(QvstTheme.self, forKey: .theme)
        self.status = try container.decode(String.self, forKey: .status)
        self.participationRate = try container.decode(String.self, forKey: .participationRate)
        
        let startDateString = try container.decode(String.self, forKey: .startDate)
        let endDateString = try container.decode(String.self, forKey: .endDate)
        
        guard let startDate = QvstCampaign.dateFormatter.date(from: startDateString),
              let endDate = QvstCampaign.dateFormatter.date(from: endDateString) else {
            throw DecodingError.dataCorruptedError(forKey: .startDate, in: container, debugDescription: "Date string does not match format expected by formatter.")
        }
        
        self.startDate = startDate
        self.endDate = endDate
    }
    
    static func fetchAll() async -> [QvstCampaign]? {
        let endpointUrl = "http://yaki.uat.xpeho.fr:7830/wp-json/xpeho/v1workaround/qvst/campaigns"
        guard let url = URL(string: endpointUrl) else {
            print("Malformed url \(endpointUrl)")
            return nil
        }
        let data: Data
        do {
            data = try await URLSession.shared.data(from: url).0
        } catch {
            print("Error trying to retrieve list of qvst: \(error)")
            return nil
        }

        let decode: [QvstCampaign]
        do {
            decode = try JSONDecoder().decode([QvstCampaign].self, from: data)
        } catch {
            print("Error parsing json: \(error)")
            return nil
        }
        
        return decode
    }
    
    static func fetchActive() async -> [QvstCampaign]? {
        let endpointUrl = "http://yaki.uat.xpeho.fr:7830/wp-json/xpeho/v1workaround/qvst/campaigns:active"
        guard let url = URL(string: endpointUrl) else {
            print("Malformed url \(endpointUrl)")
            return nil
        }
        let data: Data
        do {
            data = try await URLSession.shared.data(from: url).0
        } catch {
            print("Error trying to retrieve list of qvst: \(error)")
            return nil
        }

        let decode: [QvstCampaign]
        do {
            decode = try JSONDecoder().decode([QvstCampaign].self, from: data)
        } catch {
            print("Error parsing json: \(error)")
            return nil
        }
        
        return decode
    }
    
    // Classify campaigns by year and status
    static func classifyCampaigns (campaigns : [QvstCampaign]) -> [String: [QvstCampaign]] {
        var classifiedCampaigns: [String: [QvstCampaign]] = [:]
        for campaign in campaigns {
            if campaign.status == "OPEN" {
                if classifiedCampaigns["open"] == nil {
                    classifiedCampaigns["open"] = []
                }
                classifiedCampaigns["open"]?.append(campaign)
                continue
            }
            
            let year = Calendar.current.component(.year, from: campaign.startDate)
            let yearKey = "\(year)"
            if classifiedCampaigns[yearKey] == nil {
                classifiedCampaigns[yearKey] = []
            }
            classifiedCampaigns[yearKey]?.append(campaign)
        }
        
        return classifiedCampaigns
    }
}
