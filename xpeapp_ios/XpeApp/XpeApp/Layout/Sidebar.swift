//
//  Sidebar.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 09/08/2024.
//

import SwiftUI
import xpeho_ui

struct Sidebar: View {
    @Binding var isSidebarVisible: Bool
    
    var geometry: GeometryProxy
    
    // Global Management
    @EnvironmentObject var routerManager: RouterManager
    
    var body: some View {
        VStack(alignment: .leading) {
            if self.isSidebarVisible{
                HStack(alignment: .center) {
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
                    Spacer()
                }
                .padding(.leading, 14)
                .frame(height: 80)
                
                VStack(alignment: .leading, spacing: 20) {
                    SidebarItem(isSidebarVisible: $isSidebarVisible,
                                navigationItem: .home,
                                icon: Image("Home"),
                                label: "Accueil")
                    SidebarItem(isSidebarVisible: $isSidebarVisible,
                                navigationItem: .newsletter,
                                icon: Assets.loadImage(named: "Newsletter"),
                                label: "Newsletters")
                    SidebarItem(isSidebarVisible: $isSidebarVisible,
                                navigationItem: .qvstTemp,
                                icon: Assets.loadImage(named: "QVST"),
                                label: "QVST")
                    SidebarItem(isSidebarVisible: $isSidebarVisible,
                                navigationItem: .cra,
                                icon: Assets.loadImage(named: "Briefcase"),
                                label: "CRA")
                    SidebarItem(isSidebarVisible: $isSidebarVisible,
                                navigationItem: .vacation,
                                icon: Assets.loadImage(named: "PlaneDeparture"),
                                label: "Congés")
                    SidebarItem(isSidebarVisible: $isSidebarVisible,
                                navigationItem: .expenseReport,
                                icon: Assets.loadImage(named: "Receipt"),
                                label: "Notes de frais")
                    SidebarItem(isSidebarVisible: $isSidebarVisible,
                                navigationItem: .contacts,
                                icon: Assets.loadImage(named: "ContactFill"),
                                label: "Contacts")
                    #if DEBUG && true
                    SidebarItem(isSidebarVisible: $isSidebarVisible,
                                navigationItem: .debug,
                                icon: Image("Bug"),
                                label: "Debug")
                    #endif
                }
                .padding(.horizontal, 20)
                .accessibilityElement(children: .contain)
                .accessibilityIdentifier("Sidebar")
                
                Spacer()
            }
        }
        .opacity(self.isSidebarVisible ? 1 : 0)
        .frame(height: geometry.size.height)
        .frame(width: self.isSidebarVisible ? geometry.size.width * 0.7 : 0)
        .background(XPEHO_THEME.XPEHO_COLOR)
        .animation(.easeInOut(duration: 0.2), value: self.isSidebarVisible)
    }
}

struct SidebarItem: View {
    @Binding var isSidebarVisible: Bool
    
    var navigationItem: RouterItem
    var icon: Image
    var label: String
    
    // Global Management
    @EnvironmentObject var routerManager: RouterManager
    
    var body: some View {
        Button(action: {
            routerManager.goTo(item: navigationItem)
            withAnimation {
                self.isSidebarVisible = false
            }
        }) {
            HStack(spacing: 10) {
                icon
                    .renderingMode(.template)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(height: 24)
                Text(label)
            }
        }
        .font(.raleway(.bold, size: 20))
        .foregroundStyle(.white)
        .accessibility(identifier:"Sidebar_\(label)")
    }
}
