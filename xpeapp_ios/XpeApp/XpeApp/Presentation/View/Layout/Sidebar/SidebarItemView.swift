//
//  SidebarItemView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 18/09/2024.
//

import SwiftUI

struct SidebarItem: View {
    var routerManager = RouterManager.instance

    @Binding var isSidebarVisible: Bool

    var navigationItem: RouterItem?
    var icon: Image
    var label: String
    var action: (() -> Void)? = nil
        
    var body: some View {
            Button(action: {
                action?()
                if let navigationItem = navigationItem {
                    routerManager.goTo(item: navigationItem)
                }
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
