//
//  CustomTableViewCell.swift
//  JapaneseBenkyo
//
//  Created by Hosung.Kim on 2022/03/24.
//

import UIKit

class KanjiTableViewCell: UITableViewCell {
    
    @IBOutlet weak var lbKanji: UILabel!
    @IBOutlet weak var lbEumhun: UILabel!
    @IBOutlet weak var lbJpSound: UILabel!
    @IBOutlet weak var lbJpMeaning: UILabel!
    @IBOutlet weak var btnBookmark: UIButton!
    @IBOutlet weak var btnExpand: UIButton!
    @IBOutlet weak var stackView: UIStackView!
    @IBOutlet weak var scHanja: UISegmentedControl!
    
    var valueChangedHanja: ((_ sender: UISegmentedControl) -> Void)?
    var onClickBookmark: ((_ sender: UIButton) -> Void)?
    var onClickPronounce: ((_ sender: UIButton) -> Void)?
    var onClickExpand: ((_ sender: UIButton) -> Void)?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        lbJpSound.adjustsFontSizeToFitWidth = true
        lbJpMeaning.adjustsFontSizeToFitWidth = true
        lbEumhun.adjustsFontSizeToFitWidth = true
    }
    @IBAction func valueChangedHanja(_ sender: UISegmentedControl) {
        valueChangedHanja?(sender)
    }
    
    @IBAction func onClickBookmark(_ sender: UIButton) {
        onClickBookmark?(sender)
    }
    @IBAction func onClickPronounce(_ sender: UIButton) {
        onClickPronounce?(sender)
    }
    @IBAction func onClickExpand(_ sender: UIButton) {
        onClickExpand?(sender)
    }
    
    func initializeCell(kanjiForCell: KanjiForCell) {
        scHanja.isHidden = kanjiForCell.kanji.hanja == ""
        let attributedString = NSMutableAttributedString(string: kanjiForCell.isVisibleHanja ? kanjiForCell.kanji.hanja : kanjiForCell.kanji.kanji)
        attributedString.addAttribute(.languageIdentifier, value: kanjiForCell.isVisibleHanja ? "kr" : "ja", range: NSRange(location: 0, length: kanjiForCell.isVisibleHanja ? kanjiForCell.kanji.hanja.count : kanjiForCell.kanji.kanji.count))
        lbKanji.attributedText = attributedString
        scHanja.selectedSegmentIndex = kanjiForCell.isVisibleHanja ? 1 : 0
        
        lbEumhun.text = kanjiForCell.isVisible ? kanjiForCell.kanji.eumhun : ""
        lbJpSound.text = kanjiForCell.isVisible ? kanjiForCell.kanji.jpSound : ""
        lbJpMeaning.text = kanjiForCell.isVisible ? kanjiForCell.kanji.jpMeaning : ""
        
        btnBookmark.setImage(UIImage(systemName: kanjiForCell.isBookmark ? "star.fill" : "star"), for: .normal)
        
        btnExpand.setImage(UIImage(systemName: kanjiForCell.isExpanded ? "chevron.up" : "chevron.down"), for: .normal)
        stackView.clearSubViews()
        if kanjiForCell.isExpanded {
            expandExamples(kanjiForCell: kanjiForCell)
        }
    }
    
    private func expandExamples(kanjiForCell: KanjiForCell) {
        for example in kanjiForCell.kanji.examples {
            guard let expandableAreaView = Bundle.main.loadNibNamed(String(describing: ExampleWordView.self), owner: nil, options: nil)?.first as? ExampleWordView else {
                continue
            }
            stackView.addArrangedSubview(expandableAreaView)
            let wordString = NSMutableAttributedString(string: example.word)
            wordString.addAttribute(.languageIdentifier, value: "ja", range: NSRange(location: 0, length: example.word.count))
            expandableAreaView.lbWord.attributedText = wordString
            expandableAreaView.lbSound.text = example.sound
            expandableAreaView.lbMeaning.text = example.meaning
        }
    }
}
