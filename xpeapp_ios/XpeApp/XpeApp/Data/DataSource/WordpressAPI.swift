//
//  WordpressAPI.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 11/09/2024.
//

import Foundation

// Protocol to be able to mock the data source
protocol WordpressAPIProtocol {
    func fetchUserId(email: String) async -> String?
    func generateToken(userCandidate: UserCandidateModel) async -> TokenResponseModel?
    func checkTokenValidity(token: String) async -> TokenValidityModel?
    func fetchAllCampaigns() async -> [QvstCampaignModel]?
    func fetchActiveCampaigns() async -> [QvstCampaignModel]?
    func fetchCampaignQuestions(campaignId: String) async -> [QvstQuestionModel]?
    func sendCampaignAnswers(
        campaignId: String,
        userId: String,
        questions: [QvstQuestionModel],
        answers: [QvstAnswerModel]
    ) async -> Bool?
    func fetchCampaignsProgress(userId: String) async -> [QvstProgressModel]?
    func fetchUserInfos() async -> UserInfosModel?
    func updatePassword(userPasswordCandidate: UserPasswordEditModel) async -> UserPasswordEditReturnEnum?
}

class WordpressAPI: WordpressAPIProtocol {
    static let instance = WordpressAPI()
    let toastManager = ToastManager.instance
    
    private init() {
        // This initializer is intentionally left empty to make private
        // to prevent use without shared instance
    }
    
    // Fetch userId by email
    func fetchUserId(
        email: String
    ) async -> String? {
        
        if let (data, _) = await fetchWordpressAPI (
            endpoint: "xpeho/v1/user",
            headers: [
                "email": email
            ]
        ){
            return String(data: data, encoding: .utf8)
        } else {
            return nil
        }
        
    }
    
    // Fetch user Infos by the token
    func fetchUserInfos() async -> UserInfosModel? {
        if let (data, statusCode) = await fetchWordpressAPI (
            endpoint: "xpeho/v1/user-infos"
        ){
            if statusCode == 403 {
                debugPrint("Unauthorized access in fetchUserInfos")
                return nil
            }
            
            do {
                return try JSONDecoder().decode(UserInfosModel.self, from: data)
            } catch {
                debugPrint("Failed to decode data in fetchUserInfos : \(error)")
                let dataString = String(data: data, encoding: .utf8) ?? ""
                debugPrint("Data got : \(dataString)")
                return nil
            }
        } else {
            return nil
        }
        
    }
    
    // Token generation
    func generateToken(
        userCandidate: UserCandidateModel
    ) async -> TokenResponseModel? {
        
        if let (data, _) = await fetchWordpressAPI <UserCandidateModel> (
            endpoint: "jwt-auth/v1/token",
            method: .post,
            headers: [:],
            bodyObject: userCandidate
        ){
            do {
                let successResponse = try JSONDecoder().decode(SuccessTokenResponseModel.self, from: data)
                return TokenResponseModel(
                    success: successResponse,
                    error: nil
                )
            } catch {
                do {
                    let errorResponse = try JSONDecoder().decode(ErrorTokenResponseModel.self, from: data)
                    return TokenResponseModel(
                        success: nil,
                        error: errorResponse
                    )
                } catch {
                    debugPrint("Failed to decode data in generateToken : \(error)")
                    let dataString = String(data: data, encoding: .utf8) ?? ""
                    debugPrint("Data got : \(dataString)")
                    return nil
                }
            }
        } else {
            return nil
        }
        
    }
    
    // Token check
    func checkTokenValidity(token: String) async -> TokenValidityModel? {
        if let (data, _) = await fetchWordpressAPI (
            endpoint: "jwt-auth/v1/token/validate",
            method: .post,
            headers: [
                "authorization": token
            ]
        ){
            do {
                return try JSONDecoder().decode(TokenValidityModel.self, from: data)
            } catch {
                debugPrint("Failed to decode data in checkTokenValidity : \(error)")
                let dataString = String(data: data, encoding: .utf8) ?? ""
                debugPrint("Data got : \(dataString)")
                return nil
            }
        } else {
            return nil
        }
        
    }
    
    // Fetch all campaigns
    func fetchAllCampaigns() async -> [QvstCampaignModel]? {
        if let (data, statusCode) = await fetchWordpressAPI (
            endpoint: "xpeho/v1/qvst/campaigns"
        ){
            if statusCode == 403 {
                debugPrint("Unauthorized access in fetchAllCampaigns")
                return []
            }
            
            do {
                return try JSONDecoder().decode([QvstCampaignModel].self, from: data)
            } catch {
                debugPrint("Failed to decode data in fetchAllCampaigns : \(error)")
                let dataString = String(data: data, encoding: .utf8) ?? ""
                debugPrint("Data got : \(dataString)")
                return nil
            }
        } else {
            return nil
        }
        
    }
    
    // Fetch active campaigns
    func fetchActiveCampaigns() async -> [QvstCampaignModel]? {
        if let (data, statusCode) = await fetchWordpressAPI (
            endpoint: "xpeho/v1/qvst/campaigns:active"
        ){
            if statusCode == 403 {
                debugPrint("Unauthorized access in fetchActiveCampaigns")
                return []
            }
            
            do {
                return try JSONDecoder().decode([QvstCampaignModel].self, from: data)
            } catch {
                debugPrint("Failed to decode data in fetchActiveCampaigns : \(error)")
                let dataString = String(data: data, encoding: .utf8) ?? ""
                debugPrint("Data got : \(dataString)")
                return nil
            }
        } else {
            return nil
        }
        
    }
    
    // Fetch questions by campaignId
    func fetchCampaignQuestions(
        campaignId: String
    ) async -> [QvstQuestionModel]? {
        if let (data, statusCode) = await fetchWordpressAPI (
            endpoint: "xpeho/v1/qvst/campaigns/\(campaignId):questions"
        ){
            if statusCode == 403 {
                debugPrint("Unauthorized access in fetchCampaignQuestions")
                return []
            }
            
            do {
                return try JSONDecoder().decode([QvstQuestionModel].self, from: data)
            } catch {
                debugPrint("Failed to decode data in fetchCampaignQuestions : \(error)")
                let dataString = String(data: data, encoding: .utf8) ?? ""
                debugPrint("Data got : \(dataString)")
                return nil
            }
        } else {
            return nil
        }
        
    }
    
    // Send campaign answers
    func sendCampaignAnswers(
        campaignId: String,
        userId: String,
        questions: [QvstQuestionModel],
        answers: [QvstAnswerModel]
    ) async -> Bool? {
        var formResponses: [QvstFormResponseModel] = []
        for index in questions.indices {
            formResponses.append(
                QvstFormResponseModel(
                    questionId: questions[index].id,
                    answerId: answers[index].id
                )
            )
        }
        
        if let (data, _) = await fetchWordpressAPI <[QvstFormResponseModel]> (
            endpoint: "xpeho/v1/qvst/campaigns/\(campaignId)/questions:answer",
            method: .post,
            headers: [
                "userId": userId,
            ],
            bodyObject: formResponses
        ){
            if let dataString = String(data: data, encoding: .utf8) {
                return dataString.contains("true")
            } else {
                return nil
            }
        } else {
            return nil
        }
        
    }
    
    
    // Fetch progress for each campaign by userId
    func fetchCampaignsProgress(
        userId: String
    ) async -> [QvstProgressModel]? {
        if let (data, statusCode) = await fetchWordpressAPI (
            endpoint: "xpeho/v1/campaign-progress?userId=\(userId)"
        ){
            if statusCode == 403 {
                debugPrint("Unauthorized access in fetchCampaignsProgress")
                return []
            }
            
            do {
                return try JSONDecoder().decode([QvstProgressModel].self, from: data)
            } catch {
                debugPrint("Failed to decode data in fetchCampaignsProgress : \(error)")
                let dataString = String(data: data, encoding: .utf8) ?? ""
                debugPrint("Data got : \(dataString)")
                return nil
            }
        } else {
            return nil
        }
    }
    
    // Update password for users with token
    func updatePassword(
        userPasswordCandidate: UserPasswordEditModel
    ) async -> UserPasswordEditReturnEnum? {
        
        if let (data, statusCode) = await fetchWordpressAPI <UserPasswordCandidateModel> (
            endpoint: "xpeho/v1/update-password",
            method: .post,
            headers: [:],
            bodyObject: userPasswordCandidate
        ){
            if statusCode == 204 {
                debugPrint("Mot de passe modifié avec succès")
                return .success
            }
            
            do {
                let updatePasswordResponse = try JSONDecoder().decode(UserPasswordEditResponseModel.self, from: data)
                if updatePasswordResponse.code == "incorrect_password" {
                    debugPrint("Mot de passe initial incorrect")
                    return .invalidInitialPassword
                } else if updatePasswordResponse.code == "password_mismatch"{
                    debugPrint("Les mots de passe ne correspondent pas")
                    return .newPasswordsNotMatch
                }
            } catch {
                debugPrint("Échec du décodage des données: \(error.localizedDescription)")
                return nil
            }
        } else {
            debugPrint("Erreur de connexion au serveur")
            return nil
        }
        return nil
    }
    
    
    // send the open answer of a campaign QVST
    func submitOpenAnswers(
        text: String
    ) async -> Bool? {
        if let (_,statusCode) = await fetchWordpressAPI (
            endpoint: "xpeho/v1/qvst/open-answers?text=\(text)",
            method: .post,
            headers: [:]
        )   {
            return statusCode == 201
        }   else {
            return nil
            }
    }

}
