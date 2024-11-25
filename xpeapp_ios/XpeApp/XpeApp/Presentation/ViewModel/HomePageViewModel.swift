//
//  HomePageViewModel.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/09/2024.
//

import Foundation
import SwiftUI

@Observable class HomePageViewModel {
    
    static let instance = HomePageViewModel()
    
    // Make private constructor to prevent use without shared instance
    private init() {
        initLastNewsletter()
        initActiveCampaigns()
    }

    var lastNewsletter: NewsletterEntity? = nil
    var activeCampaigns: [QvstCampaignEntity]? = nil
    var lastNewsletterPreview: UIImage? = nil
    
    func update() {
        initLastNewsletter()
        initActiveCampaigns()
    }
    
    private func initLastNewsletter() {
        Task{
            let obtainedLastNewsletter = await NewsletterRepositoryImpl.instance.getLastNewsletter()
            DispatchQueue.main.async {
                self.lastNewsletter = obtainedLastNewsletter
                self.initLastNewsletterPreview()
            }
        }
    }
    
    private func initLastNewsletterPreview() {
        Task{
            NewsletterRepositoryImpl.instance.getNewsletterPreviewUrl(
                newsletter: self.lastNewsletter
            ) { obtainedPreviewUrl in
                if let previewUrl = obtainedPreviewUrl,
                    let url = URL(string: previewUrl) {
                    let urlSession = URLSession.shared
                    let dataTask = urlSession.dataTask(with: url) { data, response, error in
                        if let error = error {
                            debugPrint("Failed to load image data: \(error.localizedDescription)")
                            return
                        }
                        if let data = data, let image = UIImage(data: data) {
                            DispatchQueue.main.async {
                                self.lastNewsletterPreview = image
                            }
                        } else {
                            debugPrint("Failed to load image for last newsletter")
                        }
                    }
                    dataTask.resume()
                } else {
                    debugPrint("Failed to load image for last newsletter")
                }
            }
        }
    }
    
    private func initActiveCampaigns() {
        Task{
            let obtainedActiveCampaigns = await QvstRepositoryImpl.instance.getActiveCampaigns()
            DispatchQueue.main.async {
                self.activeCampaigns = obtainedActiveCampaigns
            }
        }
    }
}
