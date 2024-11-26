//
//  NewsletterRepositoryImpl.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation

class NewsletterRepositoryImpl: NewsletterRepository {
    // An instance for app and a mock for tests
    static let instance = NewsletterRepositoryImpl()
    static let mock = NewsletterRepositoryImpl(
        dataSource: MockFirebaseAPI.instance
    )
    
    // Data source to use
    private let dataSource: FirebaseAPIProtocol

    // Make private constructor to prevent use without shared instances
    private init(
        dataSource: FirebaseAPIProtocol = FirebaseAPI.instance
    ) {
        self.dataSource = dataSource
    }
    
    func getNewsletters() async -> [NewsletterEntity]? {
        // Fetch data
        guard let newsletters = await dataSource.fetchAllNewsletters() else {
            debugPrint("Failed call to fetchAllNewsletters in getNewsletters")
            return nil
        }
        
        return newsletters.sorted(by: { $0.date > $1.date }).map { model in
            model.toEntity()
        }
    }
    
    func getLastNewsletter() async -> NewsletterEntity? {
        // Fetch data
        guard let newsletters = await dataSource.fetchAllNewsletters() else {
            debugPrint("Failed call to fetchAllNewsletters in getLastNewsletter")
            return nil
        }
        
        let sortedNewsletters = newsletters.sorted(by: { $0.date < $1.date })
        
        return sortedNewsletters.last?.toEntity()
    }
    
    func getNewsletterPreviewUrl(newsletter: NewsletterEntity?, completion: @escaping (String?) -> Void) {
        guard let newsletter = newsletter else {
            debugPrint("No newsletter")
            return
        }
        dataSource.getNewsletterPreviewUrl(previewPath: newsletter.previewPath) { url in
            completion(url)
        }
    }

    // Classify newsletters by year
    func classifyNewsletters(newsletters: [NewsletterEntity]) -> [Int: [NewsletterEntity]] {
        return newsletters.reduce(into: [Int: [NewsletterEntity]]()) { classifiedNewsletters, newsletter in
            let year = Calendar.current.component(.year, from: newsletter.date)
            classifiedNewsletters[year, default: []].append(newsletter)
        }
    }
}
