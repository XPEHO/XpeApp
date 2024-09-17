//
//  UserEntity.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 16/09/2024.
//

import Foundation

@Observable class UserEntity {
    let token: String
    let id: String
    
    init(
        token: String,
        id: String
    ) {
        self.token = token
        self.id = id
    }
}
