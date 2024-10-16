//
//  QvstCampaignsPageView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 07/06/2024.
//

import SwiftUI
import xpeho_ui

struct CampaignsPage: View {
    @Bindable var campaignsPageViewModel = CampaignsPageViewModel.instance
    
    // Hide archived campaigns
    @State private var closedCampaignsShowned: Bool = false
    
    var body: some View {
        ScrollView {
            if let classifiedCampaigns = Binding($campaignsPageViewModel.classifiedCampaigns) {
                if classifiedCampaigns.wrappedValue.isEmpty {
                    Text("Rien à l'horizon !")
                } else {
                    VStack(spacing: 10) {
                        // Display open campaigns
                        if classifiedCampaigns.wrappedValue["open"] != nil {
                            Title(text: "Campagnes Ouvertes", isFirstPageElement: true)
                            CampaignsList(
                                campaigns: classifiedCampaigns["open"],
                                collapsable: true,
                                defaultOpen: true
                            )
                        }
                        
                        if closedCampaignsShowned {
                            // Display campaigns by year
                            CampaignsPerYearSection(classifiedCampaigns: classifiedCampaigns)
                        } else {
                            ClickyButton(
                                label: "Voir les campagnes fermées",
                                backgroundColor: .white,
                                labelColor: XPEHO_THEME.CONTENT_COLOR,
                                onPress: showClosedCampaigns
                            )
                            .padding(.top, 20)
                        }
                    }
                }
            } else {
                ProgressView("Chargement des campagnes...")
                    .progressViewStyle(CircularProgressViewStyle())
                    .padding()
            }
        }
        .onAppear {campaignsPageViewModel.update()}
        .refreshable {campaignsPageViewModel.update()}
        .accessibility(identifier: "QvstView")
    }
    
    func showClosedCampaigns() {
        withAnimation {
            closedCampaignsShowned = true
        }
    }
    
    struct CampaignsPerYearSection: View {
        @Binding var classifiedCampaigns: [String: [QvstCampaignEntity]]
        
        var body: some View {
            let keys = classifiedCampaigns.keys.sorted(by: { $0 > $1 })

            ForEach(keys, id: \.self) { key in
                if key != "open" {
                    CampaignsForAYearSection(
                        year: key.capitalized,
                        campaigns: $classifiedCampaigns[key]
                    )
                }
            }
        }
    }
    
    struct CampaignsForAYearSection: View {
        var year: String
        @Binding var campaigns: [QvstCampaignEntity]?
        
        var body: some View {
            if campaigns != nil {
                Title(text: "Campagnes pour \(year)")
                CampaignsList(
                    campaigns: $campaigns,
                    collapsable: true,
                    defaultOpen: false
                )
            }
        }
    }
}
