//
//  SidebarItemViewProfile.swift
//  XpeApp
//
//  Created by Théo Lebègue on 06/01/2025.
//

import SwiftUI
import xpeho_ui

struct SidebarItemProfile: View {
    var routerManager = RouterManager.instance
    
    
    @Bindable var userInfosViewModel = UserInfosPageViewModel.instance
    
    @Binding var isSidebarVisible: Bool
    
    var body: some View {
        if let userInfos = userInfosViewModel.userInfos {
         Button(action: handleButtonAction) {
            ZStack(alignment: .topLeading) {
                Color.black.opacity(0.1)
                    .edgesIgnoringSafeArea(.top)
                VStack(alignment: .leading, spacing: 4) {
                    ProfileHeaderView(lastname: userInfos.lastname, firstname: userInfos.firstname, email: userInfos.email)
                }
                .padding(.horizontal, 20)
                .padding(.vertical,15)
            }
        }
        .frame(maxHeight: 73)
        .accessibility(identifier: "Sidebar_Profile")
        }
    }
    
    private func handleButtonAction() {
        routerManager.goTo(item: .profile)
        withAnimation {
            self.isSidebarVisible = false
        }
    }
}


struct ProfileHeaderView: View {
    var lastname: String
    var firstname: String
    var email: String

    
    var body: some View {
        HStack(spacing: 10) {
            Assets.loadImage(named: "ContactFill")
                .renderingMode(.template)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(height: 24)
                .foregroundColor(.white)
            VStack(alignment: .leading, spacing: 1) {
                HStack {
                    Text(lastname.uppercased())
                        .font(.raleway(.bold, size: 20))
                        .foregroundColor(.white)
                    Text(firstname.capitalizingFirstLetter())
                        .font(.raleway(.bold, size: 20))
                        .foregroundColor(.white)
                }
                Text(email)
                    .font(.raleway(.medium, size: 16))
                    .foregroundColor(.white)
            }
            .padding(.leading, 10)
        }
    }
}


extension String {
    func capitalizingFirstLetter() -> String {
        return prefix(1).uppercased() + dropFirst()
    }
}
