//
//  LoginPageView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 05/09/2024.
//

import SwiftUI
import xpeho_ui
import FirebaseAnalytics


struct LoginPage: View {
    var loginManager = LoginManager.instance
    var toastManager = ToastManager.instance
    
    @State var username: String = ""
    @State var password: String = ""
    
    // Allow to lock the button after first click and prevent spamming
    @State var isTryingToLogin = false

    @FocusState private var focusedField: Field?
    enum Field {
        case username
        case password
    }

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
                submitLabel: .next,
                onSubmit: {
                    focusedField = .password
                },
                onInput: { input in
                    self.username = input
                }
            )
            .focused($focusedField, equals: .username)
            InputText(
                label: "Mot de passe",
                password: true,
                submitLabel: .done,
                onSubmit: {
                    focusedField = nil
                    onLoginPress()
                },
                onInput: { input in
                    self.password = input
                }
            )
            .focused($focusedField, equals: .password)
            ClickyButton(
                label: isTryingToLogin ? "Connexion..." : "Se Connecter",
                size: 18,
                horizontalPadding: 70,
                verticalPadding: 18,
                enabled: true,//!isTryingToLogin,
                onPress: {
                    onLoginPress()
                }
            )
            .padding(.top, 32)
        }
        .onAppear{
            sendAnalyticsEvent(page: "login_page")
        }
        .padding(.horizontal, 16)
        .preferredColorScheme(.dark)
    }

    func onLoginPress() {
        isTryingToLogin = true
        loginManager.login(
            username: username,
            password: password
        ) {
            isTryingToLogin = false
        }
        debugPrint("Login with \(username)") 
    }
}

#Preview {
    LoginPage()
}
