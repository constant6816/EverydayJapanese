//
//  KanjiForCell.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.12.14.
//

import Foundation

class KanjiForCell {
    let kanji: Kanji
    var isVisible: Bool = false
    var isVisibleHanja: Bool = false
    var isBookmark: Bool = false
    var isExpanded: Bool = false
    
    init(kanji: Kanji) {
        self.kanji = kanji
    }
}
