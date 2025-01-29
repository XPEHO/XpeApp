//
//  ContentView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import SwiftUI
import xpeho_ui

struct ContentView: View {
    var loginManager = LoginManager.instance
    var toastManager = ToastManager.instance
    
    // Layout management
    @State private var isSidebarVisible: Bool = false
    @State private var showAboutView = false 

    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .leading) {
                VStack {
                    if loginManager.getUser() != nil {
                        Header(isSidebarVisible: $isSidebarVisible)
                        Router()
                            .padding(.horizontal, 24)
                    } else {
                        Spacer()
                        LoginPage()
                    }
                    Spacer()
                }
                .background(XPEHO_THEME.BACKGROUND_COLOR.scaledToFill().edgesIgnoringSafeArea(.all))
                .frame(width: geometry.size.width, height: geometry.size.height)
                // Disable the app when we are logged in and the sidebar is visible
                .disabled(loginManager.getUser() != nil && self.isSidebarVisible)
                // Toast
                .toast(manager: toastManager)
                
                if loginManager.getUser() != nil {
                    // Make the app content darker and unusable while using sidebar, add also a close effect on click
                    if isSidebarVisible {
                        Color.black.opacity(0.3)
                            .edgesIgnoringSafeArea(.all)
                            .onTapGesture {
                                self.isSidebarVisible = false
                            }
                    }
                    
                    Sidebar(
                        isSidebarVisible: $isSidebarVisible,
                        showAboutView: $showAboutView,
                        geometry: geometry
                    )
                }
                
                if showAboutView {
                    Color.black.opacity(0.3)
                        .edgesIgnoringSafeArea(.all)
                    VStack {
                        Spacer()
                        AboutView(isPresented: $showAboutView)
                            .transition(.opacity)
                            .zIndex(1)
                        Spacer()
                    }
                    .frame(width: geometry.size.width, height: geometry.size.height)
                }
            }
        }
    }
}

#Preview {
    ContentView()
}