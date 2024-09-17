//
//  WordpressAPIHttpClient.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 12/09/2024.
//

import Foundation

let backendUrl = Configuration.backendUrl

enum HttpMethod: String{
    case get = "GET"
    case post = "POST"
}

func fetchWordpressAPI <BodyType: Codable> (
    endpoint: String = "",
    method: HttpMethod = .get,
    headers: [String: String] = [:],
    bodyObject: BodyType
) async -> Data? {
    
    let endpointUrl = backendUrl + endpoint
    guard let url = URL(string: endpointUrl) else {
        debugPrint("Invalid URL : \(endpointUrl)")
        return nil
    }
    
    // Setup request
    var request = URLRequest(url: url)
    request.httpMethod = method.rawValue
    
    // Set headers if needed
    for key in headers.keys {
        request.setValue(headers[key], forHTTPHeaderField: key)
    }
    
    // Set body if needed
    if !headers.keys.contains("Content-Type") {
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
    }
    
    do {
        // Encode body
        let body = try JSONEncoder().encode(bodyObject)
        request.httpBody = body
        
        // Decode data got
        let (data, _) = try await URLSession.shared.data(for: request)
        return data
    } catch {
        debugPrint("Request failed on \(endpointUrl) : \(error)")
        return nil
    }
    
}

func fetchWordpressAPI (
    endpoint: String = "",
    method: HttpMethod = .get,
    headers: [String: String] = [:]
) async -> Data? {
    
    let endpointUrl = backendUrl + endpoint
    guard let url = URL(string: endpointUrl) else {
        debugPrint("Invalid URL : \(endpointUrl)")
        return nil
    }
    
    // Setup request
    var request = URLRequest(url: url)
    request.httpMethod = method.rawValue
    
    // Set headers if needed
    for key in headers.keys {
        request.setValue(headers[key], forHTTPHeaderField: key)
    }
    
    do {
        // Decode data got
        let (data, _) = try await URLSession.shared.data(for: request)
        return data
    } catch {
        debugPrint("Request failed on \(endpointUrl) : \(error)")
        return nil
    }
    
}
