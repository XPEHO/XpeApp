//
//  QvstRepository.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/09/2024.
//

import Foundation

protocol QvstRepository {
    func classifyCampaigns(campaigns: [QvstCampaignEntity]) -> [String: [QvstCampaignEntity]]
    func getCampaigns() async -> [QvstCampaignEntity]?
    func getActiveCampaigns() async -> [QvstCampaignEntity]?
}
