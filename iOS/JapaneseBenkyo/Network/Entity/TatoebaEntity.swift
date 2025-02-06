//
//  TatoebaEntity.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.12.14.
//

import Foundation

struct TatoebaEntity: Codable {
    var data: [ResponseData]?
    struct ResponseData: Codable {
        var id: Int?
        var text: String?
        var transcriptions: [Transcription]?
        struct Transcription: Codable {
            var text: String?
            var html: String?
        }
    }
}
