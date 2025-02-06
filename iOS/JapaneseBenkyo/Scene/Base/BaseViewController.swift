//
//  BaseViewController.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2025.02.06.
//

import UIKit
import GoogleMobileAds

class BaseViewController: UIViewController {

    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var bannerView: GADBannerView!
    
    private var containerViewController: UINavigationController?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupContainerViewController()
#if DEBUG
        bannerView.adUnitID = Bundle.main.adTestKey
#else
        bannerView.adUnitID = Bundle.main.adMyKey
#endif
        bannerView.rootViewController = self
        bannerView.load(GADRequest())
    }
    
    private func setupContainerViewController() {
        containerViewController = UINavigationController(rootViewController: UIViewController.getViewController(viewControllerEnum: .main))
        guard let containerViewController = containerViewController else { return }
        containerViewController.setNavigationBarHidden(true, animated: false)
        containerViewController.interactivePopGestureRecognizer?.isEnabled = true
        containerViewController.interactivePopGestureRecognizer?.delegate = self
        addChild(containerViewController)
        containerViewController.view.frame = containerView.bounds
        containerView.addSubview(containerViewController.view)
        containerViewController.didMove(toParent: self)
    }
}

extension BaseViewController: UIGestureRecognizerDelegate {
    // Prevent the rootviewcontroller from being popped.
    func gestureRecognizerShouldBegin(_ gestureRecognizer: UIGestureRecognizer) -> Bool {
        return containerViewController?.viewControllers.count ?? 0 > 1
    }
}
