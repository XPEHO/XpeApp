//
//  XpeAppTests.swift
//  XpeAppTests
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import XCTest
@testable import XpeApp

final class XpeAppTests: XCTestCase {
    let firebaseAPI = FirebaseAPI.instance

    override func setUp() {
        super.setUp()
    }

    override func tearDownWithError() throws {
        super.tearDown()
    }
    
    func test_FirebaseFirestoreConnected() throws {
        Task {
            let fetch = await firebaseAPI.fetchAllNewsletters()
            XCTAssertNotNil(fetch)
        }
    }
}
