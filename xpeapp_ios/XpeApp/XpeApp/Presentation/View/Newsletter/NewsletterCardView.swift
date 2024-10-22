//
//  NewsletterCardView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 13/09/2024.
//

import SwiftUI
import xpeho_ui

struct NewsletterCard: View {
    var toastManager = ToastManager.instance
    @State var newsletter: NewsletterEntity
    
    @Environment(\.openURL) var openURL
    
    var open: Bool = false
    
    var body: some View {
        CollapsableCard(
            label: "Newsletter",
            headTag: TagPill(
                label: dateFormatter.string(from: newsletter.date),
                backgroundColor: XPEHO_THEME.GREEN_DARK_COLOR
            ),
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
                    if let url = URL(string: newsletter.pdfUrl)  {
                        openURL(url)
                    } else {
                        toastManager.setParams(
                            message: "URL de Newsletter incorrecte",
                            error: true
                        )
                        toastManager.play()
                    }
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
