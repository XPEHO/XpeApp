//
//  SidebarView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 09/08/2024.
//

import SwiftUI
import xpeho_ui

struct Sidebar: View {
    var featureManager = FeatureManager.instance

    @Binding var isSidebarVisible: Bool
    @Binding var showAboutView: Bool 

    var geometry: GeometryProxy

    var body: some View {
        VStack(alignment: .leading) {
            if self.isSidebarVisible {
                HStack(alignment: .center) {
                    CloseButton(isSidebarVisible: $isSidebarVisible)
                    Spacer()
                }
                .padding(.leading, 14)
                .frame(height: 80)

                // Wait for the features to be loaded
                if featureManager.isEnabled(item: .profile) {
                    SidebarItemProfile(isSidebarVisible: $isSidebarVisible)
                    Spacer().frame(height: 25)
                }
                VStack(alignment: .leading, spacing: 20) {
                    SidebarItem(
                        isSidebarVisible: $isSidebarVisible,
                        navigationItem: .home,
                        icon: Image("Home"),
                        label: "Accueil"
                    )
                    ForEach(menuItems, id: \.label) { menuItem in
                        if featureManager.isEnabled(item: menuItem.featureFlippingId) {
                            SidebarItem(
                                isSidebarVisible: $isSidebarVisible,
                                navigationItem: menuItem.navigationItem,
                                icon: Assets.loadImage(named: menuItem.iconName),
                                label: menuItem.label,
                                action: menuItem.featureFlippingId == .about ? { showAboutView.toggle() } : nil
                            )
                        }
                    }
                    SidebarItem(
                        isSidebarVisible: $isSidebarVisible,
                        icon: Image("About"),
                        label: "À propos",
                        action: { showAboutView.toggle() }
                    )

                    if Configuration.env == .local {
                        SidebarItem(
                            isSidebarVisible: $isSidebarVisible,
                            navigationItem: .debug,
                            icon: Image("Bug"),
                            label: "Debug"
                        )
                    }
                }
                .padding(.horizontal, 20)
                .accessibilityElement(children: .contain)
                .accessibilityIdentifier("Sidebar")
            }
            Spacer()
        }
        .opacity(self.isSidebarVisible ? 1 : 0)
        .frame(height: geometry.size.height)
        .frame(width: self.isSidebarVisible ? geometry.size.width * 1 : 0)
        .background(XPEHO_THEME.XPEHO_COLOR)
        .animation(.easeInOut(duration: 0.2), value: self.isSidebarVisible)
    }

    struct CloseButton: View {
        @Binding var isSidebarVisible: Bool

        var body: some View {
            Button(action: {
                withAnimation {
                    self.isSidebarVisible.toggle()
                }
            }) {
                Assets.loadImage(named: "CrossClose")
                    .renderingMode(.template)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .foregroundStyle(.white)
                    .frame(height: 50)
            }
            .accessibility(identifier: "Sidebar_CloseButton")
        }
    }
}
