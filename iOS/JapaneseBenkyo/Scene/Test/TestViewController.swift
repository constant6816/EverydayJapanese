//
//  TestViewController.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.09.07.
//

import UIKit

class TestViewController: UIViewController {
    
    var indexEnum: IndexEnum?
    var sectionEnum: SectionEnum?
    var day: String = ""
    
    var kanjis: [Kanji]?
    var vocabularies: [Vocabulary]?
    
    private var testResult: [Bool] = []
    private var index: Int = 0
    private var isVisible: Bool = false
    
    @IBOutlet weak var lbTitle: UILabel!
    @IBOutlet weak var lbSubtitle: UILabel!
    @IBOutlet weak var ivSection: UIImageView!
    
    @IBOutlet weak var lbIndex: UILabel!
    @IBOutlet weak var btnPrevious: UIButton!
    
    @IBOutlet weak var lbMain: UILabel!
    @IBOutlet weak var lbUpperSub1: UILabel!
    @IBOutlet weak var lbUpperSub2: UILabel!
    @IBOutlet weak var lbLowerSub: UILabel!
    @IBOutlet weak var lbSub: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        switch sectionEnum {
        case .kanji:
            kanjis?.shuffle()
        case .vocabulary:
            vocabularies?.shuffle()
        default:
            break
        }
        
        lbTitle.text = sectionEnum?.title
        lbSubtitle.text = "\(indexEnum?.rawValue ?? "") \(day) 테스트"
        ivSection.image = sectionEnum?.image
        
        lbMain.adjustsFontSizeToFitWidth = true
        lbUpperSub1.adjustsFontSizeToFitWidth = true
        lbUpperSub2.adjustsFontSizeToFitWidth = true
        lbLowerSub.adjustsFontSizeToFitWidth = true
        lbSub.adjustsFontSizeToFitWidth = true
        
        updateTestField()
    }
    
    private func updateTestField() {
        if index == 0 {
            btnPrevious.isEnabled = false
            btnPrevious.isHidden = true
        } else {
            btnPrevious.isEnabled = true
            btnPrevious.isHidden = false
        }
        
        switch sectionEnum {
        case .kanji:
            guard let kanjis = kanjis else {
                break
            }
            lbIndex.text = "\(index+1)/\(kanjis.count)"
            let kanjiString = NSMutableAttributedString(string: kanjis[index].kanji)
            kanjiString.addAttribute(.languageIdentifier, value: "ja", range: NSRange(location: 0, length: kanjis[index].kanji.count))
            lbMain.attributedText = kanjiString
            if isVisible {
                lbUpperSub1.text = kanjis[index].jpSound
                lbUpperSub2.text = kanjis[index].jpMeaning
                let hanjaString = NSMutableAttributedString(string: kanjis[index].hanja)
                hanjaString.addAttribute(.languageIdentifier, value: "kr", range: NSRange(location: 0, length: kanjis[index].hanja.count))
                lbLowerSub.attributedText = hanjaString
                lbSub.text = kanjis[index].eumhun
            } else {
                lbUpperSub1.text = ""
                lbUpperSub2.text = ""
                lbLowerSub.text = ""
                lbSub.text = ""
            }
        case .vocabulary:
            guard let vocabularies = vocabularies else {
                break
            }
            lbIndex.text = "\(index+1)/\(vocabularies.count)"
            lbMain.text = vocabularies[index].word
            if isVisible {
                lbUpperSub1.text = ""
                lbUpperSub2.text = vocabularies[index].sound
                lbLowerSub.text = ""
                lbSub.text = vocabularies[index].meaning
            } else {
                lbUpperSub1.text = ""
                lbUpperSub2.text = ""
                lbLowerSub.text = ""
                lbSub.text = ""
            }
        default:
            break
        }
    }
    
    private func moveOnToNext() {
        index += 1
        if (sectionEnum == .kanji && index == kanjis?.count) ||
            (sectionEnum == .vocabulary && index == vocabularies?.count) {
            let vc = UIViewController.getViewController(viewControllerEnum: .testresult) as! TestResultViewController
            vc.indexEnum = indexEnum
            vc.sectionEnum = sectionEnum
            vc.day = day
            
            switch sectionEnum {
            case .kanji:
                guard let kanjis = kanjis else {
                    break
                }
                vc.allCount = kanjis.count
                var wrongKanjis: [KanjiForCell] = []
                for i in 0..<testResult.count {
                    if !testResult[i] {
                        wrongKanjis.append(KanjiForCell(kanji: kanjis[i]))
                    }
                }
                vc.kanjisForCell = wrongKanjis
            case .vocabulary:
                guard let vocabularies = vocabularies else {
                    break
                }
                vc.allCount = vocabularies.count
                var wrongVacabularies: [VocabularyForCell] = []
                for i in 0..<testResult.count {
                    if !testResult[i] {
                        wrongVacabularies.append(VocabularyForCell(vocabulary: vocabularies[i]))
                    }
                }
                vc.vocabulariesForCell = wrongVacabularies
            default:
                return
            }
            
            navigationController?.replaceViewController(viewController: vc, animated: true)
            return
        }
        isVisible = false
        updateTestField()
    }
    
    private func moveOnToPrevious() {
        if index == 0 {
            return
        }
        index -= 1
        testResult.removeLast()
        isVisible = false
        updateTestField()
    }
    
    @IBAction func onClickBack(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }
    
    @IBAction func onClickX(_ sender: Any) {
        testResult.append(false)
        moveOnToNext()
    }
    
    @IBAction func onClickV(_ sender: Any) {
        testResult.append(true)
        moveOnToNext()
    }
    @IBAction func onClickPrevious(_ sender: Any) {
        moveOnToPrevious()
    }
    @IBAction func onClickTestField(_ sender: Any) {
        isVisible = !isVisible
        updateTestField()
    }
}
