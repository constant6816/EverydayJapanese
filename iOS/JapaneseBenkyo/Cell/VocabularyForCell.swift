//
//  VocabularyForCell.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.12.14.
//

import Foundation

class VocabularyForCell {
    let vocabulary: Vocabulary
    var isVisible: Bool = false
    var isBookmark: Bool = false
    var isExpanded: Bool = false
    var exampleSentence: TatoebaModel?
    
    init(vocabulary: Vocabulary) {
        self.vocabulary = vocabulary
    }
}
