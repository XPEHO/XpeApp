//
//  QvstProgressModel.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation

struct QvstProgressModel: Decodable {
    let userId: String
    let campaignId: String
    let answeredQuestionsCount: String
    let totalQuestionsCount: String

    enum CodingKeys: String, CodingKey {
        case userId = "user_id"
        case campaignId = "campaign_id"
        case answeredQuestionsCount = "answered_questions"
        case totalQuestionsCount = "total_questions"
    }
}
