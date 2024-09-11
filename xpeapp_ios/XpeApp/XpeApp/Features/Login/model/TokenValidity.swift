//
//  TokenValidity.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 10/09/2024.
//

import Foundation

struct TokenValidity: Codable, Hashable {
    let code: String
    let data: [String:Int]
}
