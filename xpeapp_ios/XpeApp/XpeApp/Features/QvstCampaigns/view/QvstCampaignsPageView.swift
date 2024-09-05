//
//  QvstCampaignsPageView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 07/06/2024.
//

import SwiftUI
import xpeho_ui

struct QvstCampaignsPageView: View {
    
    // Hide archived campaigns
    @State private var showClosedCampaigns: Bool = false
    
    // Global Management
    @EnvironmentObject var dataManager: DataManager
    @EnvironmentObject var routerManager: RouterManager
    
    var body: some View {
        ScrollView {
            if dataManager.qvstCampaigns.isEmpty {
                ProgressView("Chargement des campagnes...")
                    .progressViewStyle(CircularProgressViewStyle())
                    .padding()
            } else {
                VStack(spacing: 10) {
                    // Display open campaigns
                    if let activeCampaigns = dataManager.classifiedCampaigns["open"] {
                        Title(text: "Campagnes Ouvertes", isFirstPageElement: true)
                        ForEach(activeCampaigns, id: \.id) { campaign in
                            QVSTCard(
                                campaign: campaign,
                                campaignProgress: campaign.findAssociatedProgress(in: dataManager.qvstCampaignsProgress),
                                isOpen: true
                            )
                        }
                    }
                    
                    if showClosedCampaigns {
                        // Display campaigns by year
                        let keys = dataManager.classifiedCampaigns.keys.sorted(by: { $0 > $1 })
                        ForEach(keys, id: \.self) { key in
                            if key != "open" {
                                if let campaigns = dataManager.classifiedCampaigns[key] {
                                    Title(text: "Campagnes pour \(key.capitalized)")
                                    ForEach(campaigns, id: \.id) { campaign in
                                        QVSTCard(
                                            campaign: campaign
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        ClickyButton(
                            label: "Voir les campagnes fermées",
                            backgroundColor: .white,
                            labelColor: XPEHO_THEME.CONTENT_COLOR,
                            onPress: {
                                withAnimation {
                                    showClosedCampaigns = true
                                }
                            }
                        )
                        .padding(.top, 20)
                    }
                }
            }
        }
        .accessibility(identifier: "QvstView")
        .onAppear {
            dataManager.classifyCampaigns()
        }
    }
    
    struct QVSTCard: View {
        @State var campaign: QvstCampaign
        @State var campaignProgress: QvstProgress?
        
        @State private var tagsList: [TagPill] = []
        
        var isOpen: Bool = false
        
        // Global management
        @EnvironmentObject var routerManager: RouterManager
        
        var body: some View {
            CollapsableCard(
                label: campaign.name,
                tags: tagsList,
                // We hide the button if the campaign is closed or completed
                button: (campaign.status == "OPEN") && !isCampaignCompleted()
                    ? ClickyButton(
                        label: "Compléter",
                        horizontalPadding: 50,
                        verticalPadding: 12,
                        onPress: {
                            routerManager.selectedCampaign = campaign
                            routerManager.goTo(item: .campaignForm)
                        }
                    )
                    : nil,
                icon: AnyView(
                    Assets.loadImage(named: "QVST")
                        .renderingMode(.template)
                        .foregroundStyle(
                            (campaign.status == "OPEN")
                            ? XPEHO_THEME.XPEHO_COLOR
                            : XPEHO_THEME.GRAY_LIGHT_COLOR
                        )
                ),
                defaultOpen: isOpen
            )
            .onAppear{
                // Init the tagsList depending the data that we got
                tagsList.append(
                    TagPill(
                        label: campaign.theme.name,
                        backgroundColor: XPEHO_THEME.GREEN_DARK_COLOR
                    )
                )

                // If campaign has not ended we show the deadline with color that depends on the completion state
                if (!campaign.isOver()) {
                    tagsList.append(
                        TagPill(
                            label: getDeadlineLabel(),
                            backgroundColor: isCampaignCompleted() ? XPEHO_THEME.GREEN_DARK_COLOR : XPEHO_THEME.RED_INFO_COLOR
                        )
                    )
                }
                
                // If campaign is completed we show the completion state whatever the case
                // Else if it has not ended we show the completion state to indicate it can always be completed
                if (isCampaignCompleted()) {
                    tagsList.append(
                        TagPill(
                            label: "Complétée",
                            backgroundColor: XPEHO_THEME.XPEHO_COLOR
                        )
                    )
                } else {
                    tagsList.append(
                        TagPill(
                            label: "À compléter",
                            backgroundColor: XPEHO_THEME.GREEN_DARK_COLOR
                        )
                    )
                }
            }
        }
        
        // Get the content for the pill of deadline
        private func getDeadlineLabel() -> String {
            var deadline: String
            if campaign.isOver() {
                deadline = dateFormatter.string(from: campaign.endDate)
            } else {
                deadline = String(campaign.getDaysUntilEnd()) + " jours restants"
            }
            return deadline
        }
        
        // Know if the campaign is completed
        private func isCampaignCompleted() -> Bool {
            if let progress = campaignProgress {
                return progress.answeredQuestionsCount >= progress.totalQuestionsCount
            } else {
                return false
            }
        }
    }
}
