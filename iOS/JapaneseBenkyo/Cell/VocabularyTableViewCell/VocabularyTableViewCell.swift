//
//  CustomTableViewCell.swift
//  JapaneseBenkyo
//
//  Created by Hosung.Kim on 2022/03/24.
//

import UIKit

class VocabularyTableViewCell: UITableViewCell {
    
    @IBOutlet weak var lbWord: UILabel!
    @IBOutlet weak var lbSound: UILabel!
    @IBOutlet weak var lbMeaning: UILabel!
    @IBOutlet weak var btnBookmark: UIButton!
    @IBOutlet weak var btnExpand: UIButton!
    @IBOutlet weak var stackView: UIStackView!
    
    var onClickBookmark: ((_ sender: UIButton) -> Void)?
    var onClickPronounce: ((_ sender: UIButton) -> Void)?
    var onClickExpand: ((_ sender: UIButton) -> Void)?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        lbWord.adjustsFontSizeToFitWidth = true
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
    
    func initializeCell(vocabularyForCell: VocabularyForCell) {
        let wordString = NSMutableAttributedString(string: vocabularyForCell.vocabulary.word)
        wordString.addAttribute(.languageIdentifier, value: "ja", range: NSRange(location: 0, length: vocabularyForCell.vocabulary.word.count))
        lbWord.attributedText = wordString
        
        lbSound.text = vocabularyForCell.isVisible ? vocabularyForCell.vocabulary.sound : ""
        lbMeaning.text = vocabularyForCell.isVisible ? vocabularyForCell.vocabulary.meaning : ""
        
        btnBookmark.setImage(UIImage(systemName: vocabularyForCell.isBookmark ? "star.fill" : "star"), for: .normal)
        
        btnExpand.setImage(UIImage(systemName: vocabularyForCell.isExpanded ? "chevron.up" : "chevron.down"), for: .normal)
        stackView.clearSubViews()
        if vocabularyForCell.isExpanded, let expandableAreaView = Bundle.main.loadNibNamed(String(describing: ExampleSentenceView.self), owner: nil, options: nil)?.first as? ExampleSentenceView {
            stackView.addArrangedSubview(expandableAreaView)
            guard let exampleSentence = vocabularyForCell.exampleSentence else {
                expandableAreaView.showSkeleton()
                return
            }
            expandableAreaView.onClickLink = {
                if exampleSentence.id == 0 {
                    return
                }
                guard let url = URL(string: "https://tatoeba.org/ko/sentences/show/\(exampleSentence.id)"), UIApplication.shared.canOpenURL(url) else { return }
                UIApplication.shared.open(url, options: [:], completionHandler: nil)
            }
            expandableAreaView.tvSentence.attributedText = RubyAnnotationManager.shared.getRubyAnnotationString(html: exampleSentence.html, sentence: exampleSentence.text)
            expandableAreaView.hideSkeleton()
        }
    }
}
