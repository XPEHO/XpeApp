//
//  QvstRepositoryImpl.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation

class QvstRepositoryImpl: QvstRepository {
    // An instance for app and a mock for tests
    static let instance = QvstRepositoryImpl()
    static let mock = QvstRepositoryImpl(
        dataSource: MockWordpressAPI.instance
    )
    
    // Data source to use
    private let dataSource: WordpressAPIProtocol

    // Make private constructor to prevent use without shared instances
    private init(
        dataSource: WordpressAPIProtocol = WordpressAPI.instance
    ) {
        self.dataSource = dataSource
    }
    
    private func convertCampaignsToEntities(
        campaignsModels: [QvstCampaignModel],
        progressesModels: [QvstProgressModel]
    ) -> [QvstCampaignEntity]{
        
        var campaignsEntities: [QvstCampaignEntity] = []
        for campaign in campaignsModels {
        
            var remainingDays = 0
            if let remainingDaysTry = countDaysBetween(Date(), and: campaign.endDate) {
                remainingDays = remainingDaysTry
            }
            
            var completed = false
            // Try to find if there is an existing progress
            if let campaignProgress = (progressesModels.filter{$0.campaignId == campaign.id}.first) {
                completed = campaignProgress.answeredQuestionsCount >= campaignProgress.totalQuestionsCount
            }
            
            campaignsEntities.append(
                QvstCampaignEntity(
                    id: campaign.id,
                    name: campaign.name,
                    themeName: campaign.theme.name,
                    status: campaign.status,
                    outdated: remainingDays <= 0,
                    completed: completed,
                    remainingDays: remainingDays,
                    endDate: campaign.endDate
                )
            )
        }
        
        return campaignsEntities
    }
    
    // Classify campaigns by year and status
    func classifyCampaigns(campaigns: [QvstCampaignEntity]) -> [String: [QvstCampaignEntity]] {
        return campaigns.reduce(into: [String: [QvstCampaignEntity]]()) { classifiedCampaigns, campaign in
            if campaign.status == "OPEN" {
                classifiedCampaigns["open", default: []].append(campaign)
            } else {
                let year = Calendar.current.component(.year, from: campaign.endDate)
                let yearKey = "\(year)"
                classifiedCampaigns[yearKey, default: []].append(campaign)
            }
        }
    }
    
    func getCampaigns() async -> [QvstCampaignEntity]? {
        // Fetch data
        guard let campaignsModels = await dataSource.fetchAllCampaigns() else {
            debugPrint("Failed call to fetchAllCampaigns in getCampaigns")
            return nil
        }
        guard let user = UserRepositoryImpl.instance.user else {
            debugPrint("No user to use in getCampaigns")
            return nil
        }
        guard let campaignsProgressModels = await dataSource.fetchCampaignsProgress(
            userId: user.id,
            token: user.token
        ) else {
            debugPrint("Failed call to fetchCampaignsProgress in getCampaigns")
            return nil
        }
        
        // Return data
        return convertCampaignsToEntities(
            campaignsModels: campaignsModels,
            progressesModels: campaignsProgressModels
        )
    }
    
    func getActiveCampaigns() async -> [QvstCampaignEntity]? {
        // Fetch data
        guard let activeCampaignsModels = await dataSource.fetchActiveCampaigns() else {
            debugPrint("Failed call to fetchActiveCampaigns in getActiveCampaigns")
            return nil
        }
        guard let user = UserRepositoryImpl.instance.user else {
            debugPrint("No user to use in getCampaigns")
            return nil
        }
        guard let campaignsProgressModels = await dataSource.fetchCampaignsProgress(
            userId: user.id,
            token: user.token
        ) else {
            debugPrint("Failed call to fetchCampaignsProgress in getCampaigns")
            return nil
        }
        
        // Return data
        return convertCampaignsToEntities(
            campaignsModels: activeCampaignsModels,
            progressesModels: campaignsProgressModels
        )
    }
}
