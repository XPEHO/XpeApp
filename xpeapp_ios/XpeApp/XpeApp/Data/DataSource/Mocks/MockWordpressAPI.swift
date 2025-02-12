//
//  MockWordpressAPI.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 18/09/2024.
//

import Foundation

class MockWordpressAPI: WordpressAPIProtocol {
    static let instance = MockWordpressAPI()
    
    // Mocked Returns
    var fetchUserIdReturnData: String?
    var generateTokenReturnData: TokenResponseModel?
    var checkTokenValidityReturnData: TokenValidityModel?
    var fetchAllCampaignsReturnData: [QvstCampaignModel]?
    var fetchActiveCampaignsReturnData: [QvstCampaignModel]?
    var fetchCampaignQuestionsReturnData: [QvstQuestionModel]?
    var sendCampaignAnswersReturnData: Bool?
    var fetchCampaignsProgressReturnData: [QvstProgressModel]?
    var fetchUserInfosReturnData: UserInfosModel?
    var updatePasswordData: UserPasswordEditReturnEnum?
    var submitOpenAnswersReturnData: Bool?
    
    private init() {
        // This initializer is intentionally left empty to make private
        // to prevent use without shared instance
    }
    
    // Mocked Methods
    func fetchUserId(email: String) async -> String? {
        return fetchUserIdReturnData
    }
    
    func generateToken(userCandidate: UserCandidateModel) async -> TokenResponseModel? {
        return generateTokenReturnData
    }
    
    func checkTokenValidity(token: String) async -> TokenValidityModel? {
        return checkTokenValidityReturnData
    }
    
    func fetchAllCampaigns() async -> [QvstCampaignModel]? {
        return fetchAllCampaignsReturnData
    }
    
    func fetchActiveCampaigns() async -> [QvstCampaignModel]? {
        return fetchActiveCampaignsReturnData
    }
    
    func fetchCampaignQuestions(campaignId: String) async -> [QvstQuestionModel]? {
        return fetchCampaignQuestionsReturnData
    }
    
    func sendCampaignAnswers(
        campaignId: String,
        userId: String,
        questions: [QvstQuestionModel],
        answers: [QvstAnswerModel]
    ) async -> Bool? {
        return sendCampaignAnswersReturnData
    }
    
    func fetchCampaignsProgress(userId: String) async -> [QvstProgressModel]? {
        return fetchCampaignsProgressReturnData
    }

    func fetchUserInfos() async -> UserInfosModel? {
        return fetchUserInfosReturnData
    }

    func updatePassword(userPasswordCandidate: UserPasswordEditModel) async -> UserPasswordEditReturnEnum? {
        return updatePasswordData
    }
    
    func submitOpenAnswers(text: String) async -> Bool? {
        return submitOpenAnswersReturnData
    }
}
