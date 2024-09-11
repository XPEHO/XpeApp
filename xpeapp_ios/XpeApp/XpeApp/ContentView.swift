//
//  ContentView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import SwiftUI
import xpeho_ui

struct ContentView: View {
    // Layout management
    @State private var isSidebarVisible: Bool = false
    
    // Global Management
    @EnvironmentObject var userManager: UserManager
    @EnvironmentObject var dataManager: DataManager
    @EnvironmentObject var routerManager: RouterManager
    @EnvironmentObject var toastManager: ToastManager
    
    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .leading) {
                VStack {
                    if userManager.userLoaded {
                        // If we are connected we can access to the app
                        if userManager.connected {
                            Header(isSidebarVisible: $isSidebarVisible)
                            
                            if dataManager.isDataLoaded {
                                Router()
                                    .padding(.horizontal, 24)
                            } else {
                                ProgressView("Chargement des données...")
                                    .progressViewStyle(CircularProgressViewStyle())
                                    .padding()
                            }
                        } else {
                            Spacer()
                            LoginPageView()
                        }
                    } else {
                        Spacer()
                        ProgressView("Vérification de la connexion...")
                            .progressViewStyle(CircularProgressViewStyle())
                            .padding()
                    }
                    Spacer()
                }
                .background(XPEHO_THEME.BACKGROUND_COLOR.scaledToFill().edgesIgnoringSafeArea(.all))
                .frame(width: geometry.size.width, height: geometry.size.height)
                .disabled(self.isSidebarVisible ? true : false)
                // Toast
                .toast(manager: toastManager)
                
                if userManager.connected {
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
        .onAppear {
            Task {
                await dataManager.initData()
                await userManager.initUser()
            }
        }
        .onChange(of: routerManager.selectedView) { _, _ in
            Task {
                await dataManager.initData()
            }
        }
        .refreshable {
            Task {
                await dataManager.initData()
            }
        }
    }
}

#Preview {
    ContentView()
}
