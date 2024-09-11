//
//  TokenResponse.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 10/09/2024.
//

import Foundation

struct TokenResponse: Codable, Hashable {
    let token: String
    let user_email: String
    let user_nicename: String
    let user_display_name: String
}
