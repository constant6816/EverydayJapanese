package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.network.entity.ClubsResponseEntity

class ClubsModel {
    val clubs: ArrayList<ClubModel>
    val pageInfo: PageInfoModel

    constructor(clubsResponseEntity: ClubsResponseEntity?) {
        clubs = ArrayList<ClubModel>()
        clubsResponseEntity?.responseData?.let {
            it.forEach { clubEntity ->
                clubs.add(ClubModel(clubEntity))
            }
        }
        pageInfo = PageInfoModel(clubsResponseEntity?.pageInfo)
    }
}
