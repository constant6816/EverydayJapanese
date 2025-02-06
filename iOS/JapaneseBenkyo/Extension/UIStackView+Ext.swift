//
//  UIStackView+Ext.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.03.28.
//

import Foundation
import UIKit

extension UIStackView {
    func clearSubViews() {
        self.arrangedSubviews.forEach { view in
            self.removeArrangedSubview(view)
            view.removeFromSuperview()
        }
    }
}
