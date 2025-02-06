//
//  Configuration.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.12.14.
//

import Foundation

class TatoebaConfiguration {
    public static let shared = TatoebaConfiguration()
    
    var baseURL: String = "https://api.tatoeba.org"
    
    private init() { }
}
