//
//  UserPasswordCandidateModel.swift
//  XpeApp
//
//  Created by Théo Lebègue on 13/01/2025.
//


import Foundation

public struct UserPasswordEditModel: Codable {
    let initialPassword: String
    let password: String
    let passwordRepeat: String
    
    enum CodingKeys: String, CodingKey {
        case initialPassword = "initial_password"
        case password = "password"
        case passwordRepeat = "password_repeat"
    }
}

