//
//  Router.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 22/08/2024.
//

import SwiftUI

enum RouterItem: String {
    case home = "home"
    case newsletters = "newsletters"
    case campaign = "campaign"
    case campaignForm = "campaignForm"
    case expenseReport = "expenseReport"
    case colleagues = "colleagues"
    case cra = "cra"
    case vacation = "vacation"
    case debug = "debug"
}

class RouterManager: ObservableObject {
    @Published var selectedView: RouterItem = .home
    private var dataManager: DataManager
    
    // View Parameters
    @Published var selectedCampaign: QvstCampaign? = nil

    init(dataManager: DataManager) {
        self.dataManager = dataManager
    }
    
    func goTo(item: RouterItem) {
        selectedView = item
    }

    // Check in data manager si a router item is linked to a feature enabled
    func isEnabled(_ feature: RouterItem) -> Bool {
        guard let feature = dataManager.features[feature.rawValue] else {
            return false
        }
        if (ENV == .uat) {
            return feature.uatEnabled
        } else if (ENV == .prod) {
            return feature.prodEnabled
        } else {
            return false
        }
    }
}

struct Router: View {
    // Global Management
    @EnvironmentObject var dataManager: DataManager
    @EnvironmentObject var routerManager: RouterManager
    @EnvironmentObject var toastManager: ToastManager
    
    var body: some View {
        Group {
            switch routerManager.selectedView {
                case .home:
                    HomePageView()
                case .newsletters where routerManager.isEnabled(.newsletters):
                    NewsletterPageView()
                case .campaign where routerManager.isEnabled(.campaign):
                    QvstCampaignsPageView()
                case .campaignForm where routerManager.isEnabled(.campaign):
                    QvstCampaignFormView()
                case .expenseReport where routerManager.isEnabled(.expenseReport):
                    Text("Expense Report page placeholder")
                case .colleagues where routerManager.isEnabled(.colleagues):
                    Text("Colleagues page placeholder")
                case .cra where routerManager.isEnabled(.cra):
                    Text("CRA page placeholder")
                case .vacation where routerManager.isEnabled(.vacation):
                    Text("Vacation page placeholder")
                case .debug:
                    DebugPageView()
                default:
                    Text("Feature non présente")
            }
        }
    }
}
