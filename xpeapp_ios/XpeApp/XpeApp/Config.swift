//
//  Config.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation


struct Configuration {
    enum EnvItem {
        case uat, prod, local
    }
    
    static var env: EnvItem = .uat
    
    static var backendUrl: String {
        // Try to get url from environment
        if let url = ProcessInfo.processInfo.environment["BACKEND_URL"] {
            return url
        }
        // Else use default url for environment
        switch env {
            case .local:
                return "http://localhost:7830/wp-json/" //NOSONAR
            case .uat:
                return "https://wordpress.uat.xpeho.fr/wp-json/" //NOSONAR
            case .prod: 
                return "https://wordpress.uat.xpeho.fr/wp-json/" //NOSONAR
        }
    }
}
