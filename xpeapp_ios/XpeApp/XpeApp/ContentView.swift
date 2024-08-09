//
//  ContentView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import SwiftUI
import xpeho_ui

enum NavigationItem {
    case home, newsletter, cra, vacation, expenseReport, contacts, qvstTemp, debug
}

struct ContentView: View {
    @State var qvstCampaigns: [QvstCampaign] = []
    @State private var isSidebarVisible: Bool = false
    @State private var selectedView: NavigationItem? = .home
    
    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .leading) {
                VStack {
                    Header(isSidebarVisible: $isSidebarVisible)
                    Group {
                        switch selectedView {
                        case .home:
                            Text("Home page placeholder")
                        case .newsletter:
                            NewsletterPageView()
                        case .cra:
                            Text("CRA page placeholder")
                        case .vacation:
                            Text("Vacation page placeholder")
                        case .expenseReport:
                            Text("Expense Report page placeholder")
                        case .contacts:
                            Text("Contacts page placeholder")
                        case .qvstTemp:
                            QvstCampaignsPageView(campaigns: qvstCampaigns)
                        case .debug:
                            DebugPageView()
                        case .none:
                            Text("Home Sweet Home")
                        }
                    }
                    .padding(.horizontal, 24)
                    Spacer()
                }
                .background(XPEHO_THEME.BACKGROUND_COLOR.scaledToFill().edgesIgnoringSafeArea(.all))
                .frame(width: geometry.size.width, height: geometry.size.height)
                .disabled(self.isSidebarVisible ? true : false)

                if isSidebarVisible {
                    Color.black.opacity(0.3)
                        .edgesIgnoringSafeArea(.all)
                        .onTapGesture {
                            self.isSidebarVisible = false
                        }
                }

                Sidebar(
                    isSidebarVisible: $isSidebarVisible,
                    selectedView: $selectedView,
                    geometry: geometry
                )
            }
        }
        .onAppear {
            if qvstCampaigns.isEmpty {
                updateCampaigns()
            }
        }
    }
    
    private func updateCampaigns() {
        Task {
            guard let cs = await QvstCampaign.fetchActive() else { return }
            // Note: We have to do UI updates on the main thread to prevent crashes
            DispatchQueue.main.async {
                qvstCampaigns = cs
            }
        }
    }
}

#Preview {
    ContentView()
}
