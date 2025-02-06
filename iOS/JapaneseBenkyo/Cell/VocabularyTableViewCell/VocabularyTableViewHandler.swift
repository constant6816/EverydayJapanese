//
//  VocabularyTableDataSource.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 3/11/24.
//

import UIKit
import Combine
import SkeletonView

class VocabularyTableViewHandler: NSObject, UITableViewDataSource, UITableViewDelegate {
    
    private var vocabulariesForCell: [VocabularyForCell]
    private var bookmark: Set<Vocabulary> = []
    var onReload: ((_ indexPath: IndexPath)->Void)?
    var showSkeleton: ((_ indexPath: IndexPath)->Void)?
    
    private var cancellable: Set<AnyCancellable> = Set<AnyCancellable>()
    
    init(vocabulariesForCell: [VocabularyForCell]) {
        self.vocabulariesForCell = vocabulariesForCell
        if let jsonData = JSONManager.shared.convertStringToData(jsonString: UserDefaultManager.shared.vocabularyBookmark) {
            bookmark = JSONManager.shared.decodeJSONtoVocabularySet(jsonData: jsonData)
        }
        for vocabularyForCell in vocabulariesForCell {
            vocabularyForCell.isBookmark = bookmark.contains(vocabularyForCell.vocabulary)
        }
        super.init()
    }
    
    func shuffleVocabularies() {
        vocabulariesForCell.shuffle()
    }
    
    func setVisibleAll() {
        let hasInvisible: Bool = vocabulariesForCell.contains(where: { !$0.isVisible })
        
        for vocabularyForCell in vocabulariesForCell {
            vocabularyForCell.isVisible = hasInvisible
        }
    }
    
    func addBookmarkAll() {
        bookmark.formUnion(Set(vocabulariesForCell.map { $0.vocabulary }))
        UserDefaultManager.shared.vocabularyBookmark = JSONManager.shared.encodeVocabularyJSON(vocabularies: bookmark)
        for vocabularyForCell in vocabulariesForCell {
            vocabularyForCell.isBookmark = true
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return vocabulariesForCell.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: VocabularyTableViewCell
        let vocabularyForCell: VocabularyForCell = vocabulariesForCell[indexPath.row]
        if let reusableCell = tableView.dequeueReusableCell(withIdentifier: String(describing: VocabularyTableViewCell.self), for: indexPath) as? VocabularyTableViewCell {
            cell = reusableCell
        } else {
            let objectArray = Bundle.main.loadNibNamed(String(describing: VocabularyTableViewCell.self), owner: nil, options: nil)
            cell = objectArray![0] as! VocabularyTableViewCell
        }
        
        cell.onClickBookmark = { [weak self] sender in
            self?.onClickBookmark(cell, sender, vocabularyForCell: vocabularyForCell)
        }
        cell.onClickPronounce = { [weak self] sender in
            self?.onClickPronounce(cell, sender, vocabularyForCell: vocabularyForCell)
        }
        cell.onClickExpand = { [weak self] sender in
            self?.onClickExpand(cell, sender, vocabularyForCell: vocabularyForCell, indexPath: indexPath)
        }
        
        cell.initializeCell(vocabularyForCell: vocabularyForCell)
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        guard let cell = tableView.cellForRow(at: indexPath) as? VocabularyTableViewCell else { return }
        vocabulariesForCell[indexPath.row].isVisible = !vocabulariesForCell[indexPath.row].isVisible
        cell.initializeCell(vocabularyForCell: vocabulariesForCell[indexPath.row])
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    
    private func onClickBookmark(_ cell: VocabularyTableViewCell, _ sender: UIButton, vocabularyForCell: VocabularyForCell) {
        if vocabularyForCell.isBookmark {
            bookmark.remove(vocabularyForCell.vocabulary)
        } else {
            bookmark.insert(vocabularyForCell.vocabulary)
        }
        vocabularyForCell.isBookmark = !vocabularyForCell.isBookmark
        cell.initializeCell(vocabularyForCell: vocabularyForCell)
        UserDefaultManager.shared.vocabularyBookmark = JSONManager.shared.encodeVocabularyJSON(vocabularies: bookmark)
    }
    private func onClickPronounce(_ cell: VocabularyTableViewCell, _ sender: UIButton, vocabularyForCell: VocabularyForCell) {
        TTSManager.shared.play(vocabulary: vocabularyForCell.vocabulary)
    }
    private func onClickExpand(_ cell: VocabularyTableViewCell, _ sender: UIButton, vocabularyForCell: VocabularyForCell, indexPath: IndexPath) {
        vocabularyForCell.isExpanded = !vocabularyForCell.isExpanded
        cell.initializeCell(vocabularyForCell: vocabularyForCell)
        onReload?(indexPath)
        if let _ = vocabularyForCell.exampleSentence {
            return
        }
        CommonRepository.shared.getSentence(word: vocabularyForCell.vocabulary.word)
            .sink(receiveCompletion: { error in
                NSLog("\(error)")
            }, receiveValue: { [weak self] tatoebaModel in
                vocabularyForCell.exampleSentence = tatoebaModel
                cell.initializeCell(vocabularyForCell: vocabularyForCell)
                self?.onReload?(indexPath)
            })
            .store(in: &cancellable)
    }
}
