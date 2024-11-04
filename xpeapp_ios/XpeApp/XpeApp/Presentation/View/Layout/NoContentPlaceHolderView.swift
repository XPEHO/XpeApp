//
//  NoContentPlaceHolderView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 04/11/2024.
//

import SwiftUI
import xpeho_ui

struct NoContentPlaceHolder: View {
    var body: some View {
        VStack(spacing: 10) {
            Image(systemName: "clear.fill")
                .resizable()
                .scaledToFit()
                .frame(width: 24, height: 24)
                .foregroundStyle(XPEHO_THEME.GREEN_DARK_COLOR)
            Text("Rien à signaler ici !")
                .foregroundStyle(XPEHO_THEME.GREEN_DARK_COLOR)
        }
        .padding()
    }
}

#Preview {
    NoContentPlaceHolder()
}
