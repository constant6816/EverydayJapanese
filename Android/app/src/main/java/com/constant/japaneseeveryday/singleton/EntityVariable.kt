package com.constant.japaneseeveryday.singleton

import com.constant.japaneseeveryday.network.model.ConfigModel
import com.constant.japaneseeveryday.network.model.MemberModel
import com.constant.japaneseeveryday.network.model.MyClubModel

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
class EntityVariable {
    // Public Inner Class, Struct, Enum, Interface
    // ----------------------------------------------------
    // companion object
    companion object {
        @Volatile
        private lateinit var instance: EntityVariable

        fun getInstance(): EntityVariable {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = EntityVariable()
                }
                return instance
            }
        }
    }

    // Public Constant
    // Private Constant
    // Public Variable
    var configModel: ConfigModel?
    var memberModel: MemberModel
    var myClubs: ArrayList<MyClubModel>

    // Private Variable
    // Override Method or Basic Method
    constructor() {
        configModel = null
        memberModel = MemberModel()
        myClubs = ArrayList<MyClubModel>()
    }
    // Public Method
    // Private Method
}
