//
//  HomePageViewModel.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/09/2024.
//

import Foundation

@Observable class HomePageViewModel {
    
    static let instance = HomePageViewModel()
    
    // Make private constructor to prevent use without shared instance
    private init() {
        initLastNewsletter()
        initActiveCampaigns()
    }

    var lastNewsletter: NewsletterEntity? = nil
    var activeCampaigns: [QvstCampaignEntity]? = nil
    
    func update() {
        initLastNewsletter()
        initActiveCampaigns()
    }
    
    private func initLastNewsletter() {
        Task{
            let obtainedLastNewsletter = await NewsletterRepositoryImpl.instance.getLastNewsletter()
            DispatchQueue.main.async {
                self.lastNewsletter = obtainedLastNewsletter
            }
        }
    }
    
    private func initActiveCampaigns() {
        Task{
            let obtainedActiveCampaigns = await QvstRepositoryImpl.instance.getActiveCampaigns()
            DispatchQueue.main.async {
                self.activeCampaigns = obtainedActiveCampaigns
            }
        }
    }
}
