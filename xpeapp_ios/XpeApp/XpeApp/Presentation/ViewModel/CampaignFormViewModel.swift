//
//  CampaignFormViewModel.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 16/09/2024.
//

import Foundation

@Observable class CampaignFormViewModel {
    
    static let instance = CampaignFormViewModel()
    
    private init() {
        // To fill with the datas needed for CampaignFormPage
    }

    var campaign: QvstCampaignEntity? = nil
    
    func setCampaign(with campaign: QvstCampaignEntity) {
        self.campaign = campaign
    }
}
