//
//  ActiveCampaignsListView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 13/09/2024.
//

import SwiftUI

struct CampaignsList: View {
    @Binding var campaigns: [QvstCampaignEntity]
    var collapsable: Bool = true
    var defaultOpen: Bool = false
    
    var body: some View {
        if campaigns.isEmpty {
            NoContentPlaceHolder()
        } else {
            VStack(spacing: 10) {
                ForEach(campaigns.indices, id: \.self) { indices in
                    CampaignCard(
                        campaign: $campaigns[indices],
                        collapsable: collapsable,
                        defaultOpen: defaultOpen
                    )
                }
            }
        }
    }
}
