//
//  NewsletterListView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import SwiftUI

struct NewsletterPageView: View {
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
            Task {
                if let newNs = await Newsletter.fetchAll() {
                    newsletters = newNs.sorted(by: { $0.date > $1.date })
                } else {
                    newsletterError = true
                }
            }
        }
        .alert("Could not fetch newsletters", isPresented: $newsletterError) {
            
        }
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
}
