//
//  DayViewController.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.09.06.
//

import UIKit

class DayViewController: UIViewController {
    
    var indexEnum: IndexEnum?
    
    private var process: [String: [String: Bool]] = [:]
    
    private var kanjisDayDistributed: [Array<Kanji>]?
    private var vocabulariesDayDistributed: [Array<Vocabulary>]?
    
    @IBOutlet weak var lbTitle: UILabel!
    @IBOutlet weak var lbSubtitle: UILabel!
    @IBOutlet weak var ivSection: UIImageView!
    
    @IBOutlet weak var processView: ProcessView!
    @IBOutlet weak var lbProcess: UILabel!
    @IBOutlet weak var tableView: UITableView!
        
    override func viewDidLoad() {
        super.viewDidLoad()
        
        initializeView()
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.rowHeight = 64
        tableView.register(UINib(nibName: String(describing: IndexTableViewCell.self), bundle: nil), forCellReuseIdentifier: String(describing: IndexTableViewCell.self))
    }
    
    override func viewWillAppear(_ animated: Bool) {
        if let jsonData = JSONManager.shared.convertStringToData(jsonString: UserDefaultManager.shared.process) {
            process = JSONManager.shared.decodeProcessJSON(jsonData: jsonData)
        }
        tableView.reloadData()
        guard let indexEnum = indexEnum else {
            return
        }
        processView.drawProcess(process: CGFloat(process[indexEnum.rawValue]?.count ?? 0)/CGFloat(tableView.numberOfRows(inSection: 0)))
        lbProcess.text = "\((process[indexEnum.rawValue] ?? [:]).count)/\(tableView.numberOfRows(inSection: 0))"
    }
    
    func initializeView() {
        
        guard let indexEnum = indexEnum else {
            return
        }
        
        ivSection.image = indexEnum.getSection()?.image
        lbTitle.text = indexEnum.getSection()?.title
        lbSubtitle.text = indexEnum.rawValue
        
        switch indexEnum.getSection() {
        case .kanji:
            guard let jsonData = JSONManager.shared.openJSON(path: indexEnum.getFileName()) else {
                return
            }
            let kanjis: [Kanji] = JSONManager.shared.decodeJSONtoKanjiArray(jsonData: jsonData)
            kanjisDayDistributed = stride(from: 0, to: kanjis.count, by: CommonConstant.daySize).map {
                Array(kanjis[$0..<min($0 + CommonConstant.daySize, kanjis.count)])
            }
        case .vocabulary:
            guard let jsonData = JSONManager.shared.openJSON(path: indexEnum.getFileName()) else {
                return
            }
            let vocabularies: [Vocabulary] = JSONManager.shared.decodeJSONtoVocabularyArray(jsonData: jsonData)
            vocabulariesDayDistributed = stride(from: 0, to: vocabularies.count, by: CommonConstant.daySize).map {
                Array(vocabularies[$0..<min($0 + CommonConstant.daySize, vocabularies.count)])
            }
        default:
            break
        }
    }
    
    @IBAction func onClickBack(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }
}

extension DayViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return (kanjisDayDistributed?.count ?? 0) + (vocabulariesDayDistributed?.count ?? 0) + 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: IndexTableViewCell

        if let reusableCell = tableView.dequeueReusableCell(withIdentifier: String(describing: IndexTableViewCell.self), for: indexPath) as? IndexTableViewCell {
            cell = reusableCell
        } else {
            let objectArray = Bundle.main.loadNibNamed(String(describing: IndexTableViewCell.self), owner: nil, options: nil)
            cell = objectArray![0] as! IndexTableViewCell
        }
        let title: String = indexPath.row == 0 ? "전체보기" : "Day\(indexPath.row)"
        cell.initializeView(title: title, process: process[lbSubtitle.text ?? ""]?[title])
        return cell
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let vc = UIViewController.getViewController(viewControllerEnum: .study) as! StudyViewController
        vc.indexEnum = indexEnum
        vc.sectionEnum = indexEnum?.getSection()
        vc.day = indexPath.row == 0 ? "전체보기" : "Day\(indexPath.row)"
        
        guard let indexEnum = indexEnum else {
            return
        }
        
        switch indexEnum.getSection() {
        case .kanji:
            if indexPath.row == 0 {
                guard let jsonData = JSONManager.shared.openJSON(path: indexEnum.getFileName()) else {
                    return
                }
                let kanjis: [Kanji] = JSONManager.shared.decodeJSONtoKanjiArray(jsonData: jsonData)
                vc.kanjisForCell = kanjis.map { KanjiForCell(kanji: $0) }
            } else {
                vc.kanjisForCell = kanjisDayDistributed?[indexPath.row-1].map { KanjiForCell(kanji: $0) }
            }
        case .vocabulary:
            if indexPath.row == 0 {
                guard let jsonData = JSONManager.shared.openJSON(path: indexEnum.getFileName()) else {
                    return
                }
                let vocabularies: [Vocabulary] = JSONManager.shared.decodeJSONtoVocabularyArray(jsonData: jsonData)
                vc.vocabulariesForCell = vocabularies.map { VocabularyForCell(vocabulary: $0) }
            } else {
                vc.vocabulariesForCell = vocabulariesDayDistributed?[indexPath.row-1].map { VocabularyForCell(vocabulary: $0) }
            }
        default:
            break
        }
        
        navigationController?.pushViewController(vc, animated: true)
    }
}
