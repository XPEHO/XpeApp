//
//  QvstCampaignsPageView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 07/06/2024.
//

import SwiftUI
import xpeho_ui

struct CampaignsPage: View {
    @Bindable var campaignsPageViewModel = CampaignsPageViewModel.instance
    @State private var selectedYear = Calendar.current.component(.year, from: Date())
    
    var body: some View {
        ScrollView {
            if let classifiedCampaigns = Binding($campaignsPageViewModel.classifiedCampaigns) {
                let filterList = getFilterList(groupedElements: classifiedCampaigns.wrappedValue)
                HStack {
                    Title(text: "Campagnes de l'année")
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
                if let campaigns = Binding(classifiedCampaigns[selectedYear]) {
                    // Display campaigns list
                    CampaignsList(
                        campaigns: campaigns,
                        collapsable: true,
                        defaultOpen: false
                    )
                } else {
                    NoContentPlaceHolder()
                }
            } else {
                ProgressView("Chargement des campagnes...")
                    .progressViewStyle(CircularProgressViewStyle())
                    .padding()
            }
        }
        .onAppear {campaignsPageViewModel.update()}
        .refreshable {campaignsPageViewModel.update()}
        .accessibility(identifier: "QvstView")
    }
    
    func getFilterList(groupedElements: [Int: [QvstCampaignEntity]]) -> [Int] {
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
