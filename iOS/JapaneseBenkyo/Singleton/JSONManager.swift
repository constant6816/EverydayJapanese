//
//  JSONReader.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2/13/24.
//

import Foundation

class JSONManager {
    
    public static let shared = JSONManager()
    
    private init() {
        
    }
    
    func encodeProcessJSON(process: [String: [String: Bool]]) -> String {
        guard let encoded = try? JSONEncoder().encode(process) else {
            return ""
        }
        return String(decoding: encoded, as: UTF8.self)
    }
    
    func decodeProcessJSON(jsonData: Data) -> [String: [String: Bool]] {
        guard let process = try? JSONDecoder().decode([String: [String: Bool]].self, from: jsonData) else {
            return [:]
        }
        return process
    }
    
    func encodeVocabularyJSON(vocabularies: Set<Vocabulary>) -> String {
        guard let encoded = try? JSONEncoder().encode(vocabularies) else {
            return ""
        }
        return String(decoding: encoded, as: UTF8.self)
    }
    
    func decodeJSONtoVocabularyArray(jsonData: Data) -> [Vocabulary] {
        guard let vocabularies = try? JSONDecoder().decode([Vocabulary].self, from: jsonData) else {
            return []
        }
        return vocabularies
    }
    
    func decodeJSONtoVocabularySet(jsonData: Data) -> Set<Vocabulary> {
        guard let vocabularies = try? JSONDecoder().decode(Set<Vocabulary>.self, from: jsonData) else {
            return []
        }
        return vocabularies
    }
    
    
    func encodeKanjiJSON(kanjis: Set<Kanji>) -> String {
        guard let encoded = try? JSONEncoder().encode(kanjis) else {
            return ""
        }
        return String(decoding: encoded, as: UTF8.self)
    }
    
    func decodeJSONtoKanjiArray(jsonData: Data) -> [Kanji] {
        guard let kanjis = try? JSONDecoder().decode([Kanji].self, from: jsonData) else {
            return []
        }
        return kanjis
    }
    
    func decodeJSONtoKanjiSet(jsonData: Data) -> Set<Kanji> {
        guard let kanjis = try? JSONDecoder().decode(Set<Kanji>.self, from: jsonData) else {
            return []
        }
        return kanjis
    }
    
    
    func openJSON(path: String) -> Data? {
        guard let path = Bundle.main.path(forResource: path, ofType: "json") else {
            NSLog("Error, JSON file not found.")
            return nil
        }
        let url = URL(fileURLWithPath: path)
        guard let jsonData = try? Data(contentsOf: url) else {
            return nil
        }
        return jsonData
    }
    
    func convertStringToData(jsonString: String) -> Data? {
        return jsonString.data(using: .utf8)
    }
}
