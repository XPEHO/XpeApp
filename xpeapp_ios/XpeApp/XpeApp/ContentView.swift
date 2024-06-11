//
//  ContentView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import SwiftUI

struct ContentView: View {
    
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
            //Note(Loucas): This is a convenience menu for development purposes
            #if DEBUG && true
            DebugPageView()
                .tabItem {
                    Label("Debug", systemImage: "wrench.fill")
                }
            #endif
        }
    }
}

#Preview {
    ContentView()
}
