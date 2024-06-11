//
//  ContentView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import SwiftUI

struct ContentView: View {
    @State var qvstCampaigns: [QvstCampaign] = []
    
    var body: some View {
        TabView {
            NewsletterPageView()
                .tabItem {
                    Label("Newsletter", systemImage: "newspaper.circle.fill")
            }
            DeclarationMenuView()
                .tabItem {
                    Label("Déclarations", systemImage: "square.and.pencil.circle.fill")
                }
            Text("Contacts page placeholder")
                .tabItem {
                    Label("Contacts", systemImage: "person.crop.circle.fill")
                }
            QvstCampaignsPageView(campaigns: qvstCampaigns)
                .tabItem {
                    Label("QVST Temp", systemImage: "questionmark")
                }
            //Note(Loucas): This is a convenience menu for development purposes
            #if DEBUG && true
            DebugPageView()
                .tabItem {
                    Label("Debug", systemImage: "wrench.fill")
                }
            #endif
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
