//
//  UINavigationController+Ext.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 3/14/24.
//

import UIKit

extension UINavigationController {
    
    func replaceViewController(viewController: UIViewController, animated:Bool) {
        viewControllers.removeLast()
        viewControllers.append(viewController)
        setViewControllers(viewControllers, animated: animated)
    }
}
