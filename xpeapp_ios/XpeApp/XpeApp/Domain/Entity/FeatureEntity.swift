//
//  FeatureEntity.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 13/09/2024.
//

import Foundation

@Observable class FeatureEntity: Equatable{
    let name: String
    let enabled: Bool
    
    init(name: String, enabled: Bool) {
        self.name = name
        self.enabled = enabled
    }
    
    static func from(model: FeatureModel) -> FeatureEntity {
        return FeatureEntity(
            name: model.name,
            enabled: Configuration.env == .prod ? model.prodEnabled : model.uatEnabled
        )
    }
    
    static func == (lhs: FeatureEntity, rhs: FeatureEntity) -> Bool {
        return lhs.name == rhs.name
    }
}
