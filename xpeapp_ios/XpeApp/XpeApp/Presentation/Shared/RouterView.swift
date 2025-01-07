//
//  RouterView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 13/09/2024.
//

import SwiftUI

struct Router: View {
    var routerManager = RouterManager.instance
    var featureManager = FeatureManager.instance
    
    var body: some View {
        // Wait for the features to be loaded
        if (featureManager.features != nil){
            Group {
                switch routerManager.selectedView {
                case .home:
                    HomePage()
                case .newsletters where featureManager.isEnabled(item: .newsletters):
                    NewsletterPage()
                case .campaign where featureManager.isEnabled(item: .campaign):
                    CampaignsPage()
                case .campaignForm where featureManager.isEnabled(item: .campaign):
                    CampaignForm()
                case .profile where featureManager.isEnabled(item: .profile):
                    ProfilePage()
                case .expenseReport where featureManager.isEnabled(item: .expenseReport):
                    Text("Expense Report page placeholder")
                case .colleagues where featureManager.isEnabled(item: .colleagues):
                    Text("Colleagues page placeholder")
                case .cra where featureManager.isEnabled(item: .cra):
                    Text("CRA page placeholder")
                case .vacation where featureManager.isEnabled(item: .vacation):
                    Text("Vacation page placeholder")
                case .debug:
                    DebugPage()
                default:
                    DisabledFeaturePlaceHolder()
                }
            }
        } else {
            ProgressView("Chargement des features...")
                .progressViewStyle(CircularProgressViewStyle())
                .padding()
        }
    }
}
