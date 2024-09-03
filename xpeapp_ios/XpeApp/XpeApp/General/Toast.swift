//
//  Toast.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 20/08/2024.
//

import SwiftUI
import xpeho_ui

class ToastManager: ObservableObject {
    @Published var message: String = ""
    @Published var duration: Double = 2.0
    @Published var action: () -> Void = {}
    
    @Published var isError: Bool = false
    
    @Published var isShowned: Bool = false
    
    func show() {
        withAnimation{
            isShowned = true
        }
    }
    
    func reset() {
        message = ""
        duration = 2.0
        action = {}
        isError = false
    }
}

struct Toast: View {
    let message: String
    let isError: Bool
    

    var body: some View {
        Text(message)
            .foregroundColor(.white)
            .padding()
            .background(isError ? XPEHO_THEME.RED_INFO_COLOR : XPEHO_THEME.CONTENT_COLOR)
            .cornerRadius(10)
            .padding(.bottom, 20)
    }
}

// Extension pour afficher le Toast
extension View {
    func toast(manager: ToastManager) -> some View {
        ZStack {
            self
            if manager.isShowned {
                VStack {
                    Spacer()
                    Toast(
                        message: manager.message,
                        isError: manager.isError
                    )
                }
                .transition(.push(from: .leading))
                .onAppear {
                    DispatchQueue.main.asyncAfter(deadline: .now() + manager.duration) {
                        withAnimation {
                            manager.isShowned = false
                        }
                        manager.action()
                        manager.reset()
                    }
                }
            }
        }
    }
}
