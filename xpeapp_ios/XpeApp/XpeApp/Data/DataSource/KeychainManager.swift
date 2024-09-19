//
//  KeychainManager.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 17/09/2024.
//

import Foundation
import Security

final class KeychainManager {
    static let instance = KeychainManager()
    
    private init() {
        // This initializer is intentionally left empty to make private
        // to prevent use without shared instance
    }
    
    private let serviceIdentifier = "com.xpeapp.xpeho.keys"
    
    // CREATE
    func saveValue(_ value: String, forKey key: String) {
        guard let data = value.data(using: .utf8) else {
            debugPrint("Keychain: saveValue -> failed to encode value")
            return
        }
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: serviceIdentifier,
            kSecAttrAccount as String: key,
            kSecValueData as String: data
        ]
        let status = SecItemAdd(query as CFDictionary, nil)
        guard status != errSecDuplicateItem else {
            debugPrint("Keychain: saveValue -> DuplicateEntry")
            debugPrint("Keychain: try to update")
            updateValue(value, forKey: key)
            return
        }
        guard status == errSecSuccess else {
            debugPrint("Keychain: saveValue -> NoSuccess")
            return
        }
    }
    
    // READ
    func getValue(forKey key: String) -> String? {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: serviceIdentifier,
            kSecAttrAccount as String: key,
            kSecReturnData as String: true,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]
        var dataTypeRef: AnyObject?
        let status = SecItemCopyMatching(query as CFDictionary, &dataTypeRef)
        if status == errSecSuccess, let data = dataTypeRef as? Data {
            return String(data: data, encoding: .utf8)
        }
        debugPrint("Keychain: getValue -> NoSuccess")
        return nil
    }
    
    // UPDATE
    func updateValue(_ value: String, forKey key: String) {
        guard let data = value.data(using: .utf8) else {
            debugPrint("Keychain: saveValue -> failed to encode value")
            return
        }
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: serviceIdentifier,
            kSecAttrAccount as String: key
        ]
        let attributes: [String: Any] = [
            kSecValueData as String: data
        ]
        let status = SecItemUpdate(query as CFDictionary, attributes as CFDictionary)
        guard status == errSecSuccess else {
            debugPrint("Keychain: updateValue -> NoSuccess")
            return
        }
    }
    
    // DELETE
    func deleteValue(forKey key: String) {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: serviceIdentifier,
            kSecAttrAccount as String: key
        ]
        let status = SecItemDelete(query as CFDictionary)
        guard status != errSecItemNotFound else {
            debugPrint("Keychain: deleteValue -> NotFound")
            return
        }
        guard status == errSecSuccess else {
            debugPrint("Keychain: deleteValue -> NoSuccess")
            return
        }
    }
}
