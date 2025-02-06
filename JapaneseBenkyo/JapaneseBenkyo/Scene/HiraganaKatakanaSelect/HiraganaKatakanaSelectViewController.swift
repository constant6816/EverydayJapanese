//
//  HiraganaKatakanaSelectViewController.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2025.02.04.
//

import UIKit

class HiraganaKatakanaSelectViewController: UIViewController {
    
    var indexEnum: IndexEnum?
    var selected: Int!
    
    var applySelected: ((Int) -> Void)?
    
    private var collectionData: [(String, String)]? {
        get {
            switch indexEnum {
            case .hiragana:
                return HiraganaKatakanaManager.shared.hiraganaTuple
            case .katakana:
                return HiraganaKatakanaManager.shared.katakanaTuple
            default:
                return nil
            }
        }
    }
    
    @IBOutlet weak var cvHiraganaKatakana: UICollectionView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        cvHiraganaKatakana.delegate = self
        cvHiraganaKatakana.dataSource = self
        cvHiraganaKatakana.register(UINib(nibName: String(describing: HiraganaKatakanaCollectionViewCell.self), bundle: nil), forCellWithReuseIdentifier: String(describing: HiraganaKatakanaCollectionViewCell.self))
    }
    

    @IBAction func onClickBack(_ sender: Any) {
        dismiss(animated: true)
    }
}

extension HiraganaKatakanaSelectViewController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return collectionData?.count ?? 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell: HiraganaKatakanaCollectionViewCell
        if let reusableCell = collectionView.dequeueReusableCell(withReuseIdentifier: String(describing: HiraganaKatakanaCollectionViewCell.self), for: indexPath) as? HiraganaKatakanaCollectionViewCell {
            cell = reusableCell
        } else {
            let objectArray = Bundle.main.loadNibNamed(String(describing: HiraganaKatakanaCollectionViewCell.self), owner: nil, options: nil)
            cell = objectArray![0] as! HiraganaKatakanaCollectionViewCell
        }
        
        guard let collectionData = collectionData else {
            return cell
        }
        cell.borderView.backgroundColor = indexPath.row == selected ? UIColor.tertiarySystemGroupedBackground : UIColor.clear
        cell.lbMain.text = collectionData[indexPath.row].0
        cell.lbSub.text = collectionData[indexPath.row].1
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let width = (collectionView.frame.width-5)/5
        return CGSize(width: width, height: width)
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        // 컬랙션뷰 행 하단 여백
        return 1
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        // 컬랙션뷰 컬럼 사이 여백
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard let collectionData = collectionData else {
            return
        }
        if collectionData[indexPath.row].0 == "" { return }
        applySelected?(indexPath.row)
        dismiss(animated: true)
    }
}
