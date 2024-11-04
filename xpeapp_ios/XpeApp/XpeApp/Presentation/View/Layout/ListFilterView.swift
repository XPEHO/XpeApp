//
//  ListFilterView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 04/11/2024.
//

import SwiftUI
import xpeho_ui


struct ListFilter<T: Hashable & CustomStringConvertible>: View {
    let elements: [T]
    @State private var selectedElement: T
    let onSelect: (T) -> Void

    init(elements: [T], defaultSelectedElement: T, onSelect: @escaping (T) -> Void) {
        self.elements = elements
        self._selectedElement = State(initialValue: defaultSelectedElement)
        self.onSelect = onSelect
    }

    var body: some View {
        Menu {
            ForEach(elements.filter { $0 != selectedElement }, id: \.self) { element in
                Button(action: {
                    selectedElement = element
                    onSelect(element)
                }) {
                    ListFilterTitle(text: element.description)
                }
            }
        } label: {
            HStack {
                ListFilterTitle(text: selectedElement.description)
                Assets.loadImage(named: "Chevron-down")
                    .resizable()
                    .renderingMode(.template)
                    .frame(width: 24, height: 24)
                    .foregroundStyle(XPEHO_THEME.CONTENT_COLOR)
            }
            .padding(.horizontal, 5)
            .padding(.vertical, 2)
            .background(.white)
            .cornerRadius(6)
        }
    }
}

#Preview {
    ListFilter(
        elements: [2020, 2021, 2022, 2023, 2024],
        defaultSelectedElement: 2024
    ) { element in
        print("Selected year: \(element)")
    }
}
