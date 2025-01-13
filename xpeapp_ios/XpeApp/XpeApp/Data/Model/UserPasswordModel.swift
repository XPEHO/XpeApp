//
//  UserPasswordModel.swift
//  XpeApp
//
//  Created by Théo Lebègue on 13/01/2025.
//
 
import Foundation

public struct UserPasswordModel: Codable {
    let initialPassword: String
    let password: String
    let passwordRepeat: String
}

