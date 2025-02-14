//
//  CampaignFormView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 11/06/2024.
//

// TODO: Refactor this view exporting API calls with a view model and the QvstRepository
import SwiftUI
import xpeho_ui

struct CampaignForm: View {
    var campaignFormViewModel = CampaignFormViewModel.instance
    var routerManager = RouterManager.instance
    var toastManager = ToastManager.instance
    
    // Data state
    @State var questions: [QvstQuestionModel] = []
    @State var answersSelected: [QvstAnswerModel?] = []
    @State var questionsOffset: Int = 0
    @State var openAnswer: String = ""
    
    // Prevent multiple click on send button of the form
    @State private var isSending: Bool = false
    
    var body: some View {
        VStack {
            if let campaign = routerManager.parameters["campaign"] as? QvstCampaignEntity {
                HStack {
                    Title(text: campaign.themeName)
                    Spacer()
                }
                Spacer().frame(height: 60)
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
                        } else if isLastOffset() {
                            OpenAnswerView(openAnswer: $openAnswer)
                        }
                    }
                }
                Spacer()
                HStack {
                    Assets.loadImage(named: "Arrow-left")
                        .renderingMode(.template)
                        .foregroundStyle(isFirstOffset() ? XPEHO_THEME.DISABLED_COLOR : XPEHO_THEME.CONTENT_COLOR)
                        .onTapGesture {
                            questionsOffset -= 1
                        }
                        .disabled(isFirstOffset())
                    Spacer()
                    Text("Question \(questionsOffset + 1)/\(questions.count + 1)")
                        .font(.raleway(.semiBold, size: 20))
                        .foregroundStyle(XPEHO_THEME.XPEHO_COLOR)
                    Spacer()
                    Assets.loadImage(named: isLastOffset() ? "Validated" : "Arrow-right")
                        .renderingMode(.template)
                        .scaleEffect(isLastOffset() ? 1.8 : 1)
                        .foregroundStyle(isAnsweredOffset() ? XPEHO_THEME.CONTENT_COLOR : XPEHO_THEME.DISABLED_COLOR)
                        .onTapGesture {
                            if isLastOffset() {
                                sendAnswers()
                            } else if isAnsweredOffset() {
                                questionsOffset += 1
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
        var question: QvstQuestionModel
        
        @Binding var answersSelected: [QvstAnswerModel?]
        @Binding var questionsOffset: Int
        
        var body: some View {
            VStack(spacing: 20) {
                if !question.answers.isEmpty {
                    Text(question.question)
                        .font(.raleway(.semiBold, size: 16))
                        .foregroundStyle(XPEHO_THEME.CONTENT_COLOR)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    
                    ChoiceSelector(
                        choicesAvailable: question.answers.map { $0.answer },
                        defaultSelectedChoice: answersSelected.indices.contains(questionsOffset) ? answersSelected[questionsOffset]?.answer : nil,
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
    
    struct OpenAnswerView: View {
        @Binding var openAnswer: String
        
        var body: some View {
            VStack(spacing: 20) {
                Text("Des remarques ?")
                    .font(.raleway(.semiBold, size: 16))
                    .foregroundStyle(XPEHO_THEME.CONTENT_COLOR)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                InputText(
                    label: "Remarques",
                    multiline: true,
                    onInput: { input in
                        self.openAnswer = input
                    }
                )
            }
            .padding(.vertical, 12)
        }
    }
    
    // Methods to make clear the conditions of appearance
    private func isValidOffset() -> Bool {
        return (questionsOffset >= 0) && (questionsOffset < questions.count)
    }
    
    private func isLastOffset() -> Bool {
        return (questionsOffset == questions.count)
    }
    
    private func isFirstOffset() -> Bool {
        return (questionsOffset == 0)
    }
    
    private func isAnsweredOffset() -> Bool {
        if isLastOffset() {
            return !openAnswer.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
        }
        return answersSelected.indices.contains(questionsOffset) && answersSelected[questionsOffset] != nil
    }
    
    // Init questions of the campaign
    private func initCampaignData() {
        guard let campaign = routerManager.parameters["campaign"] as! QvstCampaignEntity? else {
            debugPrint("No campaign selected")
            routerManager.goBack()
            return
        }
        // Prevent access when campaign is already completed
        if (campaign.completed) {
            debugPrint("Campaign is already completed")
            routerManager.goBack()
            return
        }
        Task {
            guard let questions = await WordpressAPI.instance.fetchCampaignQuestions(campaignId: campaign.id) else {
                debugPrint("Failed to fetch campaign questions")
                routerManager.goBack()
                return
            }
            self.questions = questions
            self.answersSelected = Array(repeating: nil, count: self.questions.count)
        }
    }
    
    private func sendAnswers() {
        guard let campaign = routerManager.parameters["campaign"] as! QvstCampaignEntity? else {
            debugPrint("No campaign selected")
            return
        }
        
        Task {
            // Get user id for the request
            guard let user = LoginManager.instance.getUser() else {
                debugPrint("No user to user in sendAnswers")
                return
            }

            // Check that all questions has been answered
            var answers: [QvstAnswerModel] = []
            for index in 0..<questions.count {
                guard let answer = answersSelected[index] else {
                    debugPrint("Not all questions have been answered")
                    return
                }
                answers.append(answer)
            }

            if !isSending {
                // Lock to prevent multi sending by spamming button
                isSending = true
                // Send answers
                guard let areAnswersSent = await WordpressAPI.instance.sendCampaignAnswers(
                    campaignId: campaign.id,
                    userId: user.id,
                    questions: questions,
                    answers: answers
                ) else {
                    debugPrint("Failed to send campaign answers")
                    isSending = false
                    return
                }

                guard let isOpenAnswerSent = await WordpressAPI.instance.submitOpenAnswers(text: openAnswer)
                else {
                    debugPrint("Failed to send open answer")
                    isSending = false
                    return
                }
                // Check the return to inform user
                if (!areAnswersSent || !isOpenAnswerSent) {
                    // Inform user
                    toastManager.setParams(
                        message: "Impossible d'envoyer vos réponses",
                        error: true
                    )
                    toastManager.play()
                    // Unlock to be able to try again
                    isSending = false
                } else {
                    // Inform user
                    toastManager.setParams(
                        message: "Réponses envoyées",
                        action: {
                            // Redirect user
                            routerManager.goTo(item: .home)
                        }
                    )
                    toastManager.play()
                }
            }
        }
    }
}
