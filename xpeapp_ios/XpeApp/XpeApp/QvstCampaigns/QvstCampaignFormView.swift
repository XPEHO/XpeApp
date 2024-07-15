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
            if !questions.isEmpty {
                Stateless(questions) { responses in
                    print("Button pressed: \(responses)")
                    // Todo dismiss doesnt work
                }
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
        
        @State var answers: [QvstAnswer] = []
        
        var body: some View {
            VStack {
                Form {
                    ForEach(0..<questions.count, id: \.self) { questionOffset in
                        let question = questions[questionOffset]
                        if !answers.isEmpty {
                            Picker(question.question, selection: $answers[questionOffset]) {
                                ForEach(0..<question.answers.count, id: \.self) { answerOffset in
                                    let answer = question.answers[answerOffset]
                                    Text(answer.answer).tag(answer)
                                }
                            }
                            .pickerStyle(.inline)
                        }
                    }
                    Section("Répondre") {
                        HStack {
                            Spacer()
                            Button("Envoyer") {
//                                onSend(
//                                    answers.map {
//                                        QvstFormResponse(questionId: $0.key.id, answerId: $0.value.id)
//                                    }
//                                )
                                print(answers)
                            }
                        }
                    }
                }
            }
            .onAppear {
                populateDefaultAnswers()
            }
        }
        
        private func populateDefaultAnswers(){
            answers = []
            for question in questions {
                guard let answer = question.answers.first else {
                    print("Error: Question of id: \(question.id) does not have Answers")
                    return
                }
                answers.append(answer)
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
    {
        let q = [XpeApp.QvstQuestion(id: "1", question: "Dans votre établissement, estimez-vous que vous avez la possibilité de développer vos compétences (par la formation, la VAE, l’échange sur les pratiques…) ?", hasAnswered: true, answers: [XpeApp.QvstAnswer(id: "6", answer: "Très souvent", value: "5"), XpeApp.QvstAnswer(id: "7", answer: "Assez souvent", value: "4"), XpeApp.QvstAnswer(id: "8", answer: "Occasionnellement", value: "3"), XpeApp.QvstAnswer(id: "9", answer: "Rarement", value: "2"), XpeApp.QvstAnswer(id: "10", answer: "Jamais", value: "1")], userAnswer: nil), XpeApp.QvstQuestion(id: "70", question: "A quel point êtes vous satisfaits de l\'organisation ?", hasAnswered: true, answers: [XpeApp.QvstAnswer(id: "16", answer: "Complétement", value: "5"), XpeApp.QvstAnswer(id: "17", answer: "Suffisamment", value: "4"), XpeApp.QvstAnswer(id: "18", answer: "Cela dépend", value: "3"), XpeApp.QvstAnswer(id: "19", answer: "Un peu", value: "2"), XpeApp.QvstAnswer(id: "20", answer: "Pas du tout", value: "1")], userAnswer: nil)]
        return QvstCampaignFormView.Stateless(q) { _ in }
    }()
}
