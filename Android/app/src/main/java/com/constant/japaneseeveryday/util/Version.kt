package com.constant.japaneseeveryday.util

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class Version {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    private val debugText = "-debug"

    // Public Variable
    var major: Int
    var minor: Int
    var patch: Int
    var isDebug: Boolean

    // Private Variable
    // Override Method or Basic Method
    constructor() {
        major = -1
        minor = -1
        patch = -1
        isDebug = false
    }

    constructor(versionText: String) {
        val versionArray = versionText.split(".").toTypedArray()
        if (versionArray.size != 3) {
            major = 0
            minor = 0
            patch = 0
            isDebug = false
            return
        }
        major = versionArray[0].toInt()
        minor = versionArray[1].toInt()
        isDebug =
            if (versionArray[2].contains(debugText)) {
                true
            } else {
                false
            }
        patch = versionArray[2].replace(debugText, "").toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Version

        if (major != other.major) return false
        if (minor != other.minor) return false
        if (patch != other.patch) return false

        return true
    }

    // Public Method
    open fun isGreaterThan(other: Version): Boolean {
        // major 버전 비교
        if (this.major > other.major) return true
        if (this.major < other.major) return false

        // minor 버전 비교
        if (this.minor > other.minor) return true
        if (this.minor < other.minor) return false

        // patch 버전 비교
        if (this.patch > other.patch) return true
        if (this.patch < other.patch) return false

        // 모두 동일한 경우 false 반환
        return false
    }
    // Private Method
}
