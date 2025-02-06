//
//  CommonRepository.swift
//  JapaneseBenkyo
//
//  Created by 김호성 on 2024.12.14.
//

import Foundation
import Moya
import CombineMoya
import Combine

class CommonRepository {
    static let shared = CommonRepository()
    private init() {
        
    }
    
    private let provider = MoyaProvider<CommonRestAPI>()
    
    func getSentence(word: String) -> AnyPublisher<TatoebaModel, MoyaError> {
        return provider.requestPublisher(CommonRestAPI.getSentence(word: word))
            .map(TatoebaEntity.self)
            .subscribe(on: DispatchQueue.global())
            .receive(on: DispatchQueue.main)
            .map({ tatoebaEntity in
                guard let responseData = tatoebaEntity.data?.first else { return TatoebaModel() }
                return TatoebaModel(tatoebaEntity: responseData)
            })
            .eraseToAnyPublisher()
    }
}
