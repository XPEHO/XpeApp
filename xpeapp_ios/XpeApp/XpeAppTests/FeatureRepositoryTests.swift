//
//  FeatureRepositoryTests.swift
//  XpeAppTests
//
//  Created by Ryan Debouvries on 03/09/2024.
//

import XCTest
@testable import XpeApp

final class FeatureRepositoryTests: XCTestCase {
    // We take the mocked repository
    let featureRepo = FeatureRepositoryImpl.mock
    let featureSource = MockFirebaseAPI.instance

    override func setUp() {
        super.setUp()
    }

    override func tearDownWithError() throws {
        super.tearDown()
    }

    func test_getFeatures_fetchAllFeaturesError() throws {
        Task {
            featureSource.fetchAllFeaturesReturnData = nil
            let features = await featureRepo.getFeatures()
            
            XCTAssertNil(features)
        }
    }
    
    func test_getFeatures_fetchAllFeaturesSuccess() throws {
        Task {
            let dataGiven = [
                FeatureModel(
                    id: "id",
                    description: "description",
                    name: "Feature",
                    prodEnabled: true,
                    uatEnabled: true
                )
            ]
            let dataExpected = [
                "id": FeatureEntity(
                    name: "Feature",
                    enabled: true
                )
            ]
            featureSource.fetchAllFeaturesReturnData = dataGiven
            let features = await featureRepo.getFeatures()
            
            XCTAssertNotNil(features)
            XCTAssertEqual(features!.count, 1)
            XCTAssertEqual(features, dataExpected)
        }
    }
}
