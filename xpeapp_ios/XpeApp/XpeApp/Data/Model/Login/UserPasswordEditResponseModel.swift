//
//  UserUpdatePasswordModel.swift
//  XpeApp
//
//  Created by Théo Lebègue on 13/01/2025.
//


import Foundation


public struct UserPasswordEditResponseModel: Codable {
    let code: String
    let message: String?
    let data: [String:Int]?
}
