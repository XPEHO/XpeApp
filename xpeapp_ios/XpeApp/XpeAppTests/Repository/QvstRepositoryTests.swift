//
//  QvstRepositoryTests.swift
//  XpeAppTests
//
//  Created by Ryan Debouvries on 21/08/2024.
//

import XCTest
@testable import XpeApp

final class QvstRepositoryTests: XCTestCase {
    // We take the mocked repositories and sources
    let qvstRepo = QvstRepositoryImpl.mock
    let qvstSource = MockWordpressAPI.instance
    let userRepo = UserRepositoryImpl.mock

    override func setUp() {
        super.setUp()
    }

    override func tearDownWithError() throws {
        super.tearDown()
    }
    
    // ------------------- getCampaigns TESTS -------------------

    func test_getCampaigns_fetchAllCampaignsError() throws {
        Task {
            // GIVEN
            qvstSource.fetchAllCampaignsReturnData = nil
            userRepo.user = UserEntity(
                token: "token",
                id: "user_id"
            )
            qvstSource.fetchCampaignsProgressReturnData = [
                QvstProgressModel(
                    userId: "user_id",
                    campaignId: "campaign_id",
                    answeredQuestionsCount: "0",
                    totalQuestionsCount: "0"
                )
            ]
            
            // WHEN
            let campaigns = await qvstRepo.getCampaigns()
            
            // THEN
            XCTAssertNil(campaigns)
        }
    }
    
    func test_getCampaigns_NoUserError() throws {
        Task {
            let currentDate = Date()
            let currentDatePlusOneDay = Calendar.current.date(byAdding: .day, value: 1, to: currentDate)!
            
            // GIVEN
            qvstSource.fetchAllCampaignsReturnData = [
                QvstCampaignModel(
                    id: "campaign_id",
                    name: "Qvst Campaign",
                    theme: QvstThemeModel(id: "theme_id", name: "Qvst Theme"),
                    status: "OPEN",
                    startDate: currentDate,
                    endDate: currentDatePlusOneDay,
                    action: "action",
                    participationRate: "rate"
                )
            ]
            userRepo.user = nil
            qvstSource.fetchCampaignsProgressReturnData = [
                QvstProgressModel(
                    userId: "user_id",
                    campaignId: "campaign_id",
                    answeredQuestionsCount: "0",
                    totalQuestionsCount: "0"
                )
            ]
            
            // WHEN
            let campaigns = await qvstRepo.getCampaigns()
            
            // THEN
            XCTAssertNil(campaigns)
        }
    }
    
    func test_getCampaigns_fetchCampaignsProgressError() throws {
        Task {
            let currentDate = Date()
            let currentDatePlusOneDay = Calendar.current.date(byAdding: .day, value: 1, to: currentDate)!
            
            // GIVEN
            qvstSource.fetchAllCampaignsReturnData = [
                QvstCampaignModel(
                    id: "campaign_id",
                    name: "Qvst Campaign",
                    theme: QvstThemeModel(id: "theme_id", name: "Qvst Theme"),
                    status: "OPEN",
                    startDate: currentDate,
                    endDate: currentDatePlusOneDay,
                    action: "action",
                    participationRate: "rate"
                )
            ]
            userRepo.user = UserEntity(
                token: "token",
                id: "user_id"
            )
            qvstSource.fetchCampaignsProgressReturnData = nil
            
            // WHEN
            let campaigns = await qvstRepo.getCampaigns()
            
            // THEN
            XCTAssertNil(campaigns)
        }
    }
    
    func test_getCampaigns_Success() throws {
        Task {
            let currentDate = Date()
            let currentDatePlusOneDay = Calendar.current.date(byAdding: .day, value: 1, to: currentDate)!
            
            // GIVEN
            qvstSource.fetchAllCampaignsReturnData = [
                QvstCampaignModel(
                    id: "campaign_id",
                    name: "Qvst Campaign",
                    theme: QvstThemeModel(id: "theme_id", name: "Qvst Theme"),
                    status: "OPEN",
                    startDate: currentDate,
                    endDate: currentDatePlusOneDay,
                    action: "action",
                    participationRate: "rate"
                )
            ]
            userRepo.user = UserEntity(
                token: "token",
                id: "user_id"
            )
            qvstSource.fetchCampaignsProgressReturnData = [
                QvstProgressModel(
                    userId: "user_id",
                    campaignId: "campaign_id",
                    answeredQuestionsCount: "2",
                    totalQuestionsCount: "2"
                ),
                QvstProgressModel(
                    userId: "user_id",
                    campaignId: "campaign_id_2",
                    answeredQuestionsCount: "2",
                    totalQuestionsCount: "4"
                )
            ]
            
            // WHEN
            let campaigns = await qvstRepo.getCampaigns()
            
            // THEN
            let dataExpected = [
                QvstCampaignEntity(
                    id: "campaign_id",
                    name: "Qvst Campaign",
                    themeName: "Qvst Theme",
                    status: "OPEN",
                    outdated: false,
                    completed: true,
                    remainingDays: 1,
                    endDate: currentDatePlusOneDay,
                    resultLink: "action"
                )
            ]
            XCTAssertNotNil(campaigns)
            XCTAssertEqual(campaigns!.count, 1)
            XCTAssertEqual(campaigns, dataExpected)
        }
    }

    
    // ------------------- getActiveCampaigns TESTS -------------------

    func test_getActiveCampaigns_fetchActiveCampaignsError() throws {
        Task {
            // GIVEN
            qvstSource.fetchActiveCampaignsReturnData = nil
            userRepo.user = UserEntity(
                token: "token",
                id: "user_id"
            )
            qvstSource.fetchCampaignsProgressReturnData = [
                QvstProgressModel(
                    userId: "user_id",
                    campaignId: "campaign_id",
                    answeredQuestionsCount: "0",
                    totalQuestionsCount: "0"
                )
            ]
            
            // WHEN
            let activeCampaigns = await qvstRepo.getActiveCampaigns()
            
            // THEN
            XCTAssertNil(activeCampaigns)
        }
    }
    
    func test_getActiveCampaigns_NoUserError() throws {
        Task {
            let currentDate = Date()
            let currentDatePlusOneDay = Calendar.current.date(byAdding: .day, value: 1, to: currentDate)!
            
            // GIVEN
            qvstSource.fetchActiveCampaignsReturnData = [
                QvstCampaignModel(
                    id: "campaign_id",
                    name: "Qvst Campaign",
                    theme: QvstThemeModel(id: "theme_id", name: "Qvst Theme"),
                    status: "OPEN",
                    startDate: currentDate,
                    endDate: currentDatePlusOneDay,
                    action: "action",
                    participationRate: "rate"
                )
            ]
            userRepo.user = nil
            qvstSource.fetchCampaignsProgressReturnData = [
                QvstProgressModel(
                    userId: "user_id",
                    campaignId: "campaign_id",
                    answeredQuestionsCount: "0",
                    totalQuestionsCount: "0"
                )
            ]
            
            // WHEN
            let activeCampaigns = await qvstRepo.getActiveCampaigns()
            
            // THEN
            XCTAssertNil(activeCampaigns)
        }
    }

    func test_getActiveCampaigns_fetchCampaignsProgressError() throws {
        Task {
            let currentDate = Date()
            let currentDatePlusOneDay = Calendar.current.date(byAdding: .day, value: 1, to: currentDate)!
            
            // GIVEN
            qvstSource.fetchActiveCampaignsReturnData = [
                QvstCampaignModel(
                    id: "campaign_id",
                    name: "Qvst Campaign",
                    theme: QvstThemeModel(id: "theme_id", name: "Qvst Theme"),
                    status: "OPEN",
                    startDate: currentDate,
                    endDate: currentDatePlusOneDay,
                    action: "action",
                    participationRate: "rate"
                )
            ]
            userRepo.user = UserEntity(
                token: "token",
                id: "user_id"
            )
            qvstSource.fetchCampaignsProgressReturnData = nil
            
            // WHEN
            let activeCampaigns = await qvstRepo.getActiveCampaigns()
            
            // THEN
            XCTAssertNil(activeCampaigns)
        }
    }

    func test_getActiveCampaigns_Success() throws {
        Task {
            let currentDate = Date()
            let currentDatePlusOneDay = Calendar.current.date(byAdding: .day, value: 1, to: currentDate)!
            
            // GIVEN
            qvstSource.fetchActiveCampaignsReturnData = [
                QvstCampaignModel(
                    id: "campaign_id",
                    name: "Qvst Campaign",
                    theme: QvstThemeModel(id: "theme_id", name: "Qvst Theme"),
                    status: "OPEN",
                    startDate: currentDate,
                    endDate: currentDatePlusOneDay,
                    action: "action",
                    participationRate: "rate"
                )
            ]
            userRepo.user = UserEntity(
                token: "token",
                id: "user_id"
            )
            qvstSource.fetchCampaignsProgressReturnData = [
                QvstProgressModel(
                    userId: "user_id",
                    campaignId: "campaign_id",
                    answeredQuestionsCount: "2",
                    totalQuestionsCount: "2"
                ),
                QvstProgressModel(
                    userId: "user_id",
                    campaignId: "campaign_id_2",
                    answeredQuestionsCount: "2",
                    totalQuestionsCount: "4"
                )
            ]
            
            // WHEN
            let activeCampaigns = await qvstRepo.getActiveCampaigns()
            
            // THEN
            let dataExpected = [
                QvstCampaignEntity(
                    id: "campaign_id",
                    name: "Qvst Campaign",
                    themeName: "Qvst Theme",
                    status: "OPEN",
                    outdated: false,
                    completed: true,
                    remainingDays: 1,
                    endDate: currentDatePlusOneDay,
                    resultLink: "action"
                )
            ]
            XCTAssertNotNil(activeCampaigns)
            XCTAssertEqual(activeCampaigns!.count, 1)
            XCTAssertEqual(activeCampaigns, dataExpected)
        }
    }

    
    // ------------------- classifyCampaigns TESTS -------------------

    func test_classifyCampaigns() throws {
        // GIVEN
        let currentDate = Date()
        let currentDatePlusOneYear = Calendar.current.date(byAdding: .year, value: 1, to: currentDate)!
        let campaigns = [
            QvstCampaignEntity(
                id: "campaign_id_1",
                name: "Qvst Campaign 1",
                themeName: "Qvst Theme",
                status: "OPEN",
                outdated: false,
                completed: true,
                remainingDays: 1,
                endDate: currentDatePlusOneYear,
                resultLink: "action 1"
            ),
            QvstCampaignEntity(
                id: "campaign_id_2",
                name: "Qvst Campaign 2",
                themeName: "Qvst Theme",
                status: "CLOSED",
                outdated: true,
                completed: true,
                remainingDays: 0,
                endDate: currentDate,
                resultLink: "action 2"
            )
        ]
        
        // WHEN
        let classifiedCampaigns = qvstRepo.classifyCampaigns(campaigns: campaigns)
        
        // THEN
        let dataExpected = [
            Calendar.current.component(.year, from: currentDatePlusOneYear): [
                QvstCampaignEntity(
                    id: "campaign_id_1",
                    name: "Qvst Campaign 1",
                    themeName: "Qvst Theme",
                    status: "OPEN",
                    outdated: false,
                    completed: true,
                    remainingDays: 1,
                    endDate: currentDatePlusOneYear,
                    resultLink: "action 1"
                )
            ],
            Calendar.current.component(.year, from: currentDate): [
                QvstCampaignEntity(
                    id: "campaign_id_2",
                    name: "Qvst Campaign 2",
                    themeName: "Qvst Theme",
                    status: "CLOSED",
                    outdated: true,
                    completed: true,
                    remainingDays: 0,
                    endDate: currentDate,
                    resultLink: "action 2"
                )
            ]
        ]
        XCTAssertNotNil(classifiedCampaigns)
        XCTAssertEqual(classifiedCampaigns[Calendar.current.component(.year, from: currentDatePlusOneYear)]?.count, 1)
        XCTAssertEqual(classifiedCampaigns[Calendar.current.component(.year, from: currentDate)]?.count, 1)
        XCTAssertEqual(classifiedCampaigns, dataExpected)
    }
}

