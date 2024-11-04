//
//  ListFilterTitleView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 04/11/2024.
//

import SwiftUI
import xpeho_ui

struct ListFilterTitle: View {
    var text: String
    
    var body: some View {
        Text(text)
            .font(.rubik(.semiBold, size: 18))
            .foregroundStyle(XPEHO_THEME.CONTENT_COLOR)
    }
}

#Preview {
    ListFilterTitle(text: "Title")
}
