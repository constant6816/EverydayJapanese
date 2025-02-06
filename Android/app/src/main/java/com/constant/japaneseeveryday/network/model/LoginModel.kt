package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.network.entity.LoginResponseEntity
import com.constant.japaneseeveryday.util.nonNull

class LoginModel {
    var id: Int
    var name: String
    var email: String
    var accessToken: String
    var refreshToken: String

    constructor(loginResponseEntity: LoginResponseEntity) {
        this.id = nonNull(loginResponseEntity.responseData?.id)
        this.name = nonNull(loginResponseEntity.responseData?.name)
        this.email = nonNull(loginResponseEntity.responseData?.email)
        this.accessToken = nonNull(loginResponseEntity.responseData?.accessToken)
        this.refreshToken = nonNull(loginResponseEntity.responseData?.refreshToken)
    }

    override fun toString(): String {
        return "LoginModel(id=$id, name='$name', email='$email', accessToken='$accessToken', refreshToken='$refreshToken')"
    }
}
