//
//  SwiftUIView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 11/06/2024.
//

import SwiftUI

struct QvstCampaignFormView: View {
    let campaign: QvstCampaign
    @State var questions: [QvstQuestion] = []
    
    var body: some View {
        VStack {
            Stateless(questions) { responses in
                print("Button pressed: \(responses)")
            }
        }
        .onAppear(perform: onAppear)
    }
    
    private func onAppear() {

        Task {
            var userId: String? = nil
            await {
                // Note(Loucas):
                //  Endpoint: http://yaki.uat.xpeho.fr:7830/wp-json/xpeho/v1/user
                //  Request:
                //      Header:
                //          email: {email}
                guard let url = URL(string: "http://yaki.uat.xpeho.fr:7830/wp-json/xpeho/v1/user") else {
                    print("Invalid URL")
                    return
                }
                var request = URLRequest(url: url)
                request.setValue("lcubeddu@outlook.fr", forHTTPHeaderField: "email") // Todo: This is obviously temporary, remove
                
                guard let (data, _) = try? await URLSession.shared.data(for: request) else {
                    print("Request failed")
                    return
                }
                
                guard let str = String(data: data, encoding:.utf8) else {
                    print("Failed to serialize")
                    return
                }
                userId = str
            }()
            
            await {
                // Note(Loucas):
                //  Endpoint: http://yaki.uat.xpeho.fr:7830/wp-json/xpeho/v1/qvst/campaigns/{Id}/questions
                //  Request:
                //      Header:
                //          userId: {userId}
                guard let url = URL(string: "http://yaki.uat.xpeho.fr:7830/wp-json/xpeho/v1/qvst/campaigns/\(campaign.id)/questions") else {
                    print("Invalid URL")
                    return
                }
                var request = URLRequest(url: url)
                request.setValue(userId ?? "", forHTTPHeaderField: "userId")
                
                guard let (data, _) = try? await URLSession.shared.data(for: request) else {
                    print("Request failed")
                    return
                }
                
                guard let json = try? JSONDecoder().decode([QvstQuestion].self, from: data) else {
                    print("Failed to serialize [QvstQuestion]")
                    return
                }
                self.questions = json
            }()
        }
    }
    
    struct Stateless: View {
        var questions: [QvstQuestion]
        var onSend: ([QvstFormResponse]) -> Void
        
        init(_ questions: [QvstQuestion], onSend: @escaping ([QvstFormResponse]) -> Void) {
            self.questions = questions
            self.onSend = onSend
        }
        
        @State var responses: [QvstQuestion: QvstAnswer] = [:]
        
        var body: some View {
            VStack {
                ScrollView {
                    ForEach(questions) { question in
                        Text(question.question)
                        Text("You answered : \(question.userAnswer ?? "")")
                        ForEach(question.answers) { answer in
                            Button(answer.answer) {
                                responses[question] = answer
                            }
                            .tint(.blue)
                        }
                    }
                }
                Spacer()
                Button("Send") {
                    onSend(
                        responses.map {
                            QvstFormResponse(questionId: $0.key.id, answerId: $0.value.id)
                        }
                    )
                }
                .buttonStyle(BorderedButtonStyle())
                .padding()
            }
        }
    }
}

struct QvstFormResponse: Codable, Hashable {
    let questionId: String
    let answerId: String

    enum CodingKeys: String, CodingKey {
        case questionId = "question_id"
        case answerId = "answer_id"
    }
}

struct QvstAnswer: Codable, Identifiable {
    let id: String
    let answer: String
    let value: String
}

struct QvstQuestion: Codable, Identifiable, Hashable {
    let id: String
    let question: String
    let hasAnswered: Bool
    let answers: [QvstAnswer]
    var userAnswer: String?

    enum CodingKeys: String, CodingKey {
        case id = "question_id"
        case question
        case hasAnswered
        case answers
        case userAnswer
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }

    static func == (lhs: QvstQuestion, rhs: QvstQuestion) -> Bool {
        return lhs.id == rhs.id
    }
}

#Preview {
    QvstCampaignFormView.Stateless([]) { _ in }
}
