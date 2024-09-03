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

class RouterManager: ObservableObject {
    @Published var selectedView: RouterItem = .home
    
    // View Parameters
    @Published var selectedCampaign: QvstCampaign? = nil
    
    func goTo(item: RouterItem) {
        selectedView = item
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
                case .newsletter:
                    NewsletterPageView()
                case .cra:
                    Text("CRA page placeholder")
                case .vacation:
                    Text("Vacation page placeholder")
                case .expenseReport:
                    Text("Expense Report page placeholder")
                case .contacts:
                    Text("Contacts page placeholder")
                case .qvstTemp:
                    QvstCampaignsPageView()
                case .qvstDetail:
                    QvstCampaignFormView()
                case .debug:
                    DebugPageView()
            }
        }
    }
}
