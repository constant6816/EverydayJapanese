//
//  Bundle+Ext.swift
//  Search
//
//  Created by 김호성 on 2024.11.14.
//

import Foundation

extension Bundle {
    
    var adTestKey: String? {
        guard let file = self.path(forResource: "Secret", ofType: "plist") else { return nil }
        guard let resource = NSDictionary(contentsOfFile: file) else { return nil }
        
        guard let key = resource["ad_test_key"] as? String else { return nil }
        return key
    }
    var adMyKey: String? {
        guard let file = self.path(forResource: "Secret", ofType: "plist") else { return nil }
        guard let resource = NSDictionary(contentsOfFile: file) else { return nil }
        
        guard let key = resource["ad_my_key"] as? String else { return nil }
        return key
    }
}
