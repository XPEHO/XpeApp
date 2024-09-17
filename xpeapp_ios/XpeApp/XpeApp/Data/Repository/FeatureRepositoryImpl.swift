//
//  FeatureRepositoryImpl.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 03/09/2024.
//

import Foundation

class FeatureRepositoryImpl: FeatureRepository {
    // An instance for app and a mock for tests
    static let instance = FeatureRepositoryImpl()
    static let mock = FeatureRepositoryImpl(
        dataSource: MockFirebaseAPI.instance
    )
    
    // Data source to use
    private let dataSource: FirebaseAPIProtocol

    // Make private constructor to prevent use without shared instances
    private init(
        dataSource: FirebaseAPIProtocol = FirebaseAPI.instance
    ) {
        self.dataSource = dataSource
    }
    
    func getFeatures() async -> [String : FeatureEntity]? {
        // Fetch data
        guard let features = await dataSource.fetchAllFeatures() else {
            debugPrint("Failed call to fetchAllFeatures in getFeatures")
            return nil
        }
        
        return features.reduce(into: [String: FeatureEntity]()) { result, feature in
            if let id = feature.id {
                result[id] = feature.toEntity()
            }
        }
    }
}
