//
//  LoginManager.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/09/2024.
//

import Foundation

@Observable class LoginManager {
    
    static let instance = LoginManager()
    
    // Make private constructor to prevent use without shared instance
    private init() {
        initUser()
    }
    
    func getUser() -> UserEntity? {
        return UserRepositoryImpl.instance.user
    }
    
    func initUser() {
        Task {
            await UserRepositoryImpl.instance.loginWithCacheIfTokenValid() { result in
                switch result {
                    case .success:
                        debugPrint("Login with cache succeed")
                    case .failure:
                        debugPrint("Login with cache failed")
                    case .error:
                        debugPrint("Error during login with cache")
                }
            }
        }
    }
    
    func login(
        username: String,
        password: String,
        onFailureOrError: @escaping () -> Void = {/*Do nothing if parameter not used*/}
    ) {
        let toastManager = ToastManager.instance
        guard username != "" else {
            toastManager.setParams(
                message: "Veuillez saisir un email",
                error: true
            )
            toastManager.play()
            onFailureOrError()
            return
        }
        
        guard password != "" else {
            toastManager.setParams(
                message: "Veuillez saisir un mot de passe",
                error: true
            )
            toastManager.play()
            onFailureOrError()
            return
        }
        
        guard isValidEmail(username) else {
            toastManager.setParams(
                message: "Veuillez saisir un email valide",
                error: true
            )
            toastManager.play()
            onFailureOrError()
            return
        }

        Task {
            await UserRepositoryImpl.instance.login(username: username, password: password) { result in
                switch result {
                    case .success:
                        debugPrint("Login succeed")
                    case .failure:
                        toastManager.setParams(
                            message: "Couple login/mot de passe invalide",
                            error: true
                        )
                        toastManager.play()
                        onFailureOrError()
                    case .error:
                        toastManager.setParams(
                            message: "Une erreur est survenue",
                            error: true
                        )
                        toastManager.play()
                        onFailureOrError()
                }
            }
        }
    }
    
    func logout() {
        UserRepositoryImpl.instance.logout()
    }
    
    func updatePassword(
        initialPassword: String,
        newPassword: String,
        passwordRepeat: String
    ) async -> UserPasswordEditReturnEnum? {
        return await UserRepositoryImpl.instance.updatePassword(
            initialPassword: initialPassword,
            newPassword: newPassword,
            passwordRepeat: passwordRepeat
        )
    }
}
