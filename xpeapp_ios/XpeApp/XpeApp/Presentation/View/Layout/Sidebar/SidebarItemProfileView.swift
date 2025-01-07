//
//  SidebarItemViewProfile.swift
//  XpeApp
//
//  Created by Théo Lebègue on 06/01/2025.
//

import SwiftUI

struct SidebarItemProfile: View {
    var routerManager = RouterManager.instance
    
    @Binding var isSidebarVisible: Bool
    
    var navigationItem: RouterItem
    var icon: Image
    var lastname: String
    var firstname: String
    var email: String
    
    var body: some View {
        Button(action: {
            routerManager.goTo(item: navigationItem)
            withAnimation {
                self.isSidebarVisible = false
            }
        }) {
            ProfileContentView(icon: icon, lastname: lastname, firstname: firstname, email: email)
        }
        .frame(maxHeight: 73)
        .accessibility(identifier: "Sidebar_Profile")
    }
}

struct ProfileContentView: View {
    var icon: Image
    var lastname: String
    var firstname: String
    var email: String
    
    var body: some View {
        ZStack(alignment: .topLeading) {
            Color.black.opacity(0.1)
                .edgesIgnoringSafeArea(.top)
            VStack(alignment: .leading, spacing: 4) {
                ProfileHeaderView(icon: icon, lastname: lastname, firstname: firstname, email: email)
            }
            .padding(.horizontal, 20)
            .padding(.top, 23)
            .padding(.bottom, 23)
        }
    }
}

struct ProfileHeaderView: View {
    var icon: Image
    var lastname: String
    var firstname: String
    var email: String
    
    var body: some View {
        HStack(spacing: 10) {
            icon
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
