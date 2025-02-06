package com.constant.japaneseeveryday.singleton

import com.constant.japaneseeveryday.network.CommonRepository
import com.constant.japaneseeveryday.network.KakaoRepository
import com.constant.japaneseeveryday.util.HHStyle

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class GlobalVariable {
    // Public Inner Class, Struct, Enum, Interface

    // ----------------------------------------------------
    // companion object
    companion object {
        @Volatile
        private lateinit var instance: GlobalVariable

        fun getInstance(): GlobalVariable {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = GlobalVariable()
                }
                return instance
            }
        }
    }

    // Public Constant
    // Private Constant

    // ----------------------------------------------------
    // Public Variable
    public var startTime: Long

    // 1일에 한번 Config 를 호출해 주기위해 만듦
    public var pingTime: Long
    public var commonRepository: CommonRepository
    public var noErrorRepository: CommonRepository
    public var noloadingRepository: CommonRepository
    public var noStyleRepository: CommonRepository
    public var kakaoRepository: KakaoRepository

    // ----------------------------------------------------
    // Private Variable
    // Override Method or Basic Method
    constructor() {
        startTime = 0
        pingTime = 0
        commonRepository = CommonRepository(HHStyle(CommonRepository.Style.loadingSpinner or CommonRepository.Style.showErrorDialog))
        noErrorRepository = CommonRepository(HHStyle(CommonRepository.Style.loadingSpinner))
        noloadingRepository = CommonRepository(HHStyle(CommonRepository.Style.showErrorDialog))
        noStyleRepository = CommonRepository(HHStyle(CommonRepository.Style.none))
        kakaoRepository = KakaoRepository(HHStyle(KakaoRepository.Style.loadingSpinner or KakaoRepository.Style.showErrorDialog))
    }
    // Public Method
    // Private Method
}
