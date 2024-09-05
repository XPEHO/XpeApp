//
//  HomePageView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/08/2024.
//

import SwiftUI
import xpeho_ui

struct HomePageView: View {
    // Global Management
    @EnvironmentObject var dataManager: DataManager
    @EnvironmentObject var routerManager: RouterManager
    @EnvironmentObject var toastManager: ToastManager
    
    var body: some View {
        ScrollView {
            VStack(spacing: 10) {
                if routerManager.isEnabled(.newsletters){
                    if let lastNewsletter = dataManager.newsletters.first {
                        Title(text: "Dernière publication", isFirstPageElement: true)
                        NewsletterPreview(
                            newsletter: lastNewsletter
                        )
                    }
                }
                if !dataManager.activeQvstCampaigns.isEmpty {
                    if routerManager.isEnabled(.campaign){
                        if !routerManager.isEnabled(.newsletters){
                            Title(text: "À ne pas manquer !", isFirstPageElement: true)
                        } else {
                            Title(text: "À ne pas manquer !")
                        }
                    }
                    
                    if routerManager.isEnabled(.campaign){
                        ForEach(dataManager.activeQvstCampaigns, id: \.id) { campaign in
                            QvstCampaignCard(
                                campaign: campaign,
                                campaignProgress: campaign.findAssociatedProgress(in: dataManager.qvstCampaignsProgress)
                            )
                        }
                    }
                }
                if !routerManager.isEnabled(.newsletters)
                    && !routerManager.isEnabled(.campaign) {
                    Title(text: "Rien à l'horizon !", isFirstPageElement: true)
                }
            }
        }
        .accessibility(identifier: "HomeView")
    }
    
    struct NewsletterPreview: View {
        @State var newsletter: Newsletter
        
        @Environment(\.openURL) var openURL
        
        // Global Management
        @EnvironmentObject var toastManager: ToastManager
        
        var body: some View {
            FilePreviewButton (
                labelStart: "Newsletter",
                labelEnd: dateFormatter.string(from: newsletter.date),
                imagePreview: AnyView(
                    Image("NewsletterPreviewTest")
                        .resizable()
                ),
                tags: newsletter.summary.split(separator: ",").map({
                    TagPill(
                        label: String($0)
                    )
                }),
                height: 200,
                onPress: {
                    if let url = URL(string: newsletter.pdfUrl)  {
                        openURL(url)
                    } else {
                        toastManager.message = "URL de Newsletter incorrecte"
                        toastManager.isError = true
                        toastManager.show()
                    }
                }
            )
        }
    }
    
    struct QvstCampaignCard: View {
        @State var campaign: QvstCampaign
        @State var campaignProgress: QvstProgress?
        
        @State private var tagsList: [TagPill] = []
        
        // Global Management
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
                        .foregroundStyle(XPEHO_THEME.XPEHO_COLOR)
                ),
                collapsable: false
            )
            .onAppear {
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
