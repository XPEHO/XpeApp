//
//  ProfilePageView.swift
//  XpeApp
//
//  Created by Théo Lebègue on 07/01/2025.
//

import SwiftUI
import xpeho_ui

struct ProfilePage: View {
    var loginManager = LoginManager.instance
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            HStack {
                Title(text: "Profil")
                Spacer()
            }
            .padding(.bottom, 20)
            
            InputText(
                label: "Email",
                password: false,
                submitLabel: .next,
                onSubmit: {},
                onInput: { _ in }
            )

            InputText(
                label: "Nom",
                password: false,
                submitLabel: .next,
                onSubmit: {},
                onInput: { _ in }
            )

            InputText(
                label: "Prénom",
                password: false,
                submitLabel: .next,
                onSubmit: {},
                onInput: { _ in }
            )

            Spacer()
            
            HStack {
                Spacer()
                ClickyButton(
                    label: "Déconnexion",
                    size: 18,
                    horizontalPadding: 20,
                    verticalPadding: 10,
                    backgroundColor: .white,
                    labelColor: XPEHO_THEME.CONTENT_COLOR,
                    onPress: {
                        loginManager.logout()
                    }
                )
                Spacer()
            }
        }
        .accessibility(identifier: "ProfileView")
    }
}

struct ProfilePage_Previews: PreviewProvider {
    static var previews: some View {
        ProfilePage()
    }
}
