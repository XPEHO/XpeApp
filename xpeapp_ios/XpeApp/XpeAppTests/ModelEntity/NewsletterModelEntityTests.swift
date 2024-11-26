//
//  NewsletterModelEntityTests.swift
//  XpeAppTests
//
//  Created by Ryan Debouvries on 20/09/2024.
//

import XCTest
@testable import XpeApp

final class NewsletterEntityTests: XCTestCase {
    var currentDate: Date?
    var model: NewsletterModel?
    var entity: NewsletterEntity?

    override func setUp() {
        super.setUp()
        
        // GIVEN
        currentDate = Date()
        model = NewsletterModel(
            id: "newsletter_id",
            date: currentDate!,
            pdfUrl: "newsletter_url",
            publicationDate: currentDate!,
            summary: "summary 1, summary 2, summary 3",
            previewPath: "path/to/preview"
        )
        entity = NewsletterEntity(
            id: "newsletter_id",
            pdfUrl: "newsletter_url",
            date: currentDate!,
            summary: ["summary 1", "summary 2", "summary 3"],
            previewPath: "path/to/preview"
        )
    }

    override func tearDownWithError() throws {
        super.tearDown()
    }

    func test_modelToEntity() throws {
            
        // WHEN
        let modelToEntity = model!.toEntity()
        
        // THEN
        XCTAssertEqual(modelToEntity, entity)
    }
    
    func test_entityFromModel() throws {
            
        // WHEN
        let entityFromModel = NewsletterEntity.from(model: model!)
        
        // THEN
        XCTAssertEqual(entityFromModel, entity)
    }

}
