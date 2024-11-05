//
//  PageTitleView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 05/11/2024.
//

import SwiftUI

struct PageTitleSection: View {
    var title: String
    
    var body: some View {
        HStack {
            Title(text: title)
            Spacer()
        }
    }
}
struct PageTitleWithFilterSection: View {
    var title: String
    var filterList: [Int]
    @Binding var selectedYear: Int
    
    var body: some View {
        HStack {
            Title(text: title)
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
    }
}
