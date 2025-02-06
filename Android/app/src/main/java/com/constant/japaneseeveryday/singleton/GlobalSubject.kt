package com.constant.japaneseeveryday.singleton

import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class GlobalSubject {
    // Public Inner Class, Struct, Enum, Interface
    // ----------------------------------------------------
    // companion object
    companion object {
        @Volatile
        private lateinit var instance: GlobalSubject

        fun getInstance(): GlobalSubject {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = GlobalSubject()
                }
                return instance
            }
        }
    }

    // Public Constant
    // Private Constant
    // Public Variable

    // Home 업데이트 필요
    var updateHomeSubject: PublishSubject<Boolean>

    // Home 업데이트 필요
    var fetchMyClubCompletedSubject: ReplaySubject<Boolean>

    // Statistics 업데이트 필요
    var updateStatisticsSubject: PublishSubject<Boolean>

    // Club Main 의 좌우 Swipe 설정 (안드로이드만 있음)
    var swipeClubMainSubject: PublishSubject<Boolean>

    // Club Main에서 상위 네비게이션 버튼들 보여줄지 안 보여줄지에 대한 업데이트 필요
    var updateClubMainNaviButtonsSubject: PublishSubject<Boolean>

    // Club Home List 업데이트 필요
    var updateClubListSubject: PublishSubject<Boolean>

    // Club Home 업데이트 필요
    var updateClubHomeSubject: PublishSubject<Boolean>

    // Club Board Main List 업데이트 필요
    var updateClubBoardMainSubject: PublishSubject<Boolean>

    // Club Album List 업데이트 필요
    var updateClubAlbumSubject: PublishSubject<Boolean>

    // Board Main List 업데이트 필요
    var updateBoardMainSubject: PublishSubject<Boolean>

    // Board Detail 업데이트 필요
    var updateBoardDetailSubject: PublishSubject<Boolean>

    // Board Detail 화면 종료
    var finishBoardDetailSubject: PublishSubject<Boolean>

    // ClubId 업데이트
    var updateClubIdSubject: ReplaySubject<Int>

    // Main 화면에서 Tab 이동
    var gotoTabSubject: PublishSubject<Int>

    // var selectDaySubject: PublishSubject<LocalDateTime?>

    // Private Variable
    // Override Method or Basic Method
    constructor() {
        updateHomeSubject = PublishSubject.create()
        fetchMyClubCompletedSubject = ReplaySubject.create(1)
        updateStatisticsSubject = PublishSubject.create()
        swipeClubMainSubject = PublishSubject.create()
        updateClubMainNaviButtonsSubject = PublishSubject.create()
        updateClubListSubject = PublishSubject.create()
        updateClubHomeSubject = PublishSubject.create()
        updateClubBoardMainSubject = PublishSubject.create()
        updateClubAlbumSubject = PublishSubject.create()
        updateBoardMainSubject = PublishSubject.create()
        updateBoardDetailSubject = PublishSubject.create()
        finishBoardDetailSubject = PublishSubject.create()
        updateClubIdSubject = ReplaySubject.create(1)
        gotoTabSubject = PublishSubject.create()
        // selectDaySubject            = PublishSubject.create()
    }

    // Public Method
    // Private Method
}
