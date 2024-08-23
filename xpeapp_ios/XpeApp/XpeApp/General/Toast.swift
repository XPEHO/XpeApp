//
//  Toast.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 20/08/2024.
//

import SwiftUI
import xpeho_ui

struct ToastManager {
    var message: String = ""
    var duration: Double = 2.0
    var action: () -> Void = {}
    
    var isError: Bool = false
    
    var isShowned: Bool = false
    
    mutating func show() {
        withAnimation{
            isShowned = true
        }
    }
    
    mutating func reset() {
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
    func toast(manager: Binding<ToastManager>) -> some View {
        ZStack {
            self
            if manager.wrappedValue.isShowned {
                VStack {
                    Spacer()
                    Toast(
                        message: manager.wrappedValue.message,
                        isError: manager.wrappedValue.isError
                    )
                }
                .transition(.push(from: .leading))
                .onAppear {
                    DispatchQueue.main.asyncAfter(deadline: .now() + manager.wrappedValue.duration) {
                        withAnimation {
                            manager.wrappedValue.isShowned = false
                        }
                        manager.wrappedValue.action()
                        manager.wrappedValue.reset()
                    }
                }
            }
        }
    }
}
