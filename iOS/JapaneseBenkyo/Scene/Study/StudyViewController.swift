//
//  StudyViewController.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.09.06.
//

import UIKit

class StudyViewController: UIViewController {
    
    var indexEnum: IndexEnum?
    var sectionEnum: SectionEnum?
    var day: String = ""
    
    var vocabulariesForCell: [VocabularyForCell]?
    var kanjisForCell: [KanjiForCell]?
    
    private var vocabularyTableViewHandler: VocabularyTableViewHandler?
    private var kanjiTableViewHandler: KanjiTableViewHandler?
    
    @IBOutlet weak var lbTitle: UILabel!
    @IBOutlet weak var lbSubtitle: UILabel!
    @IBOutlet weak var ivSection: UIImageView!
    
    @IBOutlet weak var btnTest: UIButton!
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lbTitle.text = sectionEnum?.title
        lbSubtitle.text = "\(indexEnum?.rawValue ?? "") \(day)"
        ivSection.image = sectionEnum?.image
        
        tableView.rowHeight = CGFloat(CommonConstant.cellSize)
        
        if (sectionEnum == .vocabulary && vocabulariesForCell?.count == 0) ||
            (sectionEnum == .kanji && kanjisForCell?.count == 0) {
            btnTest.isEnabled = false
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
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
        default:
            return
        }
    }
    
    @IBAction func onClickBack(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }
    
    
    @IBAction func onClickVisibleAll(_ sender: Any) {
        switch sectionEnum {
        case .kanji:
            kanjiTableViewHandler?.setVisibleAll()
        case .vocabulary:
            vocabularyTableViewHandler?.setVisibleAll()
        default:
            return
        }
        tableView.reloadData()
    }
    
    @IBAction func onClickTest(_ sender: Any) {
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
        navigationController?.pushViewController(vc, animated: true)
    }
    
    @IBAction func onClickShuffle(_ sender: Any) {
        switch sectionEnum {
        case .kanji:
            kanjiTableViewHandler?.shuffleKanjis()
        case .vocabulary:
            vocabularyTableViewHandler?.shuffleVocabularies()
        default:
            return
        }
        tableView.reloadData()
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
