//
//  UtilsTests.swift
//  XpeAppTests
//
//  Created by Ryan Debouvries on 20/09/2024.
//

import XCTest
@testable import XpeApp

final class UtilsTests: XCTestCase{

    override func setUp() {
        super.setUp()
    }

    override func tearDownWithError() throws {
        super.tearDown()
    }

    func test_countDaysBetween() throws {
        // GIVEN
        let currentDate = Date()
        let currentDatePlusOneDay = Calendar.current.date(byAdding: .day, value: 1, to: currentDate)!
        
        // WHEN
        let daysBetween = countDaysBetween(currentDate, and: currentDatePlusOneDay)
        
        // THEN
        XCTAssertEqual(daysBetween, 1)
    }
    
    func test_dateFormatter() throws {
        // GIVEN
        var dateComponents = DateComponents(year: 2000, month: 1, day: 1)
        dateComponents.timeZone = TimeZone(secondsFromGMT: 0)
        let date = Calendar.current.date(from: dateComponents)!
        
        // WHEN
        let dateFormatted = dateFormatter.string(from: date)
        
        // THEN
        let dateExpected = "01/01/2000"
        XCTAssertEqual(dateFormatted, dateExpected)
    }

    func test_dateMonthFormatter() throws {
        // GIVEN
        var dateComponents = DateComponents(year: 2000, month: 1, day: 1)
        dateComponents.timeZone = TimeZone(secondsFromGMT: 0)
        let date = Calendar.current.date(from: dateComponents)!
        
        // WHEN
        let monthFormatted = dateMonthFormatter.string(from: date)
        
        // THEN
        let monthExpected = "janvier"
        XCTAssertEqual(monthFormatted, monthExpected)
    }
    
    func test_isValidEmail() throws {
        // GIVEN
        let wrongEmail1 = "test"
        let wrongEmail2 = "test.fr"
        let wrongEmail3 = "test@test"
        let goodEmail = "test@test.fr"
        
        // WHEN
        let wrongEmail1Valid = isValidEmail(wrongEmail1)
        let wrongEmail2Valid = isValidEmail(wrongEmail2)
        let wrongEmail3Valid = isValidEmail(wrongEmail3)
        let goodEmailValid = isValidEmail(goodEmail)
        
        // THEN
        XCTAssertEqual(wrongEmail1Valid, false)
        XCTAssertEqual(wrongEmail2Valid, false)
        XCTAssertEqual(wrongEmail3Valid, false)
        XCTAssertEqual(goodEmailValid, true)
    }

}
