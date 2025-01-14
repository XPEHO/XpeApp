//
//  ProfilEditPasswordView.swift
//  XpeApp
//
//  Created by Théo Lebègue on 14/01/2025.
//

import SwiftUI
import xpeho_ui

struct ProfileEditPasswordView: View  {
    var loginManager = LoginManager.instance
    
    @Binding var isChangingPassword: Bool
    
    @State private var password: String = ""
    @State private var newPassword: String = ""
    @State private var confirmPassword: String = ""
    
    @FocusState private var focusedField: Field?
    enum Field {
        case password
        case newPassword
        case confirmPassword
    }
    
    
    var body: some View {
        
        // Affichage de la vue de modification du mot de passe
        VStack(alignment: .leading, spacing: 16) {
            HStack {
                Title(text: "Modifier mon mot de passe")
                Spacer()
            }
            .padding(.bottom, 20)
            
            
            InputText(
                label: "Mot de passe",
                password: true,
                submitLabel: .next,
                onSubmit: {
                    focusedField = .newPassword
                },
                onInput: { input in
                    self.password = input
                }
            )
            .focused($focusedField, equals: .password)
            
            InputText(
                label: "Nouveau mot de passe",
                password: true,
                submitLabel: .next,
                onSubmit: {
                    focusedField = .confirmPassword
                },
                onInput: { input in
                    self.newPassword = input
                }
            )
            .focused($focusedField, equals: .newPassword)
            
            InputText(
                label: "Nouveau mot de passe",
                password: true,
                submitLabel: .done,
                onSubmit: {
                    focusedField = nil
                    onModifyPasswordPress()
                },
                onInput: { input in
                    self.confirmPassword = input
                }
            )
            .focused($focusedField, equals: .confirmPassword)
            
            Spacer().frame(height: 5)
            
            HStack {
                Spacer()
                ClickyButton(
                    label: "ANNULER",
                    verticalPadding: 12,
                    backgroundColor: .white,
                    labelColor: XPEHO_THEME.CONTENT_COLOR,
                    onPress: {
                        isChangingPassword = false
                    }
                )
                
                Spacer().frame(width: 10)
                
                ClickyButton(
                    label: "VALIDER",
                    verticalPadding: 12,
                    onPress: {
                        onModifyPasswordPress()
                    }
                )
                Spacer()
            }
        }
    }
    
    func onModifyPasswordPress() {
        Task {
            let result = await loginManager.updatePassword(
                initialPassword: password,
                newPassword: newPassword,
                passwordRepeat: confirmPassword
            )
            
            
            switch result {
            case .success:
                ToastManager.instance.setParams(
                    message: "Mot de passe modifié avec succès",
                    error: false
                )
                ToastManager.instance.play()
                isChangingPassword = false
            case .newPasswordsNotMatch:
                ToastManager.instance.setParams(
                    message: "Les mots de passe ne correspondent pas",
                    error: true
                )
                ToastManager.instance.play()
                
            case .invalidInitialPassword:
                ToastManager.instance.setParams(
                    message: "Le mot de passe initial est incorrect",
                    error: true
                )
                ToastManager.instance.play()
            case nil:
                ToastManager.instance.setParams(
                    message: "Une erreur est survenue",
                    error: true
                )
                ToastManager.instance.play()
            }
            
        }
    }
}
