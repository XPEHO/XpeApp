//
//  ActiveCampaignsListView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 13/09/2024.
//

import SwiftUI

struct CampaignsList: View {
    var campaigns: [QvstCampaignEntity]?
    var collapsable: Bool = true
    var defaultOpen: Bool = false
    
    var body: some View {
        if let campaigns = campaigns {
            ForEach(campaigns, id: \.id){ campaign in
                CampaignCard(
                    campaign: campaign,
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
