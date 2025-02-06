//
//  TestResultViewController.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.09.07.
//

import UIKit

class TestResultViewController: UIViewController {
    
    var indexEnum: IndexEnum?
    var sectionEnum: SectionEnum?
    var day: String = ""
    
    var allCount: Int?
    var vocabulariesForCell: [VocabularyForCell]?
    var kanjisForCell: [KanjiForCell]?
    
    private var vocabularyTableViewHandler: VocabularyTableViewHandler?
    private var kanjiTableViewHandler: KanjiTableViewHandler?
    
    @IBOutlet weak var lbTitle: UILabel!
    @IBOutlet weak var lbSubtitle: UILabel!
    @IBOutlet weak var ivSection: UIImageView!
    
    @IBOutlet weak var lbScore: UILabel!
    @IBOutlet weak var lbSubScore: UILabel!
    @IBOutlet weak var btnBookmark: UIButton!
    @IBOutlet weak var btnReTest: UIButton!
    
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lbTitle.text = sectionEnum?.title
        lbSubtitle.text = "\(indexEnum?.rawValue ?? "") \(day) 테스트 결과"
        ivSection.image = sectionEnum?.image
        
        tableView.rowHeight = CGFloat(CommonConstant.cellSize)
        
        switch sectionEnum {
        case .kanji:
            guard let kanjisForCell = kanjisForCell else {
                return
            }
            kanjiTableViewHandler = KanjiTableViewHandler(kanjisForCell: kanjisForCell)
            kanjiTableViewHandler?.onReload = { [weak self] indexPath in
                self?.onReload(indexPath: indexPath)
            }
            tableView.delegate = kanjiTableViewHandler
            tableView.dataSource = kanjiTableViewHandler
            tableView.register(UINib(nibName: String(describing: KanjiTableViewCell.self), bundle: nil), forCellReuseIdentifier: String(describing: KanjiTableViewCell.self))
            initializeScoreView(wrongCount: kanjisForCell.count)
        case .vocabulary:
            guard let vocabulariesForCell = vocabulariesForCell else {
                return
            }
            vocabularyTableViewHandler = VocabularyTableViewHandler(vocabulariesForCell: vocabulariesForCell)
            vocabularyTableViewHandler?.onReload = { [weak self] indexPath in
                self?.onReload(indexPath: indexPath)
            }
            tableView.delegate = vocabularyTableViewHandler
            tableView.dataSource = vocabularyTableViewHandler
            tableView.register(UINib(nibName: String(describing: VocabularyTableViewCell.self), bundle: nil), forCellReuseIdentifier: String(describing: VocabularyTableViewCell.self))
            initializeScoreView(wrongCount: vocabulariesForCell.count)
        default:
            return
        }
        
    }
    
    private func initializeScoreView(wrongCount: Int) {
        guard let allCount = allCount else {
            return
        }
        lbScore.text = "\(Int((allCount - wrongCount) * 100 / allCount))점"
        lbSubScore.text = "\(allCount - wrongCount)/\(allCount)"
        if wrongCount == 0 {
            btnBookmark.isEnabled = false
            btnReTest.isEnabled = false
            if indexEnum == .bookmark {
                return
            }
            saveProcess()
        }
    }
    @IBAction func onClickBookmark(_ sender: UIButton) {
        sender.setImage(UIImage(systemName: "star.fill"), for: .normal)
        switch sectionEnum {
        case .kanji:
            kanjiTableViewHandler?.addBookmarkAll()
        case .vocabulary:
            vocabularyTableViewHandler?.addBookmarkAll()
        default:
            return
        }
        sender.isEnabled = false
        tableView.reloadData()
    }
    
    @IBAction func onClickReTest(_ sender: Any) {
        let vc = UIViewController.getViewController(viewControllerEnum: .test) as! TestViewController
        vc.indexEnum = indexEnum
        vc.sectionEnum = sectionEnum
        vc.day = day
        
        switch sectionEnum {
        case .kanji:
            vc.kanjis = kanjisForCell?.map { $0.kanji }
        case .vocabulary:
            vc.vocabularies = vocabulariesForCell?.map { $0.vocabulary }
        default:
            return
        }
        navigationController?.replaceViewController(viewController: vc, animated: true)
    }
    
    @IBAction func onClickFinishTest(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickBack(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }
    
    private func saveProcess() {
        guard let jsonData = JSONManager.shared.convertStringToData(jsonString: UserDefaultManager.shared.process) else {
            return
        }
        guard let indexEnum = indexEnum else {
            return
        }
        var process = JSONManager.shared.decodeProcessJSON(jsonData: jsonData)
        if process[indexEnum.rawValue] == nil {
            process[indexEnum.rawValue] = [:]
        }
        process[indexEnum.rawValue]?[day] = true
        
        UserDefaultManager.shared.process = JSONManager.shared.encodeProcessJSON(process: process)
    }
    
    private func onReload(indexPath: IndexPath) {
        // When the top cell expands, it causes a problem with the reusable cell, causing the animation to operate abnormally.
        guard let visibleRows = tableView.indexPathsForVisibleRows else {
            return
        }
        if visibleRows.firstIndex(of: indexPath) == 0 {
            tableView.reloadSections(IndexSet(visibleRows.map { $0.section }), with: .automatic)
        }
        tableView.reloadRows(at: [indexPath], with: .automatic)
    }
}
