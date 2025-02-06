package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.network.entity.SignupResponseEntity
import com.constant.japaneseeveryday.util.nonNull

class SignupModel {
    var id: Int
    var name: String
    var email: String
    var phoneNumber: String
    var grantType: String
    var accessToken: String
    var refreshToken: String
    var accessTokenExpiresIn: Int
    var refreshTokenExpiresIn: Int

    constructor(signupResponseEntity: SignupResponseEntity) {
        this.id = nonNull(signupResponseEntity.responseData?.id)
        this.name = nonNull(signupResponseEntity.responseData?.name)
        this.email = nonNull(signupResponseEntity.responseData?.email)
        this.phoneNumber = nonNull(signupResponseEntity.responseData?.phoneNumber)
        this.grantType = nonNull(signupResponseEntity.responseData?.grantType)
        this.accessToken = nonNull(signupResponseEntity.responseData?.accessToken)
        this.refreshToken = nonNull(signupResponseEntity.responseData?.refreshToken)
        this.accessTokenExpiresIn = nonNull(signupResponseEntity.responseData?.accessTokenExpiresIn)
        this.refreshTokenExpiresIn = nonNull(signupResponseEntity.responseData?.refreshTokenExpiresIn)
    }

    override fun toString(): String {
        return "SignupModel(id=$id, name='$name', email='$email', phoneNumber='$phoneNumber', grantType='$grantType', accessToken='$accessToken', refreshToken='$refreshToken', accessTokenExpiresIn=$accessTokenExpiresIn, refreshTokenExpiresIn=$refreshTokenExpiresIn)"
    }
}
