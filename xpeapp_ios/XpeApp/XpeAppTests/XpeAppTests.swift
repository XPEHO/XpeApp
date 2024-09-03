//
//  XpeAppTests.swift
//  XpeAppTests
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import XCTest
@testable import XpeApp

final class XpeAppTests: XCTestCase {
    var newsletterService: NewsletterService!

    override func setUp() {
        super.setUp()
        newsletterService = NewsletterService()
    }

    override func tearDownWithError() throws {
        newsletterService = nil
        super.tearDown()
    }
    
    func test_FirebaseFirestoreConnected() throws {
        Task {
            let ns = await newsletterService.fetchNewsletters()
            XCTAssertNotNil(ns)
        }
    }

}
