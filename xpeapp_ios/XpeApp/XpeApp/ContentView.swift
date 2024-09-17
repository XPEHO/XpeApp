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
    var featureManager = FeatureManager.instance
    var toastManager = ToastManager.instance
    
    // Layout management
    @State private var isSidebarVisible: Bool = false
    
    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .leading) {
                VStack {
                    if loginManager.getUser() != nil {
                        if (featureManager.features != nil){
                            Header(isSidebarVisible: $isSidebarVisible)
                            Router()
                                .padding(.horizontal, 24)
                        } else {
                            ProgressView("Chargement des features...")
                                .progressViewStyle(CircularProgressViewStyle())
                                .padding()
                        }
                    } else {
                        Spacer()
                        LoginPage()
                    }
                    Spacer()
                }
                .background(XPEHO_THEME.BACKGROUND_COLOR.scaledToFill().edgesIgnoringSafeArea(.all))
                .frame(width: geometry.size.width, height: geometry.size.height)
                .disabled(loginManager.getUser() != nil && self.isSidebarVisible)
                // Toast
                .toast(manager: toastManager)
                
                if loginManager.getUser() != nil {
                    if (featureManager.features != nil){
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
                            geometry: geometry
                        )
                    }
                }
            }
        }
    }
}

#Preview {
    ContentView()
}
