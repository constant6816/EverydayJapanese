package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.network.entity.ReissueResponseEntity
import com.constant.japaneseeveryday.util.nonNull

class ReissueModel {
    var id: Int
    var name: String
    var email: String
    var phoneNumber: String
    var grantType: String
    var accessToken: String
    var refreshToken: String
    var accessTokenExpiresIn: Int
    var refreshTokenExpiresIn: Int

    constructor(reissueResponseEntity: ReissueResponseEntity) {
        this.id = nonNull(reissueResponseEntity.responseData?.id)
        this.name = nonNull(reissueResponseEntity.responseData?.name)
        this.email = nonNull(reissueResponseEntity.responseData?.email)
        this.phoneNumber = nonNull(reissueResponseEntity.responseData?.phoneNumber)
        this.grantType = nonNull(reissueResponseEntity.responseData?.grantType)
        this.accessToken = nonNull(reissueResponseEntity.responseData?.accessToken)
        this.refreshToken = nonNull(reissueResponseEntity.responseData?.refreshToken)
        this.accessTokenExpiresIn = nonNull(reissueResponseEntity.responseData?.accessTokenExpiresIn)
        this.refreshTokenExpiresIn = nonNull(reissueResponseEntity.responseData?.refreshTokenExpiresIn)
    }

    override fun toString(): String {
        return "ReissueModel(id=$id, name='$name', email='$email', phoneNumber='$phoneNumber', grantType='$grantType', accessToken='$accessToken', refreshToken='$refreshToken', accessTokenExpiresIn=$accessTokenExpiresIn, refreshTokenExpiresIn=$refreshTokenExpiresIn)"
    }
}
