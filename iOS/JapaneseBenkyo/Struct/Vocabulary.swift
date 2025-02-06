//
//  Vocabulary.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2/15/24.
//

import Foundation

struct Vocabulary: Codable, Equatable, Hashable {
    let word: String
    let sound: String
    let meaning: String
}
