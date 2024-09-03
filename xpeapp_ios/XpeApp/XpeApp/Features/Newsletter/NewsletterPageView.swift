//
//  NewsletterListView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import SwiftUI
import CoreData
import xpeho_ui

struct NewsletterPageView: View {
    // Global Management
    @EnvironmentObject var dataManager: DataManager
    @EnvironmentObject var toastManager: ToastManager
    
    var body: some View {
        ScrollView {
            if dataManager.newsletters.isEmpty {
                ProgressView("Chargement des newsletters...")
                    .progressViewStyle(CircularProgressViewStyle())
                    .padding()
            } else {
                VStack(spacing: 10) {
                    ForEach(Array(dataManager.newsletters.enumerated()), id: \.element.id) { index, newsletter in
                        if index == 0 {
                            NewsletterCard(newsletter: newsletter, isOpen: true)
                        } else {
                            NewsletterCard(newsletter: newsletter)
                        }
                    }
                }
            }
        }
        .accessibility(identifier: "NewsletterView")
    }
    
    struct NewsletterCard: View {
        @State var newsletter: Newsletter
        
        @Environment(\.openURL) var openURL
        
        var isOpen: Bool = false
        
        // Global management
        @EnvironmentObject var toastManager: ToastManager
        
        var body: some View {
            CollapsableCard(
                label: "Newsletter",
                headTag: dateFormatter.string(from: newsletter.date),
                tags: newsletter.summary.split(separator: ",").map({String($0)}),
                importantTags: [],
                buttonLabel: "Ouvrir",
                icon: Assets.loadImage(named: "Newsletter"),
                isDefaultOpen: isOpen,
                onPressButton: {
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
}
