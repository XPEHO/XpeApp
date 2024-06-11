//
//  QvstCampaignsPageView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 07/06/2024.
//

import SwiftUI

struct QvstCampaignsPageView: View {
    var campaigns: [QvstCampaign] = []
    
    var body: some View {
        NavigationStack {
            List {
                ForEach(campaigns) {
                    QvstCampaignsPageView.RowView(campaign: $0)
                }
            }
            .navigationTitle("Qualité de vie au travail")
        }
    }
    
    private struct RowView: View {
        var campaign: QvstCampaign
        
        var body: some View {
            NavigationLink {
                
            } label: {
                HStack {
                    VStack(alignment: .leading) {
                        Text(campaign.name)
                        Text(campaign.theme.name)
                            .font(.caption)
                            .italic()
                            .foregroundStyle(.gray)
                    }
                    Spacer()
                    VStack {
                        Image(systemName: "calendar.badge.clock")
                        Text(QvstCampaignsPageView.formatter.string(from: campaign.endDate))
                            .font(.caption)
                    }
                }
            }
        }
    }
    
    static private let formatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        formatter.timeZone = TimeZone(secondsFromGMT: 0)
        return formatter
    }()
}
