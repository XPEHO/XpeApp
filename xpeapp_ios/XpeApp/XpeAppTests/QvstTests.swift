//
//  QvstRepositoryTests.swift
//  XpeAppTests
//
//  Created by Ryan Debouvries on 21/08/2024.
//

import XCTest
@testable import XpeApp

final class QvstRepositoryTests: XCTestCase {

    /*override func setUp() {
        super.setUp()
        urlSessionMock = URLSessionMock()
        qvstRepo.setSession(session: urlSessionMock)
    }

    override func tearDown() {
        qvstRepo.setSession(session: URLSession.shared)
        urlSessionMock = nil
        super.tearDown()
    }*/

/*
    func test_getCampaigns() async throws {
        // Given
        let mockResponseData = """
        [
            {
                "id":"1",
                "name":"Campaign 1",
                "theme":{
                    "id":"1",
                    "name":"Theme 1"
                },
                "status":"OPEN",
                "start_date":"2024-01-01",
                "end_date":"2024-01-08",
                "participation_rate":"14.2857"
            }
        ]
        """.data(using: .utf8)!
        urlSessionMock.mockData = mockResponseData

        // When
        let result = await qvstRepo.getCampaigns()
        debugPrint(result)

        // Then
        XCTAssertNotNil(result, "Result should not be nil")
        XCTAssertEqual(result?.first?.id, "1")
        XCTAssertEqual(result?.first?.name, "Campaign 1")
        XCTAssertEqual(result?.first?.themeName, "Theme 1")
        XCTAssertEqual(result?.first?.status, "OPEN")
    }
    
    func test_getActiveCampaigns() async throws {
        // Given
        let mockResponseData = """
        [
            {
                "id":"2",
                "name":"Active Campaign 1",
                "theme":{
                    "id":"2",
                    "name":"Theme 2"
                },
                "status":"OPEN",
                "start_date":"2024-02-01",
                "end_date":"2024-02-08",
                "participation_rate":"20.0"
            }
        ]
        """.data(using: .utf8)!
        urlSessionMock.mockData = mockResponseData

        // When
        let result = await qvstRepo.getActiveCampaigns()

        // Then
        XCTAssertNotNil(result, "Result should not be nil")
        XCTAssertEqual(result?.first?.id, "2")
        XCTAssertEqual(result?.first?.name, "Active Campaign 1")
        XCTAssertEqual(result?.first?.themeName, "Theme 2")
        XCTAssertEqual(result?.first?.status, "OPEN")
    }*/
}

