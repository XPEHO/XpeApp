//
//  NewsletterPageViewModel.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 13/09/2024.
//

import Foundation

@Observable class NewsletterPageViewModel {
    
    static let instance = NewsletterPageViewModel()
    
    // Make private constructor to prevent use without shared instance
    private init() {
        initNewsletters()
    }

    var newsletters: [NewsletterEntity]? = nil
    
    func update() {
        initNewsletters()
    }
    
    private func initNewsletters() {
        Task{
            let obtainedNewsletters = await NewsletterRepositoryImpl.instance.getNewsletters()
            DispatchQueue.main.async {
                self.newsletters = obtainedNewsletters
            }
        }
    }
}
