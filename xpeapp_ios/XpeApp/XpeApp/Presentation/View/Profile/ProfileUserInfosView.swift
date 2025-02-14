//
//  ProfileUserInfosView.swift
//  XpeApp
//
//  Created by Théo Lebègue on 14/01/2025.
//

import SwiftUI
import xpeho_ui

struct ProfileUserInfosView: View {
    
    var userInfosViewModel = UserInfosPageViewModel.instance
    var loginManager = LoginManager.instance
    
    @Binding var isChangingPassword: Bool
    
    var body: some View {
        // Affichage du profil
        VStack(alignment: .leading, spacing: 16) {
            HStack {
                Title(text: "Profil")
                Spacer()
            }
            .padding(.bottom, 20)
            
            if let userInfos = userInfosViewModel.userInfos {
                InputText(
                    label: "Email",
                    defaultInput: userInfos.email,
                    readOnly: true
                )
                InputText(
                    label: "Nom",
                    defaultInput: userInfos.lastname.capitalizingFirstLetter(),
                    readOnly: true
                )
                
                InputText(
                    label: "Prénom",
                    defaultInput: userInfos.firstname.capitalizingFirstLetter(),
                    readOnly: true
                )
            }
            
            Spacer().frame(height: 5)
            
            HStack {
                Spacer()
                ClickyButton(
                    label: "Modifier mon mot de passe",
                    verticalPadding: 12,
                    onPress: {
                        isChangingPassword = true
                    }
                )
                Spacer()
            }
            Spacer()
            
            HStack {
                Spacer()
                ClickyButton(
                    label: "Déconnexion",
                    verticalPadding: 12,
                    backgroundColor: .white,
                    labelColor: XPEHO_THEME.CONTENT_COLOR,
                    onPress: {
                        loginManager.logout()
                    }
                )
                Spacer()
            }
        }
        .onAppear {
            userInfosViewModel.update()
        }
    }
}
