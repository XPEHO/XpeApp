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
    @State private var selectedYear = Calendar.current.component(.year, from: Date())
    
    var body: some View {
        ScrollView {
            if let classifiedNewsletters = newsletterPageViewModel.classifiedNewsletters {
                let filterList = getFilterList(groupedElements: classifiedNewsletters)
                HStack {
                    Title(text: "Newsletters de l'année")
                    Spacer()
                    if (filterList.count > 1) {
                        ListFilter<Int>(
                            elements: filterList,
                            defaultSelectedElement: selectedYear
                        ) { selectedElement in
                            selectedYear = selectedElement
                        }
                    } else {
                        ListFilterTitle(text: String(selectedYear))
                    }
                }
                Spacer().frame(height: 16)
                let newsletters: [NewsletterEntity] = classifiedNewsletters[selectedYear] ?? []
                NewslettersList(newsletters: newsletters)
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
    
    func getFilterList(groupedElements: [Int: [NewsletterEntity]]) -> [Int] {
        // Get the list of years
        var filterList = groupedElements.keys.map { $0 }
        let currentYear = Calendar.current.component(.year, from: Date())
        // Add the current year to the list if needed
        if (!filterList.contains(currentYear)) {
            filterList.append(currentYear)
        }
        // Sort the list in descending order
        filterList.sort(by: >)
        // Return the list
        return filterList
    }
}
