//
//  QvstQuestion.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation

struct QvstQuestion: Codable, Identifiable, Hashable {
    let id: String
    let question: String
    let answers: [QvstAnswer]
    
    enum CodingKeys: String, CodingKey {
        case id = "question_id"
        case question
        case answers
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
    
    static func == (lhs: QvstQuestion, rhs: QvstQuestion) -> Bool {
        return lhs.id == rhs.id
    }
}
