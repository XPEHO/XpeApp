//
//  UserRepository.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/09/2024.
//

import Foundation

enum LoginResult {
    case success
    case failure
    case error
}

protocol UserRepository {
    var user: UserEntity? { get set }
    
    func login(
        username: String,
        password: String,
        completion: @escaping (LoginResult) -> Void
    ) async
    
    func loginWithCacheIfTokenValid(
        completion: @escaping (LoginResult) -> Void
    ) async
    
    func logout()
    
    
    func fetchUserInfos() async -> UserInfosEntity?
    
    func updatePassword(
    initialPassword: String,
    newPassword: String,
    passwordRepeat: String
    ) async -> UserPasswordEditReturnEnum?
}
