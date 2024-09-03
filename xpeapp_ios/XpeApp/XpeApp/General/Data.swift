//
//  Data.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 22/08/2024.
//

import SwiftUI

class DataManager: ObservableObject {
    // Feature Flipping
    @Published var features: [String:Feature] = [:]
    
    private var featureService: FeatureService?
    
    // Newsletters
    @Published var newsletters: [Newsletter] = []
    
    private var newsletterService: NewsletterService?
    
    // QVST
    @Published var qvstCampaigns: [QvstCampaign] = []
    @Published var activeQvstCampaigns: [QvstCampaign] = []
    @Published var qvstCampaignsProgress: [QvstProgress] = []
    @Published var classifiedCampaigns: [String: [QvstCampaign]] = [:]
    
    private var qvstService: QvstService?
    
    // Loading state
    @Published var isDataLoaded: Bool = false
    
    // Classification for QVST
    func classifyCampaigns() {
        guard let qvstService = self.qvstService else {
            return
        }
        
        DispatchQueue.main.async {
            self.classifiedCampaigns = qvstService.classifyCampaigns(campaigns: self.qvstCampaigns)
        }
    }
    
    // Global initialisation
    func initData() async {
        
        // Initialise services 
        // (need to be done here to make sure FirebaseApp.configure() has enough time to be called)
        if featureService == nil {
            featureService = FeatureService()
        }
        if newsletterService == nil {
            newsletterService = NewsletterService()
        }
        if qvstService == nil {
            qvstService = QvstService()
        }

        // Initialise data
        await initFeatures()
        await initNewsletters()
        await initActiveQvstCampaigns()
        await initQvstCampaigns()
        await initQvstCampaignsProgress()

        // Notify that data is firstly loaded
        if !self.isDataLoaded {
            DispatchQueue.main.async {
                self.isDataLoaded = true
            }
        }
    }
    
    // Initialisation parts
    private func initFeatures() async {
        guard let featureService = self.featureService else {
            return
        }
        
        // Fetch newsletters
        guard let features = await featureService.fetchFeatures() else {
            print("Failed to fetch features")
            return
        }
        // Note: We have to do UI updates on the main thread to prevent crashes
        DispatchQueue.main.async {
            self.features = features
        }
    }
    
    private func initNewsletters() async {
        guard let newsletterService = self.newsletterService else {
            return
        }
        
        // Fetch newsletters
        guard let newsletters = await newsletterService.fetchNewsletters() else {
            print("Failed to fetch newsletters")
            return
        }
        // Note: We have to do UI updates on the main thread to prevent crashes
        let sortedNewsletters = newsletters.sorted(by: { $0.date > $1.date })
        DispatchQueue.main.async {
            self.newsletters = sortedNewsletters
        }
    }
    
    private func initQvstCampaigns() async {
        guard let qvstService = self.qvstService else {
            return
        }
        
        // Fetch qvst campaigns
        do {
            guard let campaigns = try await qvstService.fetchAllCampaigns() else {
                print("Failed to fetch QVST campaigns")
                return
            }
            // Note: We have to do UI updates on the main thread to prevent crashes
            let sortedCampaigns = campaigns.sorted(by: { $0.startDate > $1.startDate })
            DispatchQueue.main.async {
                self.qvstCampaigns = sortedCampaigns
            }
        } catch {
            print("Error fetching QVST campaigns: \(error)")
        }
    }
    
    private func initActiveQvstCampaigns() async {
        guard let qvstService = self.qvstService else {
            return
        }
        
        // Fetch active qvst campaigns
        do {
            guard let campaigns = try await qvstService.fetchActiveCampaigns() else {
                print("Failed to fetch active QVST campaigns")
                return
            }
            DispatchQueue.main.async {
                self.activeQvstCampaigns = campaigns.sorted(by: { $0.startDate > $1.startDate })
            }
        } catch {
            print("Error fetching active QVST campaigns: \(error)")
        }
    }
    
    private func initQvstCampaignsProgress() async {
        // Get user id for the request
        guard let userId = await fetchUserId(email: EMAIL) else {
            print("Failed to fetch user ID")
            return
        }
        
        guard let qvstService = self.qvstService else {
            return
        }
        
        // Fetch progress for each qvst campaign
        do {
            guard let campaignsProgress = try await qvstService.fetchCampaignsProgress(userId: userId, token: TOKEN) else {
                print("Failed to fetch QVST campaigns progress")
                return
            }
            // Note: We have to do UI updates on the main thread to prevent crashes
            DispatchQueue.main.async {
                self.qvstCampaignsProgress = campaignsProgress.sorted(by: { $0.campaignId < $1.campaignId })
            }
        } catch {
            print("Error fetching QVST campaigns progress: \(error)")
        }
    }
}
