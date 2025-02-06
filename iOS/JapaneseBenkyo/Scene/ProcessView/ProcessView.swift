//
//  ProcessView.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.09.07.
//

import UIKit

class ProcessView: UIView {

    private var process: CGFloat?
    
    private let backGroundColor: UIColor = .systemBackground
    private let labelColor: UIColor = .label
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.layer.cornerRadius = 5
        self.layer.borderWidth = 1
        self.layer.borderColor = UIColor.lightGray.cgColor
        self.layer.masksToBounds = true
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        self.layer.cornerRadius = 5
        self.layer.borderWidth = 1
        self.layer.borderColor = UIColor.lightGray.cgColor
        self.layer.masksToBounds = true
    }
    
    func drawProcess(process: CGFloat) {
        self.process = process
        setNeedsDisplay()
    }
    
    override func draw(_ rect: CGRect) {
        let width = rect.width
        let height = rect.height
        
        guard let process = process else {
            return
        }
        
        let backgroundRect = CGRect(x: 0, y: 0, width: width, height: height)
        let backgroundPath = UIBezierPath(rect: backgroundRect)
        backGroundColor.setFill()
        backgroundPath.fill()
        
        let processRect = CGRect(x: 0, y: 0, width: process*width, height: height)
        let processPath = UIBezierPath(rect: processRect)
        labelColor.setFill()
        processPath.fill()
    }
}
