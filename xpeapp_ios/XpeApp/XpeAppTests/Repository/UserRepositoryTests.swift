//
//  UserRepositoryTests.swift
//  XpeAppTests
//
//  Created by Ryan Debouvries on 19/09/2024.
//

import XCTest
import FirebaseAuth
@testable import XpeApp

final class UserRepositoryTests: XCTestCase {
    // We take the mocked repositories and sources
    let userRepo = UserRepositoryImpl.mock
    let userSource = MockWordpressAPI.instance
    
    override func setUp() {
        super.setUp()
        userRepo.user = nil
    }
    
    override func tearDownWithError() throws {
        super.tearDown()
        userRepo.user = nil
    }
    
    // ------------------- login TESTS -------------------
    
    func test_login_generateTokenError() throws {
        Task {
            // GIVEN
            userSource.generateTokenReturnData = nil
            userSource.fetchUserIdReturnData = "user_id"
            
            // WHEN
            await userRepo.login(
                username: "username",
                password: "password"
            ) { completion in
                
                // THEN
                XCTAssertEqual(completion, LoginResult.error)
                XCTAssertNil(self.userRepo.user)
                
            }
        }
    }
    
    func test_login_generateTokenUnhandledTokenResponse() throws {
        Task {
            // GIVEN
            userSource.generateTokenReturnData = TokenResponseModel (
                success: nil,
                error: nil
            )
            userSource.fetchUserIdReturnData = "user_id"
            
            // WHEN
            await userRepo.login(
                username: "username",
                password: "password"
            ) { completion in
                
                // THEN
                XCTAssertEqual(completion, LoginResult.error)
                XCTAssertNil(self.userRepo.user)
                
            }
        }
    }
    
    func test_login_generateTokenIncorrectPassword() throws {
        Task {
            // GIVEN
            userSource.generateTokenReturnData = TokenResponseModel (
                success: nil,
                error: ErrorTokenResponseModel(
                    code: "[jwt_auth] incorrect_password",
                    message: "<strong>Error:</strong> Incorrect password",
                    data: ErrorTokenResponseModel.ErrorData(
                        status: 403
                    )
                )
            )
            userSource.fetchUserIdReturnData = "user_id"
            
            // WHEN
            await userRepo.login(
                username: "username",
                password: "password"
            ) { completion in
                
                // THEN
                XCTAssertEqual(completion, LoginResult.failure)
                XCTAssertNil(self.userRepo.user)
                
            }
        }
    }
    
    func test_login_fetchUserIdError() throws {
        Task {
            // GIVEN
            userSource.generateTokenReturnData = TokenResponseModel (
                success: SuccessTokenResponseModel(
                    token: "token",
                    userEmail: "user_email",
                    userNiceName: "user_nice_name",
                    userDisplayName: "user_display_name"
                ),
                error: nil
            )
            userSource.fetchUserIdReturnData = nil
            
            // WHEN
            await userRepo.login(
                username: "username",
                password: "password"
            ) { completion in
                
                // THEN
                XCTAssertEqual(completion, LoginResult.error)
                XCTAssertNil(self.userRepo.user)
                
            }
        }
    }
    
    func test_login_Success() throws {
        Task {
            // GIVEN
            userSource.generateTokenReturnData = TokenResponseModel (
                success: SuccessTokenResponseModel(
                    token: "token",
                    userEmail: "user_email",
                    userNiceName: "user_nice_name",
                    userDisplayName: "user_display_name"
                ),
                error: nil
            )
            userSource.fetchUserIdReturnData = "user_id"
            
            // WHEN
            await userRepo.login(
                username: "username",
                password: "password"
            ) { completion in
                
                // THEN
                XCTAssertEqual(completion, LoginResult.success)
            }
            
            let dataExpected = UserEntity(
                token: "Bearer token",
                id: "user_id"
            )
            XCTAssertNotNil(userRepo.user)
            XCTAssertEqual(userRepo.user, dataExpected)
        }
    }
    
    
    // ------------------- logout TESTS -------------------
    
    func test_logout_clearsUser() throws {
        // GIVEN
        userRepo.user = UserEntity(token: "Bearer token", id: "user_id")
        
        // WHEN
        userRepo.logout()
        
        // THEN
        XCTAssertNil(userRepo.user)
    }
    
    func test_logout_removesUserFromCache() throws {
        // GIVEN
        KeychainManager.instance.saveValue("user_id", forKey: "user_id")
        KeychainManager.instance.saveValue("user_token", forKey: "user_token")
        
        // WHEN
        userRepo.logout()
        
        // THEN
        XCTAssertNil(KeychainManager.instance.getValue(forKey: "user_id"))
        XCTAssertNil(KeychainManager.instance.getValue(forKey: "user_token"))
    }
    
    func test_logout_disconnectsFromFirebase() throws {
        Task {
            // GIVEN
            let auth = Auth.auth()
            do {
                try await auth.signInAnonymously()
            } catch {
                XCTFail("Failed to sign in anonymously")
            }
            XCTAssertNotNil(auth.currentUser)
            
            // WHEN
            userRepo.logout()
            
            // THEN
            XCTAssertNil(auth.currentUser)
        }
    }
    
//    
//    
//    func test_updatePassword_success() async throws {
//        // GIVEN
//        userRepo.updatePasswordReturnData = .success
//        
//        // WHEN
//        let result = await userRepo.updatePassword(
//            initialPassword: "oldPassword",
//            newPassword: "newPassword",
//            passwordRepeat: "newPassword"
//        )
//        
//        // THEN
//        XCTAssertEqual(result, .success)
//    }
//    
//    func test_updatePassword_invalidInitialPassword() async throws {
//        // GIVEN
//        mockDataSource.updatePasswordReturnData = .invalidInitialPassword
//        
//        // WHEN
//        let result = await userRepo.updatePassword(
//            initialPassword: "wrongPassword",
//            newPassword: "newPassword",
//            passwordRepeat: "newPassword"
//        )
//        
//        // THEN
//        XCTAssertEqual(result, .invalidInitialPassword)
//    }
//    
//    func test_updatePassword_newPasswordsNotMatch() async throws {
//        // GIVEN
//        mockDataSource.updatePasswordReturnData = .newPasswordsNotMatch
//        
//        // WHEN
//        let result = await userRepo.updatePassword(
//            initialPassword: "oldPassword",
//            newPassword: "newPassword",
//            passwordRepeat: "differentNewPassword"
//        )
//        
//        // THEN
//        XCTAssertEqual(result, .newPasswordsNotMatch)
//    }
}
//
