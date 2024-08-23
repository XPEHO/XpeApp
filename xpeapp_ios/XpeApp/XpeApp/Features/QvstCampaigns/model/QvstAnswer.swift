//
//  QvstAnswer.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation

struct QvstAnswer: Codable, Identifiable, Hashable {
    let id: String
    let answer: String
    let value: String
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }

    static func == (lhs: QvstAnswer, rhs: QvstAnswer) -> Bool {
        return lhs.id == rhs.id
    }
}
