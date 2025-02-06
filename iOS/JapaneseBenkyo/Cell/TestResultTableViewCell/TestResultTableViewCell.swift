//
//  HiraganaKatakanaTestResultTableViewCell.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.11.19.
//

import UIKit

class TestResultTableViewCell: UITableViewCell {
    
    @IBOutlet weak var lbHiraganaKatakana: UILabel!
    @IBOutlet weak var lbSound: UILabel!
    
    @IBOutlet weak var lbMyHiraganaKatakana: UILabel!
    @IBOutlet weak var ivMyHiraganaKatakana: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
    }
    
    func initializeView(hiraganaKatakana: String, sound: String, myHiraganaKatakana: String?, myHiraganaKatakanaImage: UIImage) {
        lbHiraganaKatakana.text = hiraganaKatakana
        lbSound.text = sound
        lbMyHiraganaKatakana.text = myHiraganaKatakana
        ivMyHiraganaKatakana.image = myHiraganaKatakanaImage
    }
}
