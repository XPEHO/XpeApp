//
//  NewslettersListView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 18/09/2024.
//

import SwiftUI

struct NewslettersList: View {
    var newsletters: [NewsletterEntity]
    var collapsable: Bool = true
    var defaultOpen: Bool = false
    
    var body: some View {
        if (newsletters.isEmpty){
            NoContentPlaceHolder()
        } else {
            VStack(spacing: 10) {
                ForEach(newsletters, id: \.id) { newsletter in
                    NewsletterCard(newsletter: newsletter)
                }
            }
        }
    }
}
