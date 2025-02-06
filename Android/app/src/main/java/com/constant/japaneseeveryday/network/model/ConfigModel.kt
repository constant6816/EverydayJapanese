package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.network.entity.ConfigResponseEntity
import com.constant.japaneseeveryday.util.nonNull

class ConfigModel {
    var appName: String
    var appServerName: String
    var marketVersion: String
    var isRequiredUpdate: Boolean
    var minWorkMinute: Int
    var maxWorkMinute: Int

    constructor(configResponseEntity: ConfigResponseEntity) {
        this.appName = nonNull(configResponseEntity.responseData?.appName)
        this.appServerName = nonNull(configResponseEntity.responseData?.appServerName)
        this.marketVersion = nonNull(configResponseEntity.responseData?.marketVersion)
        this.isRequiredUpdate = nonNull(configResponseEntity.responseData?.isRequiredUpdate)
        this.minWorkMinute = nonNull(configResponseEntity.responseData?.minWorkMinute)
        this.maxWorkMinute = nonNull(configResponseEntity.responseData?.maxWorkMinute)
    }

    override fun toString(): String {
        return "ConfigModel(appName='$appName', appServerName='$appServerName', marketVersion='$marketVersion', isRequiredUpdate=$isRequiredUpdate, minWorkMinute=$minWorkMinute, maxWorkMinute=$maxWorkMinute)"
    }
}
