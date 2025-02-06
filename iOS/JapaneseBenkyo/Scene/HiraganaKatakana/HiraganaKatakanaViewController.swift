//
//  HiraganaKatakanaViewController.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.09.07.
//

import UIKit

class HiraganaKatakanaViewController: UIViewController {
    
    var indexEnum: IndexEnum?
    
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
    
    @IBOutlet weak var lbTitle: UILabel!
    @IBOutlet weak var lbSubtitle: UILabel!
    @IBOutlet weak var ivSection: UIImageView!
    
    @IBOutlet weak var cvHiraganaKatakana: UICollectionView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        ivSection.image = indexEnum?.getSection()?.image
        lbTitle.text = indexEnum?.getSection()?.title
        lbSubtitle.text = indexEnum?.rawValue
        
        cvHiraganaKatakana.delegate = self
        cvHiraganaKatakana.dataSource = self
        cvHiraganaKatakana.register(UINib(nibName: String(describing: HiraganaKatakanaCollectionViewCell.self), bundle: nil), forCellWithReuseIdentifier: String(describing: HiraganaKatakanaCollectionViewCell.self))
    }
    
    @IBAction func onClickBack(_ sender: Any) {
        navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickTest(_ sender: Any) {
        let vc = UIViewController.getViewController(viewControllerEnum: .hiraganakatakanatest) as! HiraganaKatakanaTestViewController
        vc.indexEnum = indexEnum
        navigationController?.pushViewController(vc, animated: true)
    }
}

extension HiraganaKatakanaViewController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
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
        let vc = UIViewController.getViewController(viewControllerEnum: .hiraganakatakanapractice) as! HiraganaKatakanaPracticeViewController
        vc.indexEnum = indexEnum
        vc.selected = indexPath.row
        navigationController?.pushViewController(vc, animated: true)
    }
}
