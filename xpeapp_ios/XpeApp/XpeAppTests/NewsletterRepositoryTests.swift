//
//  NewsletterRepositoryTests.swift
//  XpeAppTests
//
//  Created by Ryan Debouvries on 03/09/2024.
//

import XCTest
@testable import XpeApp

final class NewsletterRepositoryTests: XCTestCase {
    // We take the mocked repository
    let newsletterRepo = NewsletterRepositoryImpl.mock
    let newsletterSource = MockFirebaseAPI.instance

    override func setUp() {
        super.setUp()
    }

    override func tearDownWithError() throws {
        super.tearDown()
    }

    func test_getNewsletters_fetchAllNewslettersError() throws {
        Task {
            newsletterSource.fetchAllNewslettersReturnData = nil
            let newsletters = await newsletterRepo.getNewsletters()
            
            XCTAssertNil(newsletters)
        }
    }
    
    func test_getFeatures_fetchAllFeaturesSuccess() throws {
        Task {
            let dataGiven = [
                NewsletterModel(
                    id: "id",
                    date: Date(),
                    pdfUrl: "http://url.com",
                    publicationDate: Date(),
                    summary: "summary 1,summary 2,summary 3"
                )
            ]
            let dataExpected = [
                NewsletterEntity(
                    id: "id",
                    pdfUrl: "http://url.com",
                    publicationDate: Date(),
                    summary: ["summary 1", "summary 2", "summary 3"]
                )
            ]
            newsletterSource.fetchAllNewslettersReturnData = dataGiven
            let newsletters = await newsletterRepo.getNewsletters()
            
            XCTAssertNotNil(newsletters)
            XCTAssertEqual(newsletters!.count, 1)
            XCTAssertEqual(newsletters, dataExpected)
        }
    }

}
