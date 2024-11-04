//
//  CampaignsPageViewModel.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 13/09/2024.
//

import Foundation

@Observable class CampaignsPageViewModel {
    
    static let instance = CampaignsPageViewModel()
    
    // Make private constructor to prevent use without shared instance
    private init() {
        initCampaigns()
    }

    var campaigns: [QvstCampaignEntity]? = nil
    var classifiedCampaigns: [Int: [QvstCampaignEntity]]? = nil
    
    func update() {
        initCampaigns()
    }
    
    private func initCampaigns() {
        Task{
            if let obtainedCampaigns = await QvstRepositoryImpl.instance.getCampaigns() {
                let obtainedClassifiedCampaigns = QvstRepositoryImpl.instance.classifyCampaigns(campaigns: obtainedCampaigns)
                DispatchQueue.main.async {
                    self.campaigns = obtainedCampaigns
                    self.classifiedCampaigns = obtainedClassifiedCampaigns
                }
            }
        }
    }
}
