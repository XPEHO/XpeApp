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
        
        @State private var tagsList: [String] = []
        
        var isOpen: Bool = false
        
        // Global management
        @EnvironmentObject var routerManager: RouterManager
        
        var body: some View {
            CollapsableCard(
                label: campaign.name,
                headTag: "",
                tags: tagsList,
                // We highlighting the completion if the campaign is completed
                // Otherwise we highlight the deadline if the campaign has not ended
                importantTags: [
                    isCampaignCompleted()
                    ? "Complétée"
                    : !campaign.isOver()
                        ? getDeadlineLabel()
                        : ""
                ],
                buttonLabel: "Compléter",
                icon: Assets.loadImage(named: "QVST"),
                // We highlight in XPEHO color if the campaign is completed
                // Otherwise we highlight in red
                importantTagBackgroundColor: isCampaignCompleted() ? XPEHO_THEME.XPEHO_COLOR : XPEHO_THEME.RED_INFO_COLOR,
                // We highlight the icon if campaign is open
                iconColor: (campaign.status == "OPEN") ? XPEHO_THEME.XPEHO_COLOR : XPEHO_THEME.DISABLED_COLOR,
                isHeadTagVisible: false,
                // We hide the button if the campaign is closed or completed
                isButtonVisible: (campaign.status == "OPEN") && !isCampaignCompleted(),
                isDefaultOpen: isOpen,
                onPressButton: {
                    routerManager.selectedCampaign = campaign
                    routerManager.goTo(item: .campaignForm)
                }
            )
            .onAppear{
                tagsList.append(campaign.theme.name)
                tagsList.append(getDeadlineLabel())
                // If campaign has not ended or is completed we show the completion state
                if !campaign.isOver() || isCampaignCompleted() {
                    tagsList.append(isCampaignCompleted() ? "Complétée" : "À compléter")
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
