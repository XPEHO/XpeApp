//
//  Config.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation


struct Configuration {
    enum EnvItem {
        case uat, prod
    }
    
    static var env: EnvItem = .prod
    
    static var backendUrl: String {
        // Try to get url from environment
        if let url = ProcessInfo.processInfo.environment["BACKEND_URL"] {
            return url
        }
        // Else use default url for environment
        return env == .prod
            ? "https://wordpress.uat.xpeho.fr/wp-json/"
            : "http://localhost:7830/wp-json/"
    }
}
