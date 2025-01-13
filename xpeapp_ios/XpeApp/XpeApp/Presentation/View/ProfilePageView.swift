import SwiftUI
import xpeho_ui

struct ProfilePage: View {
    var loginManager = LoginManager.instance
    
    @Bindable var userInfosViewModel = UserInfosPageViewModel.instance
    @State private var isChangingPassword = false
    
    
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
        VStack(alignment: .leading, spacing: 16) {
            if isChangingPassword {
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
                        submitLabel: .done,
                        onInput: { input in
                            self.password = input
                        }
                    )
                    .focused($focusedField, equals: .password)
                    
                    InputText(
                        label: "Nouveau mot de passe",
                        password: true,
                        submitLabel: .done,
                        onInput: { input in
                            self.newPassword = input
                        }
                    )
                    .focused($focusedField, equals: .newPassword)
                    
                    InputText(
                        label: "Nouveau mot de passe",
                        password: true,
                        submitLabel: .done,
                        onInput: { input in
                            self.confirmPassword = input
                        }
                    )
                    .focused($focusedField, equals: .confirmPassword)
                    
                    HStack {
                        Spacer()
                        ClickyButton(
                            label: "VALIDER",
                            size: 18,
                            horizontalPadding: 125,
                            verticalPadding: 12,
                            onPress: {
                                onModifyPasswordPress()
                            }
                        )
                        Spacer()
                    }
                    
                    HStack {
                        Spacer()
                        ClickyButton(
                            label: "ANNULER",
                            horizontalPadding: 125,
                            verticalPadding: 10,
                            backgroundColor: .white,
                            labelColor: XPEHO_THEME.CONTENT_COLOR,
                            onPress: {
                                isChangingPassword = false
                            }
                        )
                        Spacer()
                    }
                    
                    Spacer()
                }
            } else {
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
                            isReadOnly: true
                        )
                        InputText(
                            label: "Nom",
                            defaultInput: userInfos.lastname.capitalizingFirstLetter(),
                            isReadOnly: true
                        )
                        
                        InputText(
                            label: "Prénom",
                            defaultInput: userInfos.firstname.capitalizingFirstLetter(),
                            isReadOnly: true
                        )
                    }
                    
                    HStack {
                        Spacer()
                        ClickyButton(
                            label: "MODIFIER MON MOT DE PASSE",
                            horizontalPadding: 50,
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
                            size: 18,
                            horizontalPadding: 90,
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
                .onAppear {
                    userInfosViewModel.update()
                }
            }
        }
        .accessibility(identifier: isChangingPassword ? "ChangePasswordView" : "ProfileView")
    }
    
    func onModifyPasswordPress() {
        Task {
            let success = await loginManager.updatePassword(
                initialPassword: password,
                newPassword: newPassword,
                passwordRepeat: confirmPassword
            )
            isChangingPassword = false
            if success == true {
                debugPrint("Password modified")
            } else {
                debugPrint("Failed to modify password")
            }
        }
    }
}

struct ProfilePage_Previews: PreviewProvider {
    static var previews: some View {
        ProfilePage()
    }
}
