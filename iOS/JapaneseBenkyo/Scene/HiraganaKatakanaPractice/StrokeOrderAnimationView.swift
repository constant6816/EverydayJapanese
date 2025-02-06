//
//  StrokeOrderAnimationView.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2025.01.17.
//

import UIKit
import Elephant

class StrokeOrderAnimationView: UIView {
    
    private var svgView: SVGView?
    
    private func initializeView(hiraganaKatakana: String) {
        if hiraganaKatakana == "" { return }
        svgView = SVGView(named: hiraganaKatakana, animationOwner: .svg)
        guard let svgView = svgView else { return }
        self.addSubview(svgView)
        svgView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            svgView.leadingAnchor.constraint(equalTo: self.leadingAnchor),
            svgView.trailingAnchor.constraint(equalTo: self.trailingAnchor),
            svgView.topAnchor.constraint(equalTo: self.topAnchor),
            svgView.bottomAnchor.constraint(equalTo: self.bottomAnchor),
        ])
    }
    
    func startAnimation(hiraganaKatakana: String?) {
        svgView?.stopAnimation()
        svgView?.removeFromSuperview()
        svgView = nil
        guard let hiraganaKatakana = hiraganaKatakana else { return }
        initializeView(hiraganaKatakana: hiraganaKatakana)
        svgView?.startAnimation()
    }
}
