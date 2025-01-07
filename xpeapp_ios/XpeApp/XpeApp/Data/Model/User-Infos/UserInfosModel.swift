//
//  UserInfosModel.swift
//  XpeApp
//
//  Created by Théo Lebègue on 07/01/2025.
//

import Foundation

struct UserInfosModel: Codable {
    var id: Int?
    let email: String
    let firstname: String
    let lastname: String
    
    init(id: Int? = nil, email: String, firstname: String, lastname: String) {
        self.id = id
        self.email = email
        self.firstname = firstname
        self.lastname = lastname
    }
    
    func toEntity() -> UserInfosEntity {
        return UserInfosEntity(
            id: id.map { String($0) } ?? "",
            email: self.email,
            firstname: self.firstname,
            lastname: self.lastname
        );
    }
}
