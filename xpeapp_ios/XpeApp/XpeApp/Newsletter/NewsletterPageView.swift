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
    var newsletters: [Newsletter] = []
    @State private var newsletterError = false
    
    var body: some View {
        ScrollView {
            VStack(spacing: 10) {
                ForEach(Array(newsletters.enumerated()), id: \.element.id) { index, newsletter in
                    if index == 0 {
                        NewsletterCard(newsletter: newsletter, isOpen: true)
                    } else {
                        NewsletterCard(newsletter: newsletter)
                    }
                }
            }
            .alert("Could not fetch newsletters", isPresented: $newsletterError) {}
        }
        .accessibility(identifier: "NewsletterView")
    }
    
    struct NewsletterCard: View {
        @State var newsletter: Newsletter
        @State var incorrectURL = false
        
        @Environment(\.openURL) var openURL
        
        var isOpen: Bool = false
        
        var body: some View {
            CollapsableCard(
                label: "Newsletter",
                headTag: NewsletterPageView.formatter.string(from: newsletter.date),
                tags: newsletter.summary.split(separator: ",").map({String($0)}),
                importantTags: [],
                buttonLabel: "Ouvrir",
                icon: Assets.loadImage(named: "Newsletter"),
                isDefaultOpen: isOpen,
                onPressButton: {
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
    
    // Date formatter for display
    private static let formatter: DateFormatter = {
        let df = DateFormatter()
        df.locale = Locale(identifier: "fr_FR")
        df.dateFormat = "dd/MM/yyyy"
        return df
    }()
}
