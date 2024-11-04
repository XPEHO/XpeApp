//
//  NewsletterPreviewView.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 13/09/2024.
//

import SwiftUI
import xpeho_ui

struct LastNewsletterPreview: View {
    var toastManager = ToastManager.instance
    var lastNewsletter: NewsletterEntity?
    
    @Environment(\.openURL) var openURL
    
    var body: some View {
        if let lastNewsletter = lastNewsletter {
            FilePreviewButton (
                labelStart: "Newsletter",
                labelEnd: dateMonthFormatter.string(from: lastNewsletter.date).capitalized,
                imagePreview: AnyView(
                    Image("NewsletterPreviewTest")
                        .resizable()
                ),
                tags: lastNewsletter.summary.map({
                    TagPill(
                        label: String($0)
                    )
                }),
                height: 200,
                onPress: {
                    if let url = URL(string: lastNewsletter.pdfUrl)  {
                        openURL(url)
                    } else {
                        toastManager.setParams(
                            message: "URL de Newsletter incorrecte",
                            error: true
                        )
                        toastManager.play()
                    }
                }
            )
        } else {
            ProgressView("Chargement de la dernière newsletter...")
                .progressViewStyle(CircularProgressViewStyle())
                .padding()
        }
    }
}
