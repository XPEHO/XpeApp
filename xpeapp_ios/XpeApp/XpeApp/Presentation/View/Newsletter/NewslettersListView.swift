//
//  NewslettersListView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 18/09/2024.
//

import SwiftUI

struct NewslettersList: View {
    var newsletters: [NewsletterEntity]?
    var collapsable: Bool = true
    var defaultOpen: Bool = false
    
    var body: some View {
        if let newsletters = newsletters {
            ForEach(newsletters, id: \.id) { newsletter in
                if newsletter == newsletters.first {
                    NewsletterCard(newsletter: newsletter, open: true)
                } else {
                    NewsletterCard(newsletter: newsletter)
                }
            }
        } else {
            ProgressView("Chargement des newsletters...")
                .progressViewStyle(CircularProgressViewStyle())
                .padding()
        }
    }
}
