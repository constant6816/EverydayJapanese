//
//  TatoebaModel.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.12.14.
//

import Foundation

struct TatoebaModel: Codable {
    var id: Int
    var text: String
    var rubyText: String
    var html: String
    
    init(tatoebaEntity: TatoebaEntity.ResponseData) {
        self.id = tatoebaEntity.id ?? 0
        self.text = tatoebaEntity.text ?? "예문이 없습니다."
        self.rubyText = tatoebaEntity.transcriptions?.first?.text ?? "예문이 없습니다."
        self.html = tatoebaEntity.transcriptions?.first?.html ?? "예문이 없습니다."
    }
    
    init() {
        self.id = 0
        self.text = "예문이 없습니다."
        self.rubyText = "예문이 없습니다."
        self.html = "예문이 없습니다."
    }
}
