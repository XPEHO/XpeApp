//
//  HeaderView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 09/08/2024.
//

import SwiftUI
import xpeho_ui

struct Header: View {
    @Binding var isSidebarVisible: Bool
    
    var body: some View {
        HStack(alignment: .center) {
            BurgerButton(isSidebarVisible: $isSidebarVisible)
            .accessibilityIdentifier("BurgerButton")
            Spacer()
            Image("AppIconWithoutBg")
                .renderingMode(.template)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .foregroundStyle(XPEHO_THEME.XPEHO_COLOR)
                .frame(height: 146)
        }
        .padding(.leading, 24)
        .frame(height: 80)
    }
    
    
    struct BurgerButton: View {
        @Binding var isSidebarVisible: Bool
        
        var body: some View {
            Button(action: {
                withAnimation {
                    self.isSidebarVisible.toggle()
                }
            }) {
                Assets.loadImage(named: "BurgerMenu")
                    .renderingMode(.template)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .foregroundStyle(XPEHO_THEME.XPEHO_COLOR)
                    .frame(height: 31)
            }
            .accessibilityIdentifier("BurgerButton")
        }
    }
}
