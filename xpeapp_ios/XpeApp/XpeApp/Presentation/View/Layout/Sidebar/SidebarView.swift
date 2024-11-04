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
    
    var geometry: GeometryProxy
    
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
                
                // Wait for the features to be loaded
                if (featureManager.features != nil){
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
                        if Configuration.env == .local {
                            SidebarItem(isSidebarVisible: $isSidebarVisible,
                                        navigationItem: .debug,
                                        icon: Image("Bug"),
                                        label: "Debug")
                        }
                        Spacer()
                        LogoutButtonSection()
                        InfoSection()
                            .padding(.top, 10)
                    }
                    .padding(.horizontal, 20)
                    .accessibilityElement(children: .contain)
                    .accessibilityIdentifier("Sidebar")
                }
                
                Spacer()
            }
        }
        .opacity(self.isSidebarVisible ? 1 : 0)
        .frame(height: geometry.size.height)
        .frame(width: self.isSidebarVisible ? geometry.size.width * 0.7 : 0)
        .background(XPEHO_THEME.XPEHO_COLOR)
        .animation(.easeInOut(duration: 0.2), value: self.isSidebarVisible)
    }
    
    struct LogoutButtonSection: View {
        var loginManager = LoginManager.instance
        
        var body: some View {
            HStack {
                Spacer()
                ClickyButton(
                    label: "Se Déconnecter",
                    size: 18,
                    horizontalPadding: 20,
                    verticalPadding: 10,
                    backgroundColor: .white,
                    labelColor: XPEHO_THEME.CONTENT_COLOR,
                    onPress: {
                        loginManager.logout()
                    }
                )
                Spacer()
            }
        }
    }
    
    struct InfoSection: View {
        var body: some View {
            VStack(
                spacing: 10
            ){
                Divider()
                    .frame(height: 1)
                    .background(.white)
                    .padding(.horizontal, 20)
                HStack () {
                    Copyright()
                    .accessibility(identifier:"Copyright")
                    Spacer()
                    VersionCode()
                }
            }
        }
        
        struct Copyright: View {
            var body: some View {
                Button(action: {
                    UIApplication.shared.open(URL(string: "https://www.xpeho.com")!)
                }) {
                    Text("by XPEHO")
                        .font(.raleway(.bold, size: 20))
                        .foregroundStyle(.white)
                }
            }
        }
        struct VersionCode: View {
            var body: some View {
                let bundle = Bundle.main
                let version = bundle.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String ?? "Unknown"
                Text("v\(version)")
                    .font(.raleway(.bold, size: 20))
                    .foregroundStyle(.white)
            }
        }
    }
}
