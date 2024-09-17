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
    
    static var env: EnvItem = .uat
    
    static var backendUrl: String {
        // Try to get url from environment
        if let url = ProcessInfo.processInfo.environment["BACKEND_URL"] {
            return url
        }
        // Else use default url for environment
        return env == .uat
            ? "http://nginx-wp/wp-json/"
            : "http://yaki.uat.xpeho.fr:7830/wp-json/"
    }
}
