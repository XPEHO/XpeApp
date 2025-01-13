//
//  NewsletterCardView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 13/09/2024.
//

import SwiftUI
import xpeho_ui
import FirebaseAnalytics

struct NewsletterCard: View {
    var toastManager = ToastManager.instance
    @State var newsletter: NewsletterEntity
    
    @Environment(\.openURL) var openURL
    
    var open: Bool = false
        
    var body: some View {
        CollapsableCard(
            label: dateMonthFormatter.string(from: newsletter.date).capitalized,
            tags: newsletter.summary.map({
                TagPill(
                    label: String($0),
                    backgroundColor: XPEHO_THEME.GREEN_DARK_COLOR
                )
            }),
            button: ClickyButton(
                label: "Ouvrir",
                horizontalPadding: 50,
                verticalPadding: 12,
                onPress: {
                    Analytics.logEvent(
                        "open_newsletter",
                        parameters: [
                            AnalyticsParameterItemID: newsletter.id ?? "",
                            AnalyticsParameterItemName: dateMonthFormatter.string(from: newsletter.date).capitalized,
                        ]
                    );
                    openPdf(
                        url: newsletter.pdfUrl,
                        toastManager: toastManager,
                        openMethod: openURL
                    )
                }
            ),
            icon: AnyView(
                Assets.loadImage(named: "Newsletter")
                    .renderingMode(.template)
                    .foregroundStyle(XPEHO_THEME.XPEHO_COLOR)
            ),
            defaultOpen: open
        )
    }
}
