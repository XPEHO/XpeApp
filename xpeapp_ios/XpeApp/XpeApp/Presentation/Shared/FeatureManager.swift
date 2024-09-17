//
//  FeatureManager.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 13/09/2024.
//

import Foundation

@Observable class FeatureManager {
    static let instance = FeatureManager()
    var features: [String: FeatureEntity]? = nil
    
    // Make private constructor to prevent use without shared instance
    private init() {
        initFeatures()
    }
    
    func update() {
        initFeatures()
    }
    
    private func initFeatures() {
        Task{
            let obtainedFeatures = await FeatureRepositoryImpl.instance.getFeatures()
            DispatchQueue.main.async {
                self.features = obtainedFeatures
            }
        }
    }
    
    func isEnabled(item: RouterItem) -> Bool {
        guard let features = self.features else {
            debugPrint("No features")
            return false
        }
        if let feature = features[item.rawValue] {
            return feature.enabled
        } else {
            return false
        }
    }
    
    func getName(item: RouterItem) -> String {
        guard let features = self.features else {
            debugPrint("No features")
            return ""
        }
        if let feature = features[item.rawValue] {
            return feature.name
        } else {
            return ""
        }
    }
}
