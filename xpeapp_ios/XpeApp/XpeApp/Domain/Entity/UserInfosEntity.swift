//
//  UserInfosEntity.swift
//  XpeApp
//
//  Created by Théo Lebègue on 07/01/2025.
//


import Foundation

struct UserInfosEntity: Codable, Equatable {
    let id: String
    let email: String
    let firstname: String
    let lastname: String

    init(id: String, email: String, firstname: String, lastname: String) {
        self.id = id
        self.email = email
        self.firstname = firstname
        self.lastname = lastname
    }
    
    static func == (lhs: UserInfosEntity, rhs: UserInfosEntity) -> Bool {
        return lhs.id == rhs.id
    }

}


