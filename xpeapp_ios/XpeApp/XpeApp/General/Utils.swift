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

let dateFormatter: DateFormatter = {
    let formatter = DateFormatter()
    formatter.locale = Locale(identifier: "fr_FR")
    formatter.dateFormat = "dd/MM/yyyy"
    formatter.timeZone = TimeZone(secondsFromGMT: 0)
    return formatter
}()
