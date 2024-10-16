//
//  ActiveCampaignsListView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 13/09/2024.
//

import SwiftUI

struct CampaignsList: View {
    @Binding var campaigns: [QvstCampaignEntity]?
    var collapsable: Bool = true
    var defaultOpen: Bool = false
    
    var body: some View {
        if let campaignsUnwrapped = Binding($campaigns) {
            ForEach(campaignsUnwrapped.indices, id: \.self) { indices in
                CampaignCard(
                    campaign: campaignsUnwrapped[indices],
                    collapsable: collapsable,
                    defaultOpen: defaultOpen
                )
            }
        } else {
            ProgressView("Chargement des campagnes...")
                .progressViewStyle(CircularProgressViewStyle())
                .padding()
        }
    }
}
