//
//  QvstTests.swift
//  XpeAppTests
//
//  Created by Ryan Debouvries on 21/08/2024.
//

import XCTest
@testable import XpeApp

final class QvstTests: XCTestCase {
    var qvstService: QvstService!
    var urlSessionMock: URLSessionMock!

    override func setUp() {
        super.setUp()
        urlSessionMock = URLSessionMock()
        qvstService = QvstService(session: urlSessionMock)
    }

    override func tearDown() {
        qvstService = nil
        urlSessionMock = nil
        super.tearDown()
    }

    func test_fetchAllCampaigns() async throws {
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
        let result = try await qvstService.fetchAllCampaigns()

        // Then
        XCTAssertNotNil(result, "Result should not be nil")
        XCTAssertEqual(result?.first?.id, "1")
        XCTAssertEqual(result?.first?.name, "Campaign 1")
    }
    
    func test_fetchActiveCampaigns() async throws {
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
        let result = try await qvstService.fetchActiveCampaigns()

        // Then
        XCTAssertNotNil(result, "Result should not be nil")
        XCTAssertEqual(result?.first?.id, "2")
        XCTAssertEqual(result?.first?.name, "Active Campaign 1")
    }
}

// Mock URLSession
class URLSessionMock: URLSessionProtocol {
    var mockData: Data?
    var mockError: Error?

    init(data: Data? = nil, error: Error? = nil) {
        self.mockData = data
        self.mockError = error
    }

    func data(for request: URLRequest) async throws -> (Data, URLResponse) {
        if let error = mockError {
            throw error
        }
        let response = URLResponse(url: request.url!, mimeType: nil, expectedContentLength: mockData?.count ?? 0, textEncodingName: nil)
        return (mockData ?? Data(), response)
    }

    func data(from url: URL) async throws -> (Data, URLResponse) {
        if let error = mockError {
            throw error
        }
        let response = URLResponse(url: url, mimeType: nil, expectedContentLength: mockData?.count ?? 0, textEncodingName: nil)
        return (mockData ?? Data(), response)
    }
}

