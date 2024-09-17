//
//  FeatureModel.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 03/09/2024.
//

import Foundation

struct FeatureModel: Codable {
    var id: String?
    let description: String
    let name: String
    let prodEnabled: Bool
    let uatEnabled: Bool
    
    init(id: String? = nil, description: String, name: String, prodEnabled: Bool, uatEnabled: Bool) {
        self.id = id
        self.description = description
        self.name = name
        self.prodEnabled = prodEnabled
        self.uatEnabled = uatEnabled
    }
    
    func toEntity() -> FeatureEntity {
        return FeatureEntity(
            name: self.name,
            enabled: Configuration.env == .prod ? self.prodEnabled : self.uatEnabled
        )
    }
}
