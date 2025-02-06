//
//  CustomView.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.03.28.
//

import UIKit

class ExampleWordView: UIView {

    @IBOutlet weak var lbWord: UILabel!
    @IBOutlet weak var lbSound: UILabel!
    @IBOutlet weak var lbMeaning: UILabel!
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    override func awakeFromNib() {
        initializeView()
    }
    
    func initializeView() {
        lbWord.clipsToBounds = true
        lbSound.clipsToBounds = true
        lbMeaning.clipsToBounds = true
    }
}
