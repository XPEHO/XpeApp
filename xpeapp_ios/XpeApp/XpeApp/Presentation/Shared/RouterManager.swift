//
//  RouterManager.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 22/08/2024.
//

import SwiftUI

// The raw values that reference a feature of the app need to reflect the ids of features in firebase database
enum RouterItem: String {
    case home = "home"
    case newsletters = "newsletters"
    case campaign = "campaign"
    case campaignForm = "campaignForm"
    case expenseReport = "expenseReport"
    case colleagues = "colleagues"
    case cra = "cra"
    case vacation = "vacation"
    case profile = "profile"
    case about = "about"
    case debug = "debug"
}

@Observable class RouterManager {
    static let instance = RouterManager()
    
    private init() {
        // This initializer is intentionally left empty to make private
        // to prevent use without shared instance
    }
    
    var selectedView: RouterItem = .home
    var preventView: RouterItem = .home
    var parameters: [String: Any] = [:]
    
    func goTo(item: RouterItem, parameters: [String: Any] = [:]) {
        self.preventView = self.selectedView
        self.selectedView = item
        self.parameters = parameters
        FeatureManager.instance.update()
    }
    
    func goBack() {
        self.selectedView = self.preventView
        self.parameters = [:]
        FeatureManager.instance.update()
    }
}
