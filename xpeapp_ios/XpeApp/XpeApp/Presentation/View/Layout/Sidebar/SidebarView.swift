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
                    if featureManager.isEnabled(item: .newsletters){
                        SidebarItem(isSidebarVisible: $isSidebarVisible,
                                    navigationItem: .newsletters,
                                    icon: Assets.loadImage(named: "Newsletter"),
                                    label: featureManager.getName(item: .newsletters))
                    }
                    if featureManager.isEnabled(item: .campaign){
                        SidebarItem(isSidebarVisible: $isSidebarVisible,
                                    navigationItem: .campaign,
                                    icon: Assets.loadImage(named: "QVST"),
                                    label: featureManager.getName(item: .campaign))
                    }
                    if featureManager.isEnabled(item: .expenseReport){
                        SidebarItem(isSidebarVisible: $isSidebarVisible,
                                    navigationItem: .expenseReport,
                                    icon: Assets.loadImage(named: "Receipt"),
                                    label: featureManager.getName(item: .expenseReport))
                    }
                    if featureManager.isEnabled(item: .colleagues){
                        SidebarItem(isSidebarVisible: $isSidebarVisible,
                                    navigationItem: .colleagues,
                                    icon: Assets.loadImage(named: "ContactFill"),
                                    label: featureManager.getName(item: .colleagues))
                    }
                    if featureManager.isEnabled(item: .cra){
                        SidebarItem(isSidebarVisible: $isSidebarVisible,
                                    navigationItem: .cra,
                                    icon: Assets.loadImage(named: "Briefcase"),
                                    label: featureManager.getName(item: .cra))
                    }
                    if featureManager.isEnabled(item: .vacation){
                        SidebarItem(isSidebarVisible: $isSidebarVisible,
                                    navigationItem: .vacation,
                                    icon: Assets.loadImage(named: "PlaneDeparture"),
                                    label: featureManager.getName(item: .vacation))
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

}
