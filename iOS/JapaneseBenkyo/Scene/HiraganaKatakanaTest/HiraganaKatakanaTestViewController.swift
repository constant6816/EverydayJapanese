//
//  HiraganaKatakanaViewController.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.11.18.
//

import UIKit

class HiraganaKatakanaTestViewController: UIViewController {
    
    var indexEnum: IndexEnum?
    
    private var testResult: [(String?, UIImage)] = []
    private var recognizedText: String?
    private var index: Int = 0
    private var hiraganakatakana: [(String, String)] = []
    
    @IBOutlet weak var lbTitle: UILabel!
    @IBOutlet weak var lbSubtitle: UILabel!
    @IBOutlet weak var ivSection: UIImageView!
    
    @IBOutlet weak var lbHiraganaKatakana: UILabel!
    @IBOutlet weak var lbIndex: UILabel!
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    @IBOutlet weak var ocrDrawingView: OCRDrawingView!
    
    @IBOutlet weak var ivAlert: UIImageView!
    @IBOutlet weak var lbAlert: UILabel!
    @IBOutlet weak var lbResult: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        ocrDrawingView.delegate = self
        
        ivSection.image = indexEnum?.getSection()?.image
        lbTitle.text = indexEnum?.getSection()?.title
        lbSubtitle.text = "\(indexEnum?.rawValue ?? "") 테스트"
        
        if let hiraganakatakana = getHiraganaOrKatakana(indexEnum: indexEnum) {
            self.hiraganakatakana = hiraganakatakana.shuffled().filter { $0.0 != "" && $0.1 != "" }
        }
        updateView()
    }
    @IBAction func onClickBack(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickUndo(_ sender: Any) {
        ocrDrawingView.undo()
        lbAlert.isHidden = true
        ivAlert.isHidden = true
    }
    @IBAction func onClickClear(_ sender: Any) {
        ocrDrawingView.clear()
        lbAlert.isHidden = true
        ivAlert.isHidden = true
    }
    
    @IBAction func lbSubmit(_ sender: Any) {
        testResult.append((recognizedText, ocrDrawingView.toImage() ?? UIImage()))
        if index+1 == hiraganakatakana.count {
            var correct: [(String, String, UIImage)] = []
            var wrong: [(String, String, String, UIImage)] = []
            var recognitionFailed: [(String, String, UIImage)] = []
            for i in 0..<index {
                if let recognizedString = testResult[i].0 {
                    recognizedString == hiraganakatakana[i].0 ? correct.append((hiraganakatakana[i].0, hiraganakatakana[i].1, testResult[i].1)) : wrong.append((hiraganakatakana[i].0, hiraganakatakana[i].1, recognizedString, testResult[i].1))
                } else {
                    recognitionFailed.append((hiraganakatakana[i].0, hiraganakatakana[i].1, testResult[i].1))
                }
            }
            let vc = UIViewController.getViewController(viewControllerEnum: .hiraganakatakanatestresult) as! HiraganaKatakanaTestResultViewController
            vc.indexEnum = indexEnum
            vc.correct = correct
            vc.wrong = wrong
            vc.recognitionFailed = recognitionFailed
            navigationController?.replaceViewController(viewController: vc, animated: true)
        } else {
            recognizedText = ""
            index += 1
            updateView()
        }
    }
    
    private func getHiraganaOrKatakana(indexEnum: IndexEnum?) -> [(String, String)]? {
        switch indexEnum {
        case .hiragana:
            return HiraganaKatakanaManager.shared.hiraganaTuple
        case .katakana:
            return HiraganaKatakanaManager.shared.katakanaTuple
        default:
            return nil
        }
    }
    
    private func updateView() {
        ocrDrawingView.clear()
        lbIndex.text = "\(index+1)/\(hiraganakatakana.count)"
        lbHiraganaKatakana.text = "\(indexEnum?.rawValue ?? "") \(hiraganakatakana[index].1)"
        lbResult.text = "인식된 결과가 없습니다."
        lbAlert.isHidden = true
        ivAlert.isHidden = true
    }
}

extension HiraganaKatakanaTestViewController: DrawingViewDelegate {
    func willExtractText() {
        loadingIndicator.startAnimating()
        lbResult.isHidden = true
    }
    
    func didExtractText(text: String?) {
        loadingIndicator.stopAnimating()
        lbResult.isHidden = false
        recognizedText = text
        if let text = text {
            lbResult.text = "인식 결과 : \(text)"
            lbAlert.isHidden = true
            ivAlert.isHidden = true
        } else {
            lbResult.text = "인식된 결과가 없습니다."
            lbAlert.isHidden = false
            ivAlert.isHidden = false
        }
    }
}
