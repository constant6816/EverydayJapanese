//
//  HeaderTableViewCell.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.08.31.
//

import UIKit

class HeaderTableViewCell: UITableViewCell {

    @IBOutlet weak var ivIcon: UIImageView!
    @IBOutlet weak var lbTitle: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
    }
    
    func initializeView(section: SectionEnum) {
        ivIcon.image = section.image
        lbTitle.text = section.title
    }
    
    func initializeView(image: UIImage?, text: String?) {
        ivIcon.image = image
        lbTitle.text = text
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
    }

}
