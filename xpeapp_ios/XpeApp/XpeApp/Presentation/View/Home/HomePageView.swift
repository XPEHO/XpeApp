//
//  HomePageView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/08/2024.
//

import SwiftUI
import xpeho_ui

struct HomePage: View {
    @Bindable private var homePageViewModel = HomePageViewModel.instance
    private var featureManager = FeatureManager.instance
    
    var body: some View {
        ScrollView {
            VStack {
                if lastNewsletterSectionIsEnabled() {
                    HStack {
                        Title(text: "Dernière publication")
                        Spacer()
                    }
                    Spacer().frame(height: 16)
                    LastNewsletterPreview(
                        lastNewsletter: homePageViewModel.lastNewsletter
                    )
                    Spacer().frame(height: 32)
                }
                if toNotMissSectionIsEnabled() {
                    if let activeCampaigns = Binding($homePageViewModel.activeCampaigns) {
                        HStack {
                            Title(text: "À ne pas manquer !")
                            Spacer()
                        }
                        Spacer().frame(height: 16)
                        CampaignsList(
                            campaigns: activeCampaigns,
                            collapsable: false,
                            defaultOpen: true
                        )
                    }
                }
                
                if !lastNewsletterSectionIsEnabled()
                    && !toNotMissSectionIsEnabled() {
                    NoContentPlaceHolder()
                }
            }
        }
        .onAppear {homePageViewModel.update()}
        .refreshable {homePageViewModel.update()}
        .accessibility(identifier: "HomeView")
    }

    private func lastNewsletterSectionIsEnabled() -> Bool {
        // Check that the newsletters are a feature enabled
        return featureManager.isEnabled(item: .newsletters)
    }

    private func toNotMissSectionIsEnabled() -> Bool {
        // Check that the campaigns are a feature enabled
        return featureManager.isEnabled(item: .campaign)
        
    }
}
