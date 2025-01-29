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
    @State private var showAboutView = false
    
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
                if (featureManager.isEnabled(item: .profile)){
                    SidebarItemProfile(isSidebarVisible: $isSidebarVisible)
                    Spacer().frame(height: 25)
                }
                VStack(alignment: .leading, spacing: 20) {
                    SidebarItem(isSidebarVisible: $isSidebarVisible,
                                navigationItem: .home,
                                icon: Image("Home"),
                                label: "Accueil")
                    ForEach(menuItems, id: \.label) { menuItem in
                        if featureManager.isEnabled(item: menuItem.featureFlippingId) {
                            SidebarItem(
                                isSidebarVisible: $isSidebarVisible,
                                navigationItem: menuItem.navigationItem,
                                icon: Assets.loadImage(named: menuItem.iconName),
                                label: menuItem.label,
                                action: menuItem.featureFlippingId == .about ? toggleAboutView : nil
                            )
                        }
                    }
                    SidebarItem(isSidebarVisible: $isSidebarVisible,
                                icon: Image("About"),
                                label: "À propos",
                                action: toggleAboutView)
                    
                    if Configuration.env == .local {
                        SidebarItem(isSidebarVisible: $isSidebarVisible,
                                    navigationItem: .debug,
                                    icon: Image("Bug"),
                                    label: "Debug")
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
        .aboutView(isPresented: $showAboutView)
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

    func toggleAboutView() {
    showAboutView.toggle()
}
    struct MenuItem {
        let navigationItem: RouterItem?
        let iconName: String
        let label: String
        let featureFlippingId: RouterItem
    }
    
    let menuItems: [MenuItem] = [
        MenuItem(
            navigationItem: .newsletters,
            iconName: "Newsletter",
            label: "Newsletters",
            featureFlippingId: .newsletters
        ),
        MenuItem(
            navigationItem: .campaign,
            iconName: "QVST",
            label: "Campaign",
            featureFlippingId: .campaign
        ),
        MenuItem(
            navigationItem: .expenseReport,
            iconName: "Receipt",
            label: "Expense Report",
            featureFlippingId: .expenseReport
        ),
        MenuItem(
            navigationItem: .colleagues,
            iconName: "ContactFill",
            label: "Colleagues",
            featureFlippingId: .colleagues
        ),
        MenuItem(
            navigationItem: .cra,
            iconName: "Briefcase",
            label: "CRA",
            featureFlippingId: .cra
        ),
        MenuItem(
            navigationItem: .vacation,
            iconName: "PlaneDeparture",
            label: "Vacation",
            featureFlippingId: .vacation
        ),
    ]
}
