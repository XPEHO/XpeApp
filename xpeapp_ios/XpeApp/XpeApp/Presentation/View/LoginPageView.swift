//
//  LoginPageView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 05/09/2024.
//

import SwiftUI
import xpeho_ui


struct LoginPage: View {
    var loginManager = LoginManager.instance
    var toastManager = ToastManager.instance
    
    @State var username: String = ""
    @State var password: String = ""
    
    // Allow to lock the button after first click and prevent spamming
    @State var isTryingToLogin = false

    var body: some View {
        VStack(spacing: 16) {
            Spacer()
                .frame(width: 332, height: 250)
                .background {
                    Image("AppIconWithoutBg")
                        .resizable()
                        .renderingMode(.template)
                        .foregroundStyle(XPEHO_THEME.XPEHO_COLOR)
                        .frame(width: 332, height: 332)
                }
            InputText(
                label: "Email",
                password: false,
                onInput: { input in
                    self.username = input
                }
            )
            InputText(
                label: "Mot de passe",
                password: true,
                onInput: { input in
                    self.password = input
                }
            )
            ClickyButton(
                label: isTryingToLogin ? "Connexion..." : "Se Connecter",
                size: 18,
                horizontalPadding: 70,
                verticalPadding: 18,
                enabled: true,//!isTryingToLogin,
                onPress: {
                    isTryingToLogin = true
                    loginManager.login(
                        username: username,
                        password: password
                    ) {
                        isTryingToLogin = false
                    }
                    debugPrint("Login with \(username)") 
                }
            )
            .padding(.top, 32)
        }
        .padding(.horizontal, 16)
        .preferredColorScheme(.dark)
    }
}

#Preview {
    LoginPage()
}
