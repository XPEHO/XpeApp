//
//  FeatureFlippingTests.swift
//  XpeAppTests
//
//  Created by Ryan Debouvries on 03/09/2024.
//

import XCTest
@testable import XpeApp

final class FeatureFlippingTests: XCTestCase {
    var featureService: FeatureService!


    override func setUp() {
        super.setUp()
        featureService = FeatureService()
    }

    override func tearDownWithError() throws {
        featureService = nil
        super.tearDown()
    }

    func test_fetchFeaturesNotEmpty() throws {
        Task {
            let ns = await featureService.fetchFeatures()
            XCTAssertNotNil(ns)
            XCTAssertGreaterThan(ns!.count, 0)
        }
    }

}
