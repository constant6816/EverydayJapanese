//
//  RubyAnnotationManager.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.12.18.
//

import Foundation
import UIKit

class RubyAnnotationManager {
    public static let shared = RubyAnnotationManager()
    
    private init() {
        
    }
    
    func getRubyAnnotationString(html: String, sentence: String) -> NSAttributedString? {
        let nsString = sentence as NSString
        let attributedString = NSMutableAttributedString(string: sentence)
        attributedString.addAttribute(.font, value: UIFont.systemFont(ofSize: 18), range: NSRange(location: 0, length: attributedString.length))
        
        let paragraphStyle = NSMutableParagraphStyle()
        paragraphStyle.lineBreakMode = .byWordWrapping
        paragraphStyle.lineHeightMultiple = 1.5
        
        attributedString.addAttributes([
            .paragraphStyle: paragraphStyle,
            .foregroundColor: UIColor.label
        ], range: NSRange(location: 0, length: attributedString.length))
        
        let regexExtractRuby = #"<ruby>.*?</ruby>"#

        guard let rubyRegex = try? NSRegularExpression(pattern: regexExtractRuby, options: []) else { return nil }
            
        let rubyMatches = rubyRegex.matches(in: html, options: [], range: NSRange(location: 0, length: html.utf16.count))
        var nsStringRange: NSRange = NSRange(location: 0, length: nsString.length)
        for rubyMatch in rubyMatches {
            guard let rubyRange = Range(rubyMatch.range, in: html) else { continue }
            let rubyText = String(html[rubyRange])
            let regexExtractFurigana = #"<ruby>(.*?)<rp>\（</rp><rt>(.*?)</rt><rp>\）</rp></ruby>"#
            guard let furiganaRegex = try? NSRegularExpression(pattern: regexExtractFurigana, options: []) else { continue }
            
            guard let match = furiganaRegex.firstMatch(in: rubyText, options: [], range: NSRange(location: 0, length: rubyText.utf16.count)) else { continue }
            let originalRange = Range(match.range(at: 1), in: rubyText)!
            let furiganaRange = Range(match.range(at: 2), in: rubyText)!
            
            let originalText = String(rubyText[originalRange])
            let furiganaText = String(rubyText[furiganaRange])
            
            let rubyAttribute: [CFString: Any] =  [
                kCTRubyAnnotationSizeFactorAttributeName: 0.5,
                kCTForegroundColorAttributeName: UIColor.secondaryLabel,
                kCTFontAttributeName: UIFont.systemFont(ofSize: 9)
            ]
            let rubyAnnotation = CTRubyAnnotationCreateWithAttributes(.auto, .auto, .before, furiganaText as CFString, rubyAttribute as CFDictionary)
            let rubyAnnotationRange = nsString.range(of: originalText, range: nsStringRange)
//            print("\(originalText): \(nsStringRange.location), \(nsStringRange.length)")
            nsStringRange = NSRange(location: rubyAnnotationRange.location + 1, length: nsStringRange.length - rubyAnnotationRange.location - 1 + nsStringRange.location)
            attributedString.addAttributes([kCTRubyAnnotationAttributeName as NSAttributedString.Key: rubyAnnotation], range: rubyAnnotationRange)
        }
        return attributedString
    }
}
