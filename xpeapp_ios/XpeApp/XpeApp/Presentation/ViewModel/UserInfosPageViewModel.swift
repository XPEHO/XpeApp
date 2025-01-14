//
//  UserInfosPageViewModel.swift
//  XpeApp
//
//  Created by Théo Lebègue on 08/01/2025.
//

import Foundation

@Observable class UserInfosPageViewModel {
    
    static let instance = UserInfosPageViewModel()
    
    // Make private constructor to prevent use without shared instance
    private init() {
        initUserInfos()
    }

    var userInfos: UserInfosEntity? = nil
    
    func update() {
        initUserInfos()
    }
    
    private func initUserInfos() {
        Task {
            if let obtainedUserInfos = await UserRepositoryImpl.instance.fetchUserInfos() {
                DispatchQueue.main.async {
                    debugPrint(obtainedUserInfos)
                    self.userInfos = obtainedUserInfos
                }
            }
        }
    }
}
