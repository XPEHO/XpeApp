//
//  NewsletterPageView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import SwiftUI
import CoreData
import xpeho_ui

struct NewsletterPage: View {
    var newsletterPageViewModel = NewsletterPageViewModel.instance
    
    var body: some View {
        ScrollView {
            if let newsletters = newsletterPageViewModel.newsletters {
                if newsletters.isEmpty {
                    Text("Rien à l'horizon !")
                } else {
                    VStack(spacing: 10) {
                        NewslettersList(newsletters: newsletters)
                    }
                }
            } else {
                ProgressView("Chargement des newsletters...")
                    .progressViewStyle(CircularProgressViewStyle())
                    .padding()
            }
        }
        .onAppear {newsletterPageViewModel.update()}
        .refreshable {newsletterPageViewModel.update()}
        .accessibility(identifier: "NewsletterView")
    }
}
