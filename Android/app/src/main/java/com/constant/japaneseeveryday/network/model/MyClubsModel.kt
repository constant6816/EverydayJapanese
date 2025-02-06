package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.network.entity.MyClubsResponseEntity

class MyClubsModel {
    val myClubs: ArrayList<MyClubModel>
    val pageInfo: PageInfoModel

    constructor(myClubsResponseEntity: MyClubsResponseEntity) {
        this.myClubs = ArrayList<MyClubModel>()
        myClubsResponseEntity.responseData?.let {
            it.forEach { myClubEntity ->
                myClubs.add(MyClubModel(myClubEntity))
            }
        }
        this.pageInfo = PageInfoModel(myClubsResponseEntity.pageInfo)
    }

    constructor() {
        this.myClubs = ArrayList<MyClubModel>()
        this.pageInfo = PageInfoModel()
    }
}
