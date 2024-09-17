//
//  TokenResponseModel.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 16/09/2024.
//

import Foundation

struct TokenResponseModel: Codable {
    let success: SuccessTokenResponseModel?
    let error: ErrorTokenResponseModel?
    
}

struct SuccessTokenResponseModel: Codable, Hashable {
    let token: String
    let userEmail: String
    let userNiceName: String
    let userDisplayName: String

    enum CodingKeys: String, CodingKey {
        case token
        case userEmail = "user_email"
        case userNiceName = "user_nicename"
        case userDisplayName = "user_display_name"
    }
}


struct ErrorTokenResponseModel: Codable {
    let code: String
    let message: String
    let data: ErrorData

    struct ErrorData: Codable {
        let status: Int
    }
}
