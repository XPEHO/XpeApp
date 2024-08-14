//
//  HomePageView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/08/2024.
//

import SwiftUI
import xpeho_ui

struct HomePageView: View {
    var lastNewsletter: Newsletter? = nil
    @Binding var activeQvstCampaigns: [QvstCampaign]
    
    var body: some View {
        ScrollView {
            VStack(spacing: 10) {
                if let lastNewsletter = lastNewsletter {
                    Title(text: "Dernière publication", isFirstPageElement: true)
                    NewsletterPreview(newsletter: lastNewsletter)
                }
                if !activeQvstCampaigns.isEmpty {
                    Title(text: "À ne pas manquer !")
                    ForEach(activeQvstCampaigns, id: \.id) { campaign in
                        QvstCampaignCard(campaign: campaign)
                    }
                }
            }
        }
        .accessibility(identifier: "HomeView")
    }
    
    struct NewsletterPreview: View {
        @State var newsletter: Newsletter
        @State var incorrectURL = false
        
        @Environment(\.openURL) var openURL
        
        var body: some View {
            FilePreviewButton (
                labelLeft: "Newsletter",
                labelRight: HomePageView.formatter.string(from: newsletter.date),
                imagePreview: Image("NewsletterPreviewTest"),
                pillTags: newsletter.summary.split(separator: ",").map({String($0)}),
                height: 200,
                onPress: {
                    if let url = URL(string: newsletter.pdfUrl)  {
                        openURL(url)
                    } else {
                        incorrectURL = true
                    }
                }
            )
            .alert("Incorrect newsletter URL", isPresented: $incorrectURL) {}
        }
    }
    
    struct QvstCampaignCard: View {
        @State var campaign: QvstCampaign
        
        var body: some View {
            CollapsableCard(
                label: campaign.name,
                headTag: "",
                tags: [campaign.theme.name, HomePageView.formatter.string(from: campaign.endDate), "_/_ complétés"],
                importantTags: [HomePageView.formatter.string(from: campaign.endDate)],
                buttonLabel: "Compléter",
                icon: Assets.loadImage(named: "QVST"),
                isCollapsable: false,
                isHeadTagVisible: false,
                onPressButton: {
                    debugPrint("Campaign: \(campaign.name) is pressed")
                }
            )
        }
    }
    
    // Date formatter for display
    private static let formatter: DateFormatter = {
        let df = DateFormatter()
        df.locale = Locale(identifier: "fr_FR")
        df.dateFormat = "dd/MM/yyyy"
        return df
    }()
}
