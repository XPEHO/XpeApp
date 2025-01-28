//
//  AboutViewModifier.swift
//  XpeApp
//
//  Created by Théo Lebègue on 28/01/2025.
//

import SwiftUI

struct AboutViewModifier: ViewModifier {
    @Binding var isPresented: Bool

    func body(content: Content) -> some View {
        ZStack {
            content
            if isPresented {
                Color.black.opacity(0.3)
                    .edgesIgnoringSafeArea(.all)
                AboutView(isPresented: $isPresented)
                    .transition(.opacity)
                    .zIndex(1)
            }
        }
    }
}

extension View {
    func aboutView(isPresented: Binding<Bool>) -> some View {
        self.modifier(AboutViewModifier(isPresented: isPresented))
    }
}

