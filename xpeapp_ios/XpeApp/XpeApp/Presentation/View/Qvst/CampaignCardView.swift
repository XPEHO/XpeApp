//
//  CampaignCardView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 13/09/2024.
//

import SwiftUI
import xpeho_ui

struct CampaignCard: View {
    var routerManager = RouterManager.instance
    var toastManager = ToastManager.instance
    @Binding var campaign: QvstCampaignEntity
    
    @Environment(\.openURL) var openURL
    
    var collapsable: Bool = true
    var defaultOpen: Bool = false
    
    var body: some View {
        CollapsableCard(
            label: campaign.name,
            tags: getTagsList(campaign: campaign),
            // We hide the button if the campaign is closed or completed
            button: getButton(campaign: campaign),
            icon: AnyView(
                Assets.loadImage(named: "QVST")
                    .renderingMode(.template)
                    .foregroundStyle(
                        (campaign.status == "OPEN")
                        ? XPEHO_THEME.XPEHO_COLOR
                        : XPEHO_THEME.GRAY_LIGHT_COLOR
                    )
            ),
            collapsable: collapsable,
            defaultOpen: defaultOpen || campaign.status == "OPEN"
        )
    }
    
    // Get the action button for a campaign
    private func getButton(campaign: QvstCampaignEntity) -> ClickyButton?{
        var result : ClickyButton?
        if (campaign.status == "OPEN") && !campaign.completed {
            result = ClickyButton(
                label: "Compléter",
                horizontalPadding: 50,
                verticalPadding: 12,
                onPress: {
                    routerManager.goTo(item: .campaignForm, parameters: ["campaign": campaign])
                }
            )
        } else if (campaign.status == "ARCHIVED") && (campaign.resultLink != "") {
            result = ClickyButton(
                label: "Voir les résultats",
                horizontalPadding: 50,
                verticalPadding: 12,
                onPress: {
                    openPdf(
                        url: campaign.resultLink,
                        toastManager: toastManager,
                        openMethod: openURL
                    )
                }
            )
        } else {
            result = nil
        }
        return result
    }
    
    // Get the tag pills for a campaign
    private func getTagsList(campaign: QvstCampaignEntity) -> [TagPill]{
        var result: [TagPill] = []
        // Init the tagsList depending the data that we got
        result.append(
            TagPill(
                label: campaign.themeName,
                backgroundColor: XPEHO_THEME.GREEN_DARK_COLOR
            )
        )
        
        // If the campaign is open we indicate the days remaining
        //      If it end in less or equal than 3 days and it hasn't been completed -> the color is red
        // Else we indicate the end date
        if (campaign.status == "OPEN") {
            let label: String
            if campaign.remainingDays > 1 {
                label = String(campaign.remainingDays) + " jours restants"
            } else {
                label = campaign.remainingDays == 1 ? "1 jour restant" : "Dernier jour"
            }
            result.append(
                TagPill(
                    label: label,
                    backgroundColor: (campaign.completed || (campaign.remainingDays > 3))
                        ? XPEHO_THEME.GREEN_DARK_COLOR
                        : XPEHO_THEME.RED_INFO_COLOR
                )
            )
        } else {
            result.append(
                TagPill(
                    label: dateFormatter.string(from: campaign.endDate),
                    backgroundColor: XPEHO_THEME.GREEN_DARK_COLOR
                )
            )
        }
        
        
        // If campaign is completed we indicate that it has been complete
        // Else if it is always open we indicate it can always be completed
        if (campaign.completed) {
            result.append(
                TagPill(
                    label: "Complétée",
                    backgroundColor: XPEHO_THEME.XPEHO_COLOR
                )
            )
        } else if (campaign.status == "OPEN") {
            result.append(
                TagPill(
                    label: "À compléter",
                    backgroundColor: XPEHO_THEME.GREEN_DARK_COLOR
                )
            )
        }
        
        return result
    }
}
