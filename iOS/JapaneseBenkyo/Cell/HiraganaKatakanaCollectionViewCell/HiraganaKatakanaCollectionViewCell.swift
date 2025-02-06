//
//  HiraganaKatakanaCollectionViewCell.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.09.07.
//

import UIKit

class HiraganaKatakanaCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var borderView: UIView!
    @IBOutlet weak var lbMain: UILabel!
    @IBOutlet weak var lbSub: UILabel!
    
    override func awakeFromNib() {
        borderView.layer.cornerRadius = 8
        borderView.layer.borderColor = UIColor.clear.cgColor
        borderView.layer.borderWidth = 1
    }
}
