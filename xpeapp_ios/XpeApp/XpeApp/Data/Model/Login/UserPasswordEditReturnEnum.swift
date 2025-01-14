//
//  UserPasswordEditReturnEnum.swift
//  XpeApp
//
//  Created by Théo Lebègue on 14/01/2025.
//


public enum UserPasswordEditReturnEnum: String, Codable {
    case success
    case invalidInitialPassword
    case newPasswordsNotMatch
}
