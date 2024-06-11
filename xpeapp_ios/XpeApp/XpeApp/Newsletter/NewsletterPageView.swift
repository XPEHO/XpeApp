//
//  NewsletterListView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import SwiftUI
import CoreData

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
        NavigationStack {
            List {
                ForEach(newsletters, id: \.id) { newsletter in
                    NavigationLink {
                        NewsletterDetailsView(newsletter: newsletter)
                    } label: {
                        HStack {
                            Image(systemName: "newspaper")
                            Text("Newsletter du \(NewsletterPageView.formatter.string(from: newsletter.date))")
                        }
                    }
                }
            }
            .navigationTitle("Newsletters")
        }
        .onAppear {
            // Note: This check is done in order not to refetch if state was kept
            if newsletters.isEmpty {
                onAppear()
            }
        }
        .alert("Could not fetch newsletters", isPresented: $newsletterError) {}
    }
    
    struct NewsletterDetailsView: View {
        @State var newsletter: Newsletter
        @State var incorrectURL = false
        
        @Environment(\.openURL) var openURL

        var body: some View  {
            VStack {
                HStack {
                    Spacer()
                    Label(NewsletterPageView.formatter.string(from: newsletter.date), systemImage: "calendar")
                        .font(.title)
                        .padding()
                }
                Spacer()
                Text("Sommaire de la newsletter")
                    .font(.title)
                Text(newsletter.summary.replacingOccurrences(of: ",", with: "\n"))
                    .padding()
                Spacer()
                Button {
                    if let url = URL(string: newsletter.pdfUrl)  {
                        openURL(url)
                    } else {
                        incorrectURL = true
                    }
                } label: { Text("Ouvrir la newsletter") }
                    .buttonStyle(BorderedProminentButtonStyle())
                    .padding()
            }
            .navigationTitle("Détail de la newsletter")
            .navigationBarTitleDisplayMode(.inline)
            .alert("Incorrect newsletter URL", isPresented: $incorrectURL) {
                
            }
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
