//
//  Kanji.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2/14/24.
//

import Foundation

struct OldKanji: Codable, Equatable, Hashable {
    let kanji: String
    let hanja: String
    let eumhun: String
    let jpSound: String
    let jpMeaning: String
    let examples: [String]
}

struct Kanji: Codable, Equatable, Hashable {
    let kanji: String
    let hanja: String
    let eumhun: String
    let jpSound: String
    let jpMeaning: String
    let examples: [Vocabulary]
}
