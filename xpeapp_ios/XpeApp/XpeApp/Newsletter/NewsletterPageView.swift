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
    @Environment(\.managedObjectContext) var moc
    @FetchRequest(sortDescriptors: []) private var cdNewsletters: FetchedResults<CDNewsletter>
    @State private var newsletters: [Newsletter] = []
    @State private var newsletterError = false

    private static let formatter: DateFormatter = {
        let df = DateFormatter()
        df.locale = Locale(identifier: "fr_FR")
        df.dateFormat = "dd/MM/yyyy"
        return df
    }()
    
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
            .onAppear {
                // Note: This check is done in order not to refetch if state was kept
                if newsletters.isEmpty {
                    onAppear()
                }
            }
            .alert("Could not fetch newsletters", isPresented: $newsletterError) {}
        }
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
    
    private func onAppear() {
        newsletters = cdNewsletters.map { cdn in
            return Newsletter(
                id: cdn.id ?? "",
                date:cdn.date ?? Date(),
                pdfUrl: cdn.pdfUrl ?? "",
                publicationDate: cdn.publicationDate ?? Date(),
                summary: cdn.summary ?? "")
        }.sorted(by: {$0.date > $1.date})
        
        
        Task {
            guard let newNs = await Newsletter.fetchAll() else { return } // Todo: add error logging
            DispatchQueue.main.async {
                newsletters = newNs.sorted(by: {$0.date > $1.date})
            }
            
            // Note: We repopulate the cache because the data could have
            // changed in existing and new newsletters since last time
            for cdn in cdNewsletters {
                moc.delete(cdn)
            }
            
            for n in newNs {
                let cdn = CDNewsletter(context: moc)
                cdn.id = n.id
                cdn.date = n.date
                cdn.pdfUrl = n.pdfUrl
                cdn.publicationDate = n.publicationDate
                cdn.summary = n.summary
                do {
                    try moc.save()
                } catch {
                    print("Error trying to recreate newsletter cache: \(error)")
                }
            }
        }
    }
}
