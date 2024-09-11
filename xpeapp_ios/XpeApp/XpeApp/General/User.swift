//
//  User.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 05/09/2024.
//

import SwiftUI

class UserManager: ObservableObject {
    
    private var userService: UserService?
    
    @Published var userLoaded: Bool = false
    private var token: String?
    @Published var connected: Bool = false
    
    func initUser() async {
        userService = UserService()
        DispatchQueue.main.async {
            self.userLoaded = true
        }
    }
    
    func fetchTokenFromCache() async {
        // TODO: fetch token from cache and assign token, return if succeed
        return
    }
    
    func saveTokenInCache() async {
        // TODO: fetch token from cache, return if succeed
        return
    }
    
    func login(
        username: String,
        password: String,
        onSuccess: @escaping () -> Void = {},
        onFail: @escaping () -> Void = {},
        onError: @escaping () -> Void = {}
    ) {
        Task{
            guard let tokenGenerated = await generateToken(username: username, password: password) else {
                DispatchQueue.main.async {
                    onError()
                }
                return
            }
            if tokenGenerated {
                await saveTokenInCache()
                await loginIfTokenIsValid(
                    onSuccess: onSuccess,
                    onFail: onFail
                )
            } else {
                DispatchQueue.main.async {
                    onFail()
                }
            }
            
        }
    }
    
    func loginIfTokenIsValid(onSuccess: @escaping () -> Void = {}, onFail: @escaping () -> Void = {}) async {
        guard let userService = self.userService else {
            return
        }
        guard let token = self.token else {
            return
        }
        
        // Valid token
        do {
            guard let tokenValid = try await userService.isTokenValid(token: token) else {
                print("Failed to valid token")
                return
            }
            DispatchQueue.main.async {
                if tokenValid {
                    self.connected = true
                    onSuccess()
                } else {
                    onFail()
                }
            }
        } catch {
            print("Error validating token: \(error)")
        }
        
    }
    
    func generateToken(username: String, password: String) async -> Bool? {
        guard let userService = self.userService else {
            return nil
        }
        
        // Generate token
        do {
            guard let tokenResponse = try await userService.generateToken(username: username, password: password) else {
                return false
            }
            self.token = "Bearer " + tokenResponse.token
            return true
        } catch {
            print("Error generating token: \(error)")
            return nil
        }
    }
    
}
