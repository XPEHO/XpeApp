//
//  UserRepositoryImpl.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 06/09/2024.
//

import Foundation

@Observable class UserRepositoryImpl: UserRepository {
    // An instance for app and a mock for tests
    static let instance = UserRepositoryImpl()
    static let mock = UserRepositoryImpl(
        dataSource: MockWordpressAPI.instance
    )
    
    // Data source to use
    private let dataSource: WordpressAPIProtocol
    
    // Watched user
    var user: UserEntity?

    // Make private constructor to prevent use without shared instances
    private init(
        dataSource: WordpressAPIProtocol = WordpressAPI.instance
    ) {
        self.dataSource = dataSource
    }
    
    func login (
        username: String,
        password: String,
        completion: @escaping (LoginResult) -> Void
    ) async {
        // Generate token
        guard let tokenResponse = await dataSource.generateToken(
            userCandidate: UserCandidateModel(
                username: username,
                password: password
            )
        ) else {
            debugPrint("Failed call to generateToken in login")
            completion(.error)
            return
        }

        // Check that we succeed to generate the token
        if let successTokenResponse = tokenResponse.success {
            guard let userId = await dataSource.fetchUserId(email: successTokenResponse.userEmail) else {
                debugPrint("Failed call to fetchUserId in login")
                completion(.error)
                return
            }
            // Register the user
            self.user = UserEntity(
                token: "Bearer "+successTokenResponse.token,
                id: userId
            )
            // Save it in cache
            KeychainManager.instance.saveValue(user!.id, forKey: "user_id")
            KeychainManager.instance.saveValue(user!.token, forKey: "user_token")
            completion(.success)
        } else if tokenResponse.error != nil {
            completion(.failure)
        }
    }
    
    func loginWithCacheIfTokenValid(
        completion: @escaping (LoginResult) -> Void
    ) async {
        // Get the user from cache
        guard let id = KeychainManager.instance.getValue(forKey: "user_id") else {
            completion(.failure)
            return
        }
        guard let token = KeychainManager.instance.getValue(forKey: "user_token") else {
            completion(.failure)
            return
        }
        let userFromCache = UserEntity(
            token: token,
            id: id
        )
                        
        // Check the validity of its token
        guard let validity = await dataSource.checkTokenValidity(token: userFromCache.token) else {
            debugPrint("Failed call to checkTokenValidity in isTokenInCacheValid")
            completion(.error)
            return
        }
        
        if validity.code == "jwt_auth_valid_token" {
            // Register the user
            self.user = userFromCache
            completion(.success)
        } else {
            completion(.failure)
        }
    }
    
    func logout() {
        self.user = nil
        KeychainManager.instance.deleteValue(forKey: "user_id")
        KeychainManager.instance.deleteValue(forKey: "user_token")
    }
}
