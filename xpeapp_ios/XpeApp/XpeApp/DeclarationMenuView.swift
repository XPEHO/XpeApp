//
//  DeclarationMenuView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import SwiftUI

struct DeclarationMenuView: View {
    var body: some View {
        NavigationStack {
            List {
                NavigationLink {
                    
                } label: {
                    Text("Compte Rendu d'Activité (CRA)")
                }
                NavigationLink {
                    
                } label: {
                    Text("Note de frais")
                }
                NavigationLink {
                    
                } label: {
                    Text("Congés")
                }
            }
            .navigationTitle("Déclarations")
        }
    }
}

#Preview {
    DeclarationMenuView()
}
