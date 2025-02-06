//
//  TTSManager.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.03.24.
//

import AVFoundation

class TTSManager {
    
    public static let shared = TTSManager()
    
    private let synthesizer = AVSpeechSynthesizer()
    
    private init() {
        synthesizer.usesApplicationAudioSession = true
    }
    
    func play(vocabulary: Vocabulary) {
        let line: String
        if vocabulary.sound == "" {
            line = vocabulary.word
        } else {
            line = vocabulary.sound
        }
        let utterance = AVSpeechUtterance(string: line)
        utterance.voice = AVSpeechSynthesisVoice(language: "ja-JP")
        utterance.rate = 0.2
        synthesizer.stopSpeaking(at: .immediate)
        synthesizer.speak(utterance)
    }
    
    func play(kanji: Kanji) {
        let line = "\(kanji.jpSound), \(kanji.jpMeaning)"
        let utterance = AVSpeechUtterance(string: line)
        utterance.voice = AVSpeechSynthesisVoice(language: "ja-JP")
        utterance.rate = 0.2
        synthesizer.stopSpeaking(at: .immediate)
        synthesizer.speak(utterance)
    }
    
    func play(line: String) {
        let utterance = AVSpeechUtterance(string: line)
        utterance.voice = AVSpeechSynthesisVoice(language: "ja-JP")
        utterance.rate = 0.2
        synthesizer.stopSpeaking(at: .immediate)
        synthesizer.speak(utterance)
    }
}
