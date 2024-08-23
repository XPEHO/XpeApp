//
//  QvstService.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation

protocol URLSessionProtocol {
    func data(for request: URLRequest) async throws -> (Data, URLResponse)
    func data(from url: URL) async throws -> (Data, URLResponse)
}

extension URLSession: URLSessionProtocol {}

class QvstService {
    private let session: URLSessionProtocol

    init(session: URLSessionProtocol = URLSession.shared) {
        self.session = session
    }
    
    // Fetch all campaigns
    func fetchAllCampaigns() async throws -> [QvstCampaign]? {
        let endpointUrl = "http://yaki.uat.xpeho.fr:7830/wp-json/xpeho/v1workaround/qvst/campaigns"
        guard let url = URL(string: endpointUrl) else {
            print("Invalid url (fetchAllCampaigns): \(endpointUrl)")
            return nil
        }
        let data: Data
        do {
            data = try await session.data(from: url).0
        } catch {
            print("Error trying to retrieve list of qvst (fetchAllCampaigns): \(error)")
            return nil
        }

        let decode: [QvstCampaign]
        do {
            decode = try JSONDecoder().decode([QvstCampaign].self, from: data)
        } catch {
            print("Error parsing json (fetchAllCampaigns): \(error)")
            return nil
        }
        
        return decode
    }
    
    // Fetch active campaigns
    func fetchActiveCampaigns() async throws -> [QvstCampaign]? {
        let endpointUrl = "http://yaki.uat.xpeho.fr:7830/wp-json/xpeho/v1workaround/qvst/campaigns:active"
        guard let url = URL(string: endpointUrl) else {
            print("Invalid url (fetchActiveCampaigns): \(endpointUrl)")
            return nil
        }
        let data: Data
        do {
            data = try await session.data(from: url).0
        } catch {
            print("Error trying to retrieve list of qvst (fetchActiveCampaigns): \(error)")
            return nil
        }

        let decode: [QvstCampaign]
        do {
            decode = try JSONDecoder().decode([QvstCampaign].self, from: data)
        } catch {
            print("Error parsing json (fetchActiveCampaigns): \(error)")
            return nil
        }
        
        return decode
    }
    
    // Classify campaigns by year and status
    func classifyCampaigns(campaigns: [QvstCampaign]) -> [String: [QvstCampaign]] {
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
    
    // Fetch questions by campaignId
    func fetchCampaignQuestions(campaignId: String) async -> [QvstQuestion]? {
        let endpointUrl = "http://yaki.uat.xpeho.fr:7830/wp-json/xpeho/v1workaround/qvst/campaigns/\(campaignId):questions"
        guard let url = URL(string: endpointUrl) else {
            print("Invalid URL (fetchCampaignQuestions): \(endpointUrl)")
            return nil
        }

        var request = URLRequest(url: url)
        
        do {
            let (data, _) = try await session.data(for: request)
            return try JSONDecoder().decode([QvstQuestion].self, from: data)
        } catch {
            print("Request failed (fetchCampaignQuestions): \(error)")
            return nil
        }
    }
    
    // Send campaign answers
    func sendCampaignAnswers(campaignId: String, userId: String, token: String, questions: [QvstQuestion], answers: [QvstAnswer]) async -> Bool? {
        let endpointUrl = "http://yaki.uat.xpeho.fr:7830/wp-json/xpeho/v1workaround/qvst/campaigns/\(campaignId)/questions:answer"
        guard let url = URL(string: endpointUrl) else {
            print("Invalid URL (sendCampaignAnswers): \(endpointUrl)")
            return nil
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        /*request.setValue(userId, forHTTPHeaderField: "userId")
        request.setValue(token, forHTTPHeaderField: "authorization")*/
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        var formResponses: [QvstFormResponse] = []
        for index in questions.indices {
            formResponses.append(
                QvstFormResponse(
                    questionId: questions[index].id,
                    answerId: answers[index].id
                )
            )
        }
        
        do {
            let jsonData = try JSONEncoder().encode(formResponses)
            request.httpBody = jsonData
            
            let (data, _) = try await session.data(for: request)
            if let dataString = String(data: data, encoding: .utf8) {
                return dataString.contains("true")
            } else {
                return nil
            }
        } catch {
            print("Request failed (sendCampaignAnswers): \(error)")
            return nil
        }
    }
    
    // Fetch progress for each campaign by userId
    func fetchCampaignsProgress(userId: String, token: String) async throws -> [QvstProgress]? {
        let endpointUrl = "http://yaki.uat.xpeho.fr:7830/wp-json/xpeho/v1workaround/campaign-progress/?userId=\(userId)"
        guard let url = URL(string: endpointUrl) else {
            print("Invalid URL (fetchCampaignsProgress): \(endpointUrl)")
            return nil
        }
        
        var request = URLRequest(url: url)
        /*
        request.setValue(userId, forHTTPHeaderField: "userId")
        request.setValue(token, forHTTPHeaderField: "authorization")
         */
        
        do {
            let (data, _) = try await session.data(for: request)
            return try JSONDecoder().decode([QvstProgress].self, from: data)
        } catch {
            print("Request failed (fetchCampaignsProgress): \(error)")
            return nil
        }
    }
}
