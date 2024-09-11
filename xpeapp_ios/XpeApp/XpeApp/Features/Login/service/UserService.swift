//
//  UserService.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 06/09/2024.
//

import Foundation

class UserService {
    private let session: URLSessionProtocol
    
    init(session: URLSessionProtocol = URLSession.shared) {
        self.session = session
    }
    
    // Fetch userId by email
    func fetchUserId(email: String) async -> String? {
        // Check the URL is valid
        guard let url = URL(string: BACKEND_URL + "xpeho/v1/user") else {
            print("Invalid URL")
            return nil
        }

        // Create a request with the email in the header of the url
        var request = URLRequest(url: url)
        request.setValue(email, forHTTPHeaderField: "email")
        
        // Try to fetch the data
        do {
            let (data, _) = try await URLSession.shared.data(for: request)
            return String(data: data, encoding: .utf8)
        } catch {
            print("Request failed: \(error)")
            return nil
        }
    }
    
    // Token generation
    func generateToken(username: String, password: String) async throws -> TokenResponse? {
        let endpointUrl = BACKEND_URL + "jwt-auth/v1/token"
        guard let url = URL(string: endpointUrl) else {
            print("Invalid url (generateToken): \(endpointUrl)")
            return nil
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        var userCandidate = UserCandidate(
            username: username,
            password: password
        )
        
        do {
            let jsonData = try JSONEncoder().encode(userCandidate)
            request.httpBody = jsonData
            
            let (data, _) = try await session.data(for: request)
            return try JSONDecoder().decode(TokenResponse.self, from: data)
        } catch {
            print("Request failed (generateToken): \(error)")
            return nil
        }
    }
    
    // Token check
    func isTokenValid(token: String) async throws -> Bool? {
        let endpointUrl = BACKEND_URL + "jwt-auth/v1/token/validate"
        guard let url = URL(string: endpointUrl) else {
            print("Invalid url (isTokenValid): \(endpointUrl)")
            return nil
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue(token, forHTTPHeaderField: "authorization")
        
        do {
            let (data, _) = try await session.data(for: request)
            let tokenValidity = try JSONDecoder().decode(TokenValidity.self, from: data)
            return tokenValidity.code == "jwt_auth_valid_token"
        } catch {
            print("Request failed (generateToken): \(error)")
            return nil
        }
    }
    
}
