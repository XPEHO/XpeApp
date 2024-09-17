//
//  QvstQuestionModel.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation

struct QvstQuestionModel: Codable {
    let id: String
    let question: String
    let answers: [QvstAnswerModel]
    
    enum CodingKeys: String, CodingKey {
        case id = "question_id"
        case question
        case answers
    }
}
