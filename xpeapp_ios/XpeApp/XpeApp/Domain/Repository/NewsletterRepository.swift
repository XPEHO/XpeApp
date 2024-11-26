//
//  NewsletterRepository.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/09/2024.
//

import Foundation

protocol NewsletterRepository {
    func getNewsletters() async -> [NewsletterEntity]?
    func getLastNewsletter() async -> NewsletterEntity?
    func getNewsletterPreviewUrl(newsletter: NewsletterEntity?, completion: @escaping (String?) -> Void)
    func classifyNewsletters(newsletters: [NewsletterEntity]) -> [Int: [NewsletterEntity]]
}
