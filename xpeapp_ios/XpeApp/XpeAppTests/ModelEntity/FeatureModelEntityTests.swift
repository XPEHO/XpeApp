//
//  FeatureEntityTests.swift
//  XpeAppTests
//
//  Created by Ryan Debouvries on 20/09/2024.
//

import XCTest
@testable import XpeApp

final class FeatureEntityTests: XCTestCase{
    // GIVEN
    let model = FeatureModel(
        description: "feature_description",
        name: "feature_name",
        prodEnabled: true,
        uatEnabled: true
    )
    let entity = FeatureEntity(
        name: "feature_name",
        enabled: true
    )

    override func setUp() {
        super.setUp()
    }

    override func tearDownWithError() throws {
        super.tearDown()
    }

    func test_modelToEntity() throws {
            
        // WHEN
        let modelToEntity = model.toEntity()
        
        // THEN
        XCTAssertEqual(modelToEntity, entity)
    }
    
    func test_entityFromModel() throws {
            
        // WHEN
        let entityFromModel = FeatureEntity.from(model: model)
        
        // THEN
        XCTAssertEqual(entityFromModel, entity)
    }

}
