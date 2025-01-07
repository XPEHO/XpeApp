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
    var wordpressAPI = WordpressAPI.instance
    @Binding var isSidebarVisible: Bool
    @State private var userInfos: UserInfosModel? = nil
    
    var geometry: GeometryProxy
    
    var body: some View {
        VStack(alignment: .leading) {
            if self.isSidebarVisible{
                HStack(alignment: .center) {
                    CloseButton(isSidebarVisible: $isSidebarVisible)
                    Spacer()
                }
                .padding(.leading, 14)
                .frame(height: 80)
                
                // Wait for the features to be loaded
                if (featureManager.isEnabled(item: .profile)){
                    SidebarItemProfile(isSidebarVisible: $isSidebarVisible,
                                       navigationItem: .profile,
                                       icon: Image("Profile"),
                                       lastname: userInfos?.lastname ?? "",
                                       firstname: userInfos?.firstname ?? "",
                                       email: userInfos?.email ?? "")
                    Spacer().frame(height: 30)
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
                            // SidebarItem(isSidebarVisible: $isSidebarVisible,
                            //             navigationItem: .about,
                            //             icon: Image("About"),
                            //             label: "À propos")
                        
                        if Configuration.env == .local {
                            SidebarItem(isSidebarVisible: $isSidebarVisible,
                                        navigationItem: .debug,
                                        icon: Image("Bug"),
                                        label: "Debug")
                        }
                        Spacer()
                        InfoSection()
                            .padding(.top, 10)
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
                .onAppear {
            Task {
                if let fetchedUserInfos = await WordpressAPI.instance.fetchUserInfos() {
                    userInfos = fetchedUserInfos
                }
            }
        }
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
                Confidentiality()
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
        struct Confidentiality: View {
            var body: some View {
                Button(action: {
                    UIApplication.shared.open(URL(string: "https://github.com/XPEHO/XpeApp/blob/main/PRIVACY_POLICY.md")!)
                }) {
                    Text("Politiques de confidentialité")
                        .font(.raleway(.bold, size: 16))
                        .foregroundStyle(.white)
                        .underline()
                }
            }
        }
    }
}
