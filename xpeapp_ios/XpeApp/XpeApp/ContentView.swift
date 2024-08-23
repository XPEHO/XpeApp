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
    @State private var routerManager: RouterManager = RouterManager()
    @StateObject private var dataManager: DataManager = DataManager()
    @State private var toastManager: ToastManager = ToastManager()
    
    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .leading) {
                VStack {
                    Header(isSidebarVisible: $isSidebarVisible)
                    if dataManager.isDataLoaded {
                        Router(
                            routerManager: $routerManager,
                            dataManager: dataManager,
                            toastManager: $toastManager
                        )
                        .padding(.horizontal, 24)
                    } else {
                        ProgressView("Chargement des données...")
                            .progressViewStyle(CircularProgressViewStyle())
                            .padding()
                    }
                    Spacer()
                }
                .background(XPEHO_THEME.BACKGROUND_COLOR.scaledToFill().edgesIgnoringSafeArea(.all))
                .frame(width: geometry.size.width, height: geometry.size.height)
                .disabled(self.isSidebarVisible ? true : false)
                // Toast
                .toast(manager: $toastManager)
                
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
                    geometry: geometry,
                    routerManager: $routerManager
                )
            }
        }
        .onAppear {
            Task {
                await dataManager.initData()
            }
        }
        .onChange(of: routerManager.selectedView) { _, _ in
            Task {
                await dataManager.initData()
            }
        }
    }
}

#Preview {
    ContentView()
}
