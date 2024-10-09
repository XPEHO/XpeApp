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
            // GIVEN
            newsletterSource.fetchAllNewslettersReturnData = nil
            
            // WHEN
            let newsletters = await newsletterRepo.getNewsletters()
            
            // THEN
            XCTAssertNil(newsletters)
        }
    }
    
    func test_getNewsletters_Success() throws {
        Task {
            let currentDate = Date()
            
            // GIVEN
            newsletterSource.fetchAllNewslettersReturnData = [
                NewsletterModel(
                    id: "id",
                    date: currentDate,
                    pdfUrl: "http://url.com",
                    publicationDate: currentDate,
                    summary: "summary 1,summary 2,summary 3"
                )
            ]
            
            // WHEN
            let newsletters = await newsletterRepo.getNewsletters()
            
            // THEN
            let dataExpected = [
                NewsletterEntity(
                    id: "id",
                    pdfUrl: "http://url.com",
                    date: currentDate,
                    summary: ["summary 1", "summary 2", "summary 3"]
                )
            ]
            
            XCTAssertNotNil(newsletters)
            XCTAssertEqual(newsletters!.count, 1)
            XCTAssertEqual(newsletters, dataExpected)
        }
    }
    
    func test_getLastNewsletter_fetchAllNewslettersError() throws {
        Task {
            // GIVEN
            newsletterSource.fetchAllNewslettersReturnData = nil
            
            // WHEN
            let lastNewsletter = await newsletterRepo.getLastNewsletter()
            
            // THEN
            XCTAssertNil(lastNewsletter)
        }
    }
    
    func test_getLastNewsletter_Success() throws {
        Task {
            let currentDate = Date()
            let currentDatePlusOneDay = Calendar.current.date(byAdding: .day, value: 1, to: currentDate)!
            
            // GIVEN
            newsletterSource.fetchAllNewslettersReturnData = [
                NewsletterModel(
                    id: "id1",
                    date: currentDate,
                    pdfUrl: "http://url.com",
                    publicationDate: currentDate,
                    summary: "summary 1,summary 2,summary 3"
                ),
                NewsletterModel(
                    id: "id2",
                    date: currentDatePlusOneDay,
                    pdfUrl: "http://url.com",
                    publicationDate: currentDate,
                    summary: "summary 1,summary 2,summary 3"
                )
            ]
            
            // WHEN
            let lastNewsletter = await newsletterRepo.getLastNewsletter()
            
            // THEN
            let dataExpected = NewsletterEntity(
                id: "id2",
                pdfUrl: "http://url.com",
                date: currentDatePlusOneDay,
                summary: ["summary 1", "summary 2", "summary 3"]
            )
            
            XCTAssertNotNil(lastNewsletter)
            XCTAssertEqual(lastNewsletter, dataExpected)
        }
    }

}
