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
    @State private var homePageViewModel = HomePageViewModel.instance
    
    @Environment(\.openURL) var openURL
    
    init(lastNewsletter: NewsletterEntity? = nil) {
        self.lastNewsletter = lastNewsletter
    }
    
    var body: some View {
        if let lastNewsletter = lastNewsletter {
            FilePreviewButton (
                labelStart: "Newsletter",
                labelEnd: dateMonthFormatter.string(from: lastNewsletter.date).capitalized,
                imagePreview: AnyView (
                    Group {
                        if let preview = homePageViewModel.lastNewsletterPreview {
                            Image(uiImage: preview)
                                .resizable()
                        } else {
                            Image("NewsletterPlaceHolder")
                                .resizable()
                        }
                    }
                ),
                tags: lastNewsletter.summary.map({
                    TagPill(
                        label: String($0)
                    )
                }),
                height: 200,
                onPress: {
                    openPdf(
                        url: lastNewsletter.pdfUrl,
                        toastManager: toastManager,
                        openMethod: openURL
                    )
                }
            )
        } else {
            ProgressView("Chargement de la dernière newsletter...")
                .progressViewStyle(CircularProgressViewStyle())
                .padding()
        }
    }
}
