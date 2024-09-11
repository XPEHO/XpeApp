//
//  SwiftUIView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 11/06/2024.
//

import SwiftUI
import xpeho_ui

struct QvstCampaignFormView: View {
    private let qvstService = QvstService()
    
    // Data state
    @State var questions: [QvstQuestion] = []
    @State var answersSelected: [QvstAnswer?] = []
    @State var questionsOffset: Int = 0
    
    // Prevent multiple click on send button of the form
    @State private var isSending: Bool = false
    
    // Global management
    @EnvironmentObject var dataManager: DataManager
    @EnvironmentObject var routerManager: RouterManager
    @EnvironmentObject var toastManager: ToastManager
    
    var body: some View {
        VStack {
            if let campaign = routerManager.selectedCampaign {
                Title(
                    text: campaign.theme.name,
                    isFirstPageElement: true
                )
                Spacer().frame(height: 50)
                ScrollView {
                    if questions.isEmpty {
                        ProgressView("Chargement des questions...")
                            .progressViewStyle(CircularProgressViewStyle())
                            .padding()
                    } else {
                        if isValidOffset() {
                            QuestionView(
                                question: questions[questionsOffset],
                                answersSelected: $answersSelected,
                                questionsOffset: $questionsOffset
                            )
                        }
                    }
                }
                Spacer()
                HStack {
                    Assets.loadImage(named: "Arrow-left")
                        .renderingMode(.template)
                        .foregroundStyle(isFirstOffset() ? XPEHO_THEME.DISABLED_COLOR : XPEHO_THEME.CONTENT_COLOR)
                        .onTapGesture {
                            questionsOffset-=1
                        }
                        .disabled(isFirstOffset())
                    Spacer()
                    Text("Question \(questionsOffset+1)/\(questions.count)")
                        .font(.raleway(.semiBold, size: 20))
                        .foregroundStyle(XPEHO_THEME.XPEHO_COLOR)
                    Spacer()
                    Assets.loadImage(named: isLastOffset() ? "Validated" : "Arrow-right")
                        .renderingMode(.template)
                        .foregroundStyle(isAnsweredOffset() ? XPEHO_THEME.CONTENT_COLOR : XPEHO_THEME.DISABLED_COLOR)
                        .onTapGesture {
                            if isLastOffset() {
                                sendCampaignAnswers()
                            } else if isAnsweredOffset() {
                                questionsOffset+=1
                            }
                        }
                        .disabled(!isAnsweredOffset())
                }
                .padding(.horizontal, 20)
            } else {
                Text("Aucune campagne n'a été sélectionnée")
                    .onAppear {
                        routerManager.goTo(item: .home)
                    }
            }
        }
        .onAppear(perform: initCampaignData)
    }
    
    struct QuestionView: View {
        var question: QvstQuestion
        
        @Binding var answersSelected: [QvstAnswer?]
        @Binding var questionsOffset: Int
        
        var body: some View {
            VStack (spacing: 20) {
                if !question.answers.isEmpty {
                    Text(question.question)
                        .font(.raleway(.semiBold, size: 16))
                        .foregroundStyle(XPEHO_THEME.CONTENT_COLOR)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    
                    ChoiceSelector(
                        choicesAvailable: question.answers.map { $0.answer },
                        defaultSelectedChoice: answersSelected[questionsOffset]?.answer,
                        onPress: { choice in
                            for answerAvailable in question.answers {
                                if answerAvailable.answer == choice {
                                    answersSelected[questionsOffset] = answerAvailable
                                }
                            }
                        }
                    )
                    // NOTE : This modifier is needed to force the choice selector to have differents instances through questions
                    .id(questionsOffset)
                }
            }
            .padding(.vertical, 12)
        }
    }
    
    // Methods to make clear the conditions of appearance
    private func isValidOffset() -> Bool {
        return (questionsOffset >= 0) && (questionsOffset < questions.count)
    }
    
    private func isLastOffset() -> Bool {
        return (questionsOffset+1 == questions.count)
    }
    
    private func isFirstOffset() -> Bool {
        return (questionsOffset == 0)
    }
    
    private func isAnsweredOffset() -> Bool {
        return (!answersSelected.isEmpty) && (answersSelected[questionsOffset] != nil)
    }
    
    // Init questions of the campaign
    private func initCampaignData() {
        guard let campaign = routerManager.selectedCampaign else {
            print("No campaign selected")
            return
        }
        Task {
            guard let questions = await qvstService.fetchCampaignQuestions(campaignId: campaign.id) else {
                print("Failed to fetch campaign questions")
                return
            }
            
            self.questions = questions
            self.answersSelected = Array(repeating: nil, count: self.questions.count)
        }
    }
    
    // Send answers of the campaign
    private func sendCampaignAnswers() {
        guard let campaign = routerManager.selectedCampaign else {
            print("No campaign selected")
            return
        }
        Task {
            guard let userService = dataManager.userService else {
                return
            }
            // Get user id for the request
            guard let userId = await userService.fetchUserId(email: EMAIL) else {
                print("Failed to fetch user ID")
                return
            }

            // Check that all questions has been answered
            var answers: [QvstAnswer] = []
            for index in answersSelected.indices {
                guard let answer = answersSelected[index] else {
                    print("Not all questions have been answered")
                    return
                }
                answers.append(answer)
            }
            
            if !isSending {
                // Lock to prevent multi sending by spaming button
                isSending = true
                // Send answers
                guard let areAnswersSent = await qvstService.sendCampaignAnswers(
                    campaignId: campaign.id,
                    userId: userId,
                    token: TOKEN,
                    questions: questions,
                    answers: answers
                ) else {
                    print("Failed to send campaign answers")
                    return
                }
                
                // Check the return to inform user
                if (!areAnswersSent) {
                    // Inform user
                    toastManager.message = "Impossible d'envoyer vos réponses"
                    toastManager.isError = true
                    toastManager.show()
                    // Unlock to be able to try again
                    isSending = false
                } else {
                    // Reload data
                    await dataManager.initData()
                    // Inform user
                    toastManager.message = "Réponses envoyées"
                    toastManager.action = {
                        // Redirect user
                        routerManager.goTo(item: .home)
                    }
                    toastManager.show()
                }
            }
        }
    }
}
