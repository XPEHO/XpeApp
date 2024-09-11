//
//  Utils.swift
//  XpeApp
//
//  Created by Ryan Debouvries on 23/08/2024.
//

import Foundation

// Util function to count days between two dates
func countDaysBetween(_ from: Date, and to: Date) -> Int? {
    let calendar = Calendar.current
    let components = calendar.dateComponents([.day], from: from, to: to)
    return components.day
}

// Date formatter for display in views
let dateFormatter: DateFormatter = {
    let formatter = DateFormatter()
    formatter.locale = Locale(identifier: "fr_FR")
    formatter.dateFormat = "dd/MM/yyyy"
    formatter.timeZone = TimeZone(secondsFromGMT: 0)
    return formatter
}()

// Util url session protocol to mock services
protocol URLSessionProtocol {
    func data(for request: URLRequest) async throws -> (Data, URLResponse)
    func data(from url: URL) async throws -> (Data, URLResponse)
}
extension URLSession: URLSessionProtocol {}

// Util function to valid the email format
func isValidEmail(_ email: String) -> Bool {
    let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"

    let emailPred = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
    return emailPred.evaluate(with: email)
}
