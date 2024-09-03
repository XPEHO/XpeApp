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
                if let lastNewsletter = dataManager.newsletters.first {
                    Title(text: "Dernière publication", isFirstPageElement: true)
                    NewsletterPreview(
                        newsletter: lastNewsletter
                    )
                }
                if !dataManager.activeQvstCampaigns.isEmpty {
                    Title(text: "À ne pas manquer !")
                    ForEach(dataManager.activeQvstCampaigns, id: \.id) { campaign in
                        QvstCampaignCard(
                            campaign: campaign,
                            campaignProgress: campaign.findAssociatedProgress(in: dataManager.qvstCampaignsProgress)
                        )
                    }
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
                labelLeft: "Newsletter",
                labelRight: dateFormatter.string(from: newsletter.date),
                imagePreview: Image("NewsletterPreviewTest"),
                pillTags: newsletter.summary.split(separator: ",").map({String($0)}),
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
        
        @State private var tagsList: [String] = []
        
        // Global Management
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
                isCollapsable: false,
                isHeadTagVisible: false,
                // We hide the button if the campaign is closed or completed
                isButtonVisible: (campaign.status == "OPEN") && !isCampaignCompleted(),
                onPressButton: {
                    routerManager.selectedCampaign = campaign
                    routerManager.goTo(item: .qvstDetail)
                }
            )
            .onAppear {
                // Init the tagsList depending the data that we got
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
