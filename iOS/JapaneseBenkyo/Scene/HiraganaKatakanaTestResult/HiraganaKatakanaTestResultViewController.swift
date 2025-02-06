//
//  HiraganaKatakanaTestResultViewController.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.11.19.
//

import UIKit

class HiraganaKatakanaTestResultViewController: UIViewController {
    
    var indexEnum: IndexEnum?
    
    var correct: [(String, String, UIImage)]?
    var wrong: [(String, String, String, UIImage)]?
    var recognitionFailed: [(String, String, UIImage)]?
    
    @IBOutlet weak var lbTitle: UILabel!
    @IBOutlet weak var lbSubtitle: UILabel!
    @IBOutlet weak var ivSection: UIImageView!
    
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        ivSection.image = indexEnum?.getSection()?.image
        lbTitle.text = indexEnum?.getSection()?.title
        lbSubtitle.text = "\(indexEnum?.rawValue ?? "") 테스트 결과"
        
        tableView.register(UINib(nibName: String(describing: HeaderTableViewCell.self), bundle: nil), forCellReuseIdentifier: String(describing: HeaderTableViewCell.self))
        tableView.register(UINib(nibName: String(describing: TestResultTableViewCell.self), bundle: nil), forCellReuseIdentifier: String(describing: TestResultTableViewCell.self))
        tableView.delegate = self
        tableView.dataSource = self
    }
    @IBAction func onClickBack(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }
}

extension HiraganaKatakanaTestResultViewController: UITableViewDelegate, UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            guard let count = correct?.count else { return 0 }
            return count+1
        case 1:
            guard let count = wrong?.count else { return 0 }
            return count+1
        case 2:
            guard let count = recognitionFailed?.count else { return 0 }
            return count+1
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            let cell: HeaderTableViewCell
            if let reusableCell = tableView.dequeueReusableCell(withIdentifier: String(describing: HeaderTableViewCell.self), for: indexPath) as? HeaderTableViewCell {
                cell = reusableCell
            } else {
                let objectArray = Bundle.main.loadNibNamed(String(describing: HeaderTableViewCell.self), owner: nil, options: nil)
                cell = objectArray![0] as! HeaderTableViewCell
            }
            switch indexPath.section {
            case 0:
                cell.initializeView(image: UIImage(systemName: "checkmark")?.withTintColor(UIColor.systemBlue, renderingMode: .alwaysOriginal), text: "정답")
            case 1:
                cell.initializeView(image: UIImage(systemName: "xmark")?.withTintColor(UIColor.systemRed, renderingMode: .alwaysOriginal), text: "오답")
            case 2:
                cell.initializeView(image: UIImage(systemName: "exclamationmark.triangle")?.withTintColor(UIColor.systemYellow, renderingMode: .alwaysOriginal), text: "인식 실패")
            default:
                break
            }
            return cell
        } else {
            let cell: TestResultTableViewCell
            if let reusableCell = tableView.dequeueReusableCell(withIdentifier: String(describing: TestResultTableViewCell.self), for: indexPath) as? TestResultTableViewCell {
                cell = reusableCell
            } else {
                let objectArray = Bundle.main.loadNibNamed(String(describing: TestResultTableViewCell.self), owner: nil, options: nil)
                cell = objectArray![0] as! TestResultTableViewCell
            }
            switch indexPath.section {
            case 0:
                guard let correct = correct else { return cell }
                cell.initializeView(hiraganaKatakana: correct[indexPath.row-1].0, sound: correct[indexPath.row-1].1, myHiraganaKatakana: nil, myHiraganaKatakanaImage: correct[indexPath.row-1].2)
            case 1:
                guard let wrong = wrong else { return cell }
                cell.initializeView(hiraganaKatakana: wrong[indexPath.row-1].0, sound: wrong[indexPath.row-1].1, myHiraganaKatakana: wrong[indexPath.row-1].2, myHiraganaKatakanaImage: wrong[indexPath.row-1].3)
            case 2:
                guard let recognitionFailed = recognitionFailed else { return cell }
                cell.initializeView(hiraganaKatakana: recognitionFailed[indexPath.row-1].0, sound: recognitionFailed[indexPath.row-1].1, myHiraganaKatakana: nil, myHiraganaKatakanaImage: recognitionFailed[indexPath.row-1].2)
            default:
                break
            }
            return cell
        }
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0 {
            return 70
        }
        return 50
    }
}
