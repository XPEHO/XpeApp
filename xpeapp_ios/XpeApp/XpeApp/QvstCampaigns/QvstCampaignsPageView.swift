//
//  QvstCampaignsPageView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 07/06/2024.
//

import SwiftUI
import xpeho_ui

struct QvstCampaignsPageView: View {
    var campaigns: [QvstCampaign] = []
    private var classifiedCampaigns: [String: [QvstCampaign]] = [:]
    
    @State private var showClosedCampaigns: Bool = false
    
    init(campaigns: [QvstCampaign]) {
        self.campaigns = campaigns
        
        // Temp changes for tests (need properties to be var)
        /*self.campaigns[0].status = "OPEN"
        let dateComponents = DateComponents(year: 2023, month: 1, day: 1)
        if let date = Calendar.current.date(from: dateComponents) {
            self.campaigns[1].startDate = date
        }*/
        
        // Classify campaigns by year and status
        self.classifiedCampaigns = QvstCampaign.classifyCampaigns(campaigns: self.campaigns)
    }
    
    var body: some View {
        ScrollView {
            VStack(spacing: 10) {
                // Display open campaigns
                if let activeCampaigns = classifiedCampaigns["open"] {
                    Title(text: "Campagnes Ouvertes", isFirstPageElement: true)
                    ForEach(activeCampaigns, id: \.id) { campaign in
                        QVSTCard(campaign: campaign)
                    }
                }
                
                if showClosedCampaigns {
                    // Display campaigns by year
                    let keys = classifiedCampaigns.keys.sorted(by: { $0 > $1 })
                    ForEach(keys, id: \.self) { key in
                        if key != "open" {
                            if let campaigns = classifiedCampaigns[key] {
                                Title(text: "Campagnes pour \(key.capitalized)")
                                ForEach(campaigns, id: \.id) { campaign in
                                    QVSTCard(campaign: campaign)
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
        .accessibility(identifier: "QvstView")
    }
    
    struct QVSTCard: View {
        var campaign: QvstCampaign
        
        var isOpen: Bool = false
        
        var body: some View {
            CollapsableCard(
                label: campaign.name,
                headTag: "",
                tags: [campaign.theme.name, QvstCampaignsPageView.formatter.string(from: campaign.endDate), "_/_ complétés"],
                importantTags: [],
                buttonLabel: "Compléter",
                icon: Assets.loadImage(named: "QVST"),
                iconColor: (campaign.status == "OPEN") ? XPEHO_THEME.XPEHO_COLOR : XPEHO_THEME.DISABLED_COLOR,
                isHeadTagVisible: false,
                isButtonVisible: (campaign.status == "OPEN"),
                isDefaultOpen: isOpen,
                onPressButton: {
                    debugPrint("Campaign: \(campaign.name) is pressed")
                }
            )
        }
    }
    
    // Date formatter for display
    static private let formatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        formatter.timeZone = TimeZone(secondsFromGMT: 0)
        return formatter
    }()
}
