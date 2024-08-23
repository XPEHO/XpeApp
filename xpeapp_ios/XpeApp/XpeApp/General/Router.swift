//
//  Router.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 22/08/2024.
//

import SwiftUI

enum RouterItem {
    case home, newsletter, cra, vacation, expenseReport, contacts, qvstTemp, qvstDetail, debug
}

struct RouterManager {
    var selectedView: RouterItem = .home
    
    // View Parameters
    var selectedCampaign: QvstCampaign? = nil
    
    mutating func goTo(item: RouterItem) {
        selectedView = item
    }
}

struct Router: View {
    // Global Management
    @Binding var routerManager: RouterManager
    @ObservedObject var dataManager: DataManager
    @Binding var toastManager: ToastManager
    
    var body: some View {
        Group {
            switch routerManager.selectedView {
                case .home:
                    HomePageView(
                        routerManager: $routerManager,
                        dataManager: dataManager,
                        toastManager: $toastManager
                    )
                case .newsletter:
                    NewsletterPageView(
                        dataManager: dataManager,
                        toastManager: $toastManager
                    )
                case .cra:
                    Text("CRA page placeholder")
                case .vacation:
                    Text("Vacation page placeholder")
                case .expenseReport:
                    Text("Expense Report page placeholder")
                case .contacts:
                    Text("Contacts page placeholder")
                case .qvstTemp:
                    QvstCampaignsPageView(
                        routerManager: $routerManager,
                        dataManager: dataManager
                    )
                case .qvstDetail:
                    QvstCampaignFormView(
                        routerManager: $routerManager,
                        dataManager: dataManager,
                        toastManager: $toastManager
                    )
                case .debug:
                    DebugPageView()
            }
        }
    }
}
