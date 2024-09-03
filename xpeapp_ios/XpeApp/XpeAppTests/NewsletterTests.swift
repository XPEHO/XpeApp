//
//  NewsletterTests.swift
//  XpeAppTests
//
//  Created by Ryan Debouvries on 03/09/2024.
//

import XCTest
@testable import XpeApp

final class NewsletterTests: XCTestCase {
    var newsletterService: NewsletterService!


    override func setUp() {
        super.setUp()
        newsletterService = NewsletterService()
    }

    override func tearDownWithError() throws {
        newsletterService = nil
        super.tearDown()
    }

    func test_fetchNewslettersNotEmpty() throws {
        Task {
            let ns = await newsletterService.fetchNewsletters()
            XCTAssertNotNil(ns)
            XCTAssertGreaterThan(ns!.count, 0)
        }
    }

}
