//
//  HomePageView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/08/2024.
//

import SwiftUI
import xpeho_ui

struct HomePage: View {
    private var homePageViewModel = HomePageViewModel.instance
    private var featureManager = FeatureManager.instance
    
    var body: some View {
        ScrollView {
            VStack(spacing: 10) {
                if lastNewsletterSectionIsEnabled() {
                    Title(text: "Dernière publication", isFirstPageElement: true)
                    LastNewsletterPreview(
                        lastNewsletter: homePageViewModel.lastNewsletter
                    )
                }
                if toNotMissSectionIsEnabled() {
                    if lastNewsletterSectionIsEnabled() {
                        Title(text: "À ne pas manquer !")
                    } else {
                        Title(text: "À ne pas manquer !", isFirstPageElement: true)
                    }
                    
                    CampaignsList(
                        campaigns: homePageViewModel.activeCampaigns,
                        collapsable: false,
                        defaultOpen: true
                    )
                }
                
                if !lastNewsletterSectionIsEnabled()
                    && !toNotMissSectionIsEnabled() {
                    Title(text: "Rien à l'horizon !", isFirstPageElement: true)
                }
            }
        }
        .onAppear {homePageViewModel.update()}
        .refreshable {homePageViewModel.update()}
        .accessibility(identifier: "HomeView")
    }

    private func lastNewsletterSectionIsEnabled() -> Bool {
        // Check that we have newsletter and that the newsletters are a feature enabled
        if homePageViewModel.lastNewsletter != nil {
            return featureManager.isEnabled(item: .newsletters)
        } else {
            return false
        }
    }

    private func toNotMissSectionIsEnabled() -> Bool {
        // Check that we have active campaigns and that the campaigns are a feature enabled
        if let activeCampaigns = homePageViewModel.activeCampaigns {
            return featureManager.isEnabled(item: .campaign)
            && !activeCampaigns.isEmpty
        } else {
            return false
        }
        
    }
}
