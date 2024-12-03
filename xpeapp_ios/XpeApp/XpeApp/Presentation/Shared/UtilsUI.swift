//
//  UtilsUI.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 03/12/2024.
//

import Foundation
import SwiftUI

// Util function to open a pdf
func openPdf(url: String, toastManager: ToastManager, openMethod: OpenURLAction) {
    if let url = URL(string: url), UIApplication.shared.canOpenURL(url) {
        openMethod(url)
    } else {
        toastManager.setParams(
            message: "Impossible d'ouvrir l'URL",
            error: true
        )
        toastManager.play()
    }
}
