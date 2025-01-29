//
//  ConstantsSidebarItem.swift
//  XpeApp
//
//  Created by Théo Lebègue on 29/01/2025.
//

import Foundation
struct MenuItem {
    let navigationItem: RouterItem?
    let iconName: String
    let label: String
    let featureFlippingId: RouterItem
}
let menuItems: [MenuItem] = [
    MenuItem(
        navigationItem: .newsletters,
        iconName: "Newsletter",
        label: "Newsletters",
        featureFlippingId: .newsletters
    ),
    MenuItem(
        navigationItem: .campaign,
        iconName: "QVST",
        label: "Campaign",
        featureFlippingId: .campaign
    ),
    MenuItem(
        navigationItem: .expenseReport,
        iconName: "Receipt",
        label: "Expense Report",
        featureFlippingId: .expenseReport
    ),
    MenuItem(
        navigationItem: .colleagues,
        iconName: "ContactFill",
        label: "Colleagues",
        featureFlippingId: .colleagues
    ),
    MenuItem(
        navigationItem: .cra,
        iconName: "Briefcase",
        label: "CRA",
        featureFlippingId: .cra
    ),
    MenuItem(
        navigationItem: .vacation,
        iconName: "PlaneDeparture",
        label: "Vacation",
        featureFlippingId: .vacation
    ),
]
