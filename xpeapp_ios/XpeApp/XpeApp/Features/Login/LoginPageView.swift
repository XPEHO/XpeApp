//
//  LoginPageView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 05/09/2024.
//

import SwiftUI
import xpeho_ui

struct LoginPageView: View {
    
    @State var username: String = ""
    @State var password: String = ""
    
    // Global Management
    @EnvironmentObject var userManager: UserManager
    @EnvironmentObject var dataManager: DataManager
    @EnvironmentObject var routerManager: RouterManager
    @EnvironmentObject var toastManager: ToastManager
    
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
            if isTryingToLogin {
                ProgressView("Connexion...")
                    .progressViewStyle(CircularProgressViewStyle())
                    .padding()
            } else {
                ClickyButton(
                    label: "Se Connecter",
                    size: 18,
                    horizontalPadding: 70,
                    verticalPadding: 18,
                    enabled: !isTryingToLogin,
                    onPress: {
                        // Check email validity
                        if !isValidEmail(self.username) {
                            toastManager.message = "Veuillez saisir un email valide"
                            toastManager.isError = true
                            toastManager.show()
                            return
                        }
                        
                        if self.password == "" {
                            toastManager.message = "Veuillez saisir un mot de passe valide"
                            toastManager.isError = true
                            toastManager.show()
                            return
                        }
                        
                        isTryingToLogin = true
                        userManager.login(
                            username: self.username,
                            password: self.password,
                            onFail: {
                                // Inform user
                                toastManager.message = "Correspondance email/mot de passe incorrecte"
                                toastManager.isError = true
                                toastManager.show()
                                
                                // Unlock connect button
                                isTryingToLogin = false
                            },
                            onError: {
                                // Inform user
                                toastManager.message = "Une erreur est survenur"
                                toastManager.isError = true
                                toastManager.show()
                                
                                // Unlock connect button
                                isTryingToLogin = false
                            }
                        )
                    }
                )
                .padding(.top, 32)
            }
        }
        .padding(.horizontal, 16)
        .preferredColorScheme(.dark)
    }
}

#Preview {
    LoginPageView()
}
