//
//  ToastManager.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 20/08/2024.
//

import SwiftUI
import xpeho_ui

@Observable class ToastManager {
    static let instance = ToastManager()
    
    var message: String = ""
    var duration: Double = 2.0
    var action: () -> Void = {/*Do nothing if not set with parameter*/}
    
    var error: Bool = false
    
    var showned: Bool = false
    
    func setParams (
        message: String = "",
        duration: Double = 2.0,
        action: @escaping () -> Void = {/*Do nothing if parameter not used*/},
        error: Bool = false
    ){
        self.message = message
        self.duration = duration
        self.action = action
        self.error = error
    }
    
    func play() {
        self.show()
        DispatchQueue.main.asyncAfter(deadline: .now() + self.duration) {
            self.hide()
            self.action()
            self.reset()
        }
    }
    
    private func show() {
        withAnimation{
            showned = true
        }
    }
    
    private func hide() {
        withAnimation{
            showned = false
        }
    }
    
    private func reset() {
        message = ""
        duration = 2.0
        action = {/*Reset to do nothing if not set with parameter*/}
        error = false
    }
}

struct Toast: View {
    var toastManager = ToastManager.instance

    var body: some View {
        Text(toastManager.message)
            .foregroundColor(.white)
            .padding()
            .background(toastManager.error ? XPEHO_THEME.RED_INFO_COLOR : XPEHO_THEME.CONTENT_COLOR)
            .cornerRadius(10)
            .padding(.bottom, 20)
    }
}

// Modifier à mettre sur une view globale pour que le toastManager puisse y afficher un toast
extension View {
    func toast(manager: ToastManager) -> some View {
        ZStack {
            self
            if manager.showned {
                VStack {
                    Spacer()
                    Toast()
                }
                .transition(.push(from: .leading))
            }
        }
    }
}
