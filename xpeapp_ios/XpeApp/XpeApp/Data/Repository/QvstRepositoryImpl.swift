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
        dataSource: MockWordpressAPI.instance,
        userRepo: UserRepositoryImpl.mock
    )
    
    // Data source and user repo to use (so we can mock them)
    private let dataSource: WordpressAPIProtocol
    private let userRepo: UserRepositoryImpl

    // Make private constructor to prevent use without shared instances
    private init(
        dataSource: WordpressAPIProtocol = WordpressAPI.instance,
        userRepo: UserRepositoryImpl = UserRepositoryImpl.instance
    ) {
        self.dataSource = dataSource
        self.userRepo = userRepo
    }
    
    private func convertCampaignsToEntities(
        campaignsModels: [QvstCampaignModel],
        progressesModels: [QvstProgressModel]
    ) -> [QvstCampaignEntity]{
        
        var campaignsEntities: [QvstCampaignEntity] = []

        // Filter out campaigns with status "DRAFT"
        let filteredCampaignsModels = campaignsModels.filter { $0.status != "DRAFT" }
        
        for campaign in filteredCampaignsModels {
        
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
                    endDate: campaign.endDate,
                    resultLink: campaign.action
                )
            )
        }
        
        return campaignsEntities.sorted { $0.endDate > $1.endDate }
    }
    
    // Classify campaigns by year and status
    func classifyCampaigns(campaigns: [QvstCampaignEntity]) -> [Int: [QvstCampaignEntity]] {
        return campaigns.reduce(into: [Int: [QvstCampaignEntity]]()) { classifiedCampaigns, campaign in
            let year = Calendar.current.component(.year, from: campaign.endDate)
            classifiedCampaigns[year, default: []].append(campaign)
        }
    }
    
    func getCampaigns() async -> [QvstCampaignEntity]? {
        // Fetch data
        guard let user = userRepo.user else {
            debugPrint("No user to use in getCampaigns")
            return nil
        }
        guard let campaignsModels = await dataSource.fetchAllCampaigns() else {
            debugPrint("Failed call to fetchAllCampaigns in getCampaigns")
            return nil
        }
        guard let campaignsProgressModels = await dataSource.fetchCampaignsProgress(
            userId: user.id
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
        guard let user = userRepo.user else {
            debugPrint("No user to use in getCampaigns")
            return nil
        }
        guard let activeCampaignsModels = await dataSource.fetchActiveCampaigns() else {
            debugPrint("Failed call to fetchActiveCampaigns in getActiveCampaigns")
            return nil
        }
        guard let campaignsProgressModels = await dataSource.fetchCampaignsProgress(userId: user.id) else {
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
