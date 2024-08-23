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

    func testExample() throws {
        // This is an example of a functional test case.
        // Use XCTAssert and related functions to verify your tests produce the correct results.
        // Any test you write for XCTest can be annotated as throws and async.
        // Mark your test throws to produce an unexpected failure when your test encounters an uncaught error.
        // Mark your test async to allow awaiting for asynchronous code to complete. Check the results with assertions afterwards.
    }

    func testPerformanceExample() throws {
        // This is an example of a performance test case.
        self.measure {
            // Put the code you want to measure the time of here.
        }
    }
    
    func testFirebaseFirestoreConnected() throws {
        Task {
            let ns = await newsletterService.fetchNewsletters()
            XCTAssertNotNil(ns)
        }
    }
    
    func testFirebaseNewsletterCollectionNotEmpty() throws {
        Task {
            let ns = await newsletterService.fetchNewsletters()
            XCTAssertNotNil(ns)
            XCTAssertGreaterThan(ns!.count, 0)
        }
    }

}
