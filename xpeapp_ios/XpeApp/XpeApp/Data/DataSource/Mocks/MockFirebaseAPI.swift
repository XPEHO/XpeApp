//
//  MockFirebaseAPI.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 18/09/2024.
//

import Foundation

class MockFirebaseAPI: FirebaseAPIProtocol {
    static let instance = MockFirebaseAPI()
    
    // Mocked Returns
    var fetchAllFeaturesReturnData: [FeatureModel]?
    var fetchAllNewslettersReturnData: [NewsletterModel]?
    var getNewsletterPreviewUrlReturnData: String?
    
    private init() {
        // This initializer is intentionally left empty to make private
        // to prevent use without shared instance
    }
    
    // Mocked Methods
    func fetchAllFeatures() async -> [FeatureModel]? {
        return fetchAllFeaturesReturnData
    }
    
    func fetchAllNewsletters() async -> [NewsletterModel]? {
        return fetchAllNewslettersReturnData
    }
    
    func getNewsletterPreviewUrl(previewPath: String, completion: @escaping (String?) -> Void) {
        completion(getNewsletterPreviewUrlReturnData)
    }
}
