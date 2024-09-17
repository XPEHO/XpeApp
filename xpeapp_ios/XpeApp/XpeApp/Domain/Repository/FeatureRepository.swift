//
//  FeatureRepository.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/09/2024.
//

import Foundation

protocol FeatureRepository {
    func getFeatures() async -> [String: FeatureEntity]?
}
