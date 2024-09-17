//
//  TokenValidity.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 10/09/2024.
//

import Foundation

struct TokenValidityModel: Codable, Hashable {
    let code: String
    let message: String?
    let data: [String:Int]
}
