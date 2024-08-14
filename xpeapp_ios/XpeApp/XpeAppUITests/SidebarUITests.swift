//
//  SidebarUITests.swift
//  XpeAppUITests
//
//  Created by Ryan Debouvries on 13/08/2024.
//

//
//  ClickyButtonTests.swift
//  XpehoUIUITests
//
//  Created by Ryan Debouvries on 19/07/2024.
//

import SwiftUI
import XCTest
@testable import XpeApp

import xpeho_ui

final class SidebarUITests: XCTestCase {

    override func setUpWithError() throws {
        continueAfterFailure = false
        // Setup landscape mode
        XCUIDevice.shared.orientation = .portrait
        
        // Load fonts for snapshots
        Fonts.registerFonts()
    }
    
    func testSidebar_WeCanOpenIt() throws {
        let app = XCUIApplication()
        app.launch()
        
        // Tap on burger button
        let burgerButton = app.buttons["BurgerButton"]
        if burgerButton.waitForExistence(timeout: 10) {
            burgerButton.tap()
        } else {
            XCTFail("BurgerButton did not appear within 10 seconds")
        }
        
        // Check if the sidebar appears
        let sidebar = app.otherElements["Sidebar"]
        if sidebar.waitForExistence(timeout: 10) {
            XCTAssertTrue(sidebar.exists)
        } else {
            XCTFail("Sidebar did not appear within 10 seconds")
        }
    }
    
    func testSidebar_WeCanCloseItUsingCloseButton() throws {
        let app = XCUIApplication()
        app.launch()
        
        // Tap on burger button
        let burgerButton = app.buttons["BurgerButton"]
        if burgerButton.waitForExistence(timeout: 10) {
            burgerButton.tap()
        } else {
            XCTFail("BurgerButton did not appear within 10 seconds")
        }
        
        // Tap on the close button of the sidebar
        let sidebarCloseButton = app.buttons["Sidebar_CloseButton"]
        if sidebarCloseButton.waitForExistence(timeout: 10) {
            sidebarCloseButton.tap()
        } else {
            XCTFail("The Sidebar_CloseButton did not appear within 10 seconds.")
        }
        
        // Check if the sidebar disappears
        let sidebar = app.otherElements["Sidebar"]
        XCTAssertFalse(sidebar.exists)
    }
    
    func testSidebar_WeCanNavigateToNewslettersAndItIsClosing() throws {
        let app = XCUIApplication()
        app.launch()
        
        // Tap on burger button
        let burgerButton = app.buttons["BurgerButton"]
        if burgerButton.waitForExistence(timeout: 10) {
            burgerButton.tap()
        } else {
            XCTFail("BurgerButton did not appear within 10 seconds")
        }
        
        // Tap on Newsletter link in the sidebar
        let newslettersButton = app.buttons["Sidebar_Newsletters"]
        if newslettersButton.waitForExistence(timeout: 10) {
            newslettersButton.tap()
        } else {
            XCTFail("Sidebar_Newsletters did not appear within 10 seconds")
        }
        
        // Check if the sidebar disappears
        let sidebar = app.otherElements["Sidebar"]
        XCTAssertFalse(sidebar.exists)
        
        // Check that we are on the Newsletter view
        let newslettersView = app.scrollViews["NewsletterView"]
        if newslettersView.waitForExistence(timeout: 10) {
            XCTAssertTrue(newslettersView.exists)
        } else {
            XCTFail("NewsletterView did not appear within 10 seconds")
        }
    }
    
    func testSidebar_WeCanNavigateToQvstAndItIsClosing() throws {
        let app = XCUIApplication()
        app.launch()
        
        // Tap on burger button
        let burgerButton = app.buttons["BurgerButton"]
        if burgerButton.waitForExistence(timeout: 10) {
            burgerButton.tap()
        } else {
            XCTFail("BurgerButton did not appear within 10 seconds")
        }
        
        // Tap on QVST link in the sidebar
        let qvstButton = app.buttons["Sidebar_QVST"]
        if qvstButton.waitForExistence(timeout: 10) {
            qvstButton.tap()
        } else {
            XCTFail("Sidebar_QVST did not appear within 10 seconds")
        }
        
        // Check if the sidebar disappears
        let sidebar = app.otherElements["Sidebar"]
        XCTAssertFalse(sidebar.exists)
        
        // Check that we are on the QVST view
        let qvstView = app.scrollViews["QvstView"]
        if qvstView.waitForExistence(timeout: 10) {
            XCTAssertTrue(qvstView.exists)
        } else {
            XCTFail("QvstView did not appear within 10 seconds")
        }
    }
    
    func testSidebar_WeCanNavigateToHomeAndItIsClosing() throws {
        let app = XCUIApplication()
        app.launch()
        
        // Tap on burger button
        let burgerButton = app.buttons["BurgerButton"]
        if burgerButton.waitForExistence(timeout: 10) {
            burgerButton.tap()
        } else {
            XCTFail("BurgerButton did not appear within 10 seconds")
        }
        
        // Tap on Home link in the sidebar
        let homeButton = app.buttons["Sidebar_Accueil"]
        if homeButton.waitForExistence(timeout: 10) {
            homeButton.tap()
        } else {
            XCTFail("Sidebar_Accueil did not appear within 10 seconds")
        }
        
        // Check if the sidebar disappears
        let sidebar = app.otherElements["Sidebar"]
        XCTAssertFalse(sidebar.exists)
        
        // Check that we are on the Home view
        let homeView = app.scrollViews["HomeView"]
        if homeView.waitForExistence(timeout: 10) {
            XCTAssertTrue(homeView.exists)
        } else {
            XCTFail("HomeView did not appear within 10 seconds")
        }
    }

}
